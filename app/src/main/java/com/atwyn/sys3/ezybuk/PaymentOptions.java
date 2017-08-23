package com.atwyn.sys3.ezybuk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.text.TextUtils.isEmpty;

public class PaymentOptions extends AppCompatActivity {
    ImageButton nextcreditcard;
    String Gpersonname,Gpersonemail,Fbname,Fbemail,Ezname,Ezemail,Ezmobile,Loginemail,Loginname,Loginmobileno;
    EditText name,email,mobileno;

    Button chooseoptions;
    RelativeLayout chooseopt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
  Intent i = this.getIntent(); // get Intent which we set from Previous Activity
     Gpersonname = i.getExtras().getString("Gmail_Name");
        Gpersonemail=  i.getExtras().getString("Gmail_Email");

        Fbname=i.getExtras().getString("FB_Name");
        Fbemail=i.getExtras().getString("FB_Email");

        Ezname=i.getExtras().getString("EzyBuk_name");
        Ezemail=i.getExtras().getString("EzyBuk_Email");
        Ezmobile=i.getExtras().getString("EzyBuk_mobile");

    Loginemail=i.getExtras().getString("Login_Email");
        Loginname=i.getExtras().getString("Login_Name");
        Loginmobileno=i.getExtras().getString("Login_MobileNo");
//      Log.d("Em", Loginemail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(PaymentOptions.this).setTitle(Html.fromHtml("<font color='#ff0000'>Exit</font>"))

                            .setMessage(Html.fromHtml(" Are you sure you want to exit Payment?"))
                            .setIcon(R.drawable.logoezyb)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        Intent in = new Intent(PaymentOptions.this, Main2Activity.class);
                                        startActivity(in);
                                        //finish();
                                    }

                                }
                            }).setNegativeButton("No", null).show();
                }
            });

            name=(EditText) findViewById(R.id.nametx1) ;
            email=(EditText) findViewById(R.id.textView38) ;
            mobileno=(EditText) findViewById(R.id.textView34) ;
            chooseoptions=(Button)findViewById(R.id.choosepaymentopt) ;
chooseopt=(RelativeLayout)findViewById(R.id.rpoptions);
            chooseoptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bmobile=mobileno.getText().toString();
                    if (isEmpty(bmobile)) {
                        mobileno.setError("Please Enter Mobile No. to Proceed");
                        mobileno.requestFocus();
                        return;
                    }
                    chooseopt.setVisibility(View.VISIBLE);
                }
            });

/*
if(Fbname==""|| Fbname==null ||Gpersonname==""||Gpersonname==null) {
    name.setText(Ezname);
    email.setText(Ezemail);
    mobileno.setText(Ezmobile);
}else if(Fbname==""|| Fbname==null || Ezname=="" || Ezname==null)
{
    name.setText(Gpersonname);
    email.setText(Gpersonemail);
}else
{
    name.setText(Fbname);
    email.setText(Fbemail);
}
*/
          name.setText(Loginname);
            email.setText(Loginemail);
            mobileno.setText(Loginmobileno);

/*
if(isEmpty(Fbname) && isEmpty(Ezname))

            {
                name.setText(Gpersonname);
                email.setText(Gpersonemail);
            }
            else if(isEmpty(Fbname) && isEmpty(Gpersonname))
            {
                name.setText(Ezname);
                email.setText(Ezemail);
                mobileno.setText(Ezmobile);
            }else if(isEmpty(Fbemail) && isEmpty(Ezemail) && isEmpty(Gpersonemail))
            {
               email.setText(Loginemail);
            }else
{
    name.setText(Fbname);
    email.setText(Fbemail);
}
*/



            nextcreditcard = (ImageButton) findViewById(R.id.im3);
            nextcreditcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(PaymentOptions.this, Card.class);
                    startActivity(in);
                }
            });
        }
    }
        @Override
        public void onBackPressed() {



                new AlertDialog.Builder(PaymentOptions.this).setTitle(Html.fromHtml("<font color='#ff0000'>Exit</font>"))

                        .setMessage(Html.fromHtml(" Are you sure you want to exit Payment?"))
                        .setIcon(R.drawable.logoezyb)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    Intent in = new Intent(PaymentOptions.this, Main2Activity.class);
                                    startActivity(in);
                                    //finish();
                                }

                            }
                        }).setNegativeButton("No", null).show();

        }

    }

