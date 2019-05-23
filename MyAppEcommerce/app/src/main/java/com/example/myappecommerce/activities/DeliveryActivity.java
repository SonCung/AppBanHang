package com.example.myappecommerce.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myappecommerce.Adapter.CartAdapter;
import com.example.myappecommerce.Config.Config;
import com.example.myappecommerce.DB.DBQuery;
import com.example.myappecommerce.Model.CartItem;
import com.example.myappecommerce.R;
import com.example.myappecommerce.fragment.CartFragment;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView deliveryRecyclerView;
    private Button changOrAddNewAddress;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;

    private TextView fullName;
    private TextView fullAddress;
    private TextView zipCode;

    public static List<CartItem> cartItemsModeLists;

    private Button btnContinue;
    private Dialog loadingDialog;
    private Dialog paymentMethodDialog;
    private ImageButton payPaypal;
    private ImageButton payMasterCard;

    // Pay on Paypal
    public static final int PAYPAL_REQUEST_CODE = 6969;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)   // test sandbox
            .clientId(Config.PAYPAL_CLIENT_ID);

    String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecyclerView = findViewById(R.id.delivery_recycleView);
        changOrAddNewAddress = findViewById(R.id.chang_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);

        fullName = findViewById(R.id.address_fullname);
        fullAddress = findViewById(R.id.address_view_all);
        zipCode = findViewById(R.id.pincode);
        btnContinue = findViewById(R.id.btn_cart_continue);

        // loading dialog
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(false);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // loading dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartItemsModeLists, totalAmount,false);
        deliveryRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changOrAddNewAddress.setVisibility(View.VISIBLE);
        changOrAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressIntent = new Intent(DeliveryActivity.this, AddressActivity.class);
                addressIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(addressIntent);
            }
        });

        payPaypal = (ImageButton) paymentMethodDialog.findViewById(R.id.pay_Paypal);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.show();
                payMasterCard = paymentMethodDialog.findViewById(R.id.pay_MasterCard);

            }
        });

        // Start Paypal Service
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        payPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodDialog.dismiss();
                loadingDialog.show();
                processPaypal();
            }
        });


    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    // Process Pay on paypal
    public void processPaypal(){
        amount = String.valueOf(totalAmount.getText());
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(Integer.parseInt(amount)), "USD"
                                        ,"Thanh toán hóa đơn", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        loadingDialog.dismiss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this,PaymentDetailsActivity.class)
                        .putExtra("PaymentDetails", paymentDetails)
                        .putExtra("PaymentAmount", amount));

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else if(resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                }
            }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        fullName.setText(DBQuery.addressList.get(DBQuery.selectedAddress).getFullName());
        fullAddress.setText(DBQuery.addressList.get(DBQuery.selectedAddress).getAddress());
        zipCode.setText(DBQuery.addressList.get(DBQuery.selectedAddress).getZipCode());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
