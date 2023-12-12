package com.cs407.landmarkapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

            appDatabase.userDao().getUserById(userId).observeForever(new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    usernameText.setText(user.getUsername());
                    emailText.setText(user.getEmail());
                }
            });

        Button logout = findViewById(R.id.logoutBtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("userId",-1);
                editor.putString("username",null);
                editor.putString("password",null);
                editor.commit();

                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}