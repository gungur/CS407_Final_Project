package com.cs407.landmarkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    // TODO: List of things in settings, connect the list with xml

    private AppDatabase appDatabase;
    private User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        appDatabase = AppDatabase.getInstance(this);

        TextView usernameText = (TextView) findViewById(R.id.usernameTxt);
        TextView emailText = (TextView) findViewById(R.id.emailTxt);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",-1);


        if(userId != -1){
            user = appDatabase.userDao().getUserById(userId).getValue();
        }

        if(user != null) {
            usernameText.setText(user.getUsername());
            emailText.setText(user.getEmail());
        }else{
            Toast toast = Toast.makeText(this,"Error Loading Data", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}