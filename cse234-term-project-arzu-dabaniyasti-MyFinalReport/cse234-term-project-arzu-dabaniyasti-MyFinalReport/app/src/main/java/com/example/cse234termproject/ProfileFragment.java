package com.example.cse234termproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.widget.TextView;

import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    TextView LogOut,favourites,basket,reservation,chat,currentorder,previousOrder,settings;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        mAuth = FirebaseAuth.getInstance();
        LogOut=view.findViewById(R.id.LogOutButton);
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent logOutIntent=new Intent(getActivity(),LogInActivity.class);
                startActivity(logOutIntent);
            }
        });
        favourites=view.findViewById(R.id.favourites_activity);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intent);
            }
        });
        basket=view.findViewById(R.id.shopping_basket_activity);
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), BasketActivity.class);
                startActivity(intent);
            }
        });
        reservation=view.findViewById(R.id.reservation_activity);
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ReservationActivity.class);
                startActivity(intent);
            }
        });
        chat=view.findViewById(R.id.chat_activity);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ChatActivity.class);
                startActivity(intent);
            }
        });

        currentorder=view.findViewById(R.id. current_order_activity);
        currentorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),CurrentOrderActivity.class);
                startActivity(intent);
            }
        });
        previousOrder=view.findViewById(R.id.previous_order_activity);
        previousOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),PreviousOrderActivity.class);
                startActivity(intent);
            }
        });
        settings=view.findViewById(R.id.settings_activity);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    UserModel user = task.getResult().getValue(UserModel.class);
                    TextView userName=view.findViewById(R.id.profilenamesurname);
                    userName.setText(user.getName()+" "+user.getSurname());
                    TextView userEmail=view.findViewById(R.id.profileEmail);
                    userEmail.setText(user.getEmail());
                }
            }
        });
        return view;

    }
}