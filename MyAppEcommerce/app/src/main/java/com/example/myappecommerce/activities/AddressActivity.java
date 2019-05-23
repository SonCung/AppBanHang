package com.example.myappecommerce.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappecommerce.Adapter.AddressAdapter;
import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.Address;
import com.example.myappecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myappecommerce.activities.DeliveryActivity.SELECT_ADDRESS;

public class AddressActivity extends AppCompatActivity {
    private RecyclerView addressCRecyclerView;
    private static AddressAdapter addressAdapter;
    private Button btnDeliveryHere;
    private LinearLayout btnAddNewAddress;
    private TextView addressSaved;
    private int previousAddress;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Address");

        // loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        previousAddress = DBQuery.selectedAddress;

        addressCRecyclerView = findViewById(R.id.address_recycleView);
        btnDeliveryHere = findViewById(R.id.btn_deliver_here);
        btnAddNewAddress = findViewById(R.id.btn_add_new_address);
        addressSaved = findViewById(R.id.address_saved);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addressCRecyclerView.setLayoutManager(linearLayoutManager);

        int mode = getIntent().getIntExtra("MODE", -1);
        if (mode == SELECT_ADDRESS) {
            btnDeliveryHere.setVisibility(View.VISIBLE);
        } else {
            btnDeliveryHere.setVisibility(View.GONE);
        }

        btnDeliveryHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DBQuery.selectedAddress != previousAddress){
                    final int previousAddressIndex = previousAddress;
                    loadingDialog.show();
                    Map<String, Object> updateSelection = new HashMap<>();
                    updateSelection.put("selected_"+String.valueOf(previousAddress+1),false);
                    updateSelection.put("selected_"+String.valueOf(DBQuery.selectedAddress+1),true);

                    previousAddress = DBQuery.selectedAddress;

                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                            .collection("USER_DATA").document("MY_ADDRESS")
                            .update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finish();
                            }else{
                                previousAddress = previousAddressIndex;
                                String error = task.getException().getMessage();
                                Toast.makeText(AddressActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }
                    });
                }else{
                    finish();
                }
            }
        });

        addressAdapter = new AddressAdapter(DBQuery.addressList, mode);
        addressCRecyclerView.setAdapter(addressAdapter);
        ((SimpleItemAnimator) addressCRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressAdapter.notifyDataSetChanged();

        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(AddressActivity.this, AddAddressActivity.class);
                addAddressIntent.putExtra("INTENT", "null");
                startActivity(addAddressIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        addressSaved.setText(String.valueOf(DBQuery.addressList.size()+ "saved address"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if(DBQuery.selectedAddress != previousAddress){
                DBQuery.addressList.get(DBQuery.selectedAddress).setSelected(false);
                DBQuery.addressList.get(previousAddress).setSelected(true);
                DBQuery.selectedAddress = previousAddress;
            }
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void refreshItemAddress(int deselect, int select) {
        addressAdapter.notifyItemChanged(deselect);
        addressAdapter.notifyItemChanged(select);
    }

    @Override
    public void onBackPressed() {
        if(DBQuery.selectedAddress != previousAddress){
            DBQuery.addressList.get(DBQuery.selectedAddress).setSelected(false);
            DBQuery.addressList.get(previousAddress).setSelected(true);
            DBQuery.selectedAddress = previousAddress;
        }
        super.onBackPressed();
    }
}
