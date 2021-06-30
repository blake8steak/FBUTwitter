package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import okhttp3.Headers;
// ...

public class Compose extends DialogFragment {
    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "COMPOSE";

    private EditText tweetBody;
    private Button tweetButton;

    private TwitterClient client;

    public Compose() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static Compose newInstance(String title) {
        Compose frag = new Compose();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void sendData(Tweet tweet) {
        ComposeListener listener = (ComposeListener) getActivity();
        listener.getTweet(tweet);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tweetBody = (EditText) view.findViewById(R.id.tweetBody);
        tweetButton = (Button) view.findViewById(R.id.tweetButton);
        client = new TwitterClient(view.getContext());

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContents = tweetBody.getText().toString();
                if(tweetContents.length() == 0) {
                    Log.i(TAG, "No text entered.");
                } else if (tweetContents.length() > 280) {
                    Log.i(TAG, "Tweet cannot submit - greater than 280 chars");
                } else {
                    //sendData(tweetContents);
                    client.publishTweet(tweetContents, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Success publishing Tweet");
                            try {
                                Tweet tweet = Tweet.fromJson(json.jsonObject);
                                sendData(tweet);
                                Log.i(TAG, "Tweet: "+tweetContents);
                                dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Failed to publish tweet: "+statusCode, throwable);
                        }
                    });
                }
            }
        });
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        tweetBody.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}