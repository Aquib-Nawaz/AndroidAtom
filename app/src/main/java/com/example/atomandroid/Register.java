package com.example.atomandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register extends AppCompatActivity {

    private Button continueButton;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String LOGGED_IN = "logged_in";
    private static final String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        continueButton = findViewById(R.id.button);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (continueButton.getText().toString() != null) {
                    onContinue();
                }
            }
        });
    }

    private void onContinue(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGGED_IN, true);
        editor.putString(USERNAME, continueButton.getText().toString());
        editor.apply();
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
    }
}