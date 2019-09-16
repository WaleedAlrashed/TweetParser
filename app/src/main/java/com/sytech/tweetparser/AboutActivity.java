package com.sytech.tweetparser;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AboutActivity extends AppCompatActivity {

    public TextView aboutEditText;
    public TextView versionTextView;
    public final int APP_CURRENT_VERSION = BuildConfig.VERSION_CODE;
    public  int SERVER_VERSION = 1;
    public void openWebsite(View view){
        String url = "https://waleedalrashed.wordpress.com/";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void checkForUpdate(View view){
       // ParseObject appVersion = new ParseObject("AppsVersions");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AppsVersions");
        query.whereEqualTo("objectId","BXieGFuTJu");
        query.setLimit(2);
       query.getFirstInBackground(new GetCallback<ParseObject>() {
           @Override
           public void done(ParseObject object, ParseException e) {
                if(e == null){
                    if(object != null){
                        SERVER_VERSION = object.getInt("twitter");

                    }
                }else{
                    Toast.makeText(AboutActivity.this,"Something Went Wrong!",Toast.LENGTH_SHORT).show();
                }
           }
       });

        Log.i("SERVER_VERSION:"," "+SERVER_VERSION);
        Log.i("APP_VERSION: ","" +APP_CURRENT_VERSION);

       if(appNeedsUpdate()){
           Snackbar.make(view, "Update available",
                   Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();
       }else{
           Snackbar.make(view, "You're running the latest version!",
                   Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();
       }




    }


    public boolean appNeedsUpdate(){
        return SERVER_VERSION > APP_CURRENT_VERSION ? true : false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //About edit text
        aboutEditText = findViewById(R.id.aboutEditText);

        //APP_CURRENT_VERSION Text View
        versionTextView = findViewById(R.id.versionTextView);
        versionTextView.setText("App Version: "+ APP_CURRENT_VERSION);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.feedback);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                builder.setTitle("Your feedback matters!");

                final EditText feedbackEditText = new EditText(AboutActivity.this);
                builder.setView(feedbackEditText);

                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParseObject feedback = new ParseObject("TwitterFeedback");

                        feedback.put("feedback",feedbackEditText.getText().toString());
                        feedback.put("android_version",android.os.Build.VERSION.SDK_INT);
                        feedback.put("device_name",android.os.Build.MODEL);
                        feedback.put("APP_CURRENT_VERSION", APP_CURRENT_VERSION);


                        feedback.saveEventually(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null){
                                    Snackbar.make(view, "Thank you so much for your feedback!",
                                            Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }else{
                                    Toast.makeText(AboutActivity.this, "Something went wrong",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();



            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
