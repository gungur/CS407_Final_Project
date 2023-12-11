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

public class SentFriendListAdapter extends BaseAdapter {
    private List<User> sentFriendsList;
    private LayoutInflater layoutInflater;
    public SentFriendListAdapter(Context context, List<User> sentFriendsList){
        layoutInflater = LayoutInflater.from(context);
        this.sentFriendsList = sentFriendsList;

    }
    @Override
    public int getCount() {
        return sentFriendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return sentFriendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.sent_friend_request_list_item, null);
        TextView sentFriendTextView = convertView.findViewById(R.id.sentFriendUsername);
        sentFriendTextView.setText(sentFriendsList.get(position).getUsername());
        Button cancelRequest = convertView.findViewById(R.id.cancelRequestBtn);

        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test", "Cancelled friend request");
            }
        });
        return convertView;
    }
}
