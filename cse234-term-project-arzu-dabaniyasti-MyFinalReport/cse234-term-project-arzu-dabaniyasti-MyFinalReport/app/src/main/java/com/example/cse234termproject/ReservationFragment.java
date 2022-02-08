package com.example.cse234termproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class ReservationFragment extends Fragment {
    EditText disablePastDate,time,reservationPerson,reservationName,reservationPhone;
    Button makeAReservation;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    String myDate,myTime,myPersonNumber,myName,myPhone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        disablePastDate=view.findViewById(R.id.editTextDate);
        reservationPerson=view.findViewById(R.id.editTextNumber);
        reservationPhone=view.findViewById(R.id.editTextPhone);
        reservationName=view.findViewById(R.id.editTextTextPersonName);
        time=view.findViewById(R.id.editTextTime);
        Calendar calendar= Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        disablePastDate.setInputType(InputType.TYPE_NULL);
        time.setInputType(InputType.TYPE_NULL);

        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    UserModel user = task.getResult().getValue(UserModel.class);
                    reservationName.setText(user.getName() + " " + user.getSurname());
                    reservationPhone.setText(user.getPhone());
                }

            }
        });

        disablePastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String sDate=dayOfMonth+"/"+month+"/"+year;
                        disablePastDate.setText(sDate);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();

            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                   @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        String sTime=hour+":"+minute;
                        time.setText(sTime);
                    }
                },hour,minute,false);
                timePickerDialog.show();
            }
        });
        makeAReservation=view.findViewById(R.id.makeareservation);
        makeAReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (disablePastDate.getText().toString().equals("") || time.getText().toString().equals("") || reservationPerson.getText().toString().equals("")
                        || reservationName.getText().toString().equals("") || reservationPhone.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                myDate = disablePastDate.getText().toString();
                myTime = time.getText().toString();
                myPhone = reservationPhone.getText().toString();
                myName = reservationName.getText().toString();
                myPersonNumber = reservationPerson.getText().toString();
                addReservation(myDate, myTime, myPersonNumber, myName, myPhone);
            }
        });

        return view;
    }
     private void addReservation(String myDate,String myTime,String myPersonNumber,String myName,String myPhone){
        final HashMap<String,Object> favouritesMap=new HashMap<>();
        favouritesMap.put("NameSurname",myName);
        favouritesMap.put("PhoneNumber",myPhone);
        favouritesMap.put("PersonNumber",myPersonNumber);
        favouritesMap.put("Date",myDate);
        favouritesMap.put("Time",myTime);
        firestore.collection("CurrentUser").document(mAuth.getCurrentUser().getUid()).collection("Reservations")
                .add(favouritesMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}