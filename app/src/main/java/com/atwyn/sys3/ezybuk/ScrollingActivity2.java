package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.atwyn.sys3.ezybuk.R.drawable.backfinalfour;
import static com.atwyn.sys3.ezybuk.R.id.share;

public class ScrollingActivity2 extends AppCompatActivity {
    Button book;
    ImageView mposter2;
    TextView mtitle2, mgenre2, mlanguage2, mformat2, msynopsis2, mdurationdate2;

    private MediaController mediacontroller;
    private Uri uri;

    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    //final static String moviesUrlAddress = Config.moviesUrlAddress;
    String finalurl1;
    String movietitle, movieposter, movielanguage, movieformat, moviegenre, moviesynopsis, movieduration, movievideourl, moviebigposter;
    int movieid;
    Object mreleasingdate;

    ImageView like, dislike, like1, dislike1;
    TextView liketext, disliketext, liketext1, disliketext1;
    int count = 0;
    int click = 0;
    RelativeLayout rlike, rlike1, rdislike, rdislike1;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView recyclerView, recyclerView1;
    private MovieAdapter mAdapter;
    private MovieAdapter1 mAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scrolling2);
        Intent i = this.getIntent(); // get Intent which we set from Previous Activity
        movieid = i.getExtras().getInt("MOVIE_ID");
        mreleasingdate = i.getExtras().get("Movie_ReleasingDate");
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

        mtitle2 = (TextView) findViewById(R.id.textView62);
        mgenre2 = (TextView) findViewById(R.id.textView102);
        mlanguage2 = (TextView) findViewById(R.id.textView82);
        mformat2 = (TextView) findViewById(R.id.textView332);
        msynopsis2 = (TextView) findViewById(R.id.textView172);
        mposter2 = (ImageView) findViewById(R.id.mp2);
        mdurationdate2 = (TextView) findViewById(R.id.textView92);
        TextView tooltex = (TextView) findViewById(R.id.tooltext1);
        ImageView imbigposter = (ImageView) findViewById(R.id.imageView12);

        tooltex.setText(movietitle);
        mtitle2.setText(movietitle);
        mgenre2.setText(moviegenre);
        mlanguage2.setText(movielanguage);
        mformat2.setText(movieformat);
        msynopsis2.setText(moviesynopsis);
        mdurationdate2.setText(movieduration + "  |  " + mreleasingdate);


        com.bumptech.glide.Glide.with(this)
                .load(movieposter)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

                .crossFade()
                .into(mposter2);

        com.bumptech.glide.Glide.with(this)
                .load(finalurl1)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

                .crossFade()
                .into(imbigposter);
        //new MoviesInfoDownloader(ScrollingActivity.this, moviesUrlAddress).execute();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScrollingActivity2.this, FullTrailer.class);
                in.putExtra("Movie_VideoUrl", movievideourl);
                startActivity(in);
            }
        });
        rlike = (RelativeLayout) findViewById(R.id.bookedImageLayout);
        rlike1 = (RelativeLayout) findViewById(R.id.bookedImageLayout2);

        rdislike = (RelativeLayout) findViewById(R.id.selectedImageLayout);
        rdislike1 = (RelativeLayout) findViewById(R.id.selectedImageLayout1);

        like = (ImageView) findViewById(R.id.bookedImage2);
        liketext = (TextView) findViewById(R.id.bookedText2);
        dislike = (ImageView) findViewById(R.id.bookedImage12);
        disliketext = (TextView) findViewById(R.id.bookedText12);

        like1 = (ImageView) findViewById(R.id.bookedImage22);
        liketext1 = (TextView) findViewById(R.id.bookedText22);
        dislike1 = (ImageView) findViewById(R.id.bookedImage121);
        disliketext1 = (TextView) findViewById(R.id.bookedText121);


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = click + 1;
                if (click == 1) {
                    click = 0;

                    rlike.setVisibility(View.GONE);
                    rlike1.setVisibility(View.VISIBLE);
                    like1.setImageResource(R.drawable.likewhite);

                    count++;
                    dislike.setEnabled(false);
                    disliketext.setEnabled(false);
                    // liketext.setText("" + count);
                }
                liketext1.setText("" + count);
                liketext1.setTextColor(Color.WHITE);
                Toast.makeText(ScrollingActivity2.this, "Submmitted", Toast.LENGTH_LONG);


            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = click + 1;
                if (click == 1) {
                    click = 0;

                    rdislike.setVisibility(View.GONE);
                    rdislike1.setVisibility(View.VISIBLE);
                    dislike1.setImageResource(R.drawable.whitedislike);

                    count--;
                    like.setEnabled(false);
                    liketext.setEnabled(false);
                    like1.setEnabled(false);
                    liketext1.setEnabled(false);
                    // liketext.setText("" + count);
                }
                disliketext1.setText("" + count);
                disliketext1.setTextColor(Color.WHITE);
                Toast.makeText(ScrollingActivity2.this, "Submmitted", Toast.LENGTH_LONG);


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


                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "hi");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, movietitle + "(" + movielanguage + ")" + "\n \n" + moviesynopsis + "\n \n" + "All that you would like to explore and know about movie" + movietitle + "\n" + "https://www.youtube.com/watch?v=YheC-4Qgoro&t=3s");

                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ScrollingActivity2.this);
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

                    mySQLDataBase.CastName = moviesObject.getString("CastName");
                    mySQLDataBase.CastRole = moviesObject.getString("CastRole");
                    mySQLDataBase.CastImgUrl = moviesObject.getString("CastImgUrl");
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
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    mAdapter = new MovieAdapter(ScrollingActivity2.this, data);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ScrollingActivity2.this, LinearLayoutManager.HORIZONTAL, true));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class AsyncFetch1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ScrollingActivity2.this);
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

                    mySQLDataBase.CrewName = moviesObject.getString("CrewName");
                    mySQLDataBase.CrewRole = moviesObject.getString("CrewRole");
                    mySQLDataBase.CrewImgUrl = moviesObject.getString("CrewImgUrl");
                    data1.add(mySQLDataBase);


                    recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view12);
                    mAdapter1 = new MovieAdapter1(ScrollingActivity2.this, data1);
                    recyclerView1.setAdapter(mAdapter1);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(ScrollingActivity2.this, LinearLayoutManager.HORIZONTAL, true));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}