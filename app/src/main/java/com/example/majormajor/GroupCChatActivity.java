package com.example.majormajor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class GroupCChatActivity extends AppCompatActivity {

    EditText userInput;
    ImageView sendButton;
    RecyclerView recyclerView;
    CheckBox checkBox;
    HeritageAdapter goalAdapter;
    ArrayList<Pair<String, Messages>> goalList = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth firebaseauth;
    public DatabaseReference RootRef;

    ArrayList<Messages> messagesArrayList;
    String receivedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_cchat);

        Intent intent = getIntent();
        receivedValue = intent.getStringExtra("patientName");

        recyclerView = findViewById(R.id.usersChatRecyclerView);

        database = FirebaseDatabase.getInstance();
        firebaseauth = FirebaseAuth.getInstance();
        checkBox = findViewById(R.id.usersChatCheckAnonymus);
        RootRef = FirebaseDatabase.getInstance ().getReference ().child("chats").child(firebaseauth.getUid());

        sendButton = (ImageView) findViewById(R.id.sendButton);
        userInput = findViewById(R.id.usersChatEditText);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clk();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Toast.makeText(this, receivedValue, Toast.LENGTH_SHORT).show();

    }

    private void clk() {
        String message = userInput.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(GroupCChatActivity.this, "Enter Any Text", Toast.LENGTH_SHORT).show();
            return;
        }

        userInput.setText("");
        Date date = new Date();

        if (checkBox.isChecked()) {
            receivedValue = "user (anonymous)";
        }



        Messages messages = new Messages(message, receivedValue, date.getTime());

        database.getReference().child("chats")
                .child(firebaseauth.getUid())
                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(GroupCChatActivity.this, "Send", Toast.LENGTH_SHORT).show();

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


        goalAdapter = new HeritageAdapter(this,goalList);
        recyclerView.setAdapter ( goalAdapter );
        //goalSearch.setText("");

    }




}