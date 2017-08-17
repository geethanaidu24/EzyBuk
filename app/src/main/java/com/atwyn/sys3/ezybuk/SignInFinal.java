package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PagerSnapHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInFinal extends AppCompatActivity implements View.OnClickListener  {
    private EditText editTextName;
    private EditText editTextMobile;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextConfirmPassword;
    private Button buttonRegister;
String name,email;
    private static final String REGISTER_URL = Config.EzyBuk_SignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_final);

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

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent in=new Intent(SignInFinal.this, PaymentOptions.class);
                in.putExtra("EzyBuk_name",name);
                in.putExtra("EzyBuk_Email",email);
                in.putExtra("EzyBuk_mobile",mobile);
                startActivity(in);
                Toast.makeText(getApplicationContext(),"Successfully Registered..."+s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("email",params[1]);
                data.put("password",params[2]);
                data.put("mobileno",params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;

            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,email,password,mobile);
    }
}
