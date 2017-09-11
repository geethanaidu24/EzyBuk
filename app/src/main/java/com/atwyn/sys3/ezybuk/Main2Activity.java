package com.atwyn.sys3.ezybuk;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.rom4ek.arcnavigationview.ArcNavigationView;


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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.android.volley.Request.Method.*;
import static com.facebook.internal.FacebookDialogFragment.TAG;

public class Main2Activity extends TabActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabHost.OnTabChangeListener {
    // private ViewPager viewPager;
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private String[] pageTitle = {"Now Showing", "Coming Soon "};
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private boolean loggedIn = false;
    private boolean gmail ;
    GoogleApiClient googleApiClient;
    GoogleApiClient mGoogleApiClient;
    private static final Integer[] IMAGES = {R.drawable.movposttwo, R.drawable.backezybuk, R.drawable.backezybuktwo};
    String navigationusername,navigationusername1;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    TabHost tabHost;
    protected Menu menu;
    TextView navname;
    TextView navemail;
    Intent data = null;
    String name1, mobile1, facebookname,GmailName,GmailEmail;
    private static String KEY_SUCCESS = "success";
    int valoreOnPostExecute = 0;
    final ArrayList<MySQLDataBase> mySQLDataBases4 = new ArrayList<>();
    final ArrayList userdata = new ArrayList<>();
    private ArrayAdapter<MySQLDataBase> adapter;
    String Gpersonname,Gpersonemail,Fbname,Fbemail,Ezname,Ezemail,Ezmobile,Loginemail,Loginname,Loginmobileno,typeLogin;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    OptionalPendingResult<GoogleSignInResult> opr;
    RequestQueue rq;
    List<SliderUtils> sliderImg;

    String request_url = Config.ScrollingImages;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main2);
      /*  Intent intent11 = this.getIntent(); // get Intent which we set from Previous Activity

        Gpersonname = intent11.getExtras().getString("Gmail_Name");
        Gpersonemail = intent11.getExtras().getString("Gmail_Email");

        Fbname = intent11.getExtras().getString("FB_Name");
        Fbemail = intent11.getExtras().getString("FB_Email");

        Ezname = intent11.getExtras().getString("EzyBuk_name");
        Ezemail = intent11.getExtras().getString("EzyBuk_Email");
        Ezmobile = intent11.getExtras().getString("EzyBuk_mobile");

        Loginemail = intent11.getExtras().getString("Login_Email");
        Loginname = intent11.getExtras().getString("Login_Name");
        Loginmobileno = intent11.getExtras().getString("Login_MobileNo");
        typeLogin = intent11.getExtras().getString("Type_Login");*/
        rq = Volley.newRequestQueue(this);
        sliderImg = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.sliderdots);

        sendRequest();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactivedot));

                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);


        // viewPager = (ViewPager) findViewById(R.id.view_pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.search));

        toolbar.inflateMenu(R.menu.main2);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                if (arg0.getItemId() == R.id.h1) {
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
        //init();
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

// more stuff here...


        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ArcNavigationView navigationView = (ArcNavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        navname = (TextView) header.findViewById(R.id.uname);
        navemail = (TextView) header.findViewById(R.id.uemail);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(typeLogin, "EzyBuk_Login")) {
                navigationView.getMenu().clear();
                navemail.setText(Loginemail);
                BackTask b1t = new BackTask();
                b1t.execute();

                navigationView.inflateMenu(R.menu.menu_logout);

            } else if (Objects.equals(typeLogin, "EzyBuk_Registration")) {
                navigationView.getMenu().clear();
                navemail.setText(Ezemail);
                navname.setText("Hello" + " " + Ezname);


                navigationView.inflateMenu(R.menu.menu_logout);

            } else if (Objects.equals(typeLogin, "Facebook")) {
                navigationView.getMenu().clear();


                navname.setText("Hello" + " " + Fbname);
                navemail.setText(Fbemail);
                navigationView.inflateMenu(R.menu.menu_logout);

            } else {
                navigationView.getMenu().clear();


                navname.setText("Hello" + " " + Gpersonname);
                navemail.setText(Gpersonemail);
                navigationView.inflateMenu(R.menu.menu_logout);

            }*/


    navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

      /* SharedPreferences sharedPreferences1 = getSharedPreferences(Config.SHARED_PREF_GMAIL, Context.MODE_PRIVATE);
        gmail = sharedPreferences1.getBoolean(Config.LOGGEDIN_SHARED_GMAIL, false);
Log.d("Gmail boolean", String.valueOf(gmail));
*/

      opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      Islogin = prefs.getBoolean("Islogin", false); // get value of last login status
        Log.d("Gmail boolean", String.valueOf(Islogin));*/
        Profile profile = Profile.getCurrentProfile();

    //    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


        if (loggedIn) {
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
        } else if (profile != null) {
            navigationView.getMenu().clear();

            facebookname = profile.getName();

            navname.setText("Hello" + " " + facebookname);
            navigationView.inflateMenu(R.menu.menu_logout);

        } else if (opr.isDone()) {

            navigationView.getMenu().clear();
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

            //  navemail.setText(profile.getId());
            //Log.d("shreyemail", profile.getId());

           /* GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();

            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            navname.setText("Hello" + " " + personName);*/

           // sharedPreferences1 = getSharedPreferences(Config.SHARED_PREF_GMAIL, MODE_PRIVATE);

           /* navigationusername1 = sharedPreferences1.getString("LoginEmailId1", "UNKNOWN");
            Log.d("hhhh khush", navigationusername1);
            navname.setText("Hello" + " " + navigationusername1);*/
            navigationView.inflateMenu(R.menu.menu_logout);

        } else {
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


            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#99FF99"));

                TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                tv.setTextColor(Color.parseColor("#ffffff"));
            }
            tabHost.getTabWidget().setCurrentTab(1);
            tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#CCFFCC"));
            TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
            tv.setTextColor(Color.parseColor("#000000"));
        }



    public void sendRequest() {
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest( request_url,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i=0;i<response.length();i++)
                {
                    SliderUtils sliderUtils=new SliderUtils();
                    try {
                        JSONObject jsonObject=response.getJSONObject(i);

                        sliderUtils.setSliderImageUrl(Config.mainUrlAddress+jsonObject.getString("Posterurl"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sliderImg.add(sliderUtils);
                }

                viewPagerAdapter=new ViewPagerAdapter(sliderImg,Main2Activity.this);
                viewPager.setAdapter(viewPagerAdapter);

                dotscount=viewPagerAdapter.getCount();
                dots=new ImageView[dotscount];

                for(int i=0;i<dotscount;i++)
                {
                    dots[i]=new ImageView(Main2Activity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactivedot));

                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8,0,8,0);
                    sliderDotspanel.addView(dots[i],params);

                }
                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.activedot));

            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(jsonArrayRequest);
    }



    public class MyTimerTask extends TimerTask

    {
        @Override
        public void run() {
            Main2Activity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });


        }


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
    private class BackTask extends AsyncTask<Object, Object, String> {


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

                    name1 = jo.getString("name");
                    mobile1=jo.getString("mobile");

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
            return result;
        }
        protected void onPostExecute(String result) {

     /*  // dialog.dismiss();
            String lol1= result.get(0).toString();
        //    String lol2= result.get(1).toString();
Log.d("lllllflkld", String.valueOf(result));*/
 navname.setText("Hello" + " " +name1);


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


   /* private void init() {


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
*/



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fr1) {
          Intent in=new Intent(Main2Activity.this,NowShowing_movies.class);
            startActivity(in);
        } else if (id == R.id.fr2) {
            Intent in=new Intent(Main2Activity.this,UpComing_movies.class);
            startActivity(in);

        }else if (id == R.id.fr3) {
            Intent in=new Intent(Main2Activity.this,LatestVideos.class);
            startActivity(in);

        }

        else if(id == R.id.logout)
        {
           logout();

        }
        else if(id == R.id.login)
        {
            Intent in=new Intent(Main2Activity.this,LoginMain.class);
            in.putExtra("Type","Main_Login");
            startActivity(in);


        }
     if (id == R.id.profile) {
            Intent intent = new Intent(this, MyProfile.class);
     intent.putExtra("UseName",  name1);
         intent.putExtra("UseEmail", navigationusername);
         intent.putExtra("UserMobileNo", mobile1);
         intent.putExtra("Facebook_Name",facebookname);
         intent.putExtra("Gmail_Name",GmailName);
         intent.putExtra("Gmail_Email",GmailEmail);
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
                            /*Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                            Toast.makeText(getApplicationContext(), "Successfully Logout", Toast.LENGTH_SHORT).show();
*/

                      //  Auth.GoogleSignInApi.signOut(mGoogleApiClient);
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
            GmailName=acct.getDisplayName();
            GmailEmail=acct.getEmail();
            navname.setText("Hello" + " " + acct.getDisplayName());
            navemail.setText(acct.getEmail());


        }

    }


}


