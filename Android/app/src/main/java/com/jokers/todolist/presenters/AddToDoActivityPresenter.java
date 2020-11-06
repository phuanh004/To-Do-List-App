package com.jokers.todolist.presenters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jokers.todolist.models.ToDo;

import java.util.HashMap;
import java.util.Map;

public class AddToDoActivityPresenter implements
        OnSuccessListener<Void>, OnFailureListener{

    private View mView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ToDo toDo;

    public AddToDoActivityPresenter(View view) {
        mAuth = FirebaseAuth.getInstance();                             // initialize FirebaseAuth instance
        mDatabase = FirebaseDatabase.getInstance().getReference();      // initialize Firebase Realtime Database
        mView = view;
    }

    public void setTodo(ToDo toDo){
        this.toDo = toDo;
    }

    /* DATABASE */

    public void addToDB(){
        String userId = mAuth.getCurrentUser().getUid();    // $uid
        DatabaseReference todosRef = mDatabase              // users/$uid/todos
                .child(userId)
                .child("todos");


        String toDoKey = todosRef.push().getKey();          // Push to db with unique key
        todosRef.child(toDoKey).setValue(toDo);

        this.toDo = null;                                   // Clean data
    }



    @Override
    public void onSuccess(Void aVoid) {

    }

    @Override
    public void onFailure(@NonNull Exception e) {

    }


    public interface View {
        ToDo getToDoFromView();
        void showProgressBar();
        void hideProgressBar();
    }
}