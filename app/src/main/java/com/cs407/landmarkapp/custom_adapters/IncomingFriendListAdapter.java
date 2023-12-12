package com.cs407.landmarkapp.custom_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cs407.landmarkapp.FriendDatabaseHelper;
import com.cs407.landmarkapp.R;
import com.cs407.landmarkapp.User;

import java.util.List;

public class IncomingFriendListAdapter extends BaseAdapter {
    private final List<User> incomingFriends;
    private final LayoutInflater layoutInflater;

    private final User appUser;

    private final Context context;
    public IncomingFriendListAdapter(User appUser, Context context, List<User> incomingFriends){
        this.appUser = appUser;
        this.context = context;
        this.incomingFriends = incomingFriends;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return incomingFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return incomingFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.incoming_friend_request_list_item, null);
        TextView incomingFriendTextView = convertView.findViewById(R.id.incomingFriendUsername);
        incomingFriendTextView.setText(incomingFriends.get(position).getUsername());
        Button addFriendButton = convertView.findViewById(R.id.acceptFriend);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendDatabaseHelper friendDatabaseHelper = new FriendDatabaseHelper(appUser, context);
                friendDatabaseHelper.acceptFriendRequest(incomingFriends.get(position));
            }
        });
        Button rejectFriendButton = convertView.findViewById(R.id.rejectBtn);

        rejectFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendDatabaseHelper friendDatabaseHelper = new FriendDatabaseHelper(appUser, context);
                friendDatabaseHelper.rejectFriendRequest(incomingFriends.get(position));
            }
        });

        return convertView;
    }
}
