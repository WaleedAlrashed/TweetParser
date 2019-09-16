package com.sytech.tweetparser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public EditText userNameEditText;
    public EditText passwordEditText;

    public RelativeLayout rellay1,rellay2;

    Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                rellay1.setVisibility(View.VISIBLE);
                rellay2.setVisibility(View.VISIBLE);
            }
        };

    public void redirectUser(){
        if(ParseUser.getCurrentUser() !=null){
            //user logged in
            Intent intent = new Intent(getApplicationContext(),UsersActivity.class);
            startActivity(intent);

        }
    }
    public void signupLogin(View view){
        final ArrayList isFollowing = new ArrayList();
        isFollowing.add("TweetParser Support");
        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        final String userName = userNameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        ParseUser.logInInBackground(userName,password,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e == null){
                            Log.i("Login","Success");
                            redirectUser();
                        }else{
                            ParseUser newUser = new ParseUser();
                            newUser.setUsername(userName);
                            newUser.setPassword(password);
                            newUser.put("isFollowing",isFollowing);
                            newUser.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        Log.i("Signup","Success");
                                        redirectUser();
                                    }else{
                                        Toast.makeText(MainActivity.this,e.getMessage().substring(e.getMessage().indexOf(" ")),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
        );
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlogin);//the new login UI
        //setTitle("Twitter: Login");
        //setActivityBackgroundColor(MainActivity.this,R.color.steel_blue);

        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        handler.postDelayed(runnable,2000);//splash screen timeout
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void setActivityBackgroundColor(Context context,int id) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(ContextCompat.getColor(context,id));
    }


}
