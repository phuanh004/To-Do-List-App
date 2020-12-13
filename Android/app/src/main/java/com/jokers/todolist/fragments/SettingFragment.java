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
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    //user Points
    Point point;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    // Declare view ids
    private Boolean isNightMode = null;
    private Button mLogOutButton;
    private Switch nightModeSwt;
    private SharedPreferences mPreferences;
    private SharedPreferences nightPreferences;
    private TextView mUserNameTextView, mPointCounterTextView, mUserRankTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup the points on shared preferences for updateTotalPoint()
        mPreferences = requireActivity().getSharedPreferences("points", Context.MODE_PRIVATE);
        nightPreferences = requireActivity().getSharedPreferences("nightMode", Context.MODE_PRIVATE);

        //new point
        point = new Point();

        // Setup Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        // Binding view
        mLogOutButton = requireView().findViewById(R.id.logOutButton);
        mUserNameTextView = requireView().findViewById(R.id.userNameTextView);
        mPointCounterTextView = requireView().findViewById(R.id.pointCounterTextView);
        mUserRankTextView = requireView().findViewById(R.id.rankTextView);
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

        //nightmode
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
        int p = mPreferences.getInt("total", 0);    // Point
        point.setPoints(p);
    }

    private int loadRank(int points){
        return points / 3;
    }

}

