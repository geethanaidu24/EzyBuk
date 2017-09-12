package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.rom4ek.arcnavigationview.ArcNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.internal.FacebookDialogFragment.TAG;

public class MyProfile extends AppCompatActivity {
    Button b1, b2, b3, b4, b5;
    Profile profile;
    private boolean loggedIn = false;
TextView tx,pLogin;
    GoogleApiClient mGoogleApiClient;
   // GoogleApiClient googleApiClient;
    OptionalPendingResult<GoogleSignInResult> opr;
    String profilename,profileemail,profilemobileno,facebookname1,gmailname,gamilemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_my_profile);
        Intent i = this.getIntent();
profilename = i.getExtras().getString("UseName");
        profileemail=i.getExtras().getString("UseEmail");
        profilemobileno=i.getExtras().getString("UserMobileNo");
        facebookname1=i.getExtras().getString("Facebook_Name");

        gmailname = i.getExtras().getString("Gmail_Name");
        gamilemail=i.getExtras().getString("Gmail_Email");

        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        //Log.d("PProfilemobileno",profilemobileno);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MyProfile.this, Main2Activity.class);
                    //startActivity(in);
                    finish();
                }
            });
        }
        pLogin=(TextView)findViewById(R.id.logoinprofile);
tx=(TextView)findViewById(R.id.profilename) ;
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
       profile = Profile.getCurrentProfile();

      opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if(loggedIn ) {
            tx.setText(profilename);
        }
        else if(AccessToken.getCurrentAccessToken() != null)
        {

       tx.setText(facebookname1);
        }else   if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
           /* // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);*/
            tx.setText(gmailname);
        }
        else
        {
            tx.setText("Welcome Guest");
            pLogin.setVisibility(View.VISIBLE);
            pLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MyProfile.this, LoginMain.class);
                    startActivity(in);
                }
            });


        }
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        b5 = (Button) findViewById(R.id.b5);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loggedIn ) {
                    Intent in = new Intent(MyProfile.this, FinalProfile.class);
                    in.putExtra("UseName",  profilename);
                    in.putExtra("UseEmail", profileemail);
                    in.putExtra("UserMobileNo", profilemobileno);

                    startActivity(in);
                }
                else if(profile != null)
                {

                    Intent in = new Intent(MyProfile.this, FinalProfile.class);

                    in.putExtra("Facebook_Name",facebookname1);
                    startActivity(in);


                }
                else   if (opr.isDone()) {
                    // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                    // and the GoogleSignInResult will be available instantly.
                    Intent in = new Intent(MyProfile.this, FinalProfile.class);
                    in.putExtra("Gmail_Name",  gmailname);
                    in.putExtra("Gmail_Email", gamilemail);


                    startActivity(in);
                }else
                {
                    Intent in = new Intent(MyProfile.this, LoginMain.class);
                    startActivity(in);
                }


            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loggedIn ) {
                    Intent in = new Intent(MyProfile.this, TicketsBookingHistory.class);
                    startActivity(in);

                }else if(profile != null)
                {

                    Intent in = new Intent(MyProfile.this, TicketsBookingHistory.class);
                    startActivity(in);


                }
                else
                {

                    Intent in = new Intent(MyProfile.this, LoginMain.class);
                    startActivity(in);


                }


            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loggedIn ) {
                    Intent in = new Intent(MyProfile.this, SavedCardsDetails.class);
                    startActivity(in);


                }else if(profile != null)
                {

                    Intent in = new Intent(MyProfile.this, SavedCardsDetails.class);
                    startActivity(in);


                }else   if (opr.isDone()) {
                    Intent in = new Intent(MyProfile.this, SavedCardsDetails.class);
                    startActivity(in);


                }
                else
                {

                    Intent in = new Intent(MyProfile.this, LoginMain.class);
                    startActivity(in);


                }


            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loggedIn ) {
                    Intent in = new Intent(MyProfile.this, Changepassword.class);
                    in.putExtra("UseEmail", profileemail);
                    startActivity(in);


                }else if(profile != null)
                {

                    Intent in = new Intent(MyProfile.this, Changepassword.class);
                    in.putExtra("UseEmail", profileemail);
                    startActivity(in);




                }
                else
                {

                    Intent in = new Intent(MyProfile.this, LoginMain.class);
                    startActivity(in);


                }



            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loggedIn ) {
                    logout();


                }
                else if(profile != null)
                {
                    logout();



                }else   if (opr.isDone()) {
                    logout();

                }
                else
                {

                    Intent in = new Intent(MyProfile.this, LoginMain.class);
                    startActivity(in);


                }
               
            }
        });


    }
    private void logout(){
        //Creating an alert dialog to confirm logout

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setTitle(" Are you sure you want to logout?");
        // alertDialogBuilder.setIcon(R.drawable.logoutt);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.KEY_USER, "");

                        //Saving the sharedpreferences
                        editor.apply();

                        //Starting login activity

                        Intent intent = new Intent(MyProfile.this, Main2Activity.class);
                        intent.putExtra("finish", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();

                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient.connect();
                        Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();

                        LoginManager.getInstance().logOut();
                        AccessToken.setCurrentAccessToken(null);
                        Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

        opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
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
            // showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //     hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });


        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.


            GoogleSignInAccount acct = result.getSignInAccount();

            Log.d(TAG, "display name: " + acct.getDisplayName());
            tx.setText(acct.getDisplayName());


        }

    }


}
