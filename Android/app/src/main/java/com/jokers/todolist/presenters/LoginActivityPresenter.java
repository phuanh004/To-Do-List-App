package com.jokers.todolist.presenters;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Anh Pham
 */
public class LoginActivityPresenter implements OnCompleteListener<AuthResult> {

    private final View mView;
    private final FirebaseAuth mAuth;

    public LoginActivityPresenter(FirebaseAuth mAuth, View view) {
        this.mAuth = mAuth;
        this.mView = view;
    }

    public void loginByEmail(String email, String password) {
        mView.showProgressBar();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this);
    }

    public void guestLogin() {
        mView.showProgressBar();
        mAuth.signInAnonymously().addOnCompleteListener(this);
    }
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (!task.isSuccessful()) {
            mView.showFailedMessage();
        }

        mView.hideProgressBar();
    }

    public interface View {
        void showFailedMessage();
        void showProgressBar();
        void hideProgressBar();
    }

}