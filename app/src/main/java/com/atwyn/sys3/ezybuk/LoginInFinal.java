package com.atwyn.sys3.ezybuk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class LoginInFinal extends AppCompatActivity /*implements View.OnClickListener */ {
    ImageView im4;
    int click = 0;
    //Defining views
    private EditText editName;
    private EditText editPassword;
    private Button sign_in_button;
TextView forgotpwd;
    String useremail;
    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    private EditText editTextUserName;
    private EditText editTextPassword;

    public static final String USER_NAME = "USERNAME";
    String password;
    String username;
    String loginType="app";
    //String Role="3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in_final);

        //  sign_in_button = (Button) findViewById(R.id.signin);
    /*    sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginInFinal.this, PaymentOptions.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });*/
      editName = (EditText) findViewById(R.id.editText_user);
        editPassword = (EditText) findViewById(R.id.editText_password);
        sign_in_button = (Button) findViewById(R.id.sign_in_button);

        forgotpwd=(TextView)findViewById(R.id.forgot);

        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginInFinal.this, ForgotPassword.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
      //  sign_in_button.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click = click + 1;
                    if (click == 1) {
                        click = 0;
                        Intent in = new Intent(LoginInFinal.this, LoginMain.class);


                        finish();
                    }
                    //startActivity(in);
                }
            });
        }

    }

    public void onBackPressed() {
        //finishAffinity();
        click = click + 1;
        if (click == 1) {
            click = 0;
            Intent in = new Intent(LoginInFinal.this, LoginMain.class);

            finish();
        }
    }


 @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        //getMenuInflater().inflate(R.menu.mainproducts, menu);        //If we will get true
        if(loggedIn){
            //We will start the Main Activity
            Intent intent = new Intent(LoginInFinal.this, PaymentOptions.class);
            intent.putExtra("Login_Email", useremail);
          //  intent.putExtra("Login_Email",useremail);
            Log.d("emailadd", " >" + useremail);

            // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

        }
    }
    public void invokeLogin(View view) {

        login();

    }

    private void login(){
        //Getting values from edit texts
       useremail = editName.getText().toString().trim();
      password = editPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.loginUrlAddress+'/'+loginType+'/'+useremail+'/'+password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if(response.equalsIgnoreCase(Config.LOGIN_SUCCESS)){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginInFinal.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString("LoginEmailId", useremail);
                            Log.d("Login Email", " >" + useremail);

                            editor.putString(Config.LOGIN_CHECK,"suc");


                            //editor.putBoolean(loginExits,true);


                            //Saving values to editor
                            editor.commit();
                            //Log.d("Login SharedEmaoil", " >" + geetha);

                            //Starting profile activity
                            Intent intent = new Intent(LoginInFinal.this, PaymentOptions.class);
                            // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            //intent.putExtra("loggedIn", "true");
                            intent.putExtra("Login_Email",useremail);
                            Log.d("emailadd", " >" + useremail);

                            startActivity(intent);


                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                          //  Toast toast = Toast.makeText(LoginInFinal.this, "Invalid username or password", Toast.LENGTH_LONG);


                         Toast.makeText(LoginInFinal.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_USER, useremail);
                params.put(Config.KEY_PASS, password);
params.put("loginType","app");

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



   /* public void onClick(View v) {

        login();

    }*/
}

      /*  editTextUserName = (EditText) findViewById(R.id.editText_user);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
    }

    public void invokeLogin(View view) {
        username = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();

        login(username, password);

    }

    private void login(final String username, String password) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginInFinal.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            Config.loginUrlAddress+'/'+uname+'/'+pass );
             //  String     url = new String(Config.loginUrlAddress+'/'+uname+'/'+pass );
                    //Log.d("second url", " >" + url);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                loadingDialog.dismiss();

                if (s.equalsIgnoreCase("success")) {

                    Intent intent = new Intent(LoginInFinal.this, PaymentOptions.class);
                    intent.putExtra(USER_NAME, username);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }
}*/