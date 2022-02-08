package com.example.cse234termproject;


import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse234termproject.Adapters.FavoritesAdapter;
import com.example.cse234termproject.Model.FavoritesModel;
import com.example.cse234termproject.Model.UserModel;
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

public class FavoritesActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    FavoritesAdapter favouritesAdapter;
    List<FavoritesModel> favouritesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        recyclerView= findViewById(R.id.favourites_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        favouritesList=new ArrayList<>();
        favouritesAdapter=new FavoritesAdapter(this,favouritesList);
        recyclerView.setAdapter(favouritesAdapter);
        db.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("AddToFavourites")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        String documentId=document.getId();
                        FavoritesModel favouritesModel = document.toObject(FavoritesModel.class);
                        favouritesModel.setDocumentId(documentId);
                        favouritesList.add(favouritesModel);
                        favouritesAdapter.notifyDataSetChanged();
                    }
                }
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