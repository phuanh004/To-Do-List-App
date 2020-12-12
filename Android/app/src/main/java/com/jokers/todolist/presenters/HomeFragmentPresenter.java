package com.jokers.todolist.presenters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class HomeFragmentPresenter implements
        FirebaseAuth.AuthStateListener,
        ChildEventListener,
        OnSuccessListener<Void> {

    private List<ToDo> toDos;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mTodosRef = null;
    private boolean loaded;

    private View mView;

    public HomeFragmentPresenter(View view) {
        mDatabase = FirebaseDatabase.getInstance().getReference();                      // initialize Firebase Realtime Database
        mAuth = FirebaseAuth.getInstance();                                             // initialize FirebaseAuth instance
        mAuth.addAuthStateListener(this);

        mView = view;
        this.toDos = new ArrayList<>();
    }

    // TODO: Sort functions


    /* FIREBASE RTDB LISTENERS */
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        ToDo toDo = snapshot.getValue(ToDo.class);
        assert toDo != null;
        toDo.setID(snapshot.getKey());
        toDo.setUid(mAuth.getUid());

        toDos.add(toDo);
        mView.addTodo(toDo);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//        Log.d("TAG", "onChildChanged: " + previousChildName);

        ToDo toDo = snapshot.getValue(ToDo.class);

        if (toDo == null ) { return; }

        toDo.setID(snapshot.getKey());
        toDo.setUid(mAuth.getUid());

        Log.d("TAG", "onChildChanged: "  + snapshot.getKey());

        int index;
        int pos;

        if (previousChildName != null) {
             index = IntStream.range(0, toDos.size())
                    .filter(i -> previousChildName.equals(toDos.get(i).getID()))
                    .findFirst().orElse(-1);

            pos = index != -1 ? (index + 1) : index;
        } else {
            index = IntStream.range(0, toDos.size())
                    .filter(i -> toDos.get(i).getID().equals(toDo.getID()))
                    .findFirst().orElse(-1);

            pos = index;
        }

        if (pos != -1) {
            toDos.set(pos, toDo);
            mView.changeTodo(pos, toDo);
        }

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        String removedItemId = snapshot.getKey();

        toDos.removeIf(toDo -> toDo.getID().equals(removedItemId));
        mView.removeTodo(removedItemId);
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
        // Show the progressbar
        mView.showProgressBar();

        // Start startToDoListener
        startToDoListener();

        // Hide the progressbar
        mView.hideProgressBar();
    }

    public void startToDoListener(){
        // If user signed in but does not have notes
        if (mAuth.getUid() != null && mTodosRef == null) {
            String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();    // $uid
            mTodosRef = mDatabase                                                       // users/$uid/todos
                    .child(userId)
                    .child("todos");

            // Listen for the data change
            mTodosRef.addChildEventListener(this);
        }
    }

    public void stopToDoListener(){
        if (mTodosRef != null) {
            mTodosRef.removeEventListener(this);
            mTodosRef = null;
        }
    }

    @Override
    public void onSuccess(Void aVoid) {

    }

    public interface View {
        void addTodo(ToDo todo);
        void changeTodo(int pos, ToDo todo);
        void removeTodo(String todoId);
        void resetTodoList();
        void showProgressBar();
        void hideProgressBar();
    }
}
