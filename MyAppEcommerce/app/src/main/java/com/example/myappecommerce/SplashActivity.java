package com.example.myappecommerce;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myappecommerce.activities.MainActivity;
import com.example.myappecommerce.activities.RegisterActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        // FireBase
        firebaseAuth = FirebaseAuth.getInstance();

        SystemClock.sleep(2000);
        SendMainActivity();
    }

    public void SendLoginActivity(){
        Intent login = new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(login);
        finish();
    }

    public void SendMainActivity(){
        Intent main = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(main);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null){
            SendLoginActivity();
        }else{
            SendMainActivity();
        }
    }
}
