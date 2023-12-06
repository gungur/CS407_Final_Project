package com.cs407.landmarkapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class FriendDialogue extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();

        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View friendDialogeView = layoutInflater.inflate(R.layout.friend_detailed_dialogue, null);

        TextView friendUsername = friendDialogeView.findViewById(R.id.friendUsername);
        friendUsername.setText(arguments.getString("friendUsername"));

        AlertDialog.Builder friendDialogueBuilder = new AlertDialog.Builder(getContext());
        friendDialogueBuilder.setView(friendDialogeView)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return friendDialogueBuilder.create();
    }


}
