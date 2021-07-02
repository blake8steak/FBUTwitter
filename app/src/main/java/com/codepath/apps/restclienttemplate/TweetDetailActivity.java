package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {
    private Tweet tweet;

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvUsername;
    TextView tvTime;
    ImageView tweetImage;
    TextView tvRetweetCount;
    TextView tvFavoriteCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        //for avatars
        RequestOptions circleAvi = new RequestOptions();
        circleAvi = circleAvi.transforms(new CircleCrop());
        // for tweet images
        RequestOptions imgOptions = new RequestOptions();
        imgOptions = imgOptions.transforms(new CenterCrop(), new RoundedCorners(20));

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        ivProfileImage = findViewById(R.id.ivProfileImage2);
        tvBody = findViewById(R.id.tvBody2);
        tvScreenName = findViewById(R.id.tvScreenName2);
        tvUsername = findViewById(R.id.tvUsername2);
        tvTime = findViewById(R.id.tvTime2);
        tvRetweetCount = findViewById(R.id.tvRetweetCount2);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount2);
        tweetImage = findViewById(R.id.tweetImage2);
        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.name);
        tvUsername.setText("@"+tweet.user.screenName);
        tvTime.setText(tweet.sincePosted);
        tvRetweetCount.setText(tweet.retweetCount);
        tvFavoriteCount.setText(tweet.favoriteCount);
        Glide.with(this).load(tweet.user.publicImageUrl).apply(circleAvi).into(ivProfileImage);
        if(tweet.imageUrl != null) {
            tweetImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.imageUrl).apply(imgOptions).into(tweetImage);
        } else {
            tweetImage.setVisibility(View.GONE);
        }

        this.setTitle(tweet.user.name);
    }
}