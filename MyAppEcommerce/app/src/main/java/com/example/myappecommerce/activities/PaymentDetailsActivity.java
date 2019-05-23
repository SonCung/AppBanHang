package com.example.myappecommerce.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myappecommerce.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailsActivity extends AppCompatActivity {
    private TextView paymentId;
    private TextView paymentAmountShow;
    private TextView paymentStatus;
    private ImageButton btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        paymentId = findViewById(R.id.payment_id);
        paymentAmountShow = findViewById(R.id.payment_amount);
        paymentStatus = findViewById(R.id.payment_status);
        btnHome = findViewById(R.id.btnPaymentHome);

        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
            
        } catch (JSONException e) {
            e.printStackTrace();
        }


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(PaymentDetailsActivity.this, MainActivity.class);
                startActivity(intentHome);
            }
        });
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            paymentId.setText("Mã đơn hàng: "+response.getString("id"));
            paymentStatus.setText("Trạng thái:  "+response.getString("state"));
            paymentAmountShow.setText("Giá :$"+ paymentAmount);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
