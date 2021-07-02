package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Movie;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
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

        RequestOptions circleAvi = new RequestOptions();
        circleAvi = circleAvi.transforms(new CircleCrop());

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        ivProfileImage = findViewById(R.id.ivProfileImage2);
        tvBody = findViewById(R.id.tvBody2);
        tvScreenName = findViewById(R.id.tvScreenName2);
        tvUsername = findViewById(R.id.tvUsername2);
        tvTime = findViewById(R.id.tvTime2);
        tvRetweetCount = findViewById(R.id.tvRetweetCount2);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount2);
        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.name);
        tvUsername.setText("@"+tweet.user.screenName);
        tvTime.setText(tweet.sincePosted);
        tvRetweetCount.setText(tweet.retweetCount);
        tvFavoriteCount.setText(tweet.favoriteCount);
        Glide.with(this).load(tweet.user.publicImageUrl).apply(circleAvi).into(ivProfileImage);
    }
}