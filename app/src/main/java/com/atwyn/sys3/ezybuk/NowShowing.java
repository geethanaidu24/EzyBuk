package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class NowShowing extends Fragment {
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    final static String moviesUrlAddress = Config.moviesUrlAddress;
    ImageView im1;
    TextView tx1,tx2;
    Button b1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.now_showing, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      /*  im1 = (ImageView) view.findViewById(R.id.imageDownloaded);
        tx1 = (TextView) view.findViewById(R.id.textViewURL);
        tx1.setTypeface(EasyFonts.walkwayBlack(getContext()));
        tx2=(TextView)view.findViewById(R.id.textViewURL2);
        tx2.setTypeface(EasyFonts.robotoRegular(getContext()));
        tx1.setSelected(true);
        tx2.setSelected(true);
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ScrollingActivity.class);
                getActivity().startActivity(myIntent);
            }
        });*/




        final GridView gridView = (GridView)view.findViewById(R.id.moviesgridview);
        new MoviesDownloader(NowShowing.this, moviesUrlAddress, gridView).execute();

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


        private MoviesDownloader(NowShowing c, String moviesUrlAddress, GridView gridView) {

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

                    String moviesposterUrl = moviesObject.getString("Posterurl");




                    //  flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;

                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setMovieId(movieId);
                    mySQLDataBase.setTitle(movietitle);
                    mySQLDataBase.setPosterUrl(moviesposterUrl);

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
            ImageView movieposter = (ImageView) convertView.findViewById(R.id.imageDownloaded);


            //BIND DATA
            MySQLDataBase mySQLDataBase = (MySQLDataBase) this.getItem(position);
            final String url = mySQLDataBase.getPosterUrl();
            final String finalUrl = Config.mainUrlAddress + url;

            final String movieTitle = mySQLDataBase.getTitle();

            //IMG
            Glide.downloadImage(c, finalUrl, movieposter);

            movietitle.setText( movieTitle);

            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openDetailNewsActivity(finalUrl, String.valueOf(movietitle));
                }
            });

            return convertView;
        }
        private void openDetailNewsActivity(String...details){
            Intent intent = new Intent(c, MovieBooking.class);

            c.startActivity(intent);
        }
    }
}
