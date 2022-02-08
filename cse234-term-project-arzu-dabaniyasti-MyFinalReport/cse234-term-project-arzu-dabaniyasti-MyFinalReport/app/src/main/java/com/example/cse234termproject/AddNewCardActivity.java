package com.example.cse234termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddNewCardActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText securityCode,expiryYear,expiryMonth,cardHolder,cardName,cardNumber1,cardNumber2,cardNumber3,cardNumber4;
    Button addANewCreditCard;
    String mysecurityCode,myexpiryYear,myexpiryMonth,mycardHolder,mycardName,mycardNumber;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        securityCode=findViewById(R.id.card_CVV);
        expiryYear=findViewById(R.id.cardyearedit);
        expiryMonth=findViewById(R.id.cardmonthedit);
        cardHolder=findViewById(R.id.cardholder_name);
        cardName=findViewById(R.id.cardnameedit);
        cardNumber1=findViewById(R.id.cardedit1);
        cardNumber2=findViewById(R.id.cardedit2);
        cardNumber3=findViewById(R.id.cardedit3);
        cardNumber4=findViewById(R.id.cardedit4);
        addANewCreditCard=findViewById(R.id.addanewcreditcardbutton);
        addANewCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysecurityCode=securityCode.getText().toString();
                myexpiryYear=expiryYear.getText().toString();
                myexpiryMonth=expiryMonth.getText().toString();
                mycardHolder=cardHolder.getText().toString();
                mycardName=cardName.getText().toString();
                mycardNumber=cardNumber1.getText().toString()+cardNumber2.getText().toString()+cardNumber3.getText().toString()+cardNumber4.getText().toString();
                addNewCard(mysecurityCode,myexpiryYear,myexpiryMonth,mycardHolder,mycardName,mycardNumber);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    UserModel user = task.getResult().getValue(UserModel.class);
                    TextView userName=findViewById(R.id.UserName);
                    userName.setText(user.getName()+" "+user.getSurname());
                    userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_person_24, 0, 0, 0);
                }
            }
        });

    }
    private void addNewCard(String mysecurityCode,String myexpiryYear,String myexpiryMonth,String mycardHolder,String mycardName,String mycardNumber){
        final HashMap<String,Object> creditCardMap=new HashMap<>();
        creditCardMap.put("CVV",mysecurityCode);
        creditCardMap.put("ExpiryYear",myexpiryYear);
        creditCardMap.put("ExpiryMonth",myexpiryMonth);
        creditCardMap.put("CardHolder",mycardHolder);
        creditCardMap.put("CardName",mycardName);
        creditCardMap.put("CardNumber",mycardNumber);
        firestore.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("Credit Cards")
                .add(creditCardMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(AddNewCardActivity.this, "Added", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddNewCardActivity.this,ConfirmBasketActivity.class);
                startActivity(intent);
            }
        });
    }
}