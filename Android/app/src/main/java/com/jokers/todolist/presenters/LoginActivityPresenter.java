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

    private View view;
    private final FirebaseAuth mAuth;

    public LoginActivityPresenter(FirebaseAuth mAuth, View view) {
        this.mAuth = mAuth;
        this.view = view;
    }

    public void login(String email, String password) {
        view.showProgressBar();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this);
    }
    public void guestLogin() {
        view.showProgressBar();
        mAuth.signInAnonymously().addOnCompleteListener(this);
    }
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        view.hideProgressBar();
    }

    public interface View {
        void showProgressBar();
        void hideProgressBar();
    }

}