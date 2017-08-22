package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.atwyn.sys3.ezybuk.R.id.selectedImage;
import static com.atwyn.sys3.ezybuk.R.id.share;

public class ScrollingActivity extends AppCompatActivity {
    Button book;
    ImageView mposter;
    TextView mtitle, mgenre, mlanguage, mformat, msynopsis, mdurationdate;

    final ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();
    final ArrayList<MySQLDataBase> mySQLDataBases1 = new ArrayList<>();
    final ArrayList<MySQLDataBase> mySQLDataBases2 = new ArrayList<>();
    private Spinner spTheater,spdate,spTime;
    int click=0;
    private ArrayAdapter<MySQLDataBase> adapter ;
    private ArrayAdapter<MySQLDataBase> adapter1 ;
    private ArrayAdapter<MySQLDataBase> adapter2 ;
    private static final String Spinnertheater=Config.TheaterNameUrlAddress;
    private static final String SpinnerDate=Config.DateNameUrlAddress;
    private static final String SpinnerTime=Config.TimeNameUrlAddress;

  int tid;
    int tid1;
  String  date;
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
  //  final static String moviesUrlAddress = Config.moviesUrlAddress;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView recyclerView,recyclerView1;
    private MovieAdapter mAdapter;
    private MovieAdapter1 mAdapter1;
    String ActDate1;
  String theaterid;
    BackTask1 bt1;
   BackTask2 bt2;
    String finalurl1;
    String movietitle, movieposter, movielanguage, movieformat, moviegenre, moviesynopsis, movieduration, movievideourl, moviebigposter;
    int movieid;
    String castname, castrole, castimgurl,time;
    String mreleasingdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scrolling);

        Intent i = this.getIntent(); // get Intent which we set from Previous Activity
        movieid = i.getExtras().getInt("MOVIE_ID");
        mreleasingdate = String.valueOf(i.getExtras().get("Movie_ReleasingDate"));
        movieposter = i.getExtras().getString("Movie_poster");
        movietitle = i.getExtras().getString("Movie_Title");

        moviebigposter = i.getExtras().getString("Movie_BigPosterurl");
        movielanguage = i.getExtras().getString("Movie_Language");
        movieformat = i.getExtras().getString("Movie_Format");
        moviegenre = i.getExtras().getString("Movie_Genre");
        moviesynopsis = i.getExtras().getString("Movie_Synopsis");
        movieduration = i.getExtras().getString("Movie_Duration");
        movievideourl = i.getExtras().getString("Movie_VideoUrl");

        finalurl1 = Config.mainUrlAddress + moviebigposter;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//this line shows back button
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.seat);
        toolbar.setOverflowIcon(drawable);
        //   CollapsingToolbarLayout c=((CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)).setTitle("");
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(" ");
        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.red));


        //Make call to AsyncTask
        new AsyncFetch().execute();
        new AsyncFetch1().execute();
        //final ListView listView = (ListView) findViewById(R.id.listcastview);
        //  new CastDownloader(ScrollingActivity.this, moviesUrlAddress, listView).execute();


        mtitle = (TextView) findViewById(R.id.textView6);
        mgenre = (TextView) findViewById(R.id.textView10);
        mlanguage = (TextView) findViewById(R.id.textView8);
        mformat = (TextView) findViewById(R.id.textView33);
        msynopsis = (TextView) findViewById(R.id.textView17);
        mposter = (ImageView) findViewById(R.id.mp);
        mdurationdate = (TextView) findViewById(R.id.textView9);
        TextView tooltex = (TextView) findViewById(R.id.tooltext);
        ImageView imbigposter = (ImageView) findViewById(R.id.imageView);

       // spTheater=(Spinner)findViewById(R.id.spinner) ;


        tooltex.setText(movietitle);
        mtitle.setText(movietitle);
        mgenre.setText(moviegenre);
        mlanguage.setText(movielanguage);
        mformat.setText(movieformat);
        msynopsis.setText(moviesynopsis);
        String DateFromDb = mreleasingdate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//set format of date you receiving from db
        Date date = null;
        try {
            date = sdf.parse(DateFromDb);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDate = new SimpleDateFormat("MMM dd, yyyy");//set format of new date
        System.out.println(newDate.format(date));
        String ActDate = newDate.format(date);// here is your new date !
        Log.d("DATE",ActDate);
        mdurationdate.setText(movieduration + "  |  " + ActDate);
        book = (Button) findViewById(R.id.button);

        com.bumptech.glide.Glide.with(this)
                .load(movieposter)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

                .crossFade()
                .into(mposter);

        com.bumptech.glide.Glide.with(this)
                .load(finalurl1)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

                .crossFade()
                .into(imbigposter);
 this.initializeViews();


 /*book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(ScrollingActivity.this, SeatReservation.class);
                startActivity(in);
            }
        });*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScrollingActivity.this, FullTrailer.class);
                in.putExtra("Movie_VideoUrl", movievideourl);
                startActivity(in);
            }
        });

        ImageView im = (ImageView) findViewById(R.id.selectedImage);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ScrollingActivity.this, Rating.class);
                in.putExtra("Movie_poster", movieposter);
                in.putExtra("Movie_BigPosterurl", moviebigposter);
                startActivity(in);
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share, menu);

        return true;


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                // sharingIntent.setType("image/*");

                // Make sure you put example png image named myImage.png in your
                // directory
              /*  File f=new File(finalurl1);
                Uri uri = Uri.parse(f.getAbsolutePath());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setType("image*//*");
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
               startActivity(Intent.createChooser(share, "Share image File"));
*/
                //String shareBodyText = "Check it out. Your message goes here";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "hi");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, movietitle + "(" + movielanguage + ")" + "\n \n" + moviesynopsis + "\n \n" + "All that you would like to explore and know about movie" + movietitle + "\n" + "https://www.youtube.com/watch?v=YheC-4Qgoro&t=3s");

                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
   private void initializeViews()
    {


        spTheater=(Spinner)findViewById(R.id.spinner) ;
        spdate=(Spinner)findViewById(R.id.spinner2);
        // spdate.setPrompt("Select Date");
        spTime=(Spinner)findViewById(R.id.spinner3);
        //spdate.setPrompt("Select Time");

        book = (Button) findViewById(R.id.button);


    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ScrollingActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            // Enter URL address where your json file resides
            // Even you can make call to php file which returns json data
            try {
                url = new URL(Config.castUrlAddress + movieid);
                Log.d("second url", " >" + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            ArrayList<MySQLDataBase> mySQLDataBases = null;
            pdLoading.dismiss();
            List<MySQLDataBase> data = new ArrayList<>();

            pdLoading.dismiss();
            try {

               /* JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
                    fishData.fishImage= json_data.getString("fish_img");
                    fishData.fishName= json_data.getString("fish_name");
                    fishData.catName= json_data.getString("cat_name");
                    fishData.sizeName= json_data.getString("size_name");
                    fishData.price= json_data.getInt("price");
                    data.add(fishData);*/
                JSONArray moviesArray = new JSONArray(result);


                for (int i = 0; i < moviesArray.length(); i++) {
             JSONObject moviesObject = moviesArray.getJSONObject(i);

                    Log.d("result movies response: ", "> " + moviesObject);

                    // castobject=moviesObject.getJSONObject("moviecast");
                MySQLDataBase mySQLDataBase = new MySQLDataBase();
                 mySQLDataBase.MovieId = moviesObject.getInt("MovieId");

                       mySQLDataBase.CastName  = moviesObject.getString("CastName");
                      mySQLDataBase.CastRole  = moviesObject.getString("CastRole");
                     mySQLDataBase.CastImgUrl    = moviesObject.getString("CastImgUrl");
                         data.add(mySQLDataBase);

                       /* mySQLDataBase.setCastName(castname);
                        mySQLDataBase.setCastRole(castrole);
                        mySQLDataBase.setCastImgUrl(castimgurl);*/





                 /*   mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setMovieId(movieId);
                   *//* mySQLDataBase.setTitle(movietitle);
                    mySQLDataBase.setPosterUrl(moviesposterUrl);
                    mySQLDataBase.setBigPosterUrl(moviesbigposterUrl);
                    mySQLDataBase.setGenre(moviesgenre);
                    mySQLDataBase.setMLanguage(movieslanguage);
                    mySQLDataBase.setFormat(moviesformat);
                    mySQLDataBase.setSynopsis(moviessynopsis);
                    mySQLDataBase.setDuration_min(moviesduration);
                    mySQLDataBase.setVideourl(moviesvideourl);
                    mySQLDataBase.setReleasing_Date(moviereleasingdate);*//*

                    // Setup and Handover data to recyclerview*/
                    recyclerView = (RecyclerView) findViewById(R.id.listcastview);
                    mAdapter = new MovieAdapter(ScrollingActivity.this, data);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ScrollingActivity.this,LinearLayoutManager.HORIZONTAL, true));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class AsyncFetch1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ScrollingActivity.this);
        HttpURLConnection conn;
        URL url1 = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            // Enter URL address where your json file resides
            // Even you can make call to php file which returns json data
            try {
                url1 = new URL(Config.crewUrlAddress + movieid);
                Log.d("third url", " >" + url1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url1.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            ArrayList<MySQLDataBase> mySQLDataBases = null;
            pdLoading.dismiss();
            List<MySQLDataBase> data1 = new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray moviesArray = new JSONArray(result);


                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject moviesObject = moviesArray.getJSONObject(i);

                    Log.d("result movies response: ", "> " + moviesObject);

                    // castobject=moviesObject.getJSONObject("moviecast");
                    MySQLDataBase mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.MovieId = moviesObject.getInt("MovieId");

                    mySQLDataBase.CrewName  = moviesObject.getString("CrewName");
                    mySQLDataBase.CrewRole  = moviesObject.getString("CrewRole");
                    mySQLDataBase.CrewImgUrl    = moviesObject.getString("CrewImgUrl");
                    data1.add(mySQLDataBase);


                    recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
                    mAdapter1 = new MovieAdapter1(ScrollingActivity.this, data1);
                    recyclerView1.setAdapter(mAdapter1);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(ScrollingActivity.this,LinearLayoutManager.HORIZONTAL, true));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void handleClickEvents(final int movieid, final String date, final String time)
    {
        //EVENTS : ADD
        click = click + 1;
        if (click == 1) {
            click = 0;
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (spTheater.getSelectedItem().equals("Select Theater")) {

                        Toast.makeText(ScrollingActivity.this,
                                "Please Select Theater",
                                Toast.LENGTH_SHORT).show();
                        spdate.setEnabled(false);
                        spTime.setEnabled(false);
                        spdate.setClickable(false);

                        spTime.setClickable(false);}
//                 else if(spdate.getSelectedItem().equals("Select Date")) {
//                        Toast.makeText(ScrollingActivity.this,
//                                "Please Select Theater",
//                                Toast.LENGTH_SHORT).show();
//
//                    }else if(spTime.getSelectedItem().equals("Select Time")) {
//                        Toast.makeText(ScrollingActivity.this,
//                                "Please Select Date First",
//                                Toast.LENGTH_SHORT).show();
//                    }

                  else{


                        Intent in = new Intent(ScrollingActivity.this, SeatReservation.class);
                        in.putExtra("Movie_Id", movieid);
                        in.putExtra("Show_Date", date);
                        in.putExtra("Show_Time", time);

                        startActivity(in);

                    }
                }


            });
        }

    }
    public void onStart() {
        super.onStart();
        BackTask bt = new BackTask();

        bt.execute();

        bt1 = new BackTask1();
        bt1.execute();
        bt2 = new BackTask2();
        bt2.execute();

    }


    private class BackTask extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.TheaterNameUrlAddress + movieid);
                String h1 = Config.TheaterNameUrlAddress + movieid;
                Log.d("hello url", " >" + h1);
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                org.apache.http.HttpEntity entity = response.getEntity();
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
            try {


                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;
                mySQLDataBases.clear();
                MySQLDataBase mySQLDataBase;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    // add interviewee name to arraylist
                    int tid = jo.getInt("CinemaId");
                    String tname = jo.getString("Name");
                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setTheaterId(tid);
                    mySQLDataBase.setTheaterName(tname);
                    mySQLDataBases.add(mySQLDataBase);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            // productcrafts.addAll(productcrafts);
            final ArrayList<String> listItems1 = new ArrayList<>();
            listItems1.add("Select Theater");
            for (int i = 0; i < mySQLDataBases.size(); i++) {
                listItems1.add(mySQLDataBases.get(i).getTheaterName());


            }
        /*    HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(listItems);
            listItems.clear();
            listItems.addAll(hashSet);*/

            adapter = new ArrayAdapter(ScrollingActivity.this, R.layout.spinner_layout, R.id.txt, listItems1);
            spTheater.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            spTheater.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                public void onItemSelected(AdapterView<?> arg0, View selectedItemView,
                                           int position, long id) {

                    if (spTheater.getSelectedItem().equals("Select Theater")) {
/*
                        Toast.makeText(DeleteProducts.this,
                                "Your Selected : Nothing",
                                Toast.LENGTH_SHORT).show();*/
                    } else {

                        MySQLDataBase mySQLDataBase = mySQLDataBases.get(position - 1);

                        //  final int pid
                        tid = mySQLDataBase.getTheaterId();
                        Log.d("selected id", "" + tid);


                        theaterid= spTheater.getSelectedItem().toString();
                     spdate.setEnabled(true);
                     spTime.setEnabled(false);
                        spdate.setClickable(true);
                        spTime.setClickable(false);
                        SpinnerDate(tid,movieid);
              // handleClickEvents(tid);
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        };

                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(ScrollingActivity.this,
                            "Your Selected : Nothing",
                            Toast.LENGTH_SHORT).show();
                }

            });


        }

        }
    private void SpinnerDate(int theaterid, int movieid) {
        bt1 = new BackTask1();
        bt1.execute();

        MySQLDataBase s = new MySQLDataBase();
        s.setMovieId(movieid);
        if (s == null) {
            Toast toast = Toast.makeText(ScrollingActivity.this, "Please Select Theater Name", Toast.LENGTH_SHORT);
            toast.show();
            //Toast.makeText(DeleteProducts.this, "No Data To Delete", Toast.LENGTH_SHORT).show();
            AndroidNetworking.post(SpinnerDate)
                    .addBodyParameter("CinemaId", String.valueOf(theaterid))
                    .addBodyParameter("MovieId", String.valueOf(s.getMovieId()))
                    .setTag("TAG_ADD")
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response != null) {
                                //SHOW RESPONSE FROM SERVER
                                // String responseString = response.get(0).toString();
                                // Toast.makeText(ScrollingActivity.this, responseString, Toast.LENGTH_SHORT).show();


                            }
                        }

                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ScrollingActivity.this, "UNSUCCESSFUL :  ERROR IS : " + anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



        private class BackTask1 extends AsyncTask<Void, Void, Void> {


            protected void onPreExecute() {
                super.onPreExecute();

            }

            protected Void doInBackground(Void... params) {
                InputStream is = null;
                String result = "";
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(Config.DateNameUrlAddress + tid + '/' + movieid);
                    String h2 = Config.DateNameUrlAddress + tid + '/' + movieid;
                    Log.d("hi url", " >" + h2);
                    org.apache.http.HttpResponse response = httpclient.execute(httppost);
                    org.apache.http.HttpEntity entity = response.getEntity();
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
                try {


                    JSONArray ja = new JSONArray(result);
                    JSONObject jo = null;
                    mySQLDataBases1.clear();
                    MySQLDataBase mySQLDataBase;
                    for (int i = 0; i < ja.length(); i++) {
                        jo = ja.getJSONObject(i);
                        // add interviewee name to arraylist
                        int tid = jo.getInt("CinemaId");
                        String date = jo.getString("ShowDate");
                        mySQLDataBase = new MySQLDataBase();
                        mySQLDataBase.setTheaterId(tid);
                        mySQLDataBase.setShowDate(date);
                        mySQLDataBases1.add(mySQLDataBase);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {

                // productcrafts.addAll(productcrafts);
                final ArrayList<String> listItems2 = new ArrayList<>();
                listItems2.add("Select Date");
                for (int i = 0; i < mySQLDataBases1.size(); i++) {
                  //  listItems2.add(mySQLDataBases1.get(i).getShowDate());
                    String DateFromDb = mySQLDataBases1.get(i).getShowDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//set format of date you receiving from db
                    Date date = null;
                    try {
                        date = sdf.parse(DateFromDb);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat newDate = new SimpleDateFormat("EEEE, MMM d,yyyy");//set format of new date EEEE-wed

                    String input = "EEEE MMM d,yyyy";
                    String[] out = input.split(",");

                Log.d("Month = " , out[0]);
                  Log.d("Day = " , out[1]);

                    ActDate1 = newDate.format(date);// here is your new date !
                    Log.d("DATEewretr",ActDate1);
                    listItems2.add(ActDate1);
                }
             /*   HashSet<String> hashSet=new HashSet<String>();
                hashSet.addAll(listItems);
                listItems.clear();
                listItems.addAll(hashSet);*/

                adapter1 = new ArrayAdapter(ScrollingActivity.this, R.layout.spinner_layout, R.id.txt, listItems2);
                spdate.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
                spdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    public void onItemSelected(AdapterView<?> arg0, View selectedItemView,
                                               int position, long id) {

                        if (spdate.getSelectedItem().equals("Select Date")) {

                      /*  Toast.makeText(ScrollingActivity.this,
                                "Please Select Date",
                                Toast.LENGTH_SHORT).show();*/
                        } else {

                            MySQLDataBase mySQLDataBase = mySQLDataBases1.get(position - 1);

                            //  final int pid
                            tid1 = mySQLDataBase.getTheaterId();
                            Log.d("selected id", "" + tid1);
                            date = spdate.getSelectedItem().toString();
                            Log.d("hewhrhf",date);
                        spTime.setEnabled(true);
                            spTime.setClickable(true);


                          String DateFromDb = date;
                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM d,yyyy");//set format of date you receiving from db
                            Date date = null;
                            try {
                                date = sdf.parse(DateFromDb);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");//set format of new date

                          ActDate1 = newDate.format(date);// here is your new date !
                            Log.d("DATEewretr",ActDate1);
                            SpinnerTime(ActDate1, tid1, movieid);

                            //  handleClickEvents(tid);
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss();
                                }
                            };

                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ScrollingActivity.this,
                                "Your Selected : Nothing",
                                Toast.LENGTH_SHORT).show();
                    }

                });


            }
        }

            private void SpinnerTime(String date1, int cineid, int movieid) {

                bt2 = new BackTask2();
                bt2.execute();

                MySQLDataBase s = new MySQLDataBase();
                s.setMovieId(movieid);
                if (s == null) {
                    Toast toast = Toast.makeText(ScrollingActivity.this, "Please Select Theater Name", Toast.LENGTH_SHORT);
                    toast.show();
                    //Toast.makeText(DeleteProducts.this, "No Data To Delete", Toast.LENGTH_SHORT).show();
                    AndroidNetworking.post(SpinnerTime)
                            .addBodyParameter("ShowDate",date1)
                            .addBodyParameter("CinemaId", String.valueOf(cineid))
                            .addBodyParameter("MovieId", String.valueOf(s.getMovieId()))
                            .setTag("TAG_ADD")
                            .build()
                            .getAsJSONArray(new JSONArrayRequestListener() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    if (response != null) {
                                        //SHOW RESPONSE FROM SERVER
                                        // String responseString = response.get(0).toString();
                                        // Toast.makeText(ScrollingActivity.this, responseString, Toast.LENGTH_SHORT).show();


                                    }
                                }

                                //ERROR
                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(ScrollingActivity.this, "UNSUCCESSFUL :  ERROR IS : " + anError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }



            private class BackTask2 extends AsyncTask<Void, Void, Void> {


                protected void onPreExecute() {
                    super.onPreExecute();

                }

                protected Void doInBackground(Void... params) {
                    InputStream is = null;
                    String result = "";
                    try {

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.TimeNameUrlAddress + ActDate1 + '/' + tid1 + '/' + movieid);
                        String h3 = Config.TimeNameUrlAddress + ActDate1+ '/' + tid1 + '/' + movieid;
                        Log.d("hjkchjsdhvj url", " >" + h3);
                        org.apache.http.HttpResponse response = httpclient.execute(httppost);
                        org.apache.http.HttpEntity entity = response.getEntity();
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
                    try {


                        JSONArray ja = new JSONArray(result);
                        JSONObject jo = null;
                        mySQLDataBases2.clear();
                        MySQLDataBase mySQLDataBase;
                        for (int i = 0; i < ja.length(); i++) {
                            jo = ja.getJSONObject(i);
                            // add interviewee name to arraylist
                            int tid1 = jo.getInt("CinemaId");
                            String time = jo.getString("ShowTime");
                            mySQLDataBase = new MySQLDataBase();
                            mySQLDataBase.setTheaterId(tid1);
                            mySQLDataBase.setShowTime(time);
                            mySQLDataBases2.add(mySQLDataBase);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                protected void onPostExecute(Void result) {

                    // productcrafts.addAll(productcrafts);
                    final ArrayList<String> listItems3 = new ArrayList<>();
                    listItems3.add("Select Time");
                    for (int i = 0; i < mySQLDataBases2.size(); i++) {
                        listItems3.add(mySQLDataBases2.get(i).getShowTime());


                    }
             /*   HashSet<String> hashSet=new HashSet<String>();
                hashSet.addAll(listItems);
                listItems.clear();
                listItems.addAll(hashSet);*/

                    adapter2 = new ArrayAdapter(ScrollingActivity.this, R.layout.spinner_layout, R.id.txt, listItems3);
                    spTime.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
                    spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                        public void onItemSelected(AdapterView<?> arg0, View selectedItemView,
                                                   int position, long id) {

                            if (spTime.getSelectedItem().equals("Select Time")) {

                      /*  Toast.makeText(ScrollingActivity.this,
                                "Please Select Time",
                                Toast.LENGTH_SHORT).show();*/
                            } else {

                                MySQLDataBase mySQLDataBase = mySQLDataBases2.get(position - 1);

                                //  final int pid
                                final int tid1 = mySQLDataBase.getTheaterId();
                                Log.d("selected id", "" + tid1);
                                time = spTime.getSelectedItem().toString();

                      handleClickEvents(tid1,date,time);
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                };

                            }
                        }

                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                            Toast.makeText(ScrollingActivity.this,
                                    "Your Selected : Nothing",
                                    Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            }
        }





