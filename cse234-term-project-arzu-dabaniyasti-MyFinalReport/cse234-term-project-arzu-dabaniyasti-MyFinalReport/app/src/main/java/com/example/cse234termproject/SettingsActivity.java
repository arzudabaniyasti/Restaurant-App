package com.example.cse234termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView name,surname,phone,email,birthdate,password;
    String userName,userSurname,userPhone,userEmail,userBirthdate,userPassword;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    Button updateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    UserModel user = task.getResult().getValue(UserModel.class);
                    name=findViewById(R.id.settingsName);
                    surname=findViewById(R.id.settingsSurname);
                    birthdate=findViewById(R.id.settingBirthdate);
                    phone=findViewById(R.id.settingPhone);
                    email=findViewById(R.id.settingEmail);
                    password=findViewById(R.id.settingPassword);

                    name.setText(user.getName());
                    surname.setText(user.getSurname());
                    birthdate.setText(user.getBirthdate());
                    phone.setText(user.getPhone());
                    email.setText(user.getEmail());
                    password.setText(user.getPassword());

                    userName=name.getText().toString();
                    userSurname=surname.getText().toString();
                    userBirthdate=birthdate.getText().toString();
                    userPhone=phone.getText().toString();
                    userEmail=email.getText().toString();
                    userPassword=password.getText().toString();


                }
            }
        });
        updateButton=findViewById(R.id.updatebutton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSurnameUpdated() || isNameUpdated() ||isEmailUpdated()||isBirthdateUpdated()||isPhoneUpdated()||isPasswordUpdated()){
                    Toast.makeText(SettingsActivity.this,"Updated",Toast.LENGTH_SHORT).show();
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
    private boolean isNameUpdated() {
        String nameUpdated=name.getText().toString();
        if(!userName.equals(nameUpdated)){
            databaseReference.child("name").setValue(name.getText().toString());
            userName=nameUpdated;
            return true;
        }else{
            return false;
        }
    }
    private boolean isSurnameUpdated() {
        String surnameUpdated=surname.getText().toString();
        if(!userSurname.equals(surnameUpdated)){
            databaseReference.child("surname").setValue(surname.getText().toString());
            userSurname=surnameUpdated;
            return true;
        }else{
            return false;
        }
    }
    private boolean isBirthdateUpdated() {
        String birthdateUpdated=birthdate.getText().toString();
        if(!userBirthdate.equals(birthdateUpdated)){
            databaseReference.child("birthdate").setValue(birthdate.getText().toString());
            userBirthdate=birthdateUpdated;
            return true;
        }else{
            return false;
        }
    }
    private boolean isPhoneUpdated() {
        String phoneUpdated=phone.getText().toString();
        if(!userPhone.equals(phoneUpdated)){
            databaseReference.child("phone").setValue(phone.getText().toString());
            userPhone=phoneUpdated;
            return true;
        }else{
            return false;
        }
    }
    private boolean isEmailUpdated() {
        String emailUpdated=email.getText().toString();
        if(!userEmail.equals(emailUpdated)){
            databaseReference.child("email").setValue(email.getText().toString());
            userEmail=emailUpdated;
            return true;
        }else{
            return false;
        }
    }
    private boolean isPasswordUpdated() {
        String passwordUpdated=password.getText().toString();
        if(!userPassword.equals(passwordUpdated)){
            databaseReference.child("password").setValue(password.getText().toString());
            userPassword=passwordUpdated;
            return true;
        }else{
            return false;
        }
    }

}