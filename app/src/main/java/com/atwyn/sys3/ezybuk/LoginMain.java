package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.id;
import static com.atwyn.sys3.ezybuk.R.id.imgProfilePic;
import static com.atwyn.sys3.ezybuk.R.id.txtEmail;
import static com.atwyn.sys3.ezybuk.R.id.txtName;

public class LoginMain extends AppCompatActivity  implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String UPLOAD_URL = Config.Facebook_Gmail;
    private static final String UPLOAD_URL1 = Config.Gmail;
    Button login, signup;
    CallbackManager callbackManager;


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
String name,email,gender,birthday,mobile,pass,mobile1;
    String gpersonname,gemail;
    private SignInButton btnSignIn;
    String id;
    String password=null;

//    private Button btnSignOut, btnRevokeAccess;
//    private LinearLayout llProfileLayout;
//    private ImageView imgProfilePic;
//    private TextView txtName, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(LoginMain.this).setTitle(Html.fromHtml("<font color='#ff0000'>Exit</font>"))

                            .setMessage(Html.fromHtml(" Are you sure you want to exit Payment?"))
                            .setIcon(R.drawable.logoezyb)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        Intent in = new Intent(LoginMain.this, Main2Activity.class);
                                        startActivity(in);
                                        //finish();
                                    }

                                }
                            }).setNegativeButton("No", null).show();


                }
            });
            login = (Button) findViewById(R.id.button3);
            signup = (Button) findViewById(R.id.button4);


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(LoginMain.this, RegistrationLogin.class);
                    startActivity(in);

                }
            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(LoginMain.this, RegistrationLogin.class);
                    startActivity(in);
                }
            });
        }
//FACEBOOK LOGIN
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        List< String > permissionNeeds = Arrays.asList("user_photos", "email"
                /*"user_birthday"*/, "public_profile", "AccessToken");
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback < LoginResult > () {@Override
                public void onSuccess(LoginResult loginResult) {

                    System.out.println("onSuccess");

                    String accessToken = loginResult.getAccessToken()
                            .getToken();
                    Log.i("accessToken", accessToken);

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {@Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                Log.i("LoginActivity", response.toString());

                                try {
                                /*   id = object.getString("id");
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=large");
                                        Log.i("profile_pic",
                                                profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }*/
                                    name = object.getString("name");
                                   email = object.getString("email");
                                 /*mobile=object.getString("mobileno");
                                    pass=password;
                                    if(mobile==""|| mobile==null)
                                    {
                                        mobile1=null;
                                    }else
                                    {
                                        mobile1=mobile;
                                    }*/
                                  //  gender = object.getString("gender");
                                    //birthday = object.getString("birthday");
                                   uploadMultipart();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields",
                            "name,email");

                    request.setParameters(parameters);
                    request.executeAsync();
                }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });

        //GMAIL LOGIN

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
        //btnSignOut.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }*/

   //FACEBOOK SUBMIT TO MYSQL
   public void uploadMultipart() {
       String bname =name;
     //  String baddress = gender;
        String bemail=email;
      String bmobile=mobile1;
       String bpassword = pass;
Log.d("facebook name"+">", bname);
       Log.d("fb mail"+">", bemail);



       try {
           AndroidNetworking.post(UPLOAD_URL)

                   .addBodyParameter("name", bname)
                   //.addBodyParameter("gender", baddress)
                   .addBodyParameter("email",bemail)
                  //.addBodyParameter("mobileno",bmobile)
                //   .addBodyParameter("password", bpassword)

                   .setTag("TAG_ADD")
                   .build()
                   .getAsJSONArray(new JSONArrayRequestListener() {
                       @Override
                       public void onResponse(JSONArray response) {


                           if(response != null)
                               try {
                                   //SHOW RESPONSE FROM SERVER

                                   String responseString = response.get(0).toString();
                                    Toast.makeText(LoginMain.this, "Ss" + responseString, Toast.LENGTH_SHORT).show();
                                   if (responseString.equalsIgnoreCase("Success")) {
                                       Intent intent = new Intent(LoginMain.this, PaymentOptions.class);
                                       intent.putExtra("FB_Name",name);
                                       intent.putExtra("FB_Email",email);
                                      /* intent.putExtra("FB_Gender",gender);
                                       intent.putExtra("FB_birthday",birthday);*/
                                       startActivity(intent);

                                   }else
                                   {
                                       Toast.makeText(LoginMain.this, " ", Toast.LENGTH_SHORT).show();
                                   }
                               } catch (JSONException e) {
                                   e.printStackTrace();
                                   Toast.makeText(LoginMain.this, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                       }
                       //ERROR
                       @Override
                       public void onError(ANError anError) {
                           Toast.makeText(LoginMain.this, "UNSUCCESSFUL :  ERROR IS : "+anError.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });

       } catch (Exception exc) {
           Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
       }

   }
/* protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {

                        Log.i("LoginActivity", response.toString());

                        try {

                            name = json_object.getString("personname");
                            email = json_object.getString("email");
                            //  mobile=object.getString("mobile");

                            uploadMultipartGmail();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }*/

    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


   /* private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }*/


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.


         GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());


           gpersonname = acct.getDisplayName();

            String personPhotoUrl = acct.getPhotoUrl().toString();
            gemail = acct.getEmail();

            Log.e(TAG, "Name: " + gpersonname + ", email: " + gemail
                    + ", Image: " + personPhotoUrl);

            uploadMultipartGmail();

          /*  Intent mainIntent = new Intent(LoginMain.this, PaymentOptions.class);
            mainIntent.putExtra("Gmail_Name",gpersonname);
            mainIntent.putExtra("Gmail_Email",gemail);
            startActivity(mainIntent);*/


        updateUI(true);
    } else

    {
        // Signed out, show unauthenticated UI.
        updateUI(false);
    }

}

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();

                break;

        /*    case R.id.btn_sign_out:
                signOut();
                break;*/


        }
    }
    public void uploadMultipartGmail() {
        String gname =gpersonname;
        String gemailfinal=gemail;
        // String bmobile=mobile;
        // String bbirthday = birthday;
    /*    Log.d("facebook name"+">", bname);
        Log.d("fb mail"+">", bemail);*/



        try {
            AndroidNetworking.post(UPLOAD_URL1)

                    .addBodyParameter("name", gname)
                    .addBodyParameter("email",gemailfinal)
                    //    .addBodyParameter("mobile",bmobile)
                    //.addBodyParameter("birthday", bbirthday)

                    .setTag("TAG_ADD")
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {


                            if(response != null)
                                try {
                                    //SHOW RESPONSE FROM SERVER

                                    String responseString = response.get(0).toString();
                                    Toast.makeText(LoginMain.this, "Ss" + responseString, Toast.LENGTH_SHORT).show();
                                    if (responseString.equalsIgnoreCase("Success")) {
                                        Intent mainIntent = new Intent(LoginMain.this, PaymentOptions.class);
                                        mainIntent.putExtra("Gmail_Name",gpersonname);
                                        mainIntent.putExtra("Gmail_Email",gemail);
                                        startActivity(mainIntent);

                                    }else
                                    {
                                        Toast.makeText(LoginMain.this, " ", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginMain.this, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        }
                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(LoginMain.this, "UNSUCCESSFUL :  ERROR IS : "+anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);


        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
           /* btnSignOut.setVisibility(View.VISIBLE);

            llProfileLayout.setVisibility(View.VISIBLE);*/
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            /*btnSignOut.setVisibility(View.GONE);

            llProfileLayout.setVisibility(View.GONE);*/
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}

