package com.example.majormajor;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HeritageAdapter2 extends RecyclerView.Adapter<HeritageAdapter2.StudentViewHolder2> {

    // List of goals, goals are stored as a pair of the key of the goal and the goal object
    ArrayList<Pair<String, Messages>> goalList;
    ChatBotActivity fragment;


    HeritageAdapter2(ChatBotActivity fragment, ArrayList<Pair<String,Messages>> goalList)
    {
        this.fragment=fragment;
        this.goalList=goalList;
    }



    @NonNull
    @Override
    public StudentViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragment).inflate(R.layout.item_userrow,parent,false);
        return new StudentViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder2 holder, int position) {


        // Key of the goal is the first element of the pair
        String listPostKey = goalList.get(position).first;
        DatabaseReference db = fragment.RootRef.child(listPostKey);
        StringBuilder sb = new StringBuilder();
        StringBuilder retriveBreakEndDate = new StringBuilder();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                holder.text.setText(snapshot.child("message").getValue().toString());
                holder.user.setText(snapshot.child("senderId").getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }



    @Override
    public int getItemCount() {
        return goalList.size();
    }

    // Changing the list when searching is carried out and notifying the change
    public void setGoalList(ArrayList<Pair<String,Messages>> list)
    {
        goalList= list;
        notifyDataSetChanged();
    }


    public static class StudentViewHolder2 extends  RecyclerView.ViewHolder
    {

        TextView user,text;
        public StudentViewHolder2(@NonNull View itemView) {
            super ( itemView );
            user = itemView.findViewById(R.id.user_name);
            text = itemView.findViewById(R.id.user_messages);
        }
    }


}
