package com.cs407.landmarkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SignUp extends AppCompatActivity {

    private AppDatabase appDatabase;
    private EditText emailInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailInput = findViewById(R.id.emailInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirmInput = findViewById(R.id.passwordConfirmInput);

        Button signUpButton = findViewById(R.id.signUpButton);
        ImageButton backButton = findViewById(R.id.backButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString().trim();
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String passwordConfirm = passwordConfirmInput.getText().toString().trim();

                new SignUpTask().execute(email, username, password, passwordConfirm);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });

        appDatabase = AppDatabase.getInstance(this);
    }

    private class SignUpTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String username = params[1];
            String password = params[2];
            String passwordConfirm = params[3];

            UserDao userDao = appDatabase.userDao();

            return (userDao.checkUsernameExists(username) > 0) || (userDao.checkEmailExists(email) > 0);
        }

        @Override
        protected void onPostExecute(Boolean userExists) {
            if (userExists) {
                Toast.makeText(SignUp.this, "Username or email already exists", Toast.LENGTH_SHORT).show();
            } else {
                continueSignUp();
            }
        }
    }

    private void continueSignUp() {
        String email = emailInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String passwordConfirm = passwordConfirmInput.getText().toString().trim();

        if(email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignUp.this, "Inputs can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignUp.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(SignUp.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }

        new InsertUserTask().execute();
    }

    private class InsertUserTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String email = emailInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            User newUser = new User(username, password, email, new ArrayList<>(),
                    new ArrayList<>(), new ArrayList<>(),
                    Arrays.asList( R.drawable.uw_cs_building_badge, R.drawable.union_south_badge));
            appDatabase.userDao().insertUser(newUser);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(SignUp.this, "Registered!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
        }
    }

}