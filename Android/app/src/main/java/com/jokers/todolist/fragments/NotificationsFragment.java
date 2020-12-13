package com.jokers.todolist.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jokers.todolist.R;
import com.jokers.todolist.adapters.NotificationAdapter;
import com.jokers.todolist.models.ToDo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Notification fragment
 *
 * @author Anh Pham
 */
public class NotificationsFragment extends Fragment {

    DatabaseReference mToDoRef;
    // Declare Variables
    private RecyclerView mNotificationRecyclerView;
    private NotificationAdapter mAdapter;
    private List<ToDo> mToDos;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNotificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);

        // New list
        mToDos = new ArrayList<>();

        // Get to do list from firebase if user already login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            fetchBDToDos(user.getUid());
        }


        // Removes blinks
        ((SimpleItemAnimator) Objects.requireNonNull(mNotificationRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);

        // Recyclerview Adapter
        mAdapter = new NotificationAdapter(mToDos);

        // Standard setup
        mNotificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotificationRecyclerView.setAdapter(mAdapter);
        mNotificationRecyclerView.setHasFixedSize(true);
    }

    public void fetchBDToDos(String userId) {
        mToDoRef = FirebaseDatabase.getInstance().getReference()                                                      // $uid/todos
                .child(userId)
                .child("todos");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ToDo toDo = postSnapshot.getValue(ToDo.class);
                    assert toDo != null;

                    // Add due today and overdue to-do to the list
                    if (toDo.getRemainingDays() != null) {
                        if (toDo.getRemainingDays() <= 0) {
                            mToDos.add(toDo);
                            mAdapter.notifyItemInserted(mToDos.size());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        mToDoRef.addListenerForSingleValueEvent(postListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

}