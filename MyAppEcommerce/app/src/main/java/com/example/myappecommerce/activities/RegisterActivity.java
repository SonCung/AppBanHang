package com.example.myappecommerce.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.myappecommerce.R;
import com.example.myappecommerce.fragment.SignInFragment;
import com.example.myappecommerce.fragment.SignUpFragment;

public class RegisterActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static boolean setSignUpFragment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setID();
    }

    public void setID(){
        frameLayout = findViewById(R.id.registryFrameLayout);
        if(setSignUpFragment){
            setSignUpFragment = false;
            setFragment(new SignUpFragment());
        }
        setFragment(new SignInFragment());
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
