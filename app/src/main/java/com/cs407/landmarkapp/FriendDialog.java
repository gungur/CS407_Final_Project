package com.cs407.landmarkapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class FriendDialog extends AppCompatDialogFragment {
    private static User user;

    /**
     * This static method is used to be able to pass whole User information to dialogue fragment. As
     * opposed to having to wait for observer to get User information.
     * @param friend Contains User object that is passed from FriendsFragment
     * @return new FriendDialog instance that will be used to create the dialog.
     */
    public static FriendDialog newInstance(User friend){
        FriendDialog friendDialog = new FriendDialog();
        user = friend;
        return friendDialog;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View friendDialogView = layoutInflater.inflate(R.layout.friend_detailed_dialog, null);


        TextView friendUsername = friendDialogView.findViewById(R.id.friendUsername);
        friendUsername.setText(user.getUsername());

        TextView badgeAmount = friendDialogView.findViewById(R.id.friendDialogueBadgeLabel);
        String formattedBadgeLabel = getString(R.string.badge_dialog_label, user.getBadges().size());
        badgeAmount.setText(formattedBadgeLabel);

        displayFriendBadges(friendDialogView);

        AlertDialog.Builder friendDialogBuilder = new AlertDialog.Builder(getContext());


        friendDialogBuilder.setView(friendDialogView)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return friendDialogBuilder.create();
    }

    private void displayFriendBadges( View friendDialogueView) {

        LinearLayout badgeInfoLayout = friendDialogueView.findViewById(R.id.badgeInfoLayout);

        for(int badgeId : user.getBadges()){
            ImageView badgeView = new ImageView(getContext());
            badgeView.setBackgroundResource(badgeId);
            badgeView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT

            ));
            badgeInfoLayout.addView(badgeView);
        }
    }
}
