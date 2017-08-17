package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpComing_movies extends AppCompatActivity {
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    final static String moviesUrlAddress = Config.moviesUpcomingUrlAddress;
    ImageView im1;
    TextView tx1,tx2;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_coming_movies);
        final GridView gridView = (GridView)findViewById(R.id.moviesgridview1);
        new MoviesDownloader(UpComing_movies.this, moviesUrlAddress, gridView).execute();

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


        private MoviesDownloader(UpComing_movies c, String moviesUrlAddress, GridView gridView) {
            this.c=c;
            this.moviesUrlAddress = moviesUrlAddress;
            this.gridview1 = gridView;
        }


        /*private MoviesDownloader(Ns_grid ns_grid, String moviesUrlAddress, GridView gridView) {
                    this.moviesUrlAddress = moviesUrlAddress;
                    this.gridview1 = gridview1;
                }
        */

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
                StringBuffer jsonData = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    jsonData.append(line + "n");
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
            final String moviereleasingdate=mySQLDataBase.getReleasing_Date();
            final String moviebiposterurl=mySQLDataBase.getBigPosterUrl();
            //IMG
            Glide.downloadImage1(c, finalUrl, movieposter);

            movietitle.setText( movieTitle);
            movieslanguage.setText( movielanguage + "  |  " + movieformat );
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openDetailNewsActivity(movieId,finalUrl,movieTitle,movielanguage,movieformat,moviegenre,moviesynopsis,movieduration,movievideourl,moviereleasingdate,moviebiposterurl);
                }
            });

            return convertView;
        }
        private void openDetailNewsActivity(int movieId, String...details ){
            Intent i = new Intent(c, ScrollingActivity2.class);
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
        }
    }
}

