package com.jokers.todolist.presenters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jokers.todolist.models.ToDo;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentPresenter implements ChildEventListener {

    private List<ToDo> toDos;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private View mView;

    public HomeFragmentPresenter(View mView) {
        mDatabase = FirebaseDatabase.getInstance().getReference();                      // initialize Firebase Realtime Database
        mAuth = FirebaseAuth.getInstance();                                             // initialize FirebaseAuth instance

        this.mView = mView;
        this.toDos = new ArrayList<>();

        String userId = mAuth.getCurrentUser().getUid();    // $uid
        DatabaseReference todosRef = mDatabase              // users/$uid/todos
                .child(userId)
                .child("todos");

        todosRef.addChildEventListener(this);
    }

    // TODO: Sort functions


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        ToDo toDo = snapshot.getValue(ToDo.class);
        toDo.setID(snapshot.getKey());

        toDos.add(toDo);

        mView.updateUI(toDos);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public interface View {
        void updateUI(List<ToDo> toDos);
        void showProgressBar();
        void hideProgressBar();
    }
}
