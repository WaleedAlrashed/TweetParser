package com.sytech.tweetparser;

import android.app.Application;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("8ae547a04fbae4c193608e93eda5004dd227f8a1")
                .clientKey("0c50de9ff6cf5792713d731a4a50e90c746a175e")
                .server("http://3.15.190.235:80/parse/")
                .build());

//        ParseObject gameScore = new ParseObject("GameScore");
//        gameScore.put("score",130);
//        gameScore.put("playerName","Waleed Alrashed");
//        gameScore.put("cheatMode",false);
//        gameScore.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    Toast.makeText(getApplicationContext(),"Saved data",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
       // ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
