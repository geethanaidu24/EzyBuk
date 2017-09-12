package com.atwyn.sys3.ezybuk;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PagerSnapHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInFinal extends AppCompatActivity implements View.OnClickListener  {
    private EditText editTextName;
    private EditText editTextMobile;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextConfirmPassword;
    private Button buttonRegister;
String name,email,type;
    String Type="app";
    String Role="3";
    private static final String REGISTER_URL = Config.EzyBuk_SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_final);
      Intent i = this.getIntent(); // get Intent which we set from Previous Activity
        type = i.getExtras().getString("Type");
        editTextName = (EditText) findViewById(R.id.registration_edit_text_userNamename);
        editTextEmail = (EditText) findViewById(R.id.registration_edit_text_userName);
        editTextPassword = (EditText) findViewById(R.id.registration_text_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.registration_text_confirm_password);

        editTextMobile = (EditText) findViewById(R.id.registration_text_mobile_no2);


        buttonRegister = (Button) findViewById(R.id.sign_up_button);

        buttonRegister.setOnClickListener(this);
}
    @Override
    public void onClick(View v) {
        String strPwd = editTextPassword.getText().toString();
        String strConfPwd = editTextConfirmPassword.getText().toString();

        if(v == buttonRegister){
            if (strPwd.equals(strConfPwd)) {
                //password and confirm passwords equal.go to next step
                registerUser();

            } else {
                Toast.makeText(getApplicationContext(), "Password Not Equal", Toast.LENGTH_SHORT).show();
            }
            String email = editTextEmail.getText().toString();
            String mobile = editTextMobile.getText().toString();
            Boolean onError = false;
            if(!TextUtils.isEmpty(email)){
                if (!isValidEmail(email)) {
                    onError = true;
                    editTextEmail.setError("Invalid Email");
                    return;
                }
            }
            if(!TextUtils.isEmpty(mobile)) {
                if (!isValidPhone(mobile)) {
                    onError = true;
                    editTextMobile.setError("Invalid contact");
                    return;
                }
            }
           // registerUser();
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidPhone(String phone)
    {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }


    private void registerUser() {
      /*  editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String strPass1 = editTextPassword.getText().toString();
                String strPass2 = editTextConfirmPassword.getText().toString();
                if (strPass1.equals(strPass2)) {
                    Toast.makeText(getApplicationContext(), "Password Equal", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Password Not Equal", Toast.LENGTH_SHORT).show();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });*/

        String name = editTextName.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String mobile = editTextMobile.getText().toString().trim().toLowerCase();

        register(name,password,email,mobile);
    }

    private void register(final String name, String password, final String email, final String mobile) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignInFinal.this, "Please Wait",null, true, true);
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if(s.equalsIgnoreCase(Config.LOGIN_SUCCESS)&& Objects.equals(type, "Payment_Login")) {
                        SharedPreferences sharedPreferences = SignInFinal.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        //Adding values to editor
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                        editor.putString("LoginEmailId", email);
                        Log.d("Login Email", " >" + email);

                        editor.putString(Config.LOGIN_CHECK,"suc");


                        //editor.putBoolean(loginExits,true);


                        //Saving values to editor
                        editor.commit();
                        //Log.d("Login SharedEmaoil", " >" + geetha);

                        //Starting profile activity

                        Intent in = new Intent(SignInFinal.this, PaymentOptions.class);
                        in.putExtra("EzyBuk_name", name);
                        in.putExtra("EzyBuk_Email", email);
                        in.putExtra("EzyBuk_mobile", mobile);
                       in.putExtra("Type_Login", "EzyBuk_Registration");
                        startActivity(in);
                        Toast.makeText(getApplicationContext(), "Successfully Registered..." + s, Toast.LENGTH_LONG).show();
                    } else if(s.equalsIgnoreCase(Config.LOGIN_SUCCESS)&& Objects.equals(type, "Main_Login")) {

                        SharedPreferences sharedPreferences = SignInFinal.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        //Adding values to editor
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                        editor.putString("LoginEmailId", email);
                        Log.d("Login Email", " >" + email);

                        editor.putString(Config.LOGIN_CHECK,"suc");


                        //editor.putBoolean(loginExits,true);


                        //Saving values to editor
                        editor.commit();
                        Intent intent = new Intent(SignInFinal.this, Main2Activity.class);
                      //  intent.putExtra("EzyBuk_name", name);
                       // intent.putExtra("EzyBuk_Email", email);
                      //  intent.putExtra("EzyBuk_mobile", mobile);
                      //  intent.putExtra("Type_Login", "EzyBuk_Registration");
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("Type",params[0]);
                data.put("Role",params[1]);
                data.put("name",params[2]);
                data.put("email",params[3]);
                data.put("password",params[4]);
                data.put("mobileno",params[5]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;


            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(Type,Role,name,email,password,mobile);
        Log.d("Register",REGISTER_URL+"/"+Type+"/"+Role+"/"+name+"/"+email+"/"+password+"/"+mobile);
    }
}
