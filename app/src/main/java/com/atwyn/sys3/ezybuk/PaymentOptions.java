package com.atwyn.sys3.ezybuk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PaymentOptions extends AppCompatActivity {
ImageButton nextcreditcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        nextcreditcard=(ImageButton)findViewById(R.id.im3);
        nextcreditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =new Intent(PaymentOptions.this,Card.class);
                startActivity(in);
            }
        });
    }
}
