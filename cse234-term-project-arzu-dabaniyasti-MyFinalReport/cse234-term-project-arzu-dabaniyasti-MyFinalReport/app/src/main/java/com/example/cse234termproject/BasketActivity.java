package com.example.cse234termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cse234termproject.Adapters.BasketAdapter;
import com.example.cse234termproject.Model.BasketModel;
import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    TextView totalBasket;
    RecyclerView recyclerView;
    BasketAdapter basketAdapter;
    List<BasketModel> basketModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        recyclerView= findViewById(R.id.shopping_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        totalBasket=findViewById(R.id.basket_total);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("basketTotalAmount"));
        basketModelList=new ArrayList<>();
        basketAdapter=new BasketAdapter(this,basketModelList);
        recyclerView.setAdapter(basketAdapter);

        db.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        String documentId=document.getId();
                        BasketModel basketModel = document.toObject(BasketModel.class);
                        basketModel.setDocumentId(documentId);
                        basketModelList.add(basketModel);
                        basketAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    public BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           int totalBill=intent.getIntExtra("totalAmount",0);
           totalBasket.setText("Total : $"+totalBill);
        }
    };
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

    public void confirmBasket(View view){
        Intent intent=new Intent(this,ConfirmBasketActivity.class);
        int totalBill=intent.getIntExtra("totalAmount",0);
        String totalPrice = String.valueOf(totalBill);
        intent.putExtra("totalBasket",totalBill );
        startActivity(intent);
    }

}