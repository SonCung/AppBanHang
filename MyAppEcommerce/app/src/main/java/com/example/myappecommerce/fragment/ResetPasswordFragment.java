package com.example.myappecommerce.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.transition.TransitionManager;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {
    private FrameLayout parentFrameLayout;
    private EditText  emailReset;
    private Button btnResetPassword;
    private ImageView back_resetPassword;

    // Progress bar and textShow
    private ViewGroup emailIconContainer;
    private ImageView iconEmailReset;
    private TextView iconEmailTextResetPassword;
    private ProgressBar progressBarResetPassword;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reset_password, container, false);

        setID(view);

        return view;
    }

    public void setID(View view){
        parentFrameLayout = getActivity().findViewById(R.id.registryFrameLayout);
        back_resetPassword = view.findViewById(R.id.back_resetPassword);

        emailReset = view.findViewById(R.id.email_resetPassword);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);

        // ProgressBar and textShow
        emailIconContainer = view.findViewById(R.id.emailIconContainer);
        iconEmailReset = view.findViewById(R.id.iconEmailReset);
        iconEmailTextResetPassword = view.findViewById(R.id.iconEmailTextResetPassword);
        progressBarResetPassword = view.findViewById(R.id.progressBarResetPassword);

        // FireBase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                CheckInputAndData();
            }
        });

        back_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
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
        if(!TextUtils.isEmpty(emailReset.getText())){
            TransitionManager.beginDelayedTransition(emailIconContainer);
            progressBarResetPassword.setVisibility(View.VISIBLE);

            firebaseAuth.sendPasswordResetEmail(emailReset.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
//                                    Toast.makeText(getActivity(), "Email send successfully", Toast.LENGTH_SHORT).show();
                                progressBarResetPassword.setVisibility(View.GONE);
                                iconEmailTextResetPassword.setVisibility(View.VISIBLE);
                                iconEmailReset.setVisibility(View.VISIBLE);
                                iconEmailTextResetPassword.setTextColor(Color.BLUE);
                                iconEmailTextResetPassword.setText("Recovery email send successfully!. Check inbox. ");
                            }else{
                                String error = task.getException().getMessage();
                                progressBarResetPassword.setVisibility(View.GONE);
                                iconEmailTextResetPassword.setVisibility(View.VISIBLE);
                                iconEmailReset.setVisibility(View.VISIBLE);
                                iconEmailTextResetPassword.setTextColor(Color.RED);
                                iconEmailTextResetPassword.setText(error);
                                TransitionManager.beginDelayedTransition(emailIconContainer);
//                                    ShowErrorToast(error);
                            }
                        }
                    });
        }else{
            ShowErrorEmailEmpty();
        }
    }


    public void ShowErrorToast(String value) {
        Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
    }

    public void ShowErrorEmailEmpty() {
        Toast.makeText(getActivity(), "Email is Empty. Please enter your Email!", Toast.LENGTH_LONG).show();
    }

    public void SendToSignUpActivity() {
        Intent main = new Intent(getActivity(), SignUpFragment.class);
        startActivity(main);
        getActivity().finish();
    }

}

//10   21.22