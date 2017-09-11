package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

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

public class Payment extends AppCompatActivity {
    Button payment;
    String name1, mobileno1;
    String username;

    private boolean loggedIn = false;
    SharedPreferences sharedPreferences;
    final ArrayList<MySQLDataBase> mySQLDataBases4 = new ArrayList<>();
    private ArrayAdapter<MySQLDataBase> adapter;
    private static final String EmailPost = Config.EmailSubmitUrlAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(Payment.this, SeatReservation.class);
                    //startActivity(in);
                    finish();
                }
            });

            payment = (Button) findViewById(R.id.button2);
            payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                    //getMenuInflater().inflate(R.menu.mainproducts, menu);

                    if (loggedIn) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        username = sharedPreferences.getString("LoginEmailId", "");
                        // boolean loggedIn = String.valueOf(pref.getBoolean("logged_in", false);
                        //  String username = sharedPreferences.getString(useremail, "");
                        Log.d("EmailId", username);
                        Log.d("hieel", String.valueOf(loggedIn));

                        EmailPost();


                     /*   Intent in = new Intent(Payment.this, PaymentOptions.class);
                       in.putExtra("Login_Email", username);

                        startActivity(in);
*/
                    } else {

                        Intent in = new Intent(Payment.this, LoginMain.class);
                        in.putExtra("Type","Payment_Login");
                        startActivity(in);
                    }


                }
            });
        }
    }


    public void EmailPost( ) {
        BackTask b1t = new BackTask();
        b1t.execute();

    }

    class BackTask extends AsyncTask<Object, Object, ArrayList<MySQLDataBase>> {


        //private MySQLDataBase mySQLDataBase4;

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected ArrayList<MySQLDataBase> doInBackground(Object... params) {

            InputStream is = null;
            String result = "";
            try {
                Log.d("EmailIdiii", username);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.EmailSubmitUrlAddress + username);
                String h3 = Config.EmailSubmitUrlAddress + username;
                Log.d("hjkchjsdhvj url", " >" + h3);
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                org.apache.http.HttpEntity entity = response.getEntity();
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

                  name1 = jo.getString("Name");
                   mobileno1 = jo.getString("Mobile");
                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setUserName(name1);
                    mySQLDataBase.setUserMobileNo(mobileno1);
                    mySQLDataBases4.add(mySQLDataBase);
               //     mysql = mySQLDataBases4.toString();

                    Log.d("Name", name1);
                    Log.d("Mobile", mobileno1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mySQLDataBases4;
        }

        protected void onPostExecute(ArrayList<MySQLDataBase> result) {
           // Log.d("Result", result);
         /*   name11 = mySQLDataBases4.getUserName();
           mobilno11=mySQLDataBases4.getUserMobileNo();
            Log.d("Name11", name11);
            Log.d("Mobile11", mobilno11);
*/
Intent in = new Intent(Payment.this, PaymentOptions.class);
        in.putExtra("Login_Email", username);
in.putExtra("Login_Name", name1);
in.putExtra("Login_MobileNo", mobileno1);
         //   Log.d("Mobile11111", mobileno1);
        startActivity(in);

        }
    }
}



