package com.jokers.todolist.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.jokers.todolist.R;
import com.jokers.todolist.adapters.LogbookAdapter;
import com.jokers.todolist.models.ToDo;
import com.jokers.todolist.presenters.LogbookFragmentPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class LogbookFragment extends Fragment implements LogbookFragmentPresenter.View {

    private LogbookFragmentPresenter mPresenter;

    // Retrieve data
    private RecyclerView mRecyclerView;
    private ProgressBar mLoading;
    private List<ToDo> mToDos;
    private TextView mNoLogbookTextView;
    private LogbookAdapter mAdapter;

    public LogbookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logbook, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Declare todolist
        mToDos = new ArrayList<>();

        // Binding view
        mRecyclerView = requireView().findViewById(R.id.logbookrecyclerView);
        mLoading = requireView().findViewById(R.id.toDoLogbookProgressBar);
        mNoLogbookTextView = requireView().findViewById(R.id.noLogbookTextView);

        // Declare presenter
        mPresenter = new LogbookFragmentPresenter(this);

        // Removes blinks
        ((SimpleItemAnimator) Objects.requireNonNull(mRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);

        // Recyclerview Adapter
        mAdapter = new LogbookAdapter(mToDos);

        // Standard setup
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.stopToDoListener();
    }

    @Override
    public void addTodo(ToDo todo) {
        mToDos.add(todo);
        mAdapter.notifyItemInserted(mToDos.size());
    }

    @Override
    public void changeTodo(ToDo todo) {
    }

    @Override
    public void removeTodo(String id) {
        mToDos.removeIf(toDo -> toDo.getID().equals(id));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void resetTodoList() {
        mToDos.clear();
    }

    @Override
    public void showEmptyLogbookMessage() {
        mNoLogbookTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyLogbookMessage() {
        mNoLogbookTextView.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mLoading.setVisibility(View.INVISIBLE);
    }
}