package com.example.cse234termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse234termproject.Adapters.ConfirmBasketAdapter;
import com.example.cse234termproject.Model.BasketModel;
import com.example.cse234termproject.Model.ConfirmBasketModel;
import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ConfirmBasketActivity extends AppCompatActivity{
    private FirebaseFirestore db;
    Button orderButon;
    RecyclerView recyclerView;
    ConfirmBasketAdapter confirmBasketAdapter;
    List<ConfirmBasketModel> cardList;

    EditText address,notes;
    String myaddress,mynotes;

    List<String> orderProductNameList;
    List<String> orderQuantityList;

    private FirebaseAuth mAuth;

    Integer total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_basket);

        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        orderButon=findViewById(R.id.orderbutton);
        address=findViewById(R.id.adress_edit_text);
        notes=findViewById(R.id.notes_edit);

        orderProductNameList =new ArrayList<>();
        orderQuantityList =new ArrayList<>();

        recyclerView= findViewById(R.id.cards_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        cardList=new ArrayList<>();
        confirmBasketAdapter=new ConfirmBasketAdapter(this,cardList);
        recyclerView.setAdapter(confirmBasketAdapter);

        db.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("Credit Cards")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        String documentId=document.getId();
                        ConfirmBasketModel confirmModel = document.toObject(ConfirmBasketModel.class);
                        confirmModel.setDocumentId(documentId);
                        cardList.add(confirmModel);
                        confirmBasketAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        db.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                orderProductNameList.clear();
                orderQuantityList.clear();
                for(QueryDocumentSnapshot cartDataSnap : task.getResult()){
                    BasketModel myCartModel = cartDataSnap.toObject(BasketModel.class);

                    String productName= myCartModel.getProductName();
                    orderProductNameList.add(productName);

                    String quantity= myCartModel.getTotalQuantity();
                    orderQuantityList.add(quantity);

                    total+=Integer.valueOf(myCartModel.getTotalPrice());
                }
            }
        });
        orderButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myaddress=address.getText().toString();
                mynotes=notes.getText().toString();
                addtoOrders(myaddress,mynotes,total);
                clearCart();
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
                    TextView textViewUser =(TextView) findViewById(R.id.UserName);
                    textViewUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_person_24, 0, 0, 0);
                    TextView userName=findViewById(R.id.UserName);
                    userName.setText(user.getName()+" "+user.getSurname());
                }
            }
        });

    }
    private void clearCart() {
                db.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).
                                collection("AddToCart").document(document.getId()).delete();
                    }
                } else {
                }
            }
        });
    }
    public void clickAddNewCardTextView(View view){
        Intent intent=new Intent(this,AddNewCardActivity.class);
        startActivity(intent);
    }

    private void addtoOrders(String myaddress,String mynotes,Integer total) {
    String saveCurrentTime, saveCurrentDate;
    Calendar calForDate = Calendar.getInstance();

    SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
    saveCurrentDate = currentDate.format(calForDate.getTime());

    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
    saveCurrentTime = currentTime.format(calForDate.getTime());

    String a=mAuth.getCurrentUser().getUid();

    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getInstance().getReference().child("Users").child(a).child("Order List");
    final HashMap<String, Object> cartMap = new HashMap<>();
    final HashMap<String, Object> orderMap = new HashMap<>();

    for(int i=0;i<orderProductNameList.size();i++) {
        cartMap.put("ProductName", orderProductNameList.get(i));
        cartMap.put("quantity", orderQuantityList.get(i));

        String productID = orderProductNameList.get(i);
        String orderID = "order time: "+saveCurrentDate+" "+saveCurrentTime;
        databaseReference.child("Current Orders").child(orderID).child("Products").child(productID)
            .updateChildren(cartMap)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
            });
    }

    orderMap.put("address",myaddress);
    orderMap.put("notes",mynotes);
    orderMap.put("totalBasket", String.valueOf(total));
    orderMap.put("date",saveCurrentDate);
    orderMap.put("time",saveCurrentTime);

    String orderID = "order time: "+saveCurrentDate+" "+saveCurrentTime;
    databaseReference.child("Current Orders").child(orderID).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ConfirmBasketActivity.this,CurrentOrderActivity.class);
        startActivity(intent);
    }
    });
    }
}