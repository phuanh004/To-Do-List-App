package com.jokers.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jokers.todolist.models.ToDo;
import com.jokers.todolist.presenters.AddToDoActivityPresenter;

public class AddTodoActivity extends AppCompatActivity
        implements AddToDoActivityPresenter.View {

    private AddToDoActivityPresenter mPresenter;
    private Button mAddToDoBtn, mToDoWhenBtn, mToDoDueDateBtn;
    private EditText toDoTitleEditText, toDoDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        // View variable declarations
        toDoTitleEditText = findViewById(R.id.toDoTitleEditText);
        toDoDescriptionEditText = findViewById(R.id.toDoDescriptionEditText);
        mToDoWhenBtn = findViewById(R.id.toDoWhenBtn);
        mToDoDueDateBtn = findViewById(R.id.toDoDueDateBtn);
        mAddToDoBtn = findViewById(R.id.addToDoBtn);

        // Load Presenter
        mPresenter = new AddToDoActivityPresenter(this);

        // ACTIONS
        mAddToDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDo todo = getToDoFromView();

                mPresenter.setTodo(todo);
                mPresenter.addToDB();
            }
        });
    }


    @Override
    public ToDo getToDoFromView() {
        // View
        String title = toDoTitleEditText.getText().toString();
        String description = toDoDescriptionEditText.getText().toString();

        // Model
        ToDo toDo = new ToDo();

        toDo.setTitle(title);
        toDo.setDescription(description);

        return toDo;
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}