package com.example.cse234termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
    }
    public void clickSignUpButton(View view){
        Intent signUpIntent=new Intent(this,SignUpActivity.class);
        startActivity(signUpIntent);
    }
    public void logIn(View view){
        EditText editTextEmail= (EditText)findViewById(R.id.logIn_editTextEmail);
        String email=editTextEmail.getText().toString();

        EditText editTextPassword= (EditText)findViewById(R.id.logIn_editTextPassword);
        String password=editTextPassword.getText().toString();

        Intent logInIntent=new Intent(this,HomeActivity.class);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(logInIntent);
                }
                else{
                    Toast.makeText(LogInActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }

}