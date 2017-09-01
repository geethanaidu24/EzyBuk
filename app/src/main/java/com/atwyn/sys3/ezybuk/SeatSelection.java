package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatSelection extends AppCompatActivity {
    String movietitle, selecetedlanguage, selectedformat, movieformat, movielanguage;
    TextView toolbartext;
    int movieid;
    //  private RecyclerView recyclerView;
    private AsyncFetch.DateAdapter mAdapter;
    private MoviesDateListAdapter.AsyncFetch1.DateAdapter1 mAdapter1;
    final static String dateUrlAddress = Config.DateNameUrlAddress;
    String DateFromDb;
    final static String MovietimeUrlAddress = Config.MovieTimeUrlAddress;
    private LruCache<String, Bitmap> mMemoryCache;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRecyclerView, mRecyclerView1;
    String theaterName;
    private LinearLayoutManager mLayoutManager, mLayoutManager1;
    String nametitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        Intent i = this.getIntent();
        movieid = i.getExtras().getInt("Movie_Id");

        movietitle = i.getExtras().getString("Movie_Title");
        selectedformat = i.getExtras().getString("Selected_format");
        selecetedlanguage = i.getExtras().getString("Selected_language");

        Log.d("llalalla", selecetedlanguage + selectedformat);
        // selectedformat = i.getExtras().getString("Selected_format");

        toolbartext = (TextView) findViewById(R.id.tooltext1);
        toolbartext.setText(movietitle + " " +
                " \n " + selecetedlanguage + " | " + selectedformat);

        toolbartext = (TextView) findViewById(R.id.tooltext1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(SeatSelection.this, ScrollingActivity.class);
                    //startActivity(in);
                    finish();
                }
            });
            new AsyncFetch().execute();


        }
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(SeatSelection.this);
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
                url = new URL(Config.DateNameUrlAddress + movieid + "/" + selectedformat + "/" + selecetedlanguage);
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


                JSONArray moviesArray = new JSONArray(result);


                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject moviesObject = moviesArray.getJSONObject(i);

                    Log.d("result movies response: ", "> " + moviesObject);

                    // castobject=moviesObject.getJSONObject("moviecast");
                    MySQLDataBase mySQLDataBase = new MySQLDataBase();

                    mySQLDataBase.ShowDate = moviesObject.getString("ShowDate");


                    data.add(mySQLDataBase);


                    Log.d("dddddddatat", String.valueOf(data));

                    mLayoutManager = new LinearLayoutManager(SeatSelection.this);
                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mLayoutManager.setStackFromEnd(true);
                    mRecyclerView = (RecyclerView) findViewById(R.id.recycler_viewdate);

                    mAdapter = new DateAdapter(SeatSelection.this, data);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(mLayoutManager);

               /*     mLayoutManager = new LinearLayoutManager(SeatSelection.this);
                    mLayoutManager.setReverseLayout(true);
                   // mLayoutManager.setStackFromEnd(true);

// And now set it to the RecyclerView
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new DateAdapter(SeatSelection.this, data);
                    mRecyclerView.setAdapter(mAdapter);
*/
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        class DateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
            final static String dateUrlAddress = Config.DateNameUrlAddress;
            String u;
            private Context context;
            private LayoutInflater inflater;
            List<MySQLDataBase> data = Collections.emptyList();
            MySQLDataBase current;
            int currentPos = 0;

            // create constructor to innitilize context and data sent from MainActivity
            public DateAdapter(Context context, List<MySQLDataBase> data) {
                this.context = context;
                inflater = LayoutInflater.from(context);
                this.data = data;
            }


            // Inflate the layout when viewholder created
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.listview_date, parent, false);
                DateAdapter.MyHolder holder = new DateAdapter.MyHolder(view);
                return holder;
            }

            // Bind data
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                // Get current position of item in recyclerview to bind data and assign values from list
                DateAdapter.MyHolder myHolder = (DateAdapter.MyHolder) holder;
                MySQLDataBase current = data.get(position);


                DateFromDb = current.ShowDate;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//set format of date you receiving from db
                Date date = null;
                try {
                    date = sdf.parse(DateFromDb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat newDate = new SimpleDateFormat("EEE '\n' dd ");//set format of new date
                System.out.println(newDate.format(date));
                String ShowconvertedDate = newDate.format(date);// here is your new date !
                Log.d("DATE of show", ShowconvertedDate);

                myHolder.textCastname.setText(ShowconvertedDate);

                myHolder.textCastname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ListView listView = (ListView) findViewById(R.id.dateListview);
                        new MoviesDateDownloader(SeatSelection.this, MovietimeUrlAddress, listView).execute();
                    }
                });

            }

            // return total item from List
            @Override
            public int getItemCount() {
                return data.size();
            }


            class MyHolder extends RecyclerView.ViewHolder {

                Button textCastname;


                // create constructor to get widget reference
                public MyHolder(View itemView) {
                    super(itemView);
                    textCastname = (Button) itemView.findViewById(R.id.buttondate);


                }


            }
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

    private class MoviesDateDownloader extends AsyncTask<Void, Void, String> {

        Context c;
        String MovietimeUrlAddress;
        ListView listView1;


        private MoviesDateDownloader(SeatSelection c, String MovietimeUrlAddress, ListView listView) {
            this.c = c;
            this.MovietimeUrlAddress = MovietimeUrlAddress;
            this.listView1 = listView;
            Log.d("movies url: ", "> " + dateUrlAddress);
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
                MoviesDateDataParser parser = new MoviesDateDataParser(c, listView1, s);
                parser.execute();
            }
        }

        private String downloadMoviesData() {
            HttpURLConnection con = Connector.connect(MovietimeUrlAddress + movieid + "/" + DateFromDb + "/" + selectedformat + "/" + selecetedlanguage);
            Log.d("theatertime", MovietimeUrlAddress + movieid + "/" + DateFromDb + "/" + selectedformat + "/" + selecetedlanguage);
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

    private class MoviesDateDataParser extends AsyncTask<Void, Void, Integer> {
        private final ListView listView1;
        Context c;

        String jsonData;

        ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();


        private MoviesDateDataParser(Context c, ListView listView, String jsonData) {
            this.c = c;
            this.listView1 = listView;
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

                final MoviesDateListAdapter adapter = new MoviesDateListAdapter(c, mySQLDataBases);
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
                    int cinemaId = moviesObject.getInt("CinemaId");
                   nametitle = moviesObject.getString("Name");



                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setMovieId(cinemaId);
                    mySQLDataBase.setTitle(nametitle);

                    mySQLDataBases.add(mySQLDataBase);

                    Log.d("result theater name: ", "> " + nametitle);
                /*JSONObject theaterObject = new JSONObject(jsonData);
                //JSONObject timeObject = null;
                // JSONObject theaterObject = null;
                mySQLDataBases.clear();

                MySQLDataBase mySQLDataBase;
                JSONArray showtheater = theaterObject.getJSONArray("ShowTheatre");
                Log.d("result theater response: ", "> " + showtheater);

                //  JSONArray theaterArray = new JSONArray(jsonData);

                for (int i = 0; i < showtheater.length(); i++) {
                    JSONObject theaterObject1 = showtheater.getJSONObject(i);
                    Log.d("result theater response: ", "> " + theaterObject);
                    int theaterId = theaterObject1.getInt("CinemaId");
                    theaterName = theaterObject1.getString("Name");

                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setTheaterId(theaterId);
                    mySQLDataBase.setTheaterName(theaterName);
                    mySQLDataBases.add(mySQLDataBase);
                    Log.d("result theater name: ", "> " + theaterName);
*/


               /* JSONArray showtime = theaterObject.getJSONArray("ShowTime");
                Log.d("result time response: ", "> " + showtime);
                for (int j = 0; j < showtime.length(); j++) {
                    JSONObject  theaterObject2 = showtime.getJSONObject(j);
                    Log.d("result theater response: ", "> " + theaterObject2);

                    String time = theaterObject2.getString("ShowTime");


                    mySQLDataBase = new MySQLDataBase();

                    mySQLDataBase.setShowTime(time);
                    mySQLDataBases.add(mySQLDataBase);
                    Log.d("result theater time: ", "> " + time);*/

                }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    private class MoviesDateListAdapter extends BaseAdapter {

        Context c;
        ArrayList<MySQLDataBase> mySQLDataBases;
        // ArrayList<MySQLDataBase> mySQLDataBases1;
        LayoutInflater inflater;

        private MoviesDateListAdapter(Context c, ArrayList<MySQLDataBase> mySQLDataBases) {
            this.c = c;
            this.mySQLDataBases = mySQLDataBases;
            // this.mySQLDataBases1=mySQLDataBases1;
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
                convertView = inflater.inflate(R.layout.listview_theater_time, parent, false);
            }
            final TextView theatername1 = (TextView) convertView.findViewById(R.id.theatername);
          //  final Button timebutton = (Button) convertView.findViewById(R.id.buttontime);
            mRecyclerView1 = (RecyclerView) findViewById(R.id.recycler_viewtime);

            //BIND DATA
            MySQLDataBase mySQLDataBase = (MySQLDataBase) this.getItem(position);
            final int theaterid = mySQLDataBase.getTheaterId();
            theaterName = mySQLDataBase.getTheaterName();
            //final String time = mySQLDataBase.getShowTime();
            theatername1.setText(mySQLDataBase.getTheaterName());
         new AsyncFetch1().execute();


            // timebutton.setText( time);


            return convertView;
        }

        private class AsyncFetch1 extends AsyncTask<String, String, String> {
            ProgressDialog pdLoading = new ProgressDialog(SeatSelection.this);
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
                    url = new URL(Config.TimeNameUrlAddress + movieid + "/" + DateFromDb +"/" + selectedformat + "/" + selecetedlanguage);
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
                List<MySQLDataBase> data1 = new ArrayList<>();

                pdLoading.dismiss();
                try {


                    JSONArray moviesArray = new JSONArray(result);


                    for (int i = 0; i < moviesArray.length(); i++) {
                        JSONObject moviesObject = moviesArray.getJSONObject(i);

                        Log.d("result movies response: ", "> " + moviesObject);

                        // castobject=moviesObject.getJSONObject("moviecast");
                        MySQLDataBase mySQLDataBase = new MySQLDataBase();

                        mySQLDataBase.ShowTime = moviesObject.getString("ShowTime");


                        data1.add(mySQLDataBase);


                        Log.d("dddddddatat", String.valueOf(data1));

                        mLayoutManager1 = new LinearLayoutManager(SeatSelection.this);
                        mLayoutManager1.setReverseLayout(true);
                        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mLayoutManager1.setStackFromEnd(true);


                        mAdapter1 = new DateAdapter1(SeatSelection.this, data1);
                        mRecyclerView1.setAdapter(mAdapter1);
                        mRecyclerView1.setLayoutManager(mLayoutManager1);

               /*     mLayoutManager = new LinearLayoutManager(SeatSelection.this);
                    mLayoutManager.setReverseLayout(true);
                   // mLayoutManager.setStackFromEnd(true);

// And now set it to the RecyclerView
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new DateAdapter(SeatSelection.this, data);
                    mRecyclerView.setAdapter(mAdapter);
*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            class DateAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
                final static String dateUrlAddress = Config.TimeNameUrlAddress;
                String u;
                private Context context;
                private LayoutInflater inflater;
                List<MySQLDataBase> data1 = Collections.emptyList();
                MySQLDataBase current;
                int currentPos = 0;

                // create constructor to innitilize context and data sent from MainActivity
                public DateAdapter1(Context context, List<MySQLDataBase> data1) {
                    this.context = context;
                    inflater = LayoutInflater.from(context);
                    this.data1 = data1;
                }


                // Inflate the layout when viewholder created
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = inflater.inflate(R.layout.listview_time, parent, false);
                    DateAdapter1.MyHolder holder = new DateAdapter1.MyHolder(view);
                    return holder;
                }

                // Bind data
                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                    // Get current position of item in recyclerview to bind data and assign values from list
                    DateAdapter1.MyHolder myHolder = (DateAdapter1.MyHolder) holder;
                    MySQLDataBase current = data1.get(position);


                    myHolder.textCastname1.setText(current.ShowTime);


                }

                // return total item from List
                @Override
                public int getItemCount() {
                    return data1.size();
                }


                class MyHolder extends RecyclerView.ViewHolder {

                    Button textCastname1;


                    // create constructor to get widget reference
                    public MyHolder(View itemView) {
                        super(itemView);
                        textCastname1 = (Button) itemView.findViewById(R.id.buttontime);


                    }


                }
            }
        }
    }
}




