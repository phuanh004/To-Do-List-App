package com.jokers.todolist.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jokers.todolist.AddTodoActivity;
import com.jokers.todolist.LoginActivity;
import com.jokers.todolist.MainActivity;
import com.jokers.todolist.R;
import com.jokers.todolist.models.ToDo;
import com.jokers.todolist.presenters.HomeFragmentPresenter;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements HomeFragmentPresenter.View,
        FirebaseAuth.AuthStateListener, ChildEventListener {

    private FloatingActionButton mGoToAddTaskActivityFab;
//    private TextView mResultTextView;
    private HomeFragmentPresenter mPresenter;

    // For retrieved data
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String onlineUserID;
    private RecyclerView mRecyclerView;

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
//        mResultTextView = requireView().findViewById(R.id.resultTextView);

        mRecyclerView = requireView().findViewById(R.id.recyclerView);

        mPresenter = new HomeFragmentPresenter(this);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        // ACTIONS
        mGoToAddTaskActivityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTodoActivity.class);
                startActivity(intent);
            }
        });


    }

    // For retrieved data
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        // For retrieved data
        if (firebaseAuth.getUid() != null) {
            mUser = mAuth.getCurrentUser();
            onlineUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            reference = FirebaseDatabase.getInstance().getReference().child(onlineUserID)
                    .child("todos");


//            FirebaseRecyclerOptions<ToDo> options = new FirebaseRecyclerOptions.Builder<ToDo>()
//                    .setQuery(reference, ToDo.class)
//                    .build();
            FirebaseRecyclerOptions<ToDo> options = new FirebaseRecyclerOptions.Builder<ToDo>()
                    .setQuery(reference, new SnapshotParser<ToDo>() {
                        @NonNull
                        @Override
                        public ToDo parseSnapshot(@NonNull DataSnapshot snapshot) {
                            Log.d("", "parseSnapshot: " + snapshot.getRef().toString());

                            return null;
                        }
                    })
                    .build();

            FirebaseRecyclerAdapter<ToDo, MyViewHolder> adapter = new FirebaseRecyclerAdapter<ToDo, MyViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ToDo toDo) {
                    Log.d("TAG", "onBindViewHolder: " + toDo.getID());
                    
                    holder.setDueDate(toDo.getDueDate());
                    holder.setTitle(toDo.getTitle());
                    holder.setDescription(toDo.getDescription());
                }

                @NonNull
                @Override
                public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout,parent, false);
                    return new MyViewHolder(view);
                }
            };

            reference.addChildEventListener(this);
            mRecyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.d("", "onChildAdded: " + snapshot.getRef().toString());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    //     For retrieved data
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle (String title) {
            TextView taskTextView = mView.findViewById(R.id.titleTv);
            taskTextView.setText(title);
        }
        public void setDescription(String description) {
            TextView descriptionTextView = mView.findViewById(R.id.descriptionTv);
            descriptionTextView.setText(description);
        }
        public void setDueDate(String dueDate) {
            TextView dateTextView = mView.findViewById(R.id.dueDateTv);
        }
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

//        mResultTextView.setText(todoString);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}