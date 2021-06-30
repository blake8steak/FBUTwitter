package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "TweetsAdapter";
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView tweetImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tweetImage = itemView.findViewById(R.id.tweetImage);
        }

        public void bind(Tweet tweet) {
            Log.i(TAG, "===== RUNNING BIND METHOD == RUNNING BIND METHOD =====");
            //for avatars
            RequestOptions circleAvi = new RequestOptions();
            circleAvi = circleAvi.transforms(new CircleCrop());
            // for tweet images
            RequestOptions imgOptions = new RequestOptions();
            imgOptions = imgOptions.transforms(new CenterCrop(), new RoundedCorners(20));
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.publicImageUrl).apply(circleAvi).into(ivProfileImage);
            if(tweet.imageUrl != null) {
                tweetImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.imageUrl).apply(imgOptions).into(tweetImage);
            } else {
                tweetImage.setVisibility(View.GONE);
            }
        }
    };

}
