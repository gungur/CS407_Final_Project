package com.cs407.landmarkapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

// TODO: implement functions and stuff for the fragment
public class ProfileFragment extends Fragment {
    AppDatabase appDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        appDatabase = AppDatabase.getInstance(this.getContext());
        appDatabase.userDao().getUserById(getUserIdFromPrefs()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) return;

                TextView usernameText = view.findViewById(R.id.usernameText);
                usernameText.setText(user.getUsername());

                LinearLayout badgesContainer =  view.findViewById(R.id.badgesContainer);
                if (user.getBadges() == null) {
                    return;
                }
                for(int badgeId : user.getBadges()){
                    badgesContainer.addView(buildBadgeLayout(badgeId));
                }

            }
        });
        Button settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private int getBadgeText(int badgeId) {
        if(badgeId == R.drawable.uw_cs_building_badge){
            return R.string.cs_building_badge_label;
        }else if(badgeId == R.drawable.union_south_badge){
            return R.string.union_south_badge_label;
        }else{
            return R.string.badge_label_not_found;
        }
    }

    private int getUserIdFromPrefs() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    private LinearLayout buildBadgeLayout(int badgeId){

        LinearLayout badgeLayout = new LinearLayout(getView().getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.bottomMargin = 30;
        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        badgeLayout.setLayoutParams(layoutParams);

        ImageView badgeImage = new ImageView(getView().getContext());
        badgeImage.setImageResource(badgeId);
        LinearLayout.LayoutParams badgeImageLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        badgeImageLayoutParams.weight = 1;
        badgeImage.setLayoutParams(badgeImageLayoutParams);
        badgeLayout.addView(badgeImage);

        TextView badgeLabel = new TextView(getView().getContext());
        badgeLabel.setText(getBadgeText(badgeId));


        LinearLayout.LayoutParams badgeLabelLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        badgeLabelLayoutParams.weight = 1;
        badgeLabelLayoutParams.gravity = Gravity.CENTER;

        badgeLabel.setLayoutParams(badgeLabelLayoutParams);
        badgeLayout.addView(badgeLabel);

        return badgeLayout;
    }
}