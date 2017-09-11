package com.atwyn.sys3.ezybuk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import static android.text.TextUtils.isEmpty;

public class PaymentOptions extends AppCompatActivity {
    ImageButton nextcreditcard;
    String Gpersonname,Gpersonemail,Fbname,Fbemail,Ezname,Ezemail,Ezmobile,Loginemail,Loginname,Loginmobileno,typeLogin;
    EditText name,email,mobileno;
    String name1,mobile1;
    Button chooseoptions;
    RelativeLayout chooseopt;
    final ArrayList<MySQLDataBase> mySQLDataBases4 = new ArrayList<>();

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

        typeLogin=i.getExtras().getString("Type_Login");
   //Log.d("Em", typeLogin);
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(typeLogin, "EzyBuk_Login")) {

                    email.setText(Loginemail);
                  BackTask b1t = new BackTask();
                    b1t.execute();


                } else if (Objects.equals(typeLogin, "EzyBuk_Registration")) {
                    name.setText(Ezname);
                    email.setText(Ezemail);
                    mobileno.setText(Ezmobile);
                } else if (Objects.equals(typeLogin, "Facebook")) {
                    name.setText(Fbname);
                    email.setText(Fbemail);
                } else {
                    name.setText(Gpersonname);
                    email.setText(Gpersonemail);
                }

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
            }


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

    private class BackTask extends AsyncTask<Object, Object, String> {


        //private MySQLDataBase mySQLDataBase4;

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Object... params) {

            InputStream is = null;
            String result = "";
            try {
                Log.d("EmailIdiii", Loginemail);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.EmailSubmitUrlAddress + Loginemail);
                String h3 = Config.EmailSubmitUrlAddress + Loginemail;
                Log.d("hjkchjsdhvj urlgfh", " >" + h3);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                is.close();
                //result=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // parse json data
            // String mysql = null;

            try {


                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;


                mySQLDataBases4.clear();
                MySQLDataBase mySQLDataBase;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    // add interviewee name to arraylist

                   name1 = jo.getString("name");
                 mobile1=jo.getString("mobile");

                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setUserName(name1);
                    mySQLDataBase.setUserMobileNo(mobile1);

                    mySQLDataBases4.add(mySQLDataBase);
                    //     mysql = mySQLDataBases4.toString();

                    // Log.d("Mysql",mySQLDataBases4);
                    Log.d("Name", name1);
                    Log.d("MobileNoUse",mobile1);



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result) {

     /*  // dialog.dismiss();
            String lol1= result.get(0).toString();
        //    String lol2= result.get(1).toString();
Log.d("lllllflkld", String.valueOf(result));*/

            name.setText(name1);
            mobileno.setText(mobile1);

        }
    }



}

