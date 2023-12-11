package com.cs407.landmarkapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "friend_list")
    @TypeConverters(Converter.class)
    private List<Integer> friends;
    @ColumnInfo(name = "sent_friend_requests")
    @TypeConverters(Converter.class)
    private List<Integer> sentFriendRequests;

    @ColumnInfo(name = "incoming_friend_requests")
    @TypeConverters(Converter.class)
    private List<Integer> incomingFriendRequests;

    @ColumnInfo(name = "badge_list")
    @TypeConverters(Converter.class)
    private List<Integer> badges;

    public User(String username, String password, String email, List<Integer> friends, List<Integer> badges) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
        this.badges = badges;
    }

    @Ignore
    public User( String username, String password, String email,
                List<Integer> friends, List<Integer> sentFriendRequests, List<Integer> incomingFriendRequests,
                List<Integer> badges) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.friends = friends;
        this.sentFriendRequests = sentFriendRequests;
        this.incomingFriendRequests = incomingFriendRequests;
        this.badges = badges;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Integer> getFriends() {
        return friends;
    }

    public void setFriends(List<Integer> friends) {
        this.friends = friends;
    }

    public List<Integer> getBadges() {
        return badges;
    }

    public void setBadges(List<Integer> badges) {
        this.badges = badges;
    }


    public List<Integer> getSentFriendRequests() {
        return sentFriendRequests;
    }

    public void setSentFriendRequests(List<Integer> sentFriendRequests) {
        this.sentFriendRequests = sentFriendRequests;
    }

    public List<Integer> getIncomingFriendRequests() {
        return incomingFriendRequests;
    }

    public void setIncomingFriendRequests(List<Integer> incomingFriendRequests) {
        this.incomingFriendRequests = incomingFriendRequests;
    }

}
