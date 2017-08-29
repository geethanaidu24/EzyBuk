package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class Changepassword extends AppCompatActivity {
EditText ed11,ed22;
    String profileemail;
    Button reset;
    private static final String CHANGE_PSWD_URL = Config.EzyBuk_SignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        Intent i = this.getIntent();

      profileemail = i.getExtras().getString("UseEmail");

        ed11=(EditText)findViewById(R.id.oldpasswd);
        ed22=(EditText)findViewById(R.id.newpswd);
        reset=(Button)findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldpswd = ed11.getText().toString();

                String newpswd = ed22.getText().toString();

                if (oldpswd.contentEquals("") || newpswd.contentEquals("") ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Changepassword.this);

                    builder.setMessage("Fill Data")
                            .setTitle(" Data");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    AlertDialog dialog = builder.show();
                } else {
                    Changepswd();
                }
            }
        });
            }
    private void Changepswd() {

        String oldpswd = ed11.getText().toString().trim().toLowerCase();
        String newpswd = ed22.getText().toString().trim().toLowerCase();

        register(profileemail,oldpswd,newpswd);
    }

    private void register(String email, final String oldp, String newp) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Changepassword.this, "Please Wait",null, true, true);
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
                data.put("oldpswd",params[1]);
                data.put("newpswd",params[2]);

                String result = ruc.sendPostRequest(CHANGE_PSWD_URL,data);

                return  result;

            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(email,oldp,newp);
    }







}
