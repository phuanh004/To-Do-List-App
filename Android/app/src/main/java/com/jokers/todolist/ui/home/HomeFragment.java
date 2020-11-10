package com.jokers.todolist.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jokers.todolist.AddTodoActivity;
import com.jokers.todolist.R;
import com.jokers.todolist.models.ToDo;
import com.jokers.todolist.presenters.HomeFragmentPresenter;

import java.util.List;

public class HomeFragment extends Fragment implements HomeFragmentPresenter.View {

    private FloatingActionButton mGoToAddTaskActivityFab;
    private TextView mResultTextView;
    private HomeFragmentPresenter mPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGoToAddTaskActivityFab = requireView().findViewById(R.id.goToAddTaskActivityFab);
        mResultTextView = requireView().findViewById(R.id.resultTextView);

        mPresenter = new HomeFragmentPresenter(this);

        // ACTIONS
        mGoToAddTaskActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTodoActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void updateUI(List<ToDo> toDos) {
        StringBuilder todoString = new StringBuilder();

        for (ToDo toDo:
             toDos) {
            todoString.append(toDo.getTitle()).append("\n");

            // TODO: For testing purpose, remove later
            Log.d("TAG", "updateUI: " + toDo.getTitle()
                    + " | " + toDo.getDueDate()
                    + " | " + toDo.getCurrentDate()
                    + " | " + toDo.getDateRemain());
        }

        mResultTextView.setText(todoString);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}