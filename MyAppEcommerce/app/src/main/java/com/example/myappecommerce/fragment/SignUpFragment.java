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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpFragment extends Fragment {
    private FrameLayout parentFrameLayout;
    private ImageView back;
    private TextView alreadyHaveAnAccount;
    private EditText fullName, mobile, email, password, confirmPassword;
    private ImageView showPassword, showConfirmPassword;
    private ProgressBar progressBar;
    private Button btnSignUp;

    // Show Pass
    boolean isPasswordShown = false;
    boolean isConfirmPasswordShown = false;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    // Email Validate
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public static boolean disableCloseBtn = false;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        setID(view);
        return view;
    }

    public void setID(View view) {
        parentFrameLayout = getActivity().findViewById(R.id.registryFrameLayout);
        back = view.findViewById(R.id.back_SignUp);
        fullName = view.findViewById(R.id.name_SignUp);
        mobile = view.findViewById(R.id.mobile_SignUp);
        email = view.findViewById(R.id.email_SignUp);
        password = view.findViewById(R.id.password_SignUp);
        confirmPassword = view.findViewById(R.id.passwordConfirm_SignUp);
        showPassword = view.findViewById(R.id.showPassword);
        showConfirmPassword = view.findViewById(R.id.showConfirmPassword);

        progressBar = view.findViewById(R.id.progress_SignUp);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        alreadyHaveAnAccount = view.findViewById(R.id.tv_already_have_an_account);

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

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send data to FireBase
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

        showConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfirmPasswordShown) {
                    confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    showConfirmPassword.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    isConfirmPasswordShown = false;
                } else {
                    confirmPassword.setTransformationMethod(null);
                    showConfirmPassword.setImageResource(R.drawable.ic_eye_white_24dp);
                    isConfirmPasswordShown = true;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    public void CheckInputAndData() {
        if (!TextUtils.isEmpty(fullName.getText())) {
            if (!TextUtils.isEmpty(mobile.getText())) {
                if (!TextUtils.isEmpty(email.getText())) {
                    if (!TextUtils.isEmpty(password.getText()) && password.getText().length() >= 8) {
                        if (!TextUtils.isEmpty(confirmPassword.getText())) {
                            CheckEmailAndPassword();
                        } else {
                            ShowErrorConfirmPasswordEmpty();
                        }
                    } else {
                        ShowErrorPasswordEmpty();
                    }
                } else {
                    ShowErrorEmailEmpty();
                }
            } else {
                ShowErrorMobileEmpty();
            }
        } else {
            ShowErrorFullNameEmpty();
        }
    }

    public void CheckEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Map<String, Object> userdata = new HashMap<>();
                                    userdata.put("fullName", fullName.getText().toString());
                                    userdata.put("mobile", mobile.getText().toString());

                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        CollectionReference userDataRefence = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                                                .collection("USER_DATA");


                                                        Map<String, Object> wishListMap = new HashMap<>();
                                                        wishListMap.put("list_size", (long) 0);

                                                        Map<String, Object> ratingsMap = new HashMap<>();
                                                        ratingsMap.put("list_size", (long) 0);

                                                        Map<String, Object> cartMap = new HashMap<>();
                                                        cartMap.put("list_size", (long) 0);

                                                        Map<String, Object> addressMap = new HashMap<>();
                                                        addressMap.put("list_size", (long) 0);

                                                        final List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");
                                                        documentNames.add("MY_CART");
                                                        documentNames.add("MY_ADDRESS");

                                                        List<Map<String,Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(wishListMap);
                                                        documentFields.add(ratingsMap);
                                                        documentFields.add(cartMap);
                                                        documentFields.add(addressMap);

                                                        for (int x= 0; x<documentNames.size();x++){
                                                            final int finalX = x;
                                                            userDataRefence.document(documentNames.get(x))
                                                                    .set(documentFields.get(x))
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                if(finalX == documentNames.size()-1) {
                                                                                    SendToMainActivity();
                                                                                }
                                                                            }else{
                                                                                progressBar.setVisibility(View.GONE);
                                                                                String error = task.getException().getMessage();
                                                                                ShowErrorToast(error);
                                                                            }
                                                                        }
                                                                    });
                                                        }
//                                                        SendToMainActivity();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        ShowErrorToast(error);
                                                    }
                                                }
                                            });
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    String error = task.getException().getMessage();
                                    ShowErrorToast(error);
                                }
                            }
                        });
            } else {
                ShowErrorConfirmPassword();
            }
        } else {
            ShowErrorEmailValidate();
        }
    }

    public void ShowErrorToast(String value) {
        Toast.makeText(getActivity(), value, Toast.LENGTH_LONG).show();
    }

    public void ShowErrorFullNameEmpty() {
        Toast.makeText(getActivity(), "FullName is Empty. Please enter your FullName!", Toast.LENGTH_LONG).show();
    }

    public void ShowErrorMobileEmpty() {
        Toast.makeText(getActivity(), "Mobile is Empty. Please enter your Mobile!", Toast.LENGTH_LONG).show();
    }

    public void ShowErrorEmailEmpty() {
        Toast.makeText(getActivity(), "Email is Empty. Please enter your Email!", Toast.LENGTH_LONG).show();
    }

    public void ShowErrorPasswordEmpty() {
        Toast.makeText(getActivity(), "Passwords must be at least 8 characters long. Please enter Password!", Toast.LENGTH_LONG).show();
    }

    public void ShowErrorConfirmPasswordEmpty() {
        Toast.makeText(getActivity(), "ConfirmPasswords is Empty. Please enter ConfirmPassword!", Toast.LENGTH_LONG).show();
    }

    public void ShowErrorConfirmPassword() {
        Toast.makeText(getActivity(), "ConfirmPasswords do not match. Please enter ConfirmPassword!", Toast.LENGTH_LONG).show();
    }

    public void ShowErrorEmailValidate() {
        Toast.makeText(getActivity(), "Invalid Email. Example: yumimall97@gmail.com", Toast.LENGTH_LONG).show();
    }

    public void SendToMainActivity() {
        if(disableCloseBtn){
            disableCloseBtn = false;
        }else {
            Intent main = new Intent(getActivity(), MainActivity.class);
            startActivity(main);
        }
        getActivity().finish();
    }

}


