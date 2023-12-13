package com.cs407.landmarkapp.custom_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cs407.landmarkapp.User;

import java.util.List;

public class FriendsListAdapter extends ArrayAdapter<User> {

    public FriendsListAdapter(@NonNull Context context, List<User> friends) {
        super(context, 0, friends);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User friend = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView usernameText = convertView.findViewById(android.R.id.text1);
        TextView badgesText = convertView.findViewById(android.R.id.text2);

        if (friend != null) {
            usernameText.setText(friend.getUsername());
            badgesText.setText("Badges: " + friend.getBadges().size());
        }

        return convertView;
    }


}
