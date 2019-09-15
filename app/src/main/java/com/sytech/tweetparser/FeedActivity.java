package com.sytech.tweetparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    public ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setTitle("Tweets");

        listView = findViewById(R.id.listView);

        final List<Map<String,String>> tweetData = new ArrayList<>();


        //get the tweets

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");
        query.setLimit(20);//in order NOT to get many tweets

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for (ParseObject tweet: objects){
                        Map<String,String> tweetInfo = new HashMap<>();
                        tweetInfo.put("Content",tweet.getString("tweet"));
                        tweetInfo.put("Username",tweet.getString("username"));
                        tweetData.add(tweetInfo);
                    }
                    SimpleAdapter simpleAdapter = new SimpleAdapter(FeedActivity.this,tweetData,android.R.layout.simple_list_item_2,
                            new String[] {"Content","Username"},new int[] {android.R.id.text1,android.R.id.text2});
                    listView.setAdapter(simpleAdapter);
                }
            }
        });





    }
}
