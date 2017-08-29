package com.atwyn.sys3.ezybuk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {
EditText emailaddress;
    Button Proceed;
    private static final String Forgot_PSWD_URL = Config.EzyBuk_SignUp;
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
                    String emailadd = emailaddress.getText().toString();
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

                    Forgotpswd(emailadd);

                }


            }

            private void Forgotpswd(String emailadd) {

                class RegisterUser extends AsyncTask<String, Void, String> {
                    ProgressDialog loading;
                    RegisterUserClass ruc = new RegisterUserClass();


                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        loading = ProgressDialog.show(ForgotPassword.this, "Please Wait",null, true, true);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        loading.dismiss();
             /*   Intent in=new Intent(FinalProfile.this, PaymentOptions.class);
                in.putExtra("EzyBuk_name",name);
                in.putExtra("EzyBuk_Email",email);
                in.putExtra("EzyBuk_mobile",mobile);
                startActivity(in);
                Toast.makeText(getApplicationContext(),"Successfully Registered..."+s,Toast.LENGTH_LONG).show();*/

                    }

                    @Override
                    protected String doInBackground(String... params) {

                        HashMap<String, String> data = new HashMap<String,String>();
                        data.put("email",params[0]);


                        String result = ruc.sendPostRequest(Forgot_PSWD_URL,data);

                        return  result;

                    }
                }

                RegisterUser ru = new RegisterUser();
                ru.execute(emailadd);
            }


        });


    }
}
