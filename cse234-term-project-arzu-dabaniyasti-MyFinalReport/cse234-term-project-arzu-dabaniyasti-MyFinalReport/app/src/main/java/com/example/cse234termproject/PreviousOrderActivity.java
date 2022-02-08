package com.example.cse234termproject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import com.example.cse234termproject.Adapters.PreviousOrderAdapter;
import com.example.cse234termproject.Model.OrderModel;
import com.example.cse234termproject.Model.OrderProductModel;
import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PreviousOrderActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    PreviousOrderAdapter orderAdapter;
    List<OrderModel> orderList;
    List<String> orderDateList;
    List<String> orderTimeList;
    List<String> orderPriceList;
    List<String> quantityList;
    List<String> productNameList;
    List<Integer> productNameCountList,productQuantityCountList;
    int count=0;
    int count2=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        recyclerView= findViewById(R.id.pastOrder_listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        orderList=new ArrayList<>();
        orderTimeList=new ArrayList<>();
        orderDateList=new ArrayList<>();
        orderPriceList=new ArrayList<>();
        quantityList=new ArrayList<>();
        productNameList=new ArrayList<>();
        productNameCountList=new ArrayList<>();
        productQuantityCountList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid())
                .child("Order List").child("Past Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderPriceList.clear();
                orderTimeList.clear();
                for (DataSnapshot favDataSnap : snapshot.getChildren()) {
                    OrderModel currentOrderModel = favDataSnap.getValue(OrderModel.class);

                    String time= currentOrderModel.getTime();
                    orderTimeList.add(time);

                    String price= currentOrderModel.getTotalBasket();
                    orderPriceList.add(price);

                    String date= currentOrderModel.getDate();
                    orderDateList.add(date);


                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productNameList.clear();
                quantityList.clear();
                for (DataSnapshot favDataSnap : snapshot.getChildren()) {
                    for(DataSnapshot favDataSnap2:favDataSnap.child("Products").getChildren()){
                        count=count+1;
                        count2=count2+1;
                        OrderProductModel currentOrderModel = favDataSnap2.getValue(OrderProductModel.class);
                        String productName= currentOrderModel.getProductName();
                        productNameList.add(productName);
                        String quantity= currentOrderModel.getQuantity();
                        quantityList.add(quantity);
                    }
                    productNameCountList.add(count);
                    productQuantityCountList.add(count2);
                    count=0;
                    count2=0;
                }
                orderAdapter=new PreviousOrderAdapter(getApplicationContext(),orderDateList,orderPriceList,orderTimeList,productNameList,quantityList,productNameCountList,productQuantityCountList);
                recyclerView.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

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
}