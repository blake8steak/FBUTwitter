package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tweet {
    private static final String TAG = "Tweet";
    public String body;
    public String createdAt;
    public User user;
    public String imageUrl;
    public long postedOn;
    public String sincePosted;

    public static Tweet fromJson(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = json.getString("text");
        tweet.createdAt = json.getString("created_at");
        tweet.user = User.fromJson(json.getJSONObject("user"));
        tweet.postedOn = Date.parse(json.getString("created_at"));
        Log.i(TAG, "timestamp: "+tweet.postedOn);
        long currentTime = System.currentTimeMillis();
        long seconds = (currentTime - tweet.postedOn) / 1000; //convert to seconds
        Log.i(TAG, "seconds since posted: "+seconds);
        if(seconds < 60) {
            tweet.sincePosted = seconds + "s";
        } else if (seconds < 3600) {
            tweet.sincePosted = seconds / 60 + "m";
        } else if (seconds < 86400) {
            tweet.sincePosted = seconds / 3600 + "h";
        } else {
            tweet.sincePosted = seconds / 86400 + "d";
        }
        try {
            JSONArray media = json.getJSONObject("entities").getJSONArray("media");
            tweet.imageUrl = ((JSONObject) media.get(0)).getString("media_url_https");
        } catch (Exception e) {
            //Log.i(TAG, "No media for this tweet.");
        }
        Log.i(TAG, "Data: "+tweet.imageUrl);
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray arr) throws JSONException {
        ArrayList<Tweet> tweets = new ArrayList();
        for(int i=0; i<arr.length(); i++) {
            tweets.add(fromJson(arr.getJSONObject(i)));
        }
        return tweets;
    }
}
