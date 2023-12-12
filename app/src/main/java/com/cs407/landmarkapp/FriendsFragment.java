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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.cs407.landmarkapp.custom_adapters.IncomingFriendListAdapter;
import com.cs407.landmarkapp.custom_adapters.SentFriendListAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FriendsFragment extends Fragment {
    private  List<User> userFriends = new ArrayList<>();
    private AppDatabase appDatabase;
    private String searchInput;
    private User appUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        appDatabase = AppDatabase.getInstance(this.getContext());
        //new AddTestUsers().execute();


        /* Since the LiveData Object that is returned, gets returned Asynchronously, we need an observer
        to watch for when it does eventually resolve into the User object.
         */
        appDatabase.userDao().getUserById(getUserIdFromPrefs()).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) return;

                appUser = user;
               // if(getView().findViewById(R.id.friendsListView) != null || getView().findViewById(R.id.noMatchingFriendsTextView) != null) {

                    if ((user.getFriends() == null || user.getFriends().size() == 0)) {
                        if (userFriends.isEmpty()) generateTestFriends();
                    } else {
                        appDatabase.userDao().getUsersByIds(user.getFriends()).observe(getViewLifecycleOwner(), new Observer<List<User>>() {
                            @Override
                            public void onChanged(List<User> updatedFriends) {
                                userFriends = updatedFriends;
                                TabLayout tabLayout = getView().findViewById(R.id.tabs);
                                tabLayout.getTabAt(0).select();
                                displayFriends(updatedFriends);
                            }
                        });

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
     *
     * @param friendsToDisplay list of user friends to display
     */
    private void displayFriends(List<User> friendsToDisplay) {
        FrameLayout frameLayout = getView().findViewById(R.id.contentFrame);

        ListView friendsListView = getView().findViewById(R.id.friendsListView);

        if (friendsListView == null) {

            frameLayout.removeAllViews();
            friendsListView = new ListView(requireContext());
            friendsListView.setId(R.id.friendsListView);
            frameLayout.addView(friendsListView);

        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1,
                friendsToDisplay.stream().map(friend -> friend.getUsername()).collect(Collectors.toList()));

        friendsListView = getView().findViewById(R.id.friendsListView);

        friendsListView.setAdapter(arrayAdapter);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayFriendDialog(friendsToDisplay.get(position));
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
                    : Collections.singletonList(R.drawable.union_south_badge);

            String friendEmail = testFriendUserNames[i] + i + "@gmail.com";
            String password = "testPASSWORD54389";
            List<Integer> friendsOfUserFriend = new ArrayList<>();


            userFriends.add(new User(testFriendUserNames[i], password,
                    friendEmail, friendsOfUserFriend, new ArrayList<>(), new ArrayList<>(), badgeList));

        }

        new AddTestFriends().execute(userFriends.stream().toArray(User[]::new));

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
                TabLayout tabLayout = view.findViewById(R.id.tabs);
                tabLayout.getTabAt(0).select();
                if (resultsMatchingInput.size() > 0) {
                    displayFriends(resultsMatchingInput);
                } else {
                    displayNoMatchingFriends();
                }

                return true;
            }
        });

        Button addFriendBtn = view.findViewById(R.id.addFriendButton);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchInput == null || searchInput.equals("")){
                    Toast.makeText(requireContext(), "Enter non empty value", Toast.LENGTH_SHORT).show();
                    return;
                }


                for(User friend : userFriends){
                    if(searchInput.equals(friend.getUsername())){
                        Toast.makeText(requireContext(), "Friend is already added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                new ValidatePendingFriend().execute(searchInput);
            }
        });
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
                FrameLayout frameLayout = view.findViewById(R.id.contentFrame);
                if (tab.getPosition() == 0) {

                    if(frameLayout.getChildAt(0) != view.findViewById(R.id.friendsListView)){
                        frameLayout.removeAllViews();
                        ListView listView = new ListView(requireContext());
                        listView.setId(R.id.friendsListView);
                        frameLayout.addView(listView);
                        displayFriends(userFriends);

                    }

                } else if (tab.getPosition() == 1) {

                    if (frameLayout.getChildAt(0) != view.findViewById(R.id.pendingFriendsRoot)) {
                        frameLayout.removeAllViews();
                        layoutInflater.inflate(R.layout.pending_friends, frameLayout);

                    }
                    ListView sentFriendsView = view.findViewById(R.id.sentFriendsListView);
                    appDatabase.userDao().getUsersByIds(appUser.getSentFriendRequests()).observe(getViewLifecycleOwner(), new Observer<List<User>>() {
                        @Override
                        public void onChanged(List<User> userSentFriendRequests) {
                            SentFriendListAdapter sentFriendListAdapter = new SentFriendListAdapter(getContext(), userSentFriendRequests, appUser);
//
                            sentFriendsView.setAdapter(sentFriendListAdapter);
                        }
                    });

                    ListView incomingFriendsView = view.findViewById(R.id.incomingFriendsListView);
                    appDatabase.userDao().getUsersByIds(appUser.getIncomingFriendRequests()).observe(getViewLifecycleOwner(), new Observer<List<User>>() {
                        @Override
                        public void onChanged(List<User> incomingFriendRequests) {

                            IncomingFriendListAdapter incomingFriendListAdapter = new IncomingFriendListAdapter(appUser, getContext(), incomingFriendRequests);
                            incomingFriendsView.setAdapter(incomingFriendListAdapter);
                        }
                    });

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void displayNoMatchingFriends() {

        ListView friendsListView = getView().findViewById(R.id.friendsListView);

        if (friendsListView != null) {

            FrameLayout frameLayout = getView().findViewById(R.id.contentFrame);
            frameLayout.removeAllViews();
            TextView noMatchingFriendsTextView = new TextView(requireContext());
            noMatchingFriendsTextView.setId(R.id.noMatchingFriendsTextView);
            noMatchingFriendsTextView.setTextSize(16);

            noMatchingFriendsTextView.setText(R.string.friends_not_found);
            frameLayout.addView(noMatchingFriendsTextView);

        }

    }

    private void displayFriendDialog(User selectedFriend) {
        FriendDialog friendDialog = FriendDialog.newInstance(selectedFriend);

        friendDialog.show(getActivity().getSupportFragmentManager(), "Friend Dialog");
    }

    private void displayPendingFriends(View view) {
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        FrameLayout frameLayout = view.findViewById(R.id.contentFrame);


        if (frameLayout.getChildAt(0) != view.findViewById(R.id.pendingFriendsRoot)) {
            frameLayout.removeAllViews();
            layoutInflater.inflate(R.layout.pending_friends, frameLayout);

        }
        ListView sentFriendsView = view.findViewById(R.id.sentFriendsListView);
        if(appUser.getSentFriendRequests().size() > 0){
            List<User> userSentFriendRequests = new ArrayList<>();
            for (int userSentFriendRequestId : appUser.getSentFriendRequests()) {
                appDatabase.userDao().getUserById(userSentFriendRequestId).observe(getViewLifecycleOwner(),
                        new Observer<User>() {
                            @Override
                            public void onChanged(User user) {
                                if (appUser.getSentFriendRequests().size() > 0) {


                                    userSentFriendRequests.add(user);
                                    SentFriendListAdapter sentFriendListAdapter = new SentFriendListAdapter(getContext(), userSentFriendRequests, appUser);

                                    sentFriendsView.setAdapter(sentFriendListAdapter);

                                }
                            }
                        });
            }
        }else{
            SentFriendListAdapter sentFriendListAdapter = new SentFriendListAdapter(getContext(), new ArrayList<>(), appUser);

            sentFriendsView.setAdapter(sentFriendListAdapter);
        }

    }

    private class AddTestFriends extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... friendsToSave) {
            List<Integer> friendIds = new ArrayList<>();

            for (User friend : friendsToSave) {
                int idOfSavedFriend = (int) appDatabase.userDao().insertUser(friend);
                friendIds.add(idOfSavedFriend);
            }
            appUser.setFriends(friendIds);
            appDatabase.userDao().updateUser(appUser);
            return null;
        }

    }

    private class AddTestUsers extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String[] testUsers = {"Luke", "Alejandra", "Xavier", "Liz"};
            for(String user : testUsers){
                User testUser = new User(user, "testUserPassword2023", user+"@gmail.com", new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());
                appDatabase.userDao().insertUser(testUser);
            }

            return null;
        }
    }

    private class ValidatePendingFriend extends AsyncTask<String, Void, User>{

        @Override
        protected User doInBackground(String... strings) {
            if(strings == null) return null;

            String friendUsername = strings[0];
            appDatabase.userDao().checkUsernameExists(friendUsername);
            return appDatabase.userDao().getUserByUsername(friendUsername);
        }

        @Override
        protected void onPostExecute(User user) {
           if(user != null){
               new AddFriend().execute(user);
           }else{
               Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
           }
        }
    }

    private class AddFriend extends AsyncTask<User, Void, Void>{

        @Override
        protected Void doInBackground(User... users) {
            User pendingFriend = users[0];
            if(appUser.getSentFriendRequests() == null){
                appUser.setSentFriendRequests(new ArrayList<>());
            }
            appUser.getSentFriendRequests().add(pendingFriend.getUserId());
            if (pendingFriend.getIncomingFriendRequests() == null){
                pendingFriend.setIncomingFriendRequests(new ArrayList<>());
            }
            pendingFriend.getIncomingFriendRequests().add(appUser.getUserId());
            appDatabase.userDao().updateUser(appUser);
            appDatabase.userDao().updateUser(pendingFriend);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Toast.makeText(getContext(), "Friend Request Sent!", Toast.LENGTH_SHORT).show();

        }
    }
}