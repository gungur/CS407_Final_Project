package com.cs407.landmarkapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FriendsFragment extends Fragment {
    private final List<User> userFriends = new ArrayList<>();
    private AppDatabase appDatabase;
    private String searchInput;
    private User appUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        appDatabase = AppDatabase.getInstance(this.getContext());

        /* Since the LiveData Object that is returned, gets returned Asynchronously, we need an observer
        to watch for when it does eventually resolve into the User object.
         */
        appDatabase.userDao().getUserById(getUserIdFromPrefs()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null) return;
                appUser = user;

                if ((user.getFriends() == null || user.getFriends().size() == 0)) {
                     if (userFriends.isEmpty()) generateTestFriends();
                } else if(userFriends.isEmpty()){
                    for (int friendId : user.getFriends()) {
                        appDatabase.userDao().getUserById(friendId).observe(getViewLifecycleOwner(), new Observer<User>() {
                            @Override
                            public void onChanged(User friend) {
                                userFriends.add(friend);
                                displayFriends(userFriends);
                            }
                        });
                    }
                }

            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    /**
     * Method to display the list of friends passed as arguments.
     * First it checks to see if the noMatchingFriendsTextView is there, if so, then it will remove it
     * and the replace it with the list view.
     * Then will insert friendsToDisplay into the friends list view.
     * @param friendsToDisplay
     */
    private void displayFriends(List<User> friendsToDisplay) {
        TextView noMatchingFriendsTextView = getView().findViewById(R.id.noMatchingFriendsTextView);

        ListView friendsListView = getView().findViewById(R.id.friendsListView);

        if(noMatchingFriendsTextView != null && friendsListView == null ){

            RelativeLayout friendsFragmentParent = (RelativeLayout) noMatchingFriendsTextView.getParent();

            int viewIndex = friendsFragmentParent.indexOfChild(noMatchingFriendsTextView);

            friendsFragmentParent.removeView(noMatchingFriendsTextView);
            friendsListView = new ListView(requireContext());
            friendsListView.setId(R.id.friendsListView);
            friendsFragmentParent.addView(friendsListView, viewIndex);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (friendsListView.getLayoutParams());
           params.addRule(RelativeLayout.BELOW, R.id.searchLayout);


        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1,
                friendsToDisplay.stream().map(friend -> friend.getUsername()).collect(Collectors.toList()));

        friendsListView = getView().findViewById(R.id.friendsListView);

        friendsListView.setAdapter(arrayAdapter);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayFriendDialogue(friendsToDisplay.get(position).getUserId());
            }
        });

    }

    private int getUserIdFromPrefs() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    private void generateTestFriends() {
        Random random = new Random();
        String[] testFriendUserNames = {"James", "David", "Tara", "Lee", "Sam"};

        for (int i = 0; i < testFriendUserNames.length; i++) {

            List<Integer> badgeList = random.nextInt(2) == 1
                    ? Arrays.asList(R.drawable.uw_cs_building_badge, R.drawable.union_south_badge)
                    : Arrays.asList(R.drawable.union_south_badge);

            String friendEmail = testFriendUserNames[i] + i + "@gmail.com";
            String password = "testPASSWORD54389";
            List<Integer> friendsOfUserfriend = new ArrayList<>();


            userFriends.add(new User(testFriendUserNames[i], password,
                    friendEmail, friendsOfUserfriend, badgeList));

        }

        new AddFriends().execute(userFriends.stream().toArray(User[]::new));

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SearchView friendSearchView = view.findViewById(R.id.searchView);

        /*
         Handles search logic
         Everytime input changes, adjust the friends the display by filtering by search input change
         */
        friendSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchInput = newText;

                List<User> resultsMatchingInput = userFriends.stream()
                        .filter(friend -> friend.getUsername().toLowerCase().contains(newText.toLowerCase()))
                        .collect(Collectors.toList());

                if(resultsMatchingInput.size() > 0){
                    displayFriends(resultsMatchingInput);
                }else{
                   displayNoMatchingFriends();
                }

                return true;
            }
        });

        Button addFriendBtn = view.findViewById(R.id.addFriendButton);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        ListView friendsListView = view.findViewById(R.id.friendsListView);



    }

    private void displayNoMatchingFriends() {

        ListView friendsListView = getView().findViewById(R.id.friendsListView);

        if(friendsListView != null) {


            RelativeLayout friendsFragmentParent = (RelativeLayout) friendsListView.getParent();

            int viewIndex = friendsFragmentParent.indexOfChild(friendsListView);

            friendsFragmentParent.removeView(friendsListView);

            TextView noMatchingFriendsTextView = new TextView(requireContext());
            noMatchingFriendsTextView.setId(R.id.noMatchingFriendsTextView);
            noMatchingFriendsTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            noMatchingFriendsTextView.setTextSize(16);

            noMatchingFriendsTextView.setText("No matching friends found");

            friendsFragmentParent.addView(noMatchingFriendsTextView, viewIndex);
             RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) (noMatchingFriendsTextView.getLayoutParams());
             params.addRule(RelativeLayout.BELOW, R.id.searchLayout);
        }
    }

    private void displayFriendDialogue(int selectedFriendId) {
        FriendDialogue friendDialogue = new FriendDialogue();

        Bundle friendDialogueArgs = new Bundle();
        friendDialogueArgs.putInt("friendId", selectedFriendId);
        friendDialogue.setArguments(friendDialogueArgs);

        friendDialogue.show(getActivity().getSupportFragmentManager(),"Friend Dialogue");
    }

    private class AddFriends extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... friendsToSave) {
            List<Integer> friendIds = new ArrayList<>();

            for(User friend : friendsToSave){
                int idOfSavedFriend =  (int) appDatabase.userDao().insertUser(friend);
                friendIds.add(idOfSavedFriend);
            }
            appUser.setFriends(friendIds);
            appDatabase.userDao().updateUser(appUser);
            return null;
        }

    }




}