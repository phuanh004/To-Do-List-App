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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jokers.todolist.AddTodoActivity;
import com.jokers.todolist.R;
import com.jokers.todolist.models.ToDo;
import com.jokers.todolist.presenters.HomeFragmentPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements
        HomeFragmentPresenter.View {

    private FloatingActionButton mGoToAddTaskActivityFab;
    private HomeFragmentPresenter mPresenter;

    // Retrieve data
    private RecyclerView mRecyclerView;
    private static List<ToDo> mToDos;
    private MyAdapter mAdapter;

    public HomeFragment() {
        // Required empty public constructor\
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("TAG", "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView: ");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("TAG", "onViewCreated: ");

        // Declare todolist
        mToDos = new ArrayList<>();

        // Binding view
        mGoToAddTaskActivityFab = requireView().findViewById(R.id.goToAddTaskActivityFab);
        mRecyclerView = requireView().findViewById(R.id.recyclerView);

        // Declare presenter
        mPresenter = new HomeFragmentPresenter(this);

        // Removes blinks
        ((SimpleItemAnimator) Objects.requireNonNull(mRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);

        // Recyclerview Adapter
        mAdapter = new MyAdapter();

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
    public void onStop() {
        super.onStop();
        Log.d("TAG", "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d("TAG", "onDestroyView: ");
        mPresenter.stopToDoListener();
    }

    // Retrieve data
    @Override
    public void onStart() {
        super.onStart();
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class MyViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTaskTextView;
            private final TextView mDescriptionTexView;
            private final TextView mDueDate;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                // TODO: Add remains folder
                mTaskTextView = itemView.findViewById(R.id.titleTv);
                mDescriptionTexView = itemView.findViewById(R.id.descriptionTv);
                mDueDate = itemView.findViewById(R.id.dueDateTv);
            }

            private void bind(ToDo toDo) {
                mTaskTextView.setText(toDo.getTitle());
                mDescriptionTexView.setText(toDo.getDescription());
                mDueDate.setText(toDo.getDateInDisplayFormat("EEE, MMM d", toDo.getDueDate()));
            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter() {

        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.retrieved_layout, parent, false);

            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.bind(mToDos.get(position));

        }

        @Override
        public int getItemCount() {
            return mToDos.size();
        }
    }

    @Override
    public void addTodo(ToDo todo) {
        Log.d("TAG", "addTodo: " + todo.getID() + " | " + todo.getTitle());
        mToDos.add(todo);
        mAdapter.notifyItemInserted(mToDos.size());
    }

    @Override
    public void changeTodo(ToDo todo) {
    }

    @Override
    public void removeTodo(String toDoId) {
        mToDos.removeIf(toDo -> toDo.getID().equals(toDoId));
    }

    @Override
    public void resetTodoList() {
        mToDos.clear();
    }

    //  TODO: Add Progressbar
    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}