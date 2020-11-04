package com.jokers.todolist.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivityPresenter implements OnCompleteListener<AuthResult> {

    private final SignUpActivityPresenter.View view;
    private final FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // TODO: Convert to User object
    private String mUserDisplayName;

    public SignUpActivityPresenter(FirebaseAuth mAuth, SignUpActivityPresenter.View view) {
        // Initialize authentication info
        this.mAuth = mAuth;

        // Initialize view
        this.view = view;
    }

    /**
     * Sign Up using user account and password
     *
     * @param email           user's email
     * @param password        user's email
     * @param userDisplayName user's displayName
     */
    public void signUpByEmail(String email, String password, String userDisplayName) {
        // Display progress bar
        view.showProgressBar();

        Log.d("SIGNUP", "signUpByEmail: " + email + "/" + password + "/" + userDisplayName);

        // Sign Up
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this);

        // Set display name
        this.mUserDisplayName = userDisplayName;
    }

    /**
     * Update user display name
     *
     * @param displayName User's display name
     */
    private void setUserDisplayName(String displayName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        mUser.updateProfile(profileUpdates);
    }


    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        // Hide progress bar
        view.hideProgressBar();

        // Print success message if success
        if (task.isSuccessful()) {
            view.displaySuccessMsg();

            // Set current user
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            setUserDisplayName(mUserDisplayName);

            // TODO: Auto login user in
        } else {
            view.displayFailedMsg();
        }
    }

    public interface View {
        void showProgressBar();

        void hideProgressBar();

        void displaySuccessMsg();

        void displayFailedMsg();
    }

}
