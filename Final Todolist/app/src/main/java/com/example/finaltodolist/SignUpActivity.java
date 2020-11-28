package com.example.finaltodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText SignUpEmail, SignUpPassword;
    private Button SignUpButton;
    private TextView SignUpPage;
    private FirebaseAuth mAuth;


    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        toolbar = findViewById(R.id.SignUpToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        SignUpEmail = findViewById(R.id.SignUpEmail);
        SignUpPassword = findViewById(R.id.SignUpPassword);
        SignUpButton = findViewById(R.id.SignUpButton);
        SignUpPage = findViewById(R.id.SignUpPage);


        SignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( SignUpActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = SignUpEmail.getText().toString().trim();
                String password = SignUpPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    SignUpEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    SignUpPassword.setError("Password is required");
                    return;
                }else {
                    loader.setMessage("Sign up in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();

                            }else{
                                String error = task.getException().toString();
                                Toast.makeText(SignUpActivity.this, "Sign up failed"+ error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }


                        }
                    });

                }


            }
        });



    }
}