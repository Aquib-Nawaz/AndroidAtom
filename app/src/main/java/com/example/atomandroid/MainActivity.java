package com.example.atomandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String LOGGED_IN = "logged_in";
    private static final String USERNAME = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        headline = findViewById(R.id.headline);
//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
//        String name = sharedPreferences.getString(USERNAME,null);
//        if (name == null) name = "there";
//        text = "Hi  " + name + "!\nLet's start our journey for today";
//        headline.setText(text);
    }
}