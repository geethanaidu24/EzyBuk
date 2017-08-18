package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

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

import static com.atwyn.sys3.ezybuk.R.id.username;

public class Payment extends AppCompatActivity {
    Button payment;
    String useremail,userE;
    String username;
    private boolean loggedIn = false;
    SharedPreferences sharedPreferences;
    final ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();
    private ArrayAdapter<MySQLDataBase> adapter ;
    private static final String EmailPost=Config.EmailSubmitUrlAddress;
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

                       // EmailPost(username);


                        Intent in = new Intent(Payment.this, PaymentOptions.class);
                       in.putExtra("Login_Email", username);

                        startActivity(in);

                    } else  {

                        Intent in = new Intent(Payment.this, LoginMain.class);
                        startActivity(in);
                    }


                }
            });
        }
    }
    public void onStart() {
        super.onStart();
    BackTask b1t = new BackTask();

        b1t.execute();

    }

    private void EmailPost(String username) {
        BackTask b1t = new BackTask();
        b1t.execute();
        AndroidNetworking.post(EmailPost)
                .addBodyParameter("emailid",username)

                .setTag("TAG_ADD")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            //SHOW RESPONSE FROM SERVER
                            // String responseString = response.get(0).toString();
                            // Toast.makeText(ScrollingActivity.this, responseString, Toast.LENGTH_SHORT).show();


                        }
                    }

                    //ERROR
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(Payment.this, "UNSUCCESSFUL :  ERROR IS : " + anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
class BackTask extends AsyncTask<Void, Void, Void> {


    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected Void doInBackground(Void... params) {
        InputStream is = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.EmailSubmitUrlAddress + username );
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
       /* try {




            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;

            mySQLDataBases.clear();
            MySQLDataBase mySQLDataBase;
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                // add interviewee name to arraylist

                String name = jo.getString("UserName");
                String mobileno=jo.getString("UserMobileNo");
                mySQLDataBase = new MySQLDataBase();
                mySQLDataBase.setUserName(name);
                mySQLDataBase.setUserMobileNo(mobileno);
                mySQLDataBases.add(mySQLDataBase);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return null;
    }


    protected void onPostExecute(Void result) {



    }
}



