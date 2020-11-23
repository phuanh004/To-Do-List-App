package com.jokers.todolist.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jokers.todolist.LoginActivity;
import com.jokers.todolist.R;
import com.jokers.todolist.adapters.ToDoListAdapter;

public class SettingFragment extends Fragment implements FirebaseAuth.AuthStateListener {

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    // Declare view ids
    private Button mLogOutButton;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        // Binding view
        mLogOutButton = requireView().findViewById(R.id.logOutButton);

        // ACTIONS
        mLogOutButton.setOnClickListener(v -> mAuth.signOut());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        // Get current user from FirebaseAuth
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // If user login
        if (currentUser != null) {
            // TODO: DISPLAY USER DATA
//            Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();
        }

        // If not, navigate user to LoginActivity
        else {
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
    }
}