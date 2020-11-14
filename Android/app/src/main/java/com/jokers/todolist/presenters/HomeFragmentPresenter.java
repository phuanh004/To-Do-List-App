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
import java.util.Objects;

public class HomeFragmentPresenter implements
        FirebaseAuth.AuthStateListener,
        ChildEventListener {

    private List<ToDo> toDos;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private View mView;
    private DatabaseReference mTodosRef = null;

    public HomeFragmentPresenter(View mView) {
        mDatabase = FirebaseDatabase.getInstance().getReference();                      // initialize Firebase Realtime Database
        mAuth = FirebaseAuth.getInstance();                                             // initialize FirebaseAuth instance
        mAuth.addAuthStateListener(this);

        this.mView = mView;
        this.toDos = new ArrayList<>();
    }

    // TODO: Sort functions


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        ToDo toDo = snapshot.getValue(ToDo.class);
        assert toDo != null;
        toDo.setID(snapshot.getKey());

        toDos.add(toDo);
        mView.addTodo(toDo);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//        Log.d("TAG", "onChildChanged: " + snapshot.getKey());
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//        Log.d("TAG", "onChildRemoved: " + snapshot.getKey());
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//        Log.d("TAG", "onChildMoved: ");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.d("TAG", "onCancelled: ");
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        // Start startToDoListener
        if (mAuth.getUid() != null) {
            String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();    // $uid
            mTodosRef = mDatabase                                      // users/$uid/todos
                    .child(userId)
                    .child("todos");

            // Listen for the data change
            mTodosRef.addChildEventListener(this);
        }
    }

    public void stopToDoListener(){
        if (mTodosRef != null) {
            mTodosRef.removeEventListener(this);
        }
    }

    public interface View {
        void addTodo(ToDo todo);
        void changeTodo(ToDo todo);
        void removeTodo(String toDoId);
        void resetTodoList();
        void showProgressBar();
        void hideProgressBar();
    }
}
