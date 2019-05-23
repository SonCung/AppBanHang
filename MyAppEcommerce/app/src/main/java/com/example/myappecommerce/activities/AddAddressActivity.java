package com.example.myappecommerce.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.Address;
import com.example.myappecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    private EditText city;
    private EditText locatity;
    private EditText zipCode;
    private EditText name;
    private EditText mobile;
    private EditText alterlateMobile;

    // dialog
    private Dialog loadingDialog;

    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add a new address");

        // loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        city = findViewById(R.id.city);
        locatity = findViewById(R.id.locality);
        zipCode = findViewById(R.id.pin_code);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.moblie);
        alterlateMobile = findViewById(R.id.alterlate_mobile);


        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(city.getText())) {
                    if (!TextUtils.isEmpty(locatity.getText())) {
                        if (!TextUtils.isEmpty(zipCode.getText()) && zipCode.getText().length() == 6) {
                            if (!TextUtils.isEmpty(name.getText())) {
                                if (!TextUtils.isEmpty(mobile.getText()) && mobile.getText().length() == 10) {
                                    loadingDialog.show();

                                    final String fullAddress = locatity.getText().toString() + " " + city.getText().toString();

                                    Map<String, Object> addAddress = new HashMap<>();
                                    addAddress.put("list_size", (long) DBQuery.addressList.size() + 1);
                                    if (TextUtils.isEmpty(alterlateMobile.getText())) {
                                        addAddress.put("fullName_" + String.valueOf((long) DBQuery.addressList.size() + 1), name.getText().toString() + "- Mobile " + mobile.getText().toString());
                                    } else {
                                        addAddress.put("fullName_" + String.valueOf((long) DBQuery.addressList.size() + 1), name.getText().toString() + "- Mobile " + mobile.getText().toString() + " or " + alterlateMobile.getText().toString());
                                    }
                                    addAddress.put("address_" + String.valueOf((long) DBQuery.addressList.size() + 1), fullAddress);
                                    addAddress.put("zipCode_" + String.valueOf((long) DBQuery.addressList.size() + 1), zipCode.getText().toString());
                                    addAddress.put("selected_" + String.valueOf((long) DBQuery.addressList.size() + 1), true);
                                    if (DBQuery.addressList.size() > 0) {
                                        addAddress.put("selected_" + (DBQuery.selectedAddress + 1), false);
                                    }

                                    FirebaseFirestore.getInstance().collection("USERS")
                                            .document(FirebaseAuth.getInstance().getUid())
                                            .collection("USER_DATA")
                                            .document("MY_ADDRESS")
                                            .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (DBQuery.addressList.size() > 0) {
                                                    DBQuery.addressList.get(DBQuery.selectedAddress).setSelected(false);
                                                }
                                                if (TextUtils.isEmpty(alterlateMobile.getText())) {
                                                    DBQuery.addressList.add(new Address(
                                                            name.getText().toString() + "- Mobile " + mobile.getText().toString()
                                                            , fullAddress
                                                            , zipCode.getText().toString()
                                                            , true));
                                                } else {
                                                    DBQuery.addressList.add(new Address(
                                                            name.getText().toString() + "- Mobile " + mobile.getText().toString() + " or " + alterlateMobile.getText().toString()
                                                            , fullAddress
                                                            , zipCode.getText().toString()
                                                            , true));
                                                }

                                                if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                    Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                    startActivity(deliveryIntent);
                                                }else{
                                                    AddressActivity.refreshItemAddress(DBQuery.selectedAddress, DBQuery.addressList.size() - 1);
                                                }
                                                    DBQuery.selectedAddress = DBQuery.addressList.size() - 1;
                                                    finish();
                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                            loadingDialog.dismiss();
                                        }
                                    });
                                } else {
                                    mobile.requestFocus();
                                    Toast.makeText(AddAddressActivity.this, "Please check mobile. Ex: 0368686868 ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                name.requestFocus();
                            }
                        } else {
                            zipCode.requestFocus();
                            Toast.makeText(AddAddressActivity.this, "Please check zipcode. EX: 889900 ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        locatity.requestFocus();
                    }
                } else {
                    city.requestFocus();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
