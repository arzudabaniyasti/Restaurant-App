package com.example.cse234termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
    }
    public void clickLogInTextView(View view){
        Intent intent1=new Intent(this,LogInActivity.class);
        startActivity(intent1);
    }
    public void createUser(View view){

        EditText editTextName= (EditText)findViewById(R.id.signUp_editTextName);
        String name=editTextName.getText().toString();

        EditText editTextSurname= (EditText)findViewById(R.id.signUp_editTextSurname);
        String surname=editTextSurname.getText().toString();

        EditText editTextEmail= (EditText)findViewById(R.id.signUp_editTextEmail);
        String email=editTextEmail.getText().toString();

        EditText editTextPassword= (EditText)findViewById(R.id.signUp_editTextPassword);
        String password=editTextPassword.getText().toString();

        EditText editTextConfirmPassword= (EditText)findViewById(R.id.signUp_editTextConfirmPassword);
        String confirmPassword=editTextConfirmPassword.getText().toString();

        EditText editTextBirthdate = (EditText) findViewById(R.id.signUp_editTextBirthdate);
        String birthdate = editTextBirthdate.getText().toString();

        EditText editTextPhoneNumber= (EditText) findViewById(R.id.signUp_editTextPhoneNumber);
        String phoneNumber = editTextPhoneNumber.getText().toString();

        Intent intent2=new Intent(this,LogInActivity.class);

        if (!password.equals(confirmPassword)){
            Toast.makeText(SignUpActivity.this,"Password and Confirm password must be the same",Toast.LENGTH_SHORT ).show();

        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //load user's information to firebase
                        UserModel user=new UserModel(name,surname,email,phoneNumber,birthdate,password);
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    startActivity(intent2);
                                }else{
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT ).show();
                    }
                }
            });
        }
    }

}