package com.cs407.landmarkapp.custom_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cs407.landmarkapp.R;
import com.cs407.landmarkapp.User;

import java.util.List;

public class IncomingFriendListAdapter extends BaseAdapter {
    private List<User> incomingFriends;
    private LayoutInflater layoutInflater;
    public IncomingFriendListAdapter(Context context, List<User> incomingFriends){
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
        convertView = layoutInflater.inflate(R.layout.incoming_friend_request_list_item, parent);
        TextView incomingFriendTextView = convertView.findViewById(R.id.incomingFriendUsername);
        incomingFriendTextView.setText(incomingFriends.get(position).getUsername());
        return convertView;
    }
}
