package com.atwyn.sys3.ezybuk;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rom4ek.arcnavigationview.ArcNavigationView;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.internal.FacebookDialogFragment.TAG;

public class Main2Activity extends TabActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabHost.OnTabChangeListener {
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private String[] pageTitle = {"Now Showing", "Coming Soon "};
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private boolean loggedIn = false;
    GoogleApiClient googleApiClient;
    private static final Integer[] IMAGES= {R.drawable.movposttwo, R.drawable.backezybuk, R.drawable.backezybuktwo};
    String navigationusername;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
TabHost tabHost;
    protected Menu menu;
    TextView navname;
    TextView navemail;
    String name1,mobile1,facebookname;
    private static String KEY_SUCCESS = "success";
    int valoreOnPostExecute = 0;
    final ArrayList<MySQLDataBase> mySQLDataBases4 = new ArrayList<>();
    final ArrayList userdata = new ArrayList<>();
    private ArrayAdapter<MySQLDataBase> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main2);


       // viewPager = (ViewPager) findViewById(R.id.view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  //setActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.search));

         toolbar.inflateMenu(R.menu.main2);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if(arg0.getItemId() == R.id.h1){
                    Intent in = new Intent(Main2Activity.this, Search.class);
                    startActivity(in);
                }
                return false;
            }
        });
      /*  googleApiClient = new GoogleApiClient.Builder(getApplicationContext()) //Use app context to prevent leaks using activity
                //.enableAutoManage(this *//* FragmentActivity *//*, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
   init();
*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)

                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

// more stuff here...

        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
  ArcNavigationView navigationView = (ArcNavigationView) findViewById(R.id.nav_view);

        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        navname=(TextView)header.findViewById(R.id.uname) ;
        navemail=(TextView)header.findViewById(R.id.uemail) ;
       // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        Profile profile = Profile.getCurrentProfile();
        if(loggedIn)
        {
            navigationView.getMenu().clear();
            sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);

            navigationusername = sharedPreferences.getString("LoginEmailId", "UNKNOWN");
            // boolean loggedIn = String.valueOf(pref.getBoolean("logged_in", false);
            //  String username = sharedPreferences.getString(useremail, "");
            Log.d("EmailIdddddddddd", navigationusername);
            //  Log.d("hieel", String.valueOf(loggedIn));
            navemail.setText(navigationusername);

          BackTask b1t = new BackTask();
            b1t.execute();

            navigationView.inflateMenu(R.menu.menu_logout);
        }else if(profile != null )
        {
            navigationView.getMenu().clear();
        /*   navemail.setText(profile.getId());
            Log.d("shreyemail", profile.getId());*/
         facebookname=profile.getName();

            navname.setText("Hello" + " " +facebookname);
            navigationView.inflateMenu(R.menu.menu_logout);

        }
        else if(googleApiClient != null && googleApiClient.isConnected() )
        {
            navigationView.getMenu().clear();
        /*   navemail.setText(profile.getId());
            Log.d("shreyemail", profile.getId());*/
            Intent data = null;

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();

            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            navname.setText("Hello" + " " +personName);
            navigationView.inflateMenu(R.menu.menu_logout);

        }
        else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main2_drawer);


        }

       ProfileTracker fbProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // User logged in or changed profile
            }
        };

       // Profile profile = Profile.getCurrentProfile();
      /*  if (profile != null) {
            Log.d(TAG, "Logged, user name=" + profile.getFirstName() + " " + profile.getLastName()+ " "+ profile.getName());
        }*/

        //setting Tab layout (number of Tabs = number of ViewPager pages)
       /* tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (int i = 0; i < 2; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //handling navigation view item event

        //set viewpager adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

       /* TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);// initiate TabHost
        tabHost.setup();


        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        spec = tabHost.newTabSpec("Now Showing"); // Create a new TabSpec using tab host
        spec.setIndicator("Now Showing"); // set the “HOME” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, NowShowing_movies.class);
        spec.setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs

        spec = tabHost.newTabSpec("Upcoming Movies"); // Create a new TabSpec using tab host
        spec.setIndicator("Upcoming Movies"); // set the “CONTACT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, UpComing_movies.class);
        spec.setContent(intent);
        tabHost.addTab(spec);

     *//*   spec = tabHost.newTabSpec("About"); // Create a new TabSpec using tab host
        spec.setIndicator("ABOUT"); // set the “ABOUT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, AboutActivity.class);
        spec.setContent(intent);
        tabHost.addTab(spec);*//*
        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // display the name of the tab whenever a tab is changed
                //  Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });
*/
        tabHost = getTabHost();
        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec spec;
        Intent intent;

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, NowShowing_movies.class);
        spec = tabHost.newTabSpec("Now Showing").setIndicator("Now Showing")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, UpComing_movies.class);
        spec = tabHost.newTabSpec("Upcoming Movies").setIndicator("Upcoming Movies")
                .setContent(intent);
        tabHost.addTab(spec);


        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#99FF99"));
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
        tabHost.getTabWidget().setCurrentTab(1);
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#CCFFCC"));
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#000000"));
    }



    @Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#99FF99"));
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        }

        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#CCFFCC"));
        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        tv.setTextColor(Color.parseColor("#000000"));
    }
    class BackTask extends AsyncTask<Object, Object, String> {


        //private MySQLDataBase mySQLDataBase4;

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Object... params) {

            InputStream is = null;
            String result = "";
            try {
                Log.d("EmailIdiii", navigationusername);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.EmailSubmitUrlAddress + navigationusername);
                String h3 = Config.EmailSubmitUrlAddress + navigationusername;
                Log.d("hjkchjsdhvj url", " >" + h3);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
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
            // String mysql = null;

            try {


                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;


                mySQLDataBases4.clear();
                MySQLDataBase mySQLDataBase;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    // add interviewee name to arraylist

                    name1 = jo.getString("Name");
                    mobile1=jo.getString("Mobile");

                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setUserName(name1);
                    mySQLDataBase.setUserMobileNo(mobile1);

                    mySQLDataBases4.add(mySQLDataBase);
                    //     mysql = mySQLDataBases4.toString();

                   // Log.d("Mysql",mySQLDataBases4);
                    Log.d("Name", name1);
                    Log.d("MobileNoUse",mobile1);



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return name1;
        }
        protected void onPostExecute(String result) {

     /*  // dialog.dismiss();
            String lol1= result.get(0).toString();
        //    String lol2= result.get(1).toString();
Log.d("lllllflkld", String.valueOf(result));*/
 navname.setText("Hello" + " " +result);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.h1) {
            Intent in = new Intent(Main2Activity.this, Search.class);
            startActivity(in);
        }/*else if(id== R.id.h2)
        {
            Intent in = new Intent(Main2Activity.this, SelectCity.class);
            startActivity(in);
        }*/

        return super.onOptionsItemSelected(item);
    }


    private void init() {


        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) findViewById(R.id.pager1);


        mPager.setAdapter(new SlidingImage_Adapter(Main2Activity.this,ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator1);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);



        NUM_PAGES =IMAGES.length;



        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fr1) {
          Intent in=new Intent(Main2Activity.this,NowShowing_movies.class);
            startActivity(in);
        } else if (id == R.id.fr2) {
            Intent in=new Intent(Main2Activity.this,UpComing_movies.class);
            startActivity(in);

        }

        else if(id == R.id.logout)
        {
           logout();

        }
        else if(id == R.id.login)
        {
            Intent in=new Intent(Main2Activity.this,LoginMain.class);
            startActivity(in);


        }
     if (id == R.id.profile) {
            Intent intent = new Intent(this, MyProfile.class);
     intent.putExtra("UseName",  name1);
         intent.putExtra("UseEmail", navigationusername);
         intent.putExtra("UserMobileNo", mobile1);
         intent.putExtra("Facebook_Name",facebookname);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

                        Intent intent = new Intent(Main2Activity.this, Main2Activity.class);
                        intent.putExtra("finish",true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();


                       if (googleApiClient.isConnected()) {
                            Auth.GoogleSignInApi.signOut(googleApiClient);
                            googleApiClient.disconnect();
                            googleApiClient.connect();
                            Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();
                        }
                      /* LoginManager.getInstance().logOut();
                         Intent intent1 = new Intent(Main2Activity.this, MainActivity.class);
                            startActivity(intent1);
                            Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();
                            finish();
                        LoginManager.getInstance().logOut();

                        AccessToken.setCurrentAccessToken(null);
                        Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();*/

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
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

  /*  private void signOut() {
        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient);
            googleApiClient.disconnect();
            googleApiClient.connect();
        }*/
    }


