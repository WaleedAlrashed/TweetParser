package com.sytech.tweetparser;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.ActionBar;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

    public EditText userNameEditText;
    public EditText passwordEditText;

    public RelativeLayout rellay1, rellay2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    public void redirectUser() {
        if (ParseUser.getCurrentUser() != null) {

            //user logged in


            Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
            startActivity(intent);

        }
    }

    public boolean checkCredentials() {
        if (userNameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches(""))
            return false;
        return true;
    }

    public void signupLogin(View view) {

        final ArrayList isFollowing = new ArrayList();
        isFollowing.add("TweetParser Support");
        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        if (!checkCredentials())
            Toast.makeText(this, "Username and password are required", Toast.LENGTH_SHORT).show();

        Toast.makeText(MainActivity.this, "Loading,Please Wait", Toast.LENGTH_SHORT).show();
        final String userName = userNameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        ParseUser.logInInBackground(userName, password,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            Log.i("Login", "Success");
                            redirectUser();
                        } else {
                            ParseUser newUser = new ParseUser();
                            newUser.setUsername(userName);
                            newUser.setPassword(password);
                            newUser.put("isFollowing", isFollowing);
                            newUser.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("Signup", "Success");
                                        redirectUser();
                                    } else {
                                        Toast.makeText(MainActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
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
        //setActionBar(new Toolbar(getApplicationContext()).setVisibility(View.GONE));
        //setTitle("Twitter: Login");
        //setActivityBackgroundColor(MainActivity.this,R.color.steel_blue);


        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 2000);//splash screen timeout
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"ID")
//                .setSmallIcon(R.drawable.ic_twitter)
//                .setContentTitle("Test Content")
//                .setContentText("Context Text")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        builder.build();
//        Notification notification = builder.build();



        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void setActivityBackgroundColor(Context context, int id) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(ContextCompat.getColor(context, id));
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            signupLogin(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.imgView_logo || view.getId() == R.id.mainRelativeLayout) {
        if (!(view.getId() == R.id.userNameEditText || view.getId() == R.id.passwordEditText)){
            //Case 2: user tapped on either the background or the Instagram logo.
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error in keyboard management", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
