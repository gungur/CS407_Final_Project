package com.cs407.landmarkapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeUsername extends AppCompatActivity {

    private AppDatabase appDatabase;
    private TextView newUsernameTxt;
    private String newUsername;
    private String currUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        appDatabase = AppDatabase.getInstance(this);

        Button changeBtn = findViewById(R.id.changePasswordBtn);
        TextView currUsernameTxt = findViewById(R.id.currUsernameTxt);
        newUsernameTxt = findViewById(R.id.newUsernameTxt);
        TextView backArrow = findViewById(R.id.backArrowChangePassword);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",-1);

        appDatabase.userDao().getUserById(userId).observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currUsernameTxt.setText(user.getUsername());
                currUsername = user.getUsername();
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUsername = newUsernameTxt.getText().toString();
                new FormCheckTask().execute(newUsername);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeUsername.this,SettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    private class FormCheckTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            UserDao userDao = appDatabase.userDao();

            return (userDao.checkUsernameExists(newUsername) > 0);
        }

        @Override
        protected void onPostExecute(Boolean userExists) {
            if (userExists) {
                Toast.makeText(ChangeUsername.this, "Username Already Exists", Toast.LENGTH_SHORT).show();
            } else {
                continueSignUp();
            }
        }
    }

    private void continueSignUp() {

        if(newUsername.isEmpty()) {
            Toast.makeText(ChangeUsername.this, "Username Can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        new UpdateUserInfo().execute();
    }

    private class UpdateUserInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            User updatedUser = appDatabase.userDao().getUserByUsername(currUsername);
            updatedUser.setUsername(newUsername);
            appDatabase.userDao().updateUser(updatedUser);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ChangeUsername.this, "Username Changed!", Toast.LENGTH_SHORT).show();

            SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username",newUsername).commit();

            Intent intent = new Intent(ChangeUsername.this, SettingsActivity.class);
            startActivity(intent);
        }
    }
}