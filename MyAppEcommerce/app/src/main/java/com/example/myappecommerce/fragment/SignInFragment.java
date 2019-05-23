package com.example.myappecommerce.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappecommerce.R;
import com.example.myappecommerce.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignInFragment extends Fragment {
    ImageView back, showPassword;
    private FrameLayout parentFrameLayout;
    private TextView forgotPassword, dontHaveAnAccount;
    private EditText email, password;
    private ProgressBar progressBar;
    private Button btnSignIn;
    // Show Pass
    boolean isPasswordShown = false;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    // Email Validate
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        setID(view);

        return view;
    }

    public void setID(View view){
        parentFrameLayout = getActivity().findViewById(R.id.registryFrameLayout);
        back = view.findViewById(R.id.back_signIn);
        email = view.findViewById(R.id.email_signIn);
        password = view.findViewById(R.id.password_SignIn);
        forgotPassword = view.findViewById(R.id.tv_Forgot_SignIn);
        dontHaveAnAccount = view.findViewById(R.id.tv_dont_have_an_account);
        progressBar = view.findViewById(R.id.progress_SignIn);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        showPassword = view.findViewById(R.id.showPasswordSignIn);

        // FireBase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(disableCloseBtn){
            back.setVisibility(View.GONE);
        }else{
            back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInputAndData();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToMainActivity();
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordShown) {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    showPassword.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    isPasswordShown = false;
                } else {
                    password.setTransformationMethod(null);
                    showPassword.setImageResource(R.drawable.ic_eye_white_24dp);
                    isPasswordShown = true;
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new ResetPasswordFragment());
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    public void CheckInputAndData(){
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(password.getText())) {
                CheckEmailAndPassword();
            } else {
                ShowErrorPasswordEmpty();
            }
        } else {
            ShowErrorEmailEmpty();
        }
    }
    public void CheckEmailAndPassword(){
        if (email.getText().toString().matches(emailPattern)){
            if(password.getText().length() >= 8){

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    SendToMainActivity();
                                }else{
                                    progressBar.setVisibility(View.GONE);
                                    String error = task.getException().getMessage();
                                    ShowErrorToast(error);
                                }
                            }
                        });
            }else{
                ShowErrorPasswordEmpty();

            }
        }else{
            ShowErrorEmailValidate();

        }
    }

    public void ShowErrorToast(String value) {
        Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
    }

    public void ShowErrorEmailEmpty() {
        Toast.makeText(getActivity(), "Email is Empty. Please enter your Email!", Toast.LENGTH_LONG).show();
    }

    public void ShowErrorEmailValidate() {
        Toast.makeText(getActivity(), "Invalid Email. Example: yumimall97@gmail.com", Toast.LENGTH_LONG).show();
    }

    public void ShowErrorPasswordEmpty() {
        Toast.makeText(getActivity(), "Passwords must be at least 8 characters long. Please enter Password!", Toast.LENGTH_LONG).show();
    }

    public void SendToMainActivity(){
        if(disableCloseBtn){
            disableCloseBtn = false;
        }else {
            Intent main = new Intent(getActivity(), MainActivity.class);
            startActivity(main);
        }
        getActivity().finish();
    }
}
