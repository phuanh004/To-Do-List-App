package com.jokers.todolist.presenters;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jokers.todolist.models.ToDo;

import java.util.Objects;

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
        mView.showProgressBar();

        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();    // $uid
        DatabaseReference todosRef = mDatabase              // users/$uid/todos
                .child(userId)
                .child("todos");


        String toDoKey = todosRef.push().getKey();          // Push to db with unique key

        assert toDoKey != null;
        todosRef.child(toDoKey).setValue(toDo).addOnSuccessListener(this);

        this.toDo = null;                                   // Clean data
    }



    @Override
    public void onSuccess(Void aVoid) {
        mView.hideProgressBar();
        mView.showSuccessMessage();
        mView.backToMainActivity();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        mView.hideProgressBar();
        mView.showFailedMessage();
    }


    public interface View {
        ToDo getToDoFromView();
        void showDateTimeDialog();
        void showProgressBar();
        void hideProgressBar();
        void showSuccessMessage();
        void showFailedMessage();
        void backToMainActivity();
    }
}
