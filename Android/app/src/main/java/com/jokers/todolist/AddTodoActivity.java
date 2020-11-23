package com.jokers.todolist;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jokers.todolist.models.ToDo;
import com.jokers.todolist.presenters.AddToDoActivityPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTodoActivity extends AppCompatActivity
        implements AddToDoActivityPresenter.View {

    enum DateDialogOf {
        DO_DATE,
        DUE_DATE
    }

    private AddToDoActivityPresenter mPresenter;
    private Button mAddToDoBtn, mToDoWhenBtn, mToDoDueDateBtn;
    private EditText mToDoTitleEditText, mToDoDescriptionEditText;
    private TextView mWhenTextView, mDueDateTextView;
    private ProgressBar mLoadingProgressBar;


    private ToDo mToDo;
    private String mDate;
    private DateDialogOf currentDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        // View variable declarations
        mWhenTextView = findViewById(R.id.whenToDoTextView);
        mDueDateTextView = findViewById(R.id.dueDateToDoTextView);
        mToDoTitleEditText = findViewById(R.id.toDoTitleEditText);
        mToDoDescriptionEditText = findViewById(R.id.toDoDescriptionEditText);
        mToDoWhenBtn = findViewById(R.id.toDoWhenBtn);
        mToDoDueDateBtn = findViewById(R.id.toDoDueDateBtn);
        mAddToDoBtn = findViewById(R.id.addToDoBtn);
        mLoadingProgressBar = findViewById(R.id.addToDoLoading);

        // Load Presenter
        mPresenter = new AddToDoActivityPresenter(this);

        // Init ToDoObject
        mToDo = new ToDo();

        // ACTIONS

        mToDoWhenBtn.setOnClickListener(v -> {
            currentDateDialog = DateDialogOf.DO_DATE;
            showDateTimeDialog();
        });

        mToDoDueDateBtn.setOnClickListener(v -> {
            currentDateDialog = DateDialogOf.DUE_DATE;
            showDateTimeDialog();
        });

        mAddToDoBtn.setOnClickListener(v -> {
            ToDo todo = getToDoFromView();

            mPresenter.setTodo(todo);
            mPresenter.addToDB();

            // Reset
            mToDo = new ToDo();
        });
    }


    @Override
    public ToDo getToDoFromView() {
        // View
        String title = mToDoTitleEditText.getText().toString();
        String description = mToDoDescriptionEditText.getText().toString();
        String createdDate = String.valueOf(System.currentTimeMillis() / 1000L);   // Unix time

        // Model

        mToDo.setTitle(title);
        mToDo.setDescription(description);
        mToDo.setCreatedDate(createdDate);

        return mToDo;
    }

    @Override
    public void showDateTimeDialog() {
        // Declare calendar for date picker
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // date picker dialog
        @SuppressLint("SetTextI18n")
        DatePickerDialog picker = new DatePickerDialog(AddTodoActivity.this,
            (view, year1, monthOfYear, dayOfMonth) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                Date date;
                try {
                    date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    mDate = String.valueOf(date.getTime()).substring(0, 10); // 10 char unixtime

                    switch (currentDateDialog) {
                        case DUE_DATE:
                            mToDo.setDueDate(mDate);
                            mDueDateTextView.setText(
                                    "Deadline: " +
                                    mToDo
                                    .getDateInDisplayFormat("EEE, MMM d", mToDo.getDueDate()));
                            break;
                        case DO_DATE:
                            mToDo.setDoDate(mDate);
                            mWhenTextView.setText(
                                    "Do it on: " +
                                    mToDo
                                    .getDateInDisplayFormat("EEE, MMM d", mToDo.getDoDate()));
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }, year, month, day);
        picker.show();
    }

    @Override
    public void showProgressBar() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showSuccessMessage() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedMessage() {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backToMainActivity() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}