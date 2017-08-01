package com.atwyn.sys3.ezybuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.Arrays;

import static com.atwyn.sys3.ezybuk.R.drawable.backfinalfour;
import static com.atwyn.sys3.ezybuk.R.id.share;

public class ScrollingActivity2 extends AppCompatActivity {
    Button book;
    ImageView mposter2;
    TextView mtitle2,mgenre2,mlanguage2,mformat2,msynopsis2,mdurationdate2;
    RecyclerView mRecyclerView, mRecyclerView1;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    RecyclerView.LayoutManager mLayoutManager1;
    RecyclerView.Adapter mAdapter1;
    ArrayList<String> alName;
    ArrayList<Integer> alImage;

    ArrayList<String> alName1;
    ArrayList<Integer> alImage1;
    private MediaController mediacontroller;
    private Uri uri;

    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    final static String moviesUrlAddress = Config.moviesUrlAddress;
    String finalurl1;
    String movietitle,movieposter,movielanguage,movieformat,moviegenre,moviesynopsis,movieduration,movievideourl,moviebigposter;
    int movieid;
    Object mreleasingdate;

    ImageView like,dislike,like1,dislike1;
    TextView liketext,disliketext,liketext1,disliketext1;
    int count=0;
    int click=0;
    RelativeLayout rlike,rlike1,rdislike,rdislike1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scrolling2);
        Intent i = this.getIntent(); // get Intent which we set from Previous Activity
        movieid = i.getExtras().getInt("MOVIE_ID");
        mreleasingdate=  i.getExtras().get("Movie_ReleasingDate");
        movieposter = i.getExtras().getString("Movie_poster");
        movietitle =i.getExtras().getString("Movie_Title");

        moviebigposter=i.getExtras().getString("Movie_BigPosterurl");
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


      /*  alName = new ArrayList<>(Arrays.asList("Cheesy...", "Crispy... ", "Fizzy...", "Cool...", "Softy...", "Fruity...", "Fresh...", "Sticky..."));
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

        mAdapter = new HLVAdapter(ScrollingActivity2.this, alName, alImage);
        mRecyclerView.setAdapter(mAdapter);


        mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView1.setLayoutManager(mLayoutManager1);

        mAdapter1 = new HLVAdapter(ScrollingActivity2.this, alName1, alImage1);
        mRecyclerView1.setAdapter(mAdapter1);
*/

        mtitle2=(TextView)findViewById(R.id.textView62);
        mgenre2=(TextView)findViewById(R.id.textView102);
        mlanguage2=(TextView)findViewById(R.id.textView82);
        mformat2=(TextView)findViewById(R.id.textView332);
        msynopsis2=(TextView)findViewById(R.id.textView172);
        mposter2=(ImageView)findViewById(R.id.mp2);
        mdurationdate2=(TextView)findViewById(R.id.textView92);
        TextView tooltex=(TextView)findViewById(R.id.tooltext1);
        ImageView imbigposter=(ImageView)findViewById(R.id.imageView12);

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
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)

                .crossFade()
                .into(mposter2);

        com.bumptech.glide.Glide.with(this)
                .load(finalurl1)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)

                .crossFade()
                .into(imbigposter);
        //new MoviesInfoDownloader(ScrollingActivity.this, moviesUrlAddress).execute();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ScrollingActivity2.this, FullTrailer.class);
                in.putExtra("Movie_VideoUrl",movievideourl );
                startActivity(in);
            }
        });
 rlike=(RelativeLayout)findViewById(R.id.bookedImageLayout);
        rlike1=(RelativeLayout)findViewById(R.id.bookedImageLayout2);

        rdislike=(RelativeLayout)findViewById(R.id.selectedImageLayout);
        rdislike1=(RelativeLayout)findViewById(R.id.selectedImageLayout1) ;

        like =(ImageView) findViewById(R.id.bookedImage2);
        liketext=(TextView)findViewById(R.id.bookedText2);
        dislike=(ImageView)findViewById(R.id.bookedImage12) ;
        disliketext=(TextView)findViewById(R.id.bookedText12);

        like1 =(ImageView) findViewById(R.id.bookedImage22);
        liketext1=(TextView)findViewById(R.id.bookedText22);
        dislike1 =(ImageView) findViewById(R.id.bookedImage121);
        disliketext1=(TextView)findViewById(R.id.bookedText121);


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
                Toast.makeText(ScrollingActivity2.this,"Submmitted",Toast.LENGTH_LONG);


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
                Toast.makeText(ScrollingActivity2.this,"Submmitted",Toast.LENGTH_LONG);


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
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"hi");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,movietitle +"("+movielanguage +")" + "\n \n"+ moviesynopsis  +"\n \n"+ "All that you would like to explore and know about movie"+ movietitle + "\n"+  "https://www.youtube.com/watch?v=YheC-4Qgoro&t=3s" );

                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

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



    private class MoviesInfoDownloader extends AsyncTask<Void, Void, String> {

        Context c;
        String moviesUrlAddress;





        public MoviesInfoDownloader(ScrollingActivity scrollingActivity, String moviesUrlAddress) {
            this.c=c;
            this.moviesUrlAddress = moviesUrlAddress;
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
             MoviesInfoDataParser parser = new MoviesInfoDataParser(c, s);
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
    private class MoviesInfoDataParser extends AsyncTask<Void, Void, Integer> {

        Context c;

        String jsonData;

        ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();

        private MoviesInfoDataParser(Context c, String jsonData) {
            this.c = c;

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

                final MoviesInfoListAdapter adapter = new MoviesInfoListAdapter(c, mySQLDataBases);

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
                    String moviesgenre=moviesObject.getString("Genre");
                    String movieslanguage=moviesObject.getString("MLanguage");
                    String moviesformat=moviesObject.getString("Format");



                    //  flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;

                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setMovieId(movieId);
                    mySQLDataBase.setTitle(movietitle);
                    mySQLDataBase.setPosterUrl(moviesposterUrl);
                    mySQLDataBase.setGenre(moviesgenre);
                    mySQLDataBase.setMLanguage(movieslanguage);
                    mySQLDataBase.setFormat(moviesformat);
                    mySQLDataBases.add(mySQLDataBase);

                }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
    private class MoviesInfoListAdapter extends BaseAdapter {

        Context c;
        ArrayList<MySQLDataBase> mySQLDataBases;
        LayoutInflater inflater;

        private MoviesInfoListAdapter(Context c, ArrayList<MySQLDataBase> mySQLDataBases) {
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
           *//* if (convertView == null) {
                convertView = inflater.inflate(R.layout.nowshowing_gridview, parent, false);
            }
*//*
            final TextView movietitle =(TextView) convertView.findViewById(R.id.textViewURL);
            movietitle.setSelected(true);
            ImageView movieposter = (ImageView) convertView.findViewById(R.id.imageDownloaded);
            final TextView movieslanguage=(TextView)convertView.findViewById(R.id.textgenre);

            //BIND DATA
            MySQLDataBase mySQLDataBase = (MySQLDataBase) this.getItem(position);
            final String url = mySQLDataBase.getPosterUrl();
            final String finalUrl = Config.mainUrlAddress + url;

            final String movieTitle = mySQLDataBase.getTitle();
            final String movielanguage=mySQLDataBase.getGenre();
            final String movieformat=mySQLDataBase.getFormat();
            //IMG
            Glide.downloadImage1(c, finalUrl, movieposter);

            movietitle.setText( movieTitle);
            movieslanguage.setText( movielanguage + "  |  " + movieformat );
            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openDetailNewsActivity(finalUrl, String.valueOf(movietitle));
                }
            });

            return convertView;
        }
        private void openDetailNewsActivity(String...details){
            Intent intent = new Intent(c, ScrollingActivity.class);

            c.startActivity(intent);
        }
    }

}
*/