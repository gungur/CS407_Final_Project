package com.cs407.landmarkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // TODO: check box for remember username and password so next time the user opens the app, just needs to click "login"
    private AppDatabase appDatabase;
    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = AppDatabase.getInstance(this);

        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameInput = findViewById(R.id.usernameLoginInput);
                EditText passwordInput = findViewById(R.id.passwordLoginInput);

                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                new LoginTask().execute(username, password);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            return appDatabase.userDao().login(username, password);
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                saveUserIdToPreferences(user.getUserId());
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "User info incorrect.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserIdToPreferences(int userId){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId).apply();


    }

}

