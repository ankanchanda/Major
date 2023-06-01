package com.example.majormajor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatBotActivity extends AppCompatActivity {

    EditText userInput;
    ImageView sendButton;
    RecyclerView recyclerView;
    HeritageAdapter2 goalAdapter;
    ArrayList<Pair<String, Messages>> goalList = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth firebaseauth;
    public DatabaseReference RootRef;

    ArrayList<Messages> messagesArrayList;
    String patientName;

    String[] questions = {
            "What is your mood today?",
            "What brought you here?",
            "Over the past few days, how have you been feeling?",
            "Have you experienced any significant life changes recently?",
            "Have you ever experienced an ‘attack’ of fear, anxiety, or panic?",
            "Do you have a support system in place that helps you cope up?",
            "How is your quality of sleep these days? (good or bad)",
            "Are you focused at work/college these days?",
            "Do you feel you spend enough time with your loved ones?",
            "Do you have any hobbies or activities that bring you joy or relaxation?"
    };

    int question_no = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        Intent intent = getIntent();
        patientName = intent.getStringExtra("patientName");

        recyclerView = findViewById(R.id.usersChatRecyclerView);

        database = FirebaseDatabase.getInstance();
        firebaseauth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance ().getReference ().child("chatbot").child(firebaseauth.getUid());
        RootRef.removeValue();

        sendButton = (ImageView) findViewById(R.id.sendButton);
        userInput = findViewById(R.id.usersChatEditText);

        String message = "Hey, WhatSup?";
        Date date = new Date();

        Messages messages = new Messages(message, "Bot", date.getTime());
        database.getReference().child("chatbot")
                .child(firebaseauth.getUid())
                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(ChatBotActivity.this, "Sent", Toast.LENGTH_SHORT).show();

                    }
                });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTheFollowing();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void doTheFollowing() {
        String message = userInput.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(ChatBotActivity.this, "Enter Any Text", Toast.LENGTH_SHORT).show();
            return;
        }

        userInput.setText("");
        Date date = new Date();



        Messages messages = new Messages(message, patientName, date.getTime());

        database.getReference().child("chatbot")
                .child(firebaseauth.getUid())
                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(ChatBotActivity.this, "Send", Toast.LENGTH_SHORT).show();

                    }
                });
        chatbottext();
    }

    private void chatbottext() {
        for(int i=0; i<100000; i++){

        }
        String message = questions[(question_no++)%question_no];
        Date date = new Date();

        Messages messages = new Messages(message, "Bot", date.getTime());
        database.getReference().child("chatbot")
                .child(firebaseauth.getUid())
                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(ChatBotActivity.this, "Sent", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart ();

        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Getting the latest list of goals and updating the goalList
                ArrayList<Pair<String,Messages>> currList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Messages goal = dataSnapshot.getValue(Messages.class);
                    currList.add(new Pair<>(dataSnapshot.getKey(),goal ));
                }
                goalList = currList;

                // Updating the adapter with the new goal list
                goalAdapter.setGoalList(goalList);

                // Making the recycler view appear and the loading dialog disappear

                // Making the empty goal message visible when goal list is empty
//                if(goalList.size()==0) emptyGoal.setVisibility(View.VISIBLE);
//                else emptyGoal.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        goalAdapter = new HeritageAdapter2(this,goalList);
        recyclerView.setAdapter ( goalAdapter );
        //goalSearch.setText("");

    }
}