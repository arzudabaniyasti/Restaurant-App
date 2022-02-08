package com.example.cse234termproject;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse234termproject.Adapters.ChatAdapter;
import com.example.cse234termproject.Model.ChatModel;
import com.example.cse234termproject.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
public class ChatActivity extends AppCompatActivity {
    EditText userInput;
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    List<ChatModel> replyList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth = FirebaseAuth.getInstance();
        userInput = findViewById(R.id.userInput);
        recyclerView = (RecyclerView) findViewById(R.id.conversation);
        replyList = new ArrayList<>();
        chatAdapter = new ChatAdapter(replyList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(chatAdapter);
        ChatModel reply = new ChatModel("Hi, Wait For It is ready for you. How can I help you? ", false);
        replyList.add(reply);
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEND) {
                    ChatModel reply1 = new ChatModel(userInput.getText().toString(), true);
                    replyList.add(reply1);
                    ChatModel botReply;
                    String userinputtext=userInput.getText().toString();

                    if(userinputtext.equals("hi")||userinputtext.equals("hello")){
                        botReply = new ChatModel("Hi again, How can I help you?", false);
                        replyList.add(botReply);}

                    else if(userinputtext.equals("What is the address of the restaurant?")) {
                        botReply = new ChatModel("66 Knightsbridge, London SW1X 7LA, United Kingdom. You should visit us.", false);
                        replyList.add(botReply);
                    }
                    else if(userinputtext.equals("What is your restaurant's phone number?")||userinputtext.equals("phone number")) {
                        botReply = new ChatModel("Our phone number +44 (0)20 7201 3833. Call us. ", false);
                        replyList.add(botReply);
                    }
                    else if(userinputtext.equals("What time does the restaurant close?")) {
                        botReply = new ChatModel("Our restaurant closes at 12 but you can order 24/7 ", false);
                        replyList.add(botReply);
                    }
                    else if(userinputtext.equals("Thank you")) {
                        botReply = new ChatModel("Thank you for choosing us. Have a nice day! ", false);
                        replyList.add(botReply);
                    }
                    else{
                        botReply = new ChatModel("Sorry. I couldn't understand. ", false);
                        replyList.add(botReply);
                    }
                    chatAdapter.notifyDataSetChanged();
                    if (!isLastVisible())
                        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
                }
                return false;
            }
        });
    }

    public boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int a = layoutManager.findLastCompletelyVisibleItemPosition();
        int numberItems = recyclerView.getAdapter().getItemCount();
        return (a >= numberItems);
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