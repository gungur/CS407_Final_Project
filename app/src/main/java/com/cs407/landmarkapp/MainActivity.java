package com.cs407.landmarkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private EditText usernameInput;
    private EditText passwordInput;
    private String username;
    private String password;
    private Switch saveSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = AppDatabase.getInstance(this);

        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        username = sp.getString("username",null);
        password = sp.getString("password",null);

        if(password != null && username != null){
            new LoginTask().execute(username, password);
        }

        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        saveSwitch = findViewById(R.id.saveLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameInput = findViewById(R.id.usernameLoginInput);
                EditText passwordInput = findViewById(R.id.passwordLoginInput);

                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();

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
                if(saveSwitch.isChecked()) {
                    saveLoginInfo(username, password);
                }
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "User info incorrect.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserIdToPreferences(int userId){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId).apply();
    }

    private void saveLoginInfo(String username, String password){

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", username);
        editor.putString("password",password);
        editor.apply();
    }

}

