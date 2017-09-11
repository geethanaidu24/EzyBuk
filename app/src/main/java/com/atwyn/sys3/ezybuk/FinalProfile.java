package com.atwyn.sys3.ezybuk;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FinalProfile extends AppCompatActivity {
    EditText ed1, ed2, ed3, ed4;
    //  JSONParser jsonParser = new JSONParser();
    String finalprofilname, finalprofileemail, finalprofilemobileno,facebookname2,gmailname2,gmailemail2;
    private DatePicker datePicker;
    private Calendar calendar;
    private boolean loggedIn = false;
    private int year, month, day;
    Button save, selectdate;
    String gender = "";
    private Button pPickDate;
    private int pYear;
    private int pMonth;
    Profile profile;
    private int pDay;
    private static final String REGISTER_URL = Config.EzyBuk_SignUp;
    AccessToken accessToken;
    private static final String TAG_SUCCESS = "success";
    GoogleApiClient mGoogleApiClient;
    OptionalPendingResult<GoogleSignInResult> opr;

    /**
     * This integer will uniquely define the dialog to be used for displaying date picker.
     */
    static final int DATE_DIALOG_ID = 0;
    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay();
                    displayToast();
                }
            };

    /**
     * Updates the date in the TextView
     */
    private void updateDisplay() {
        ed3.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(pMonth + 1).append("/")
                        .append(pDay).append("/")
                        .append(pYear).append(" "));

    }

    /**
     * Displays a notification when the date is updated
     */
    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(ed3.getText()), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_profile);

        Intent i = this.getIntent();
        finalprofilname = i.getExtras().getString("UseName");
        finalprofileemail = i.getExtras().getString("UseEmail");
        finalprofilemobileno = i.getExtras().getString("UserMobileNo");
        facebookname2=i.getExtras().getString("Facebook_Name");
        gmailname2=i.getExtras().getString("Gmail_Name");
        gmailemail2=i.getExtras().getString("Gmail_Email");

        ed1 = (EditText) findViewById(R.id.editText_profilename);
        ed2 = (EditText) findViewById(R.id.editText_user2);
        ed3 = (EditText) findViewById(R.id.editText_user22);
        ed4 = (EditText) findViewById(R.id.editText_user222);
        selectdate = (Button) findViewById(R.id.button8);
        save = (Button) findViewById(R.id.savefinal);
        final RadioGroup rgroup = (RadioGroup) findViewById(R.id.radioGender);
        final RadioButton male = (RadioButton) findViewById(R.id.genderMale);
        final RadioButton female = (RadioButton) findViewById(R.id.genderFemale);

        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        profile = Profile.getCurrentProfile();
        opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(loggedIn)
        {
            ed1.setText(finalprofilname);
            ed2.setText(finalprofilemobileno);
            ed4.setText(finalprofileemail);
        }else if (AccessToken.getCurrentAccessToken() != null)
        {


           ed1.setText(facebookname2);
        }else   if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
           /* // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);*/
            ed1.setText(gmailname2);

            ed4.setText(gmailemail2);
        }


        selectdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });


        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);


        updateDisplay();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed1.getText().toString();

                String email = ed4.getText().toString();
                String mobileno = ed2.getText().toString();
                String dob = ed3.getText().toString();

                if (rgroup.getCheckedRadioButtonId() == male.getId()) {
                    gender = "Male";
                } else if (rgroup.getCheckedRadioButtonId() == female.getId()) {
                    gender = "Female";
                }

                if (name.contentEquals("") || email.contentEquals("") || mobileno.contentEquals("") || dob.contentEquals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FinalProfile.this);

                    builder.setMessage("Fill Data one ghgjghj  ykuh")
                            .setTitle(" Data");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    AlertDialog dialog = builder.show();
                } else {
                 registerUser();
                }
            }
        });
    }



//    class RegisterNewUser extends AsyncTask<String, String, String> {
//        protected String doInBackground(String... args) {
//            String name1 = ed1.getText().toString();
//            String mobileno1 = ed2.getText().toString();
//            String email1 = ed4.getText().toString();
//            String dob1 = ed3.getText().toString();
//
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("name", name1));
//            params.add(new BasicNameValuePair("username", mobileno1));
//            params.add(new BasicNameValuePair("email", email1));
//            params.add(new BasicNameValuePair("password", dob1));
//            params.add(new BasicNameValuePair("role", gender));
//
//            JSONObject json = jsonParser.makeHttpRequest(Config.EzyBuk_SignUp,
//                    "GET", params);
//
//            Log.d("Send Notification", json.toString());
//
//            try
//            {
//                int success = json.getInt(TAG_SUCCESS);
//
//                if (success == 1)
//                {
//
//                  /*  if   (gender.contentEquals("Student"))
//                    {
//                        Intent i = new Intent(getApplicationContext(), StudMain.class);
//                        startActivity(i);
//                        finish();
//                    }
//                    else if ((role.contentEquals("Shop Owner")))
//                    {
//                        Intent i = new Intent(getApplicationContext(), ShopMain.class);
//                        startActivity(i);
//                        finish();
//                    }
//                    else
//                    {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
//
//                        builder.setMessage(R.string.roleNullAlert)
//                                .setTitle(R.string.alertTitle);
//
//                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                            }
//                        });
//
//                        AlertDialog dialog = builder.show();
//                    }
//
//                }
//
//                else
//                {
//
//                }*/
//            }
//
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

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


//            String mobileno1 = ed2.getText().toString();
//            String email1 = ed4.getText().toString();
//            String dob1 = ed3.getText().toString();
        String name1 = ed1.getText().toString().trim().toLowerCase();
        String mobileno1 = ed2.getText().toString().trim().toLowerCase();
        String email1 = ed4.getText().toString().trim().toLowerCase();
        String dob1 = ed3.getText().toString().trim().toLowerCase();
String gend=gender;
        register(name1,mobileno1,email1,dob1,gend);
    }

    private void register(String name1, final String mobileno1, String email1, final String dob1, final String gend) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FinalProfile.this, "Please Wait",null, true, true);
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
                data.put("name",params[0]);
                data.put("email",params[1]);
                data.put("mobileno",params[2]);
                data.put("dob",params[3]);
                data.put("gender",params[4]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;

            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name1,mobileno1,email1,dob1,gend);
    }












    @Override
protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
        return new DatePickerDialog(this,
        pDateSetListener,
        pYear, pMonth, pDay);
        }
        return null;
        }
        }
