package com.jokers.todolist.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jokers.todolist.LoginActivity;
import com.jokers.todolist.R;

public class SettingsFragment extends PreferenceFragmentCompat
                                implements FirebaseAuth.AuthStateListener {

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(this);

        Preference logOutBtn = findPreference(getString(R.string.settings_logout_btn));
        logOutBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mAuth.signOut();
                return true;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
        }
    }
}