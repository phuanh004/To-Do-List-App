package com.jokers.todolist.presenters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jokers.todolist.models.Point;
import com.jokers.todolist.models.ToDo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogbookFragmentPresenter implements
        FirebaseAuth.AuthStateListener,
        ChildEventListener {

    private final List<ToDo> toDos;

    private final DatabaseReference mDatabase;
    private final FirebaseAuth mAuth;
    private DatabaseReference mLogbookRef = null;

    private final Point point;

    private final View mView;

    public LogbookFragmentPresenter(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();                      // initialize Firebase Realtime Database
        mAuth = FirebaseAuth.getInstance();                                             // initialize FirebaseAuth instance
        mAuth.addAuthStateListener(this);

        mView = view;
        this.toDos = new ArrayList<>();

        point = new Point();
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        // Show the progressbar
        mView.showProgressBar();

        // Start startToDoListener
        startToDoListener();

        // Hide the progressbar
        mView.hideProgressBar();

        // Check show empty logbook condition
        if ((toDos.size() > 0)) {
            mView.hideEmptyLogbookMessage();
        } else {
            mView.showEmptyLogbookMessage();
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        mView.hideEmptyLogbookMessage();

        // Create new to do
        ToDo toDo = snapshot.getValue(ToDo.class);
        assert toDo != null;
        toDo.setID(snapshot.getKey());
        toDo.setUid(mAuth.getUid());

        // Add to-to to the list and update the view
        toDos.add(toDo);
        mView.addTodo(toDo);

        // Update users points
        point.increasePoints();
        mView.updateTotalPoint();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        String removedItemId = snapshot.getKey();

        // Remove the to-do from list
        toDos.removeIf(toDo -> toDo.getID().equals(removedItemId));
        mView.removeTodo(removedItemId);

        // Show empty list if no to-do
        if (toDos.size() == 0) {
            mView.showEmptyLogbookMessage();
        }

        // Update users points
        point.decreasePoints();
        mView.updateTotalPoint();
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }

    public void startToDoListener(){
        // If user signed in but does not have notes
        if (mAuth.getUid() != null && mLogbookRef == null) {
            String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();       // $uid
            mLogbookRef = mDatabase                                                       // users/$uid/logbook
                    .child(userId)
                    .child("logbook");

            // Listen for the data change
            mLogbookRef.addChildEventListener(this);
        }
    }

    public void stopToDoListener(){
        if (mLogbookRef != null) {
            mLogbookRef.removeEventListener(this);
            mLogbookRef = null;
        }
    }

    public interface View {
        void addTodo(ToDo todo);
        void removeTodo(String todoId);
        void updateTotalPoint();
        void showEmptyLogbookMessage();
        void hideEmptyLogbookMessage();
        void showProgressBar();
        void hideProgressBar();
    }
}
