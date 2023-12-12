package com.cs407.landmarkapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ChangePassword extends AppCompatActivity {
    private AppDatabase appDatabase;
    private EditText oldPasswordEdit;
    private String oldPassword;
    private EditText newPasswordEdit;
    private String newPassword;
    private EditText confirmPasswordEdit;
    private String confirmPassword;
    private String currUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        appDatabase = AppDatabase.getInstance(this);


        oldPasswordEdit = findViewById(R.id.oldPasswordEdit);
        newPasswordEdit = findViewById(R.id.newPasswordEdit);
        confirmPasswordEdit = findViewById(R.id.confimPasswordEdit);

        Button changeBtn = findViewById(R.id.changePasswordBtn);
        TextView backBtn = findViewById(R.id.backArrowChangePassword);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",-1);

        appDatabase.userDao().getUserById(userId).observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currUsername = user.getUsername();
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = oldPasswordEdit.getText().toString();
                newPassword = newPasswordEdit.getText().toString();
                confirmPassword = confirmPasswordEdit.getText().toString();
                new FormCheckTask().execute(oldPassword,newPassword,confirmPassword);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this,SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    private class FormCheckTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            UserDao userDao = appDatabase.userDao();
            User user = userDao.getUserByUsername(currUsername);

            if(!user.getPassword().equals(oldPassword))
                return null;

            return (user.getPassword().equals(newPassword));
        }

        @Override
        protected void onPostExecute(Boolean userExists) {
            if(userExists == null){
                Toast.makeText(ChangePassword.this,"Old Password is incorrect", Toast.LENGTH_SHORT).show();
            }else if (userExists) {
                Toast.makeText(ChangePassword.this, "New Password Must Be Different From Current Password", Toast.LENGTH_SHORT).show();
            } else {
                continueSignUp();
            }
        }
    }

    private void continueSignUp() {
        newPassword = newPasswordEdit.getText().toString();
        oldPassword = oldPasswordEdit.getText().toString();
        confirmPassword = confirmPasswordEdit.getText().toString();

        if(newPassword.isEmpty() || oldPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(ChangePassword.this, "All Input Fields Must Be Filled Out", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!newPassword.equals(confirmPassword)){
            Toast.makeText(ChangePassword.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }

        new UpdateUserInfo().execute();
    }

    private class UpdateUserInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            User updatedUser = appDatabase.userDao().getUserByUsername(currUsername);
            updatedUser.setPassword(newPassword);
            appDatabase.userDao().updateUser(updatedUser);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ChangePassword.this, "Password Changed!", Toast.LENGTH_SHORT).show();

            SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("password",newPassword).commit();


            Intent intent = new Intent(ChangePassword.this, SettingsActivity.class);
            startActivity(intent);
        }
    }
}