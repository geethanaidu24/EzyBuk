package com.atwyn.sys3.ezybuk;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Handler;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient client;
    private static int SPLASH_TIME_OUT = 500;
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        // MultiDex.install(this);
        setContentView(R.layout.activity_main);
        PrintHashKey();
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }
    }
        private void PrintHashKey() {

            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.atwyn.sys3.ezybuk",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }



     new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent startActivityIntent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(startActivityIntent);

                MainActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);





                // close this activity

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
}