package com.jokers.todolist;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /* Define class variables */
    protected EditText logName;
    protected EditText logPass;
    protected TextView numberAttempts;
    protected Button logButton;

    protected String userName = "Admin";
    protected String userPassword = "1234";

    protected boolean isValid = false;
    protected int counters = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /* Bind the XML views to Java Code Elements */
        logName = findViewById(R.id.logNameID);
        logPass = findViewById(R.id.logPassID);
        numberAttempts = findViewById(R.id.numberAttemptsID);
        logButton = findViewById(R.id.logID);

        /* Setting up login button when it is clicked */
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Obtain user inputs */
                String inputName = logName.getText().toString();
                String inputPassword = logPass.getText().toString();

                /* Validate the user inputs */
                if(inputName.isEmpty() || inputPassword.isEmpty())
                {

                    Toast.makeText(MainActivity.this, "Please enter User Name (ID) and Password!", Toast.LENGTH_LONG).show();

                }else {

                    isValid = validate(inputName, inputPassword);

                    /* If not valid */
                    if (!isValid) {

                        counters--;

                        Toast.makeText(MainActivity.this, "Invalid User Name (ID) or Password ", Toast.LENGTH_LONG).show();

                        numberAttempts.setText("No. attempts remaining: " + counters);


                        /* Disable the login button if there are no attempts left */
                        if (counters == 0) {
                            logButton.setEnabled(false);
                            Toast.makeText(MainActivity.this, "You have reached the last attempts!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                    /* If valid */
                    else {

                        Toast.makeText(MainActivity.this, "Successful Login", Toast.LENGTH_LONG).show();

                        /* Allow the user in to your app by going into the next activity */
                        startActivity(new Intent(MainActivity.this, NextFragment.class));
                    }

                }
            }
        });
    }

    protected boolean validate(String inputName, String inputPassword) {

        if(inputName.equals(userName) && inputPassword.equals(userPassword))
        {
            return true;
        }
        return false;
    }
}