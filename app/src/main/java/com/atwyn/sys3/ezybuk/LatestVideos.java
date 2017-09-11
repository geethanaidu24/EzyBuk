package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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

public class LatestVideos extends AppCompatActivity {
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    final static String videosUrlAddress = Config.videosUrlAddress;
    ImageView im1;
    TextView tx1,tx2;
    Button b1;
    int click = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_videos);
        final ListView listView = (ListView) findViewById(R.id.videogridview);
        new VideosDownloader(LatestVideos.this, videosUrlAddress, listView).execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click = click + 1;
                    if (click == 1) {
                        click = 0;
                        Intent in = new Intent(LatestVideos.this, Main2Activity.class);


                        finish();
                    }
                    //startActivity(in);
                }
            });
        }

    }
    public void onBackPressed() {
        //finishAffinity();
        click = click + 1;
        if (click == 1) {
            click = 0;
            Intent in = new Intent(LatestVideos.this, Main2Activity.class);

            finish();
        }
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



    private class VideosDownloader extends AsyncTask<Void, Void, String> {

        Context c;
        String videosUrlAddress;
        ListView listView1;


        private VideosDownloader(LatestVideos c, String videosUrlAddress, ListView listView) {
            this.c=c;
            this.videosUrlAddress = videosUrlAddress;
            this.listView1 = listView;
            Log.d("movies url: ", "> " + videosUrlAddress);
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
               VideoDataParser parser = new VideoDataParser(c, listView1, s);
                parser.execute();
            }
        }

        private String downloadMoviesData() {
            HttpURLConnection con = Connector.connect(videosUrlAddress);
            Log.d("URRRRRLLLL",videosUrlAddress);
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
    private class VideoDataParser extends AsyncTask<Void, Void, Integer> {
        private final ListView listView1;
        Context c;

        String jsonData;

        ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();

        private VideoDataParser(Context c, ListView listView1, String jsonData) {
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

                final VideosListAdapter adapter = new VideosListAdapter(c, mySQLDataBases);
                listView1.setAdapter(adapter);
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
                    int movieId = moviesObject.getInt("Id");
                    String movietitle = moviesObject.getString("Title");

                    String moviestrailerimage = moviesObject.getString("TrailerImage");
                    String moviestrailerUrl=moviesObject.getString("TrailerUrl");






                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setTrailerId(movieId);
                    mySQLDataBase.setTrailerName(movietitle);
                    mySQLDataBase.setTrailerImageUrl(moviestrailerimage);
                    mySQLDataBase.setTrailerVideo(moviestrailerUrl);

                    mySQLDataBases.add(mySQLDataBase);

                }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
    private class VideosListAdapter extends BaseAdapter {

        Context c;
        ArrayList<MySQLDataBase> mySQLDataBases;
        LayoutInflater inflater;

        private VideosListAdapter(Context c, ArrayList<MySQLDataBase> mySQLDataBases) {
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
                convertView = inflater.inflate(R.layout.videos_listview, parent, false);
            }

            final TextView movietitle =(TextView) convertView.findViewById(R.id.postername);
            movietitle.setSelected(true);
            ImageView movieposter = (ImageView) convertView.findViewById(R.id.poster);

            final ImageButton play=(ImageButton) convertView.findViewById(R.id.button7);

            //BIND DATA
            MySQLDataBase mySQLDataBase = (MySQLDataBase) this.getItem(position);
            final String url = mySQLDataBase.getTrailerImageUrl();
            final String finalUrl = Config.mainUrlAddress + url;
            final int trailerid = mySQLDataBase.getTrailerId();
            final String trailertitle = mySQLDataBase.getTrailerName();
            final String trailervideo=mySQLDataBase.getTrailerVideo();

            Glide.downloadImage1(c, finalUrl, movieposter);

            movietitle.setText( trailertitle);

            play.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent in = new Intent(LatestVideos.this, FullTrailer.class);
                    in.putExtra("Movie_VideoUrl", trailervideo);
                    startActivity(in);
                }
            });

            return convertView;
        }

    }
}



