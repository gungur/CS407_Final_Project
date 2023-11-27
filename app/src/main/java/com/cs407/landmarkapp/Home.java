package com.cs407.landmarkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.room.Delete;
import androidx.room.Room;
import androidx.room.RoomDatabase;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    FriendsFragment friendsFragment = new FriendsFragment();

    public static AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Room Database
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "my-database").build();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // make sure begins with home frag
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setSelectedItemId(R.id.home);

        // bottom nav action
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.friends) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, friendsFragment).commit();
                    return true;
                }
                return false;
            }
        });

        appDatabase = AppDatabase.getInstance(this);
        // DONT TOUCH
        // new DeleteUserTask().execute(2);
    }


    private static class DeleteUserTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... userIds) {
            int userId = userIds[0];
            appDatabase.userDao().deleteUserById(userId);
            return null;
        }
    }
}