package com.jokers.todolist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.jokers.todolist.presenters.SignUpActivityPresenter;

public class SignUpActivity extends AppCompatActivity implements
        SignUpActivityPresenter.View {

    private FirebaseAuth mAuth;

    private SignUpActivityPresenter presenter;

    private TextInputLayout mDisplayNameEditText, mEmailEditText, mPasswordEditText;
    private Button mSignUpButton;
    private ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Load Presenter
        presenter = new SignUpActivityPresenter(mAuth, this);

        // Binding view
        mLoadingProgressBar = findViewById(R.id.loadingSignUp);
        mDisplayNameEditText = findViewById(R.id.usernNameSignUpTextField);
        mEmailEditText = findViewById(R.id.emailSignUpTextField);
        mPasswordEditText = findViewById(R.id.passwordSignUpTextField);
        mSignUpButton = findViewById(R.id.signUpBtn);

        // Actions
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validate Data (Password must be at least 6 characters)
                // Get user information from view
                String email = mEmailEditText.getEditText().getText().toString();
                String password = mPasswordEditText.getEditText().getText().toString();
                String displayName = mDisplayNameEditText.getEditText().getText().toString();

                // Sign Up
                presenter.signUpByEmail(email, password, displayName);
            }
        });
    }

    @Override
    public void showProgressBar() {
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displaySuccessMsg() {
        Toast.makeText(this, "Sign up success!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayFailedMsg() {
        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
    }
}