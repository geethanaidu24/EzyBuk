package com.atwyn.sys3.ezybuk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {
EditText emailaddress;
    Button Proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailaddress=(EditText)findViewById(R.id.editText_user);
        Proceed=(Button)findViewById(R.id.proceed);
        Proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((emailaddress.length()==0))
                {
                    Toast toast = Toast.makeText(ForgotPassword.this, "Fill All", Toast.LENGTH_SHORT);
                    toast.show();
                }else
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPassword.this);
                    alert.setTitle(Html.fromHtml("<font color='#ff0000'>Information!</font>"));
                    alert.setMessage("Reset Instructions have been sent to your Email Address");

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                        }
                    });
                    alert.show();

                }
            }
        });

    }
}
