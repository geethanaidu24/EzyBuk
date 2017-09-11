package com.atwyn.sys3.ezybuk;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.interfaces.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Search extends AppCompatActivity {
    private EditText filterText;
    private LruCache<String, Bitmap> mMemoryCache;
    private ArrayAdapter<String> listAdapter;
    String urlAddress = Config.Movies_Search;
    SearchView sv;
    ListView lv;
    ImageView noDataImg, noNetworkImg;
    String movietitle;
    ArrayList<MySQLDataBase> mySQLDataBases1 = new ArrayList<>();
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFish;
    private AdapterFish mAdapter;
    public static ArrayList<MySQLDataBase> array_sort;
    SearchView searchView = null;
    public static ArrayList<MySQLDataBase> movieNamesArrayList;
    int textlength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(Search.this, SeatReservation.class);
                    //startActivity(in);
                    finish();
                }
            });
        }*/
        filterText = (EditText) findViewById(R.id.editText);

        ListView listView = (ListView) findViewById(R.id.listView);
        new MoviesDownloader(Search.this, urlAddress, listView).execute();



        filterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable theWatchedText) {

            }
        });
    }

        /*String[] listViewAdapterContent = {"School", "House", "Building", "Food", "Sports", "Dress", "Ring"};

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listViewAdapterContent);

        itemList.setAdapter(listAdapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// make Toast when click
                Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
            }
        });
        filterText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Search.this.listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });*/

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
    // ATTENTION: This was auto-generated to implement the App Indexing API.


    // See https://g.co/AppIndexing/AndroidStudio for more information.


    private class MoviesDownloader extends AsyncTask<Void, Void, String> {

        Context c;
        String moviesUrlAddress;
        ListView listView1;


        private MoviesDownloader(Search c, String urlAddress, ListView listView) {
            this.c = c;
            this.moviesUrlAddress = urlAddress;
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
                MoviesDataParser parser = new MoviesDataParser(c, listView1, s);
                parser.execute();
            }
        }

        private String downloadMoviesData() {
            HttpURLConnection con = Connector.connect(urlAddress);
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

        private class MoviesDataParser extends AsyncTask<Void, Void, Integer> {
            private final ListView listView1;
            Context c;

            String jsonData;

            ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();

            private MoviesDataParser(Context c, ListView listView, String jsonData) {
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

                    final MoviesListAdapter adapter = new MoviesListAdapter(c, mySQLDataBases);
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
               // return mySQLDataBases.size();

                return (mySQLDataBases == null) ? 0 : mySQLDataBases.size();
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
                    convertView = inflater.inflate(R.layout.search_listview, parent, false);
                }


                final TextView movietitle1 = (TextView) convertView.findViewById(R.id.searchtext);


                // Log.d("result movies ", movietitle);

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
                movietitle1.setText(movieTitle);
                Log.d("result movies ", movieTitle);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(Search.this, mySQLDataBases.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });

                filterText.addTextChangedListener(new TextWatcher() {


                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                       textlength = filterText.getText().length();
                       // array_sort.clear();
                        for (int i = 0; i < mySQLDataBases.size(); i++) {
                            if (textlength <= mySQLDataBases.get(i).getTitle().length()) {
                                Log.d("ertyyy", mySQLDataBases.get(i).getTitle().toLowerCase().trim());
                                if (mySQLDataBases.get(i).getTitle().toLowerCase().trim().contains(
                                        filterText.getText().toString().toLowerCase().trim())) {
                       //   mySQLDataBases.add(mySQLDataBases.get(i));
                                    Log.d("hhhh", filterText.getText().toString());
                                   // Toast.makeText(Search.this,filterText.getText().toString(), Toast.LENGTH_SHORT).show();
                                    mySQLDataBases1.add(mySQLDataBases.get(i));
                            /* Intent in =new Intent(Search.this,ScrollingActivity.class);
                                    in.putExtra("MOVIE_ID", movieId);
                                    in.putExtra("Movie_poster",finalUrl );

                                    in.putExtra("Movie_Language",movielanguage);
                                    in.putExtra("Movie_Format", movieformat);
                                    in.putExtra("Movie_Genre",moviegenre);
                                    in.putExtra("Movie_Synopsis", moviesynopsis);
                                    in.putExtra("Movie_Duration",movieduration);
                                    in.putExtra("Movie_VideoUrl", movievideourl);
                                    in.putExtra("Movie_ReleasingDate",moviereleasingdate);
                                    in.putExtra("Movie_BigPosterurl",moviebiposterurl);
                                    in.putExtra("Movie_Certification",moviecertification);

                                    in.putExtra("Movie_Title", filterText.getText().toString());
                                    startActivity(in);*/

                                }
                            }
                        }
                        MoviesListAdapter adapter = new MoviesListAdapter(Search.this, mySQLDataBases1);
                        listView1.setAdapter(adapter);

                    }
                });


                return convertView;
            }


        }
    }
}





/*

@Override
public boolean onCreateOptionsMenu(Menu menu) {

        // adds item to action bar
        getMenuInflater().inflate(R.menu.main2, menu);

        // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.h1);
        SearchManager searchManager = (SearchManager) Search.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
        searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
        searchView.setSearchableInfo(searchManager.getSearchableInfo(Search.this.getComponentName()));
        searchView.setIconified(false);
        }

        return true;
        }

@Override
public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
        }

// Every time when you press search button on keypad an Activity is recreated which in turn calls this function
@Override
protected void onNewIntent(Intent intent) {
    // Get search query and create object of class AsyncFetch
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        String query = intent.getStringExtra(SearchManager.QUERY);
        if (searchView != null) {
            searchView.clearFocus();
        }
        new AsyncFetch(query).execute();

    }
}

// Create class AsyncFetch
    private class AsyncFetch extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(Search.this);
        HttpURLConnection conn;
        URL url = null;
        String searchQuery;

        public AsyncFetch(String searchQuery) {
            this.searchQuery = searchQuery;
        }

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
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.0.11/ezybuk/home/searchMovies");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", searchQuery);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

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
                    return ("Connection error");
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
            pdLoading.dismiss();
            List<DataFish> data = new ArrayList<>();

            pdLoading.dismiss();
            if (result.equals("no rows")) {
                Toast.makeText(Search.this, "No Results found for entered query", Toast.LENGTH_LONG).show();
            } else {

                try {

                    JSONArray jArray = new JSONArray(result);

                    // Extract data from json and store into ArrayList as class objects
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);
                        DataFish fishData = new DataFish();
                        fishData.fishName = json_data.getString("Title");

                        data.add(fishData);
                    }

                    // Setup and Handover data to recyclerview
                    mRVFish = (RecyclerView) findViewById(R.id.fishPriceList);
                    mAdapter = new AdapterFish(Search.this, data);

                    mRVFish.setLayoutManager(new LinearLayoutManager(Search.this));
                    mRVFish.setAdapter(mAdapter);
                } catch (JSONException e) {
                    // You to understand what actually error is and handle it appropriately
                    Toast.makeText(Search.this, e.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(Search.this, result.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }
}



*/
