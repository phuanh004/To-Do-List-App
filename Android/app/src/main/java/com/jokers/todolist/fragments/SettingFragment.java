package com.jokers.todolist.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jokers.todolist.LoginActivity;
import com.jokers.todolist.R;
import com.jokers.todolist.models.Point;

import java.util.Objects;

public class SettingFragment extends Fragment implements FirebaseAuth.AuthStateListener {

    // P
    Point point;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    // Declare view ids
    private Boolean isNightMode = null;
    private Button mLogOutButton;
    private Switch nightModeSwt;
    private SharedPreferences mPreferences;
    private SharedPreferences nightPreferences;
    private TextView mUserNameTextView, mPointCounterTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup the points on shared preferences for updateTotalPoint()
        mPreferences = requireActivity().getSharedPreferences("points", Context.MODE_PRIVATE);
        nightPreferences = requireActivity().getSharedPreferences("nightMode", Context.MODE_PRIVATE);



        point = new Point();

        // Setup Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        // Binding view

        mLogOutButton = requireView().findViewById(R.id.logOutButton);
        mUserNameTextView = requireView().findViewById(R.id.userNameTextView);
        mPointCounterTextView = requireView().findViewById(R.id.pointCounterTextView);
        nightModeSwt = requireView().findViewById(R.id.nightModeSwitch);

        if (isNightMode != null)
        {
            // Read the value from setting pref
            isNightMode = nightPreferences.getBoolean("isDark",  false);
            nightModeSwt.setChecked(isNightMode);
        }

        // ACTIONS
        mLogOutButton.setOnClickListener(v -> {
            // If anonymous user, delete it before sign out
            if (Objects.requireNonNull(mAuth.getCurrentUser()).isAnonymous()) {
                mAuth.getCurrentUser().delete();
            }
            mAuth.signOut();
        });





        nightModeSwt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (nightModeSwt.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                isNightMode = true;
                SharedPreferences.Editor editor = nightPreferences.edit();
                editor.putBoolean("isDark", isNightMode);
                editor.apply();
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                isNightMode = false;
                SharedPreferences.Editor editor = nightPreferences.edit();
                editor.putBoolean("isDark", isNightMode);
                editor.apply();
            }
        });
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        // Get current user from FirebaseAuth
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // If user login
        if (currentUser != null) {
            // Update name
            mUserNameTextView.setText("Name: " + currentUser.getDisplayName());

            // Update user's points
            loadPoints();
            mPointCounterTextView.setText("Current points: " + point.getPoints());
        }

        // If not, navigate user to LoginActivity
        else if (getContext() != null){
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private void loadPoints() {
        int p = mPreferences.getInt("total", 0);    // Point
        point.setPoints(p);
    }
}

