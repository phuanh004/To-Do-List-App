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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jokers.todolist.LoginActivity;
import com.jokers.todolist.R;
import com.jokers.todolist.models.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Notification fragment
 *
 * @author Guan Li
 * @author Anh Pham
 */

public class SettingFragment extends Fragment implements FirebaseAuth.AuthStateListener {

    // User Points
    Point point;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    private Button mLogOutButton, mResetTaskButton;
    private SwitchMaterial nightModeSwt;
    private SharedPreferences mPointPreferences, mNightPreferences;
    private TextView mUserNameTextView, mPointCounterTextView, mUserRankTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup the points on shared preferences for updateTotalPoint()
        mPointPreferences = requireActivity().getSharedPreferences("points", Context.MODE_PRIVATE);
        mNightPreferences = requireActivity().getSharedPreferences("nightMode", Context.MODE_PRIVATE);

        //new point
        point = new Point();

        // Setup Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        // Binding view
        mLogOutButton = requireView().findViewById(R.id.logOutButton);
        mResetTaskButton = requireView().findViewById(R.id.resetTaskButton);
        mUserNameTextView = requireView().findViewById(R.id.userNameTextView);
        mPointCounterTextView = requireView().findViewById(R.id.pointCounterTextView);
        mUserRankTextView = requireView().findViewById(R.id.rankTextView);
        nightModeSwt = requireView().findViewById(R.id.nightModeSwitch);

        // Read the night mode value from setting pref
        // Declare view ids
        Boolean isNightMode = mNightPreferences.getBoolean("isDark", false);

        // Set night mode if saved
        if (isNightMode != null) {
            // Read the value from setting pref
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

        mResetTaskButton.setOnClickListener(v -> {
            // Remove all the tasks in logbook
            String uid = mAuth.getCurrentUser().getUid();   // user id

            if (uid != null) {
                DatabaseReference logbookRef = FirebaseDatabase.getInstance().getReference()
                        .child(uid)
                        .child("logbook");  // users/$uid/logbook

                logbookRef.removeValue();
            }

            // Toast the message
            Toast.makeText(getContext(), "Your logbook is now empty!", Toast.LENGTH_SHORT).show();
        });

        // Night mode
        nightModeSwt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (nightModeSwt.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Save settings
            SharedPreferences.Editor editor = mNightPreferences.edit();
            editor.putBoolean("isDark", isChecked);
            editor.apply();
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

            //Update the User's Rank (Motavation):
            ArrayList<String> rank = new ArrayList<String>(Arrays.asList("New User","Good job","Awesome","Wonderful", "Your positivity is infectious",
                                                                            "proud of yourself","You’re amazing!",
                                                                            "lv1 Organization skill", "lv2 Organization skill",
                                                                            "lv3 Organization skill","lv4 Organization skill",
                                                                            "lv5 Organization skill", "God Organization skill",
                                                                            "lv1 Awesomeness", "lv2 Awesomeness",
                                                                            "lv3 Awesomeness","lv4 Awesomeness",
                                                                            "lv5 Awesomeness", "God lv Awesomeness"));
            mUserRankTextView.setText("Rank: " + rank.get(loadRank(point.getPoints())));
        }

        // If not, navigate user to LoginActivity
        else if (getContext() != null){
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private void loadPoints() {
        int p = mPointPreferences.getInt("total", 0);    // Point
        point.setPoints(p);
    }

    private int loadRank(int points){
        return points / 3;
    }

}

