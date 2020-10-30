package com.jokers.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.jokers.todolist.presenters.LoginActivityPresenter;

public class LoginActivity extends AppCompatActivity implements
        FirebaseAuth.AuthStateListener, LoginActivityPresenter.View{

    private FirebaseAuth mAuth;

    private LoginActivityPresenter presenter;

    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton, mSignUpButton, mAsGuestLoginButton;
    private ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Load Presenter
        presenter = new LoginActivityPresenter(mAuth, this);
        mAuth.addAuthStateListener(this);

        // TODO: Sign Up Button
        mLoadingProgressBar = findViewById(R.id.loading);
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.pwdEditText);
        mLoginButton = findViewById(R.id.loginBtn);
        mAsGuestLoginButton = findViewById(R.id.asGuestLoginBtn);
        mSignUpButton = findViewById(R.id.signUpBtn);


        // TODO: Validate data from input
        // Click on the login button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login
                presenter.loginByEmail(mEmailEditText.getText().toString(),
                        mPasswordEditText.getText().toString());
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        mAsGuestLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login
                presenter.guestLogin();
            }
        });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        // If user is valid
        // Jump back to MainActivity
        if (firebaseAuth.getUid() != null) {
            finish();
        }

    }

    @Override
    public void showProgressBar() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

}