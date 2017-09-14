package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class NowShowing_movies extends AppCompatActivity {
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    final static String moviesUrlAddress = Config.moviesUrlAddress;
    ImageView im1;
    TextView tx1, tx2;
    GridView gridView;
    Button b1;
    String langarray;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private AsyncFetch.MovieDetailsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_showing_movies);

        Intent i = this.getIntent();
        if (i.getExtras() == null) {
            //Do first time stuff here
        } else {
            langarray = i.getExtras().getString("Array_languages");
            Log.d("llllasd", langarray);
        }
        new AsyncFetch().execute();
    }

        /*Intent i = this.getIntent();
        langarray = i.getExtras().getString("Array_languages");
        Log.d("llllasd",langarray);*/

      /* gridView = (GridView)findViewById(R.id.moviesgridview);
        new MoviesDownloader(NowShowing_movies.this, moviesUrlAddress, gridView).execute();

    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);

        // ATTENTION: This was auto-generated to implement the App Indexing API.


        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }



    private class MoviesDownloader extends AsyncTask<Void, Void, String> {

        Context c;
        String moviesUrlAddress;
        GridView gridview1;


        private MoviesDownloader(NowShowing_movies c, String moviesUrlAddress, GridView gridView) {
            this.c=c;
            this.moviesUrlAddress = moviesUrlAddress;
            this.gridview1 = gridView;
            Log.d("movies url: ", "> " + moviesUrlAddress);
        }


        *//*private MoviesDownloader(Ns_grid ns_grid, String moviesUrlAddress, GridView gridView) {
                    this.moviesUrlAddress = moviesUrlAddress;
                    this.gridview1 = gridview1;
                }
        *//*

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
                MoviesDataParser parser = new MoviesDataParser(c, gridview1, s);
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
    private class MoviesDataParser extends AsyncTask<Void, Void, Integer> {
        private final GridView gridview1;
        Context c;

        String jsonData;

        ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();

        private MoviesDataParser(Context c, GridView gridview1, String jsonData) {
            this.c = c;
            this.gridview1 = gridview1;
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

                final MoviesListAdapter adapter = new MoviesListAdapter(c, mySQLDataBases);
                gridview1.setAdapter(adapter);
            }
        }

        private int parseData() {
            try {
                JSONArray moviesArray = new JSONArray(jsonData);
                JSONObject moviesObject = null;
                mySQLDataBases.clear();
                MySQLDataBase mySQLDataBase;
                for (int i = 0; i < moviesArray.length(); i++) {
                    moviesObject = moviesArray.getJSONObject(i);
                    //   Log.d("result movies response: ", "> " + moviesObject);
                    int movieId = moviesObject.getInt("MovieId");
                    String movietitle = moviesObject.getString("Title");

                    String moviesposterUrl = moviesObject.getString("SmallPoster");
                    String moviesbigposterUrl=moviesObject.getString("Posterurl");
                    String moviesgenre=moviesObject.getString("Genre");
                    String movieslanguage=moviesObject.getString("MLanguage");
                    String moviesformat=moviesObject.getString("Format");
                    String moviessynopsis=moviesObject.getString("Synopsis");
                    String moviesduration=moviesObject.getString("Duration_min");
                    String moviecertification=moviesObject.getString("Certification");
                    String moviesvideourl=moviesObject.getString("Videourl");
                    String moviereleasingdate= moviesObject.getString("Releasing_Date");


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
                    mySQLDataBase.setCertification(moviecertification);
                    mySQLDataBase.setReleasing_Date(moviereleasingdate);
                    mySQLDataBases.add(mySQLDataBase);

                }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
    private class MoviesListAdapter extends BaseAdapter {

        Context c;
        ArrayList<MySQLDataBase> mySQLDataBases;
        LayoutInflater inflater;

        private MoviesListAdapter(Context c, ArrayList<MySQLDataBase> mySQLDataBases) {
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
                convertView = inflater.inflate(R.layout.nowshowing_gridview, parent, false);
            }

            final TextView movietitle =(TextView) convertView.findViewById(R.id.textViewURL);
            movietitle.setSelected(true);
            ImageView movieposter = (ImageView) convertView.findViewById(R.id.imageDownloaded);
            final TextView movieslanguage=(TextView)convertView.findViewById(R.id.textgenre);
            final TextView moviescertification=(TextView)convertView.findViewById(R.id.textView);

            //BIND DATA
            MySQLDataBase mySQLDataBase = (MySQLDataBase) this.getItem(position);
            final String url = mySQLDataBase.getPosterUrl();
            final String finalUrl = Config.mainUrlAddress + url;
            final int movieId = mySQLDataBase.getMovieId();
            final String movieTitle = mySQLDataBase.getTitle();
            final String movielanguage=mySQLDataBase.getMLanguage();
            final String movieformat=mySQLDataBase.getFormat();
            final String moviegenre=mySQLDataBase.getGenre();
            final String moviesynopsis=mySQLDataBase.getSynopsis();
            final String movieduration=mySQLDataBase.getDuration_min();
            final String movievideourl=mySQLDataBase.getVideourl();
            final String moviecertification=mySQLDataBase.getCertification();
            final String moviereleasingdate=mySQLDataBase.getReleasing_Date();
            final String moviebiposterurl=mySQLDataBase.getBigPosterUrl();
            //IMG
     Glide.downloadImage1(c, finalUrl, movieposter);

             movietitle.setText( movieTitle);
            movieslanguage.setText( movielanguage + "  |  " + movieformat );
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openDetailNewsActivity(movieId,finalUrl,movieTitle,movielanguage,movieformat,moviegenre,moviesynopsis,movieduration,movievideourl,moviereleasingdate,moviebiposterurl,moviecertification);
                }
            });
            moviescertification.setText(moviecertification);
            return convertView;
        }
        private void openDetailNewsActivity(int movieId, String...details ){
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
            i.putExtra("Movie_Certification",details[10]);
            c.startActivity(i);
        }
    }
}



*/

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(NowShowing_movies.this);
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
                url = new URL(Config.moviesUrlAddress);
                Log.d("second 22url", " >" + url);
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


                JSONArray moviesArray = new JSONArray(result);


                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject moviesObject = moviesArray.getJSONObject(i);

                    Log.d("result movies response: ", "> " + moviesObject);

                    // castobject=moviesObject.getJSONObject("moviecast");
                    MySQLDataBase mySQLDataBase = new MySQLDataBase();

                    mySQLDataBase.MovieId = moviesObject.getInt("MovieId");
                    mySQLDataBase.Title = moviesObject.getString("Title");

                    mySQLDataBase.SmallPosterUrl = moviesObject.getString("SmallPoster");
                    mySQLDataBase.BigPosterUrl = moviesObject.getString("Posterurl");
                    mySQLDataBase.Genre = moviesObject.getString("Genre");
                    mySQLDataBase.MLanguage = moviesObject.getString("MLanguage");
                    mySQLDataBase.Format = moviesObject.getString("Format");
                    mySQLDataBase.Synopsis = moviesObject.getString("Synopsis");
                    mySQLDataBase.Duration_min = moviesObject.getString("Duration_min");
                    mySQLDataBase.Certification = moviesObject.getString("Certification");
                    mySQLDataBase.Videourl = moviesObject.getString("Videourl");
                    mySQLDataBase.Releasing_Date = moviesObject.getString("Releasing_Date");


                    data.add(mySQLDataBase);


                    mLayoutManager = new GridLayoutManager(NowShowing_movies.this, 2);
                   // mLayoutManager.setReverseLayout(true);
                    //mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                   // mLayoutManager.setStackFromEnd(true);
                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

                    mAdapter = new MovieDetailsAdapter(NowShowing_movies.this, data);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setLayoutManager(mLayoutManager);

                      /*  RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);*/


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            final static String moviesUrlAddress = Config.moviesUrlAddress;
            String u;
            private Context context;
            private LayoutInflater inflater;
            List<MySQLDataBase> data = Collections.emptyList();
            MySQLDataBase current;
            int currentPos = 0;

            // create constructor to innitilize context and data sent from MainActivity
            public MovieDetailsAdapter(Context context, List<MySQLDataBase> data) {
                this.context = context;
                inflater = LayoutInflater.from(context);
                this.data = data;
            }


            // Inflate the layout when viewholder created
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.nowshowing_gridview, parent, false);
                MyHolder holder = new MyHolder(view);
                return holder;
            }

            // Bind data
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                // Get current position of item in recyclerview to bind data and assign values from list
                MyHolder myHolder = (MyHolder) holder;
                final MySQLDataBase current = data.get(position);
                myHolder.mtitle.setText(current.Title);
                myHolder.mlang.setText(current.MLanguage);
                myHolder.mcertification.setText(current.Certification);
                u = Config.mainUrlAddress + current.SmallPosterUrl;
                Log.d("result image ", "> " + u);
                Glide.downloadImage1(context, u, myHolder.im);

                final int movieId = current.MovieId;
                final String movieTitle =current.Title;
                final String movieposter =u;
                final String movielanguage=current.MLanguage;
                final String movieformat=current.Format;
                final String moviegenre=current.Genre;
                final String moviesynopsis=current.Synopsis;
                final String movieduration=current.Duration_min;
                final String movievideourl=current.Videourl;
                final String moviecertification=current.Certification;
                final String moviereleasingdate=current.Releasing_Date;
                final String moviebiposterurl=current.BigPosterUrl;
                myHolder.im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(NowShowing_movies.this, ScrollingActivity.class);
                        i.putExtra("MOVIE_ID", movieId);

                        i.putExtra("Movie_poster", movieposter);
                        i.putExtra("Movie_Title", movieTitle);
                        i.putExtra("Movie_Language",movielanguage);
                        i.putExtra("Movie_Format", movieformat);
                        i.putExtra("Movie_Genre",moviegenre);
                        i.putExtra("Movie_Synopsis", moviesynopsis);
                        i.putExtra("Movie_Duration",movieduration);
                        i.putExtra("Movie_VideoUrl", movievideourl);
                        i.putExtra("Movie_ReleasingDate",moviereleasingdate);
                        i.putExtra("Movie_BigPosterurl",moviebiposterurl);
                        i.putExtra("Movie_Certification",moviecertification);
                         startActivity(i);
                     // openDetailNewsActivity(movieId,movieTitle,movielanguage,movieformat,moviegenre,moviesynopsis,movieduration,movievideourl,moviereleasingdate,moviebiposterurl,moviecertification);
                    }
                });
            }

            // return total item from List
            @Override
            public int getItemCount() {
                return data.size();
            }


            class MyHolder extends RecyclerView.ViewHolder {

                TextView mtitle;
                TextView mlang,mcertification;
                ImageView im;


                // create constructor to get widget reference
                public MyHolder(View itemView) {
                    super(itemView);
                    mtitle = (TextView) itemView.findViewById(R.id.textViewURL);
                    mlang = (TextView) itemView.findViewById(R.id.textgenre);
                    mcertification = (TextView) itemView.findViewById(R.id.textView);
                    im = (ImageView) itemView.findViewById(R.id.imageDownloaded);

                }

            }


        }
    }
}