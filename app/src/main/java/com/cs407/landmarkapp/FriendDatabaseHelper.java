package com.cs407.landmarkapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class FriendDatabaseHelper {

    private User appUser;
    private AppDatabase appDatabase;
    private final Context context;
    public FriendDatabaseHelper(User appUser, Context context){
        this.context = context;
        this.appUser = appUser;
        appDatabase = AppDatabase.getInstance(context);
    }

    public void acceptFriendRequest(User friendRequestRecipient){
        new AcceptFriendRequest().execute(friendRequestRecipient);

    }

    public void rejectFriendRequest(User friendRequestRecipient){
        new RejectFriendRequest().execute(friendRequestRecipient);
    }

    public void cancelSentFriendRequest(User friendRequestRecipient){
        new CancelSentFriendRequest().execute(friendRequestRecipient);
    }

    private class AcceptFriendRequest extends AsyncTask<User, Void, Void>{

        @Override
        protected Void doInBackground(User... users) {
            User friendRequestSender = users[0];

            friendRequestSender.getSentFriendRequests().remove(new Integer(appUser.getUserId()));
            appUser.getIncomingFriendRequests().remove(new Integer(friendRequestSender.getUserId()));

            appUser.getFriends().add(friendRequestSender.getUserId());
            friendRequestSender.getFriends().add(appUser.getUserId());

            appDatabase.userDao().updateUser(appUser);
            appDatabase.userDao().updateUser(friendRequestSender);
            return null;
        }
    }

    private class RejectFriendRequest extends AsyncTask<User, Void, Void>{

        @Override
        protected Void doInBackground(User... users) {
            User friendRequestSender = users[0];

            appUser.getIncomingFriendRequests().remove(new Integer(friendRequestSender.getUserId()));
            appDatabase.userDao().updateUser(appUser);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(context, "Friend request ignored!", Toast.LENGTH_SHORT).show();
        }
    }
    private class CancelSentFriendRequest extends AsyncTask<User, Void, Void>{

        @Override
        protected Void doInBackground(User... users) {
            User friendRequestRecipient = users[0];
            appUser.getSentFriendRequests().remove(new Integer(friendRequestRecipient.getUserId()));
            friendRequestRecipient.getIncomingFriendRequests().remove(new Integer(appUser.getUserId()));
            appDatabase.userDao().updateUser(appUser);
            appDatabase.userDao().updateUser(friendRequestRecipient);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(context, "Friend request cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
