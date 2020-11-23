package com.jokers.todolist.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jokers.todolist.AddTodoActivity;
import com.jokers.todolist.R;
import com.jokers.todolist.adapters.ToDoListAdapter;
import com.jokers.todolist.models.ToDo;
import com.jokers.todolist.presenters.HomeFragmentPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements HomeFragmentPresenter.View {

    private FloatingActionButton mGoToAddTaskActivityFab;
    private HomeFragmentPresenter mPresenter;

    // Retrieve data
    private RecyclerView mRecyclerView;
    private ProgressBar mLoading;
    private List<ToDo> mToDos;
    private ToDoListAdapter mAdapter;

    public HomeFragment() {
        // Required empty public constructor\
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mPresenter != null) { mPresenter.startToDoListener(); }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Declare todolist
        mToDos = new ArrayList<>();

        // Binding view
        mGoToAddTaskActivityFab = requireView().findViewById(R.id.goToAddTaskActivityFab);
        mRecyclerView = requireView().findViewById(R.id.recyclerView);
        mLoading = requireView().findViewById(R.id.toDoHomeProgressBar);

        // Show progressbar
        showProgressBar();

        // Declare presenter
        mPresenter = new HomeFragmentPresenter(this);

        // Removes blinks
        ((SimpleItemAnimator) Objects.requireNonNull(mRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);

        // Recyclerview Adapter
        mAdapter = new ToDoListAdapter(mToDos);

        // Standard setup
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        // ACTIONS
        mGoToAddTaskActivityFab.setOnClickListener(v ->
            startActivity(
                new Intent(getActivity(), AddTodoActivity.class)
            )
        );
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
    public void changeTodo(int pos, ToDo todo) {
        mToDos.set(pos, todo);
        mAdapter.notifyItemChanged(pos);
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
    public void showProgressBar() {
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mLoading.setVisibility(View.INVISIBLE);
    }


}