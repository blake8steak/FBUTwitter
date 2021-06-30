package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    private static final String TAG = "Tweet";
    public String body;
    public String createdAt;
    public User user;
    public String imageUrl;

    public static Tweet fromJson(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = json.getString("text");
        tweet.createdAt = json.getString("created_at");
        tweet.user = User.fromJson(json.getJSONObject("user"));
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
