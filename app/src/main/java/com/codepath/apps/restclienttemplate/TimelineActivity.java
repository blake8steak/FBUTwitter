package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity implements ComposeListener {
    public static final String TAG = "TimelineActivity";

    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;
    private RecyclerView rvTweets;
    private TweetsAdapter adapter;

    private List<Tweet> tweets;
    private long maxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.twitter_logo_xml);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Loading...");


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);

        client = TwitterApp.getRestClient(this);
        rvTweets = findViewById(R.id.rvTweets);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        EndlessRecyclerViewScrollListener endlessListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        rvTweets.addOnScrollListener(endlessListener);

        tweets = new ArrayList();
        adapter = new TweetsAdapter(this, tweets);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(adapter);
        populateHomeTimeline();
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        Log.i(TAG, "running loadNextDataFromApi");
        // Send an API request to retrieve appropriate paginated data
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                Log.i(TAG, "+25 tweets");
                try {
                    adapter.addAll(Tweet.fromJsonArray(json.jsonArray));
                    adapter.notifyDataSetChanged();
                    maxId = Long.valueOf(tweets.get(tweets.size()-1).id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Fetch timeline error: " + throwable.toString());
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        client.getInitTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Clear old items before appending in the new ones
                adapter.clear();
                // Data has come back, add new items to adapter
                try {
                    adapter.addAll(Tweet.fromJsonArray(json.jsonArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
                getSupportActionBar().setTitle("Twitter");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "Fetch timeline error: " + throwable.toString());
                getSupportActionBar().setTitle("Twitter");
            }
        });
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.compose) {
            showCompose();
            return true;
        }
        if(item.getItemId() == R.id.signOut) {
            client.clearAccessToken(); // forget who's logged in
            finish(); // navigate backwards to Login screen
        }
        if(item.getItemId() == android.R.id.home) {
            rvTweets.smoothScrollToPosition(0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCompose() {
        FragmentManager fm = getSupportFragmentManager();
        Compose composeFrag = Compose.newInstance("Compose");
        composeFrag.show(fm, "fragment_compose");

    }

    private void populateHomeTimeline() {
        client.getInitTimeline( new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    Log.i(TAG, "Success! "+json.toString());
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                    maxId = Long.valueOf(tweets.get(tweets.size()-1).id);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception - Error parsing tweets", e);
                    e.printStackTrace();
                }
                getSupportActionBar().setTitle("Twitter");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "failed to retrieve tweets: "+statusCode, throwable);
                getSupportActionBar().setTitle("Twitter");
            }
        });
    }

    //used for passing data between Compose fragment and TimelineActivity
    @Override
    public void getTweet(Tweet tweet) {
        //will do more parsing and whatnot here later for UI and stuff
        Log.i(TAG, "GOT TWEET CONTENT: "+tweet.body);
        tweets.add(0, tweet);
        adapter.notifyItemInserted(0);
        rvTweets.smoothScrollToPosition(0);
    }
}