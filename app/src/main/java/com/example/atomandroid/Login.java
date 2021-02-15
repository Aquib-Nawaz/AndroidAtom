package com.example.atomandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {
    private SignInButton signInButton;
    private Button guestSign;
    GoogleSignInClient mGoogleSignInClient;
    private String TAG = "Login";
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String LOGGED_IN = "logged_in";
    private static final String USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        Boolean logged_in = sharedPreferences.getBoolean(LOGGED_IN, false);

        this.setTitle("Login");
        if (logged_in){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        signInButton = findViewById(R.id.google_sign);
        guestSign = findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        guestSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerActivity();
            }
        });
    }

    private void registerActivity(){
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(LOGGED_IN, false);
                editor.putString(USERNAME, null);
                editor.apply();
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
    private void signIn(){
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(LOGGED_IN, false);
                editor.putString(USERNAME, null);
                editor.apply();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent data) {
        super.onActivityResult(requestcode, requestcode, data);
        if (requestcode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private  void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(Login.this, "Signed In Succesfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch(ApiException e){
            Toast.makeText(Login.this, "Sign in failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Succesful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser fuser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            String personeName = account.getDisplayName();
            String personGivenEmail = account.getEmail();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(LOGGED_IN,true);
            editor.putString(USERNAME, personeName);
            editor.apply();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }
}