package com.jokers.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private ToDo mToDo;
    private String mDate;

    private DateDialogOf currentDateDialog;
    enum DateDialogOf {
        DO_DATE,
        DUE_DATE
    }

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

        // Get to do id from the intent extra bundle
        Bundle bundle = getIntent().getExtras();
        assert (bundle != null);
        String Id = bundle.getString("toDoId");

        // Get value from firebase one time
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();    // $uid
            mTodosRef = mDatabase              // users/$uid/todos
                .child(userId)
                .child("todos")
                .child(Id);

        ValueEventListener postListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get ToDo object and use the values to update the UI
                mToDo = dataSnapshot.getValue(ToDo.class);
                mToDo.setID(dataSnapshot.getKey());
                mToDo.setUid(mAuth.getUid());

                // Set data to UI
                mEditTitle.setText(mToDo.getTitle());
                mEditDescription.setText(mToDo.getDescription());
                mUpdatedWhenTodoTextView.setText(
                        "Deadline: " +
                                mToDo.getDateInDisplayFormat("EEE, MMM d", mToDo.getDueDate()));
                mUpdatedDueDateTodoTextView.setText("Do it on: " +
                        mToDo.getDateInDisplayFormat("EEE, MMM d", mToDo.getDoDate()));

                // ACTIONS

                mEditWhenBtn.setOnClickListener(v -> {
                    currentDateDialog = DateDialogOf.DO_DATE;
                    showDateTimeDialog();
                });

                mEditDueDateBtn.setOnClickListener(v -> {
                    currentDateDialog = DateDialogOf.DUE_DATE;
                    showDateTimeDialog();
                });

                // Click update btn
                mUpdateBtn.setOnClickListener(v -> {

                    // Get value from UI
                    String title = mEditTitle.getText().toString();
                    String description = mEditDescription.getText().toString();

                    // Set new value into toDo
                    mToDo.setTitle(title);
                    mToDo.setDescription(description);

                    // Set value to firebase
                    mTodosRef.setValue(mToDo);
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        mTodosRef.addValueEventListener(postListener);
    }


    public void showDateTimeDialog() {
        // Declare calendar for date picker
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // date picker dialog
        @SuppressLint("SetTextI18n")
        DatePickerDialog picker = new DatePickerDialog(EditToDoActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    Date date;
                    try {
                        date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                        mDate = String.valueOf(date.getTime()).substring(0, 10); // 10 char unixtime

                        switch (currentDateDialog) {
                            case DUE_DATE:
                                mToDo.setDueDate(mDate);
                                mUpdatedDueDateTodoTextView.setText(
                                    "Deadline: " +
                                    mToDo.getDateInDisplayFormat("EEE, MMM d", mToDo.getDueDate())
                                );
                                break;
                            case DO_DATE:
                                mToDo.setDoDate(mDate);
                                mUpdatedWhenTodoTextView.setText(
                                    "Do it on: " +
                                    mToDo.getDateInDisplayFormat("EEE, MMM d", mToDo.getDoDate())
                                );
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }, year, month, day);
        picker.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}