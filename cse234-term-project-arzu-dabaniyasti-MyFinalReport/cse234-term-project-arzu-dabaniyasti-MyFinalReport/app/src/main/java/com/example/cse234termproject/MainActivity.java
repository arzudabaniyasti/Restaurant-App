package com.example.cse234termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            Intent mainIntent=new Intent(this,HomeActivity.class);
            startActivity(mainIntent);
        }
    }
    public void clickLogInButtonInMain(View view){
        Intent LogInButtonInMain=new Intent(this,LogInActivity.class);
        startActivity(LogInButtonInMain);
    }
}