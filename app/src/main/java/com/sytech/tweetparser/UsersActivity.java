package com.sytech.tweetparser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    public ArrayList<String> users = new ArrayList<>();
    public ArrayAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.tweet_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.tweet:
                //Make an alert dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Send A tweet");
                final EditText tweetEditText = new EditText(this);
                builder.setView(tweetEditText);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("Info",tweetEditText.getText().toString());
                        ParseObject tweet = new ParseObject("Tweet");
                        tweet.put("tweet",tweetEditText.getText().toString());
                        tweet.put("username",ParseUser.getCurrentUser().getUsername());

                        tweet.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Toast.makeText(UsersActivity.this,"Tweet sent!",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(UsersActivity.this,"Tweet Failed :(",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("Info","I don't wanna tweet!");
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                break;
            case R.id.logout:
                ParseUser.logOut();

            Intent intent_Main = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent_Main);
            break;

            case R.id.viewFeed:
                Intent intent_feed = new Intent(getApplicationContext(),FeedActivity.class);
            startActivity(intent_feed);
            break;

            case R.id.about:
                Intent intent_about = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(intent_about);
                break;

            default:
                break;

        }
//        if(item.getItemId() == R.id.tweet){
//            //Make an alert dialogue
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Send A tweet");
//            final EditText tweetEditText = new EditText(this);
//            builder.setView(tweetEditText);
//
//            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Log.i("Info",tweetEditText.getText().toString());
//                    ParseObject tweet = new ParseObject("Tweet");
//                    tweet.put("tweet",tweetEditText.getText().toString());
//                    tweet.put("username",ParseUser.getCurrentUser().getUsername());
//
//                    tweet.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if(e == null){
//                                Toast.makeText(UsersActivity.this,"Tweet sent!",Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(UsersActivity.this,"Tweet Failed :(",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//                }
//            });
//
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Log.i("Info","I don't wanna tweet!");
//                    dialogInterface.cancel();
//                }
//            });
//            builder.show();
//        }else if(item.getItemId() == R.id.logout){
//            ParseUser.logOut();
//
//            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
//        }else if(item.getItemId() == R.id.viewFeed){
//            Intent intent = new Intent(getApplicationContext(),FeedActivity.class);
//            startActivity(intent);
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        setTitle("Users' List");

//        users.add("nick");
//        users.add("sarah");

        final ListView listView = findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE); //so we can choose multiple users

        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,users);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView)view;
                if(checkedTextView.isChecked()){
                    Log.i("info","Checked");
                    ParseUser.getCurrentUser().add("isFollowing",users.get(i));
                }else{
                    //Algorithm as follows
                    /*
                    1 - remove the users that the user clicked on
                    2 - get the list of users that haven't been uncheked yet
                    3 - clear the isFollowing for that user
                    4 - add the users to isFollowing
                     */
                    Log.i("info","Not checked");
                    ParseUser.getCurrentUser().getList("isFollowing").remove(users.get(i));
                    List tempUsers = ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing",tempUsers);
                }
                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size()>0){
                    for (ParseUser user: objects){
                        users.add(user.getUsername());

                    }

                    adapter.notifyDataSetChanged();
                    //update the list
                    for (String username: users){
                        if(ParseUser.getCurrentUser().getList("isFollowing").contains(username)){
                            listView.setItemChecked(users.indexOf(username),true);

                        }
                    }
                }
            }
        });
    }
}
