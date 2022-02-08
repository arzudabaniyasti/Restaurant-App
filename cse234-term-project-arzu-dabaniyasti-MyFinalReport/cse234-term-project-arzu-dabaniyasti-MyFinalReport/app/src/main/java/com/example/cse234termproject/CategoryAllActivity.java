package com.example.cse234termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import com.example.cse234termproject.Adapters.CategoryAllAdapter;
import com.example.cse234termproject.Model.UserModel;
import com.example.cse234termproject.Model.CategoryAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryAllActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    List<CategoryAllModel> categoryAllModelList;
    CategoryAllAdapter viewAllAdapter;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_all);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        recyclerView= findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        categoryAllModelList =new ArrayList<>();
        viewAllAdapter=new CategoryAllAdapter(this, categoryAllModelList);
        recyclerView.setAdapter(viewAllAdapter);
        type=getIntent().getStringExtra("type");
        //Getting fastfoods
        if(type!=null && type.equalsIgnoreCase("Fast Foods")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","Fast Foods").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        CategoryAllModel categoryAllModel = document.toObject(CategoryAllModel.class);
                            categoryAllModelList.add(categoryAllModel);
                            viewAllAdapter.notifyDataSetChanged();
                        }
                    }

            });
        }
        //Getting steaks
        if(type!=null && type.equalsIgnoreCase("steaks")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","steaks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        CategoryAllModel categoryAllModel = document.toObject(CategoryAllModel.class);
                        categoryAllModelList.add(categoryAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                    }
                }

            });
        }
        //Getting drinks
        if(type!=null && type.equalsIgnoreCase("drinks")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","drinks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        CategoryAllModel categoryAllModel = document.toObject(CategoryAllModel.class);
                        categoryAllModelList.add(categoryAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                    }
                }

            });
        }
        //Getting desserts
        if(type!=null && type.equalsIgnoreCase("desserts")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","desserts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        CategoryAllModel categoryAllModel = document.toObject(CategoryAllModel.class);
                        categoryAllModelList.add(categoryAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                    }
                }

            });
        }
        //Getting salads
        if(type!=null && type.equalsIgnoreCase("salads")){
            firebaseFirestore.collection("AllProducts").whereEqualTo("type","salads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        CategoryAllModel categoryAllModel = document.toObject(CategoryAllModel.class);
                        categoryAllModelList.add(categoryAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                    }
                }

            });
        }

    }
    @Override
    protected void onStart() {
        TextView category=(TextView) findViewById(R.id.category) ;
        category.setText(" "+type.substring(0,1).toUpperCase()+type.substring(1)+" ");
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
    public void clickShoppingCartIcon(View view){
        Intent shoppingCart=new Intent(this, BasketActivity.class);
        startActivity(shoppingCart);
    }

}