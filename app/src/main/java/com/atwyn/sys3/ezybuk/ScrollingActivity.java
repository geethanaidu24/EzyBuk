package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

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
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.atwyn.sys3.ezybuk.R.id.selectedImage;
import static com.atwyn.sys3.ezybuk.R.id.share;

public class ScrollingActivity extends AppCompatActivity {
    Button book;
    ImageView mposter;
    TextView mtitle, mgenre, mlanguage, mformat, msynopsis, mdurationdate;
    /* RecyclerView mRecyclerView, mRecyclerView1;
     RecyclerView.LayoutManager mLayoutManager;
     RecyclerView.Adapter mAdapter;

     RecyclerView.LayoutManager mLayoutManager1;
     RecyclerView.Adapter mAdapter1;
     ArrayList<String> alName;
     ArrayList<Integer> alImage;

     ArrayList<String> alName1;
     ArrayList<Integer> alImage1;*/
    private MediaController mediacontroller;
    private Uri uri;

    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    final static String moviesUrlAddress = Config.moviesUrlAddress;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView recyclerView;
    private MovieAdapter mAdapter;


    String finalurl1;
    String movietitle, movieposter, movielanguage, movieformat, moviegenre, moviesynopsis, movieduration, movievideourl, moviebigposter;
    int movieid;
    String castname, castrole, castimgurl;
    Object mreleasingdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scrolling);

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
        //final ListView listView = (ListView) findViewById(R.id.listcastview);
        //  new CastDownloader(ScrollingActivity.this, moviesUrlAddress, listView).execute();

     /*   alName = new ArrayList<>(Arrays.asList("Cheesy...", "Crispy... ", "Fizzy...", "Cool...", "Softy...", "Fruity...", "Fresh...", "Sticky..."));
        alImage = new ArrayList<>(Arrays.asList(R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour));


        alName1 = new ArrayList<>(Arrays.asList("Sharukh...", "Crispy... ", "Fizzy...", "Cool...", "Softy...", "Fruity...", "Fresh...", "Sticky..."));
        alImage1 = new ArrayList<>(Arrays.asList(R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour, R.drawable.backfinalfour));
        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);


        mRecyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
        mRecyclerView1.setHasFixedSize(true);


        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HLVAdapter(ScrollingActivity.this, alName, alImage);
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView1.setLayoutManager(mLayoutManager1);

        mAdapter1 = new HLVAdapter(ScrollingActivity.this, alName1, alImage1);
        mRecyclerView1.setAdapter(mAdapter1);
*/

        mtitle = (TextView) findViewById(R.id.textView6);
        mgenre = (TextView) findViewById(R.id.textView10);
        mlanguage = (TextView) findViewById(R.id.textView8);
        mformat = (TextView) findViewById(R.id.textView33);
        msynopsis = (TextView) findViewById(R.id.textView17);
        mposter = (ImageView) findViewById(R.id.mp);
        mdurationdate = (TextView) findViewById(R.id.textView9);
        TextView tooltex = (TextView) findViewById(R.id.tooltext);
        ImageView imbigposter = (ImageView) findViewById(R.id.imageView);

        tooltex.setText(movietitle);
        mtitle.setText(movietitle);
        mgenre.setText(moviegenre);
        mlanguage.setText(movielanguage);
        mformat.setText(movieformat);
        msynopsis.setText(moviesynopsis);
        mdurationdate.setText(movieduration + "  |  " + mreleasingdate);


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
        //new MoviesInfoDownloader(ScrollingActivity.this, moviesUrlAddress).execute();
        book = (Button) findViewById(R.id.button);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(ScrollingActivity.this, SeatReservation.class);
                startActivity(in);
            }
        });

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

    /*public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);

        // ATTENTION: This was auto-generated to implement the App Indexing API.


        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    private class CastDownloader extends AsyncTask<Void, Void, String> {

        Context c;
        String moviesUrlAddress;
        ListView listView1;


        private CastDownloader(ScrollingActivity c, String moviesUrlAddress, ListView listView) {
            this.c = c;
            this.moviesUrlAddress = moviesUrlAddress;
            this.listView1 = listView;
            Log.d("movies url: ", "> " + moviesUrlAddress);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return downloadMoviesData();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                Toast.makeText(c, "Unsuccessful,Null returned", Toast.LENGTH_SHORT).show();
            } else {
                //CALL DATA PARSER TO PARSE
                CastDataParser parser = new CastDataParser(c, listView1, s);
                parser.execute();
            }
        }

        private String downloadMoviesData() {
            HttpURLConnection con = Connector.connect(moviesUrlAddress);
            if (con == null) {
                return null;
            }
            try {
                InputStream is = new BufferedInputStream(con.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder jsonData = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    jsonData.append(line).append("n");
                }
                br.close();
                is.close();
                return jsonData.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CastDataParser extends AsyncTask<Void, Void, Integer> {
        private final ListView listView1;
        Context c;

        String jsonData;

        ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();

        private CastDataParser(Context c, ListView listView1, String jsonData) {
            this.c = c;
            this.listView1 = listView1;
            this.jsonData = jsonData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return this.parseData();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0) {
                Toast.makeText(c, "No Data, Add New", Toast.LENGTH_SHORT).show();
            } else {

                final CastListAdapter adapter = new CastListAdapter(c, mySQLDataBases);
                listView1.setAdapter(adapter);
            }
        }

        private int parseData() {
            try {
                JSONArray moviesArray = new JSONArray(jsonData);
                JSONObject moviesObject = null;
                //  JSONObject castobject = null;
                mySQLDataBases.clear();
                MySQLDataBase mySQLDataBase = null;
                for (int i = 0; i < moviesArray.length(); i++) {
                    moviesObject = moviesArray.getJSONObject(i);

                    Log.d("result movies response: ", "> " + moviesObject);

                    // castobject=moviesObject.getJSONObject("moviecast");

                    int movieId = moviesObject.getInt("MovieId");
                    String movietitle = moviesObject.getString("Title");

                    String moviesposterUrl = moviesObject.getString("SmallPoster");
                    String moviesbigposterUrl = moviesObject.getString("Posterurl");
                    String moviesgenre = moviesObject.getString("Genre");
                    String movieslanguage = moviesObject.getString("MLanguage");
                    String moviesformat = moviesObject.getString("Format");
                    String moviessynopsis = moviesObject.getString("Synopsis");
                    String moviesduration = moviesObject.getString("Duration_min");
                    String moviesvideourl = moviesObject.getString("Videourl");
                    String moviereleasingdate = moviesObject.getString("Releasing_Date");

                    //JSONObject castmovie=moviesObject.getJSONObject("moviecast");
                    JSONArray castarray = moviesObject.getJSONArray("moviecast");
                    for (int ii = 0; ii < castarray.length(); ii++) {

                        JSONObject c = castarray.getJSONObject(ii);
                        castname = c.getString("CastName");
                        castrole = c.getString("CastRole");
                        castimgurl = c.getString("CastImgUrl");

                        mySQLDataBase = new MySQLDataBase();
                        mySQLDataBase.setCastName(castname);
                        mySQLDataBase.setCastRole(castrole);
                        mySQLDataBase.setCastImgUrl(castimgurl);
                        mySQLDataBases.add(mySQLDataBase);
                    }
                    // String castname=moviesObject.getString("CastName");
                    //String castrole=moviesObject.getString("CastRole");
                    //String castimgurl= moviesObject.getString("CastImgUrl");


                    DateFormat iso8601Format = new SimpleDateFormat("dd-MM-yyyy ");
                    Date date = null;
                    try {
                        date = iso8601Format.parse(moviereleasingdate);
                    } catch (ParseException e) {
                        //Log.d("Parsing ISO8601 datetime failed", "" + e);
                    }


                    int flags = 0;
                    //  flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
                    flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
                    flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
                    //  flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;


                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setMovieId(movieId);
                    mySQLDataBase.setTitle(movietitle);
                    mySQLDataBase.setPosterUrl(moviesposterUrl);
                    mySQLDataBase.setBigPosterUrl(moviesbigposterUrl);
                    mySQLDataBase.setGenre(moviesgenre);
                    mySQLDataBase.setMLanguage(movieslanguage);
                    mySQLDataBase.setFormat(moviesformat);
                    mySQLDataBase.setSynopsis(moviessynopsis);
                    mySQLDataBase.setDuration_min(moviesduration);
                    mySQLDataBase.setVideourl(moviesvideourl);
                    mySQLDataBase.setReleasing_Date(moviereleasingdate);
                *//*   *//**//**//**//* mySQLDataBase.setCastName(castname);
                    mySQLDataBase.setCastRole(castrole);
                    mySQLDataBase.setCastImgUrl(castimgurl);*//**//**//**//**//*
                    mySQLDataBases.add(mySQLDataBase);

                }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    class CastListAdapter extends BaseAdapter {

        Context c;
        ArrayList<MySQLDataBase> mySQLDataBases;
        LayoutInflater inflater;

        private CastListAdapter(Context c, ArrayList<MySQLDataBase> mySQLDataBases) {
            this.c = c;
            this.mySQLDataBases = mySQLDataBases;
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mySQLDataBases.size();
        }

        @Override
        public Object getItem(int position) {
            return mySQLDataBases.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.scrollhorizontal_list, parent, false);
            }

            final TextView movietitle = (TextView) convertView.findViewById(R.id.textViewURL);
            //movietitle.setSelected(true);
            //  CircleImageView movieposter = (CircleImageView) convertView.findViewById(R.id.imageDownloaded);
            ImageView movieposter = (ImageView) convertView.findViewById(R.id.imageDownloaded);
            final TextView movieslanguage = (TextView) convertView.findViewById(R.id.textViewURL1);

            //BIND DATA
            MySQLDataBase mySQLDataBase = (MySQLDataBase) this.getItem(position);
            final String url = mySQLDataBase.getPosterUrl();
            final String moviecastimgurl = mySQLDataBase.getCastImgUrl();
            final String finalUrl = Config.mainUrlAddress + moviecastimgurl;
            final String moviecastname = mySQLDataBase.getCastName();
            final String moviecastrole = mySQLDataBase.getCastRole();


            final int movieId = mySQLDataBase.getMovieId();
            final String movieTitle = mySQLDataBase.getTitle();
            final String movielanguage = mySQLDataBase.getMLanguage();
            final String movieformat = mySQLDataBase.getFormat();
            final String moviegenre = mySQLDataBase.getGenre();
            final String moviesynopsis = mySQLDataBase.getSynopsis();
            final String movieduration = mySQLDataBase.getDuration_min();
            final String movievideourl = mySQLDataBase.getVideourl();
            final String moviereleasingdate = mySQLDataBase.getReleasing_Date();
            final String moviebiposterurl = mySQLDataBase.getBigPosterUrl();


            //IMG
            Glide.downloadImage(c, finalUrl, movieposter);

            movietitle.setText(moviecastrole);
            movieslanguage.setText(moviecastname);
          *//* *//**//**//**//* convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openDetailNewsActivity(movieId,finalUrl,movieTitle,movielanguage,movieformat,moviegenre,moviesynopsis,movieduration,movievideourl,moviereleasingdate,moviebiposterurl);
                }
            });*//**//**//**//*
*//*
            return convertView;
        }
      *//* *//**//**//**//* private void openDetailNewsActivity(int movieId, String...details ){
            Intent i = new Intent(c, ScrollingActivity.class);
            i.putExtra("MOVIE_ID", movieId);

            i.putExtra("Movie_poster", details[0]);
            i.putExtra("Movie_Title", details[1]);
            i.putExtra("Movie_Language",details[2]);
            i.putExtra("Movie_Format", details[3]);
            i.putExtra("Movie_Genre",details[4]);
            i.putExtra("Movie_Synopsis", details[5]);
            i.putExtra("Movie_Duration",details[6]);
            i.putExtra("Movie_VideoUrl", details[7]);
            i.putExtra("Movie_ReleasingDate",details[8]);
            i.putExtra("Movie_BigPosterurl",details[9]);
            c.startActivity(i);
        }*//**//**//**//**//*
    }
}
*/
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
}
