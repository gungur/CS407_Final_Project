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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;

public class FriendDialogue extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        AppDatabase appDatabase = AppDatabase.getInstance(this.getContext());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View friendDialogueView = layoutInflater.inflate(R.layout.friend_detailed_dialogue, null);

        AlertDialog.Builder friendDialogueBuilder = new AlertDialog.Builder(getContext());


        appDatabase.userDao().getUserById(arguments.getInt("friendId")).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                TextView friendUsername = friendDialogueView.findViewById(R.id.friendUsername);
                friendUsername.setText(user.getUsername());

                TextView badgeAmount = friendDialogueView.findViewById(R.id.friendDialogueBadgeLabel);
                String formattedBadgeLabel = getString(R.string.badge_dialogue_label, user.getBadges().size());
                badgeAmount.setText(formattedBadgeLabel);

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
        });

        friendDialogueBuilder.setView(friendDialogueView)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });


        return friendDialogueBuilder.create();
    }
}
