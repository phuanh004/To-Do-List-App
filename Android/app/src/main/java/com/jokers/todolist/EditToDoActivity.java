package com.jokers.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jokers.todolist.models.ToDo;
import com.jokers.todolist.presenters.AddToDoActivityPresenter;

import java.util.List;
import java.util.Objects;

public class EditToDoActivity extends AppCompatActivity {

    private DatabaseReference mTodosRef;
    private ValueEventListener postListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private AddToDoActivityPresenter mPresenter;
    private EditText mEditTitle, mEditDescription;
    private Button mUpdateBtn, mEditWhenBtn, mEditDueDateBtn;
    private TextView mUpdatedWhenTodoTextView, mUpdatedDueDateTodoTextView;
    private ProgressBar mLoadingProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);

        mEditTitle = findViewById(R.id.editTitle);
        mEditDescription = findViewById(R.id.editDescription);
        mUpdateBtn = findViewById(R.id.updateBtn);
        mEditWhenBtn = findViewById(R.id.editWhenBtn);
        mEditDueDateBtn = findViewById(R.id.editDueDateBtn);
        mUpdatedWhenTodoTextView = findViewById(R.id.updatedWhenToDoTextView);
        mUpdatedDueDateTodoTextView = findViewById(R.id.updatedDueDateToDoTextView);
        mLoadingProgressBar = findViewById(R.id.updateToDoLoading);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String Id = bundle.getString("toDoId");

            // Get value from firebase
            String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();    // $uid
                mTodosRef = mDatabase              // users/$uid/todos
                    .child(userId)
                    .child("todos")
                    .child(Id);

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get ToDo object and use the values to update the UI
                    ToDo toDo = dataSnapshot.getValue(ToDo.class);
                    toDo.setID(dataSnapshot.getKey());
                    toDo.setUid(mAuth.getUid());

                    // Set data to UI
                    mEditTitle.setText(toDo.getTitle());
                    mEditDescription.setText(toDo.getDescription());
                    mUpdatedWhenTodoTextView.setText(
                            "Deadline: " +
                                    toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDueDate()));
                    mUpdatedDueDateTodoTextView.setText("Do it on: " +
                            toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDoDate()));

                    // Click update btn
                    mUpdateBtn.setOnClickListener(v -> {

                        // Get value from UI
                        String title = mEditTitle.getText().toString();
                        String description = mEditDescription.getText().toString();

                        // Set new value into toDo
                        toDo.setTitle(title);
                        toDo.setDescription(description);

                        // Set value to firebase
                        mTodosRef.setValue(toDo);
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };

            mTodosRef.addValueEventListener(postListener);

        }

    }
}