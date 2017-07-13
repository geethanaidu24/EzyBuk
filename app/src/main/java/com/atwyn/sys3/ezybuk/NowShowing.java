package com.atwyn.sys3.ezybuk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

public class NowShowing extends Fragment {
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
   // final static String moviesUrlAddress = Config.moviesUrlAddress;
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
        im1 = (ImageView) view.findViewById(R.id.imageDownloaded);
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
        });
    }


}
      /* final ListView listView = (ListView) view.findViewById(R.id.listmovies);
        new MoviesDownloader(NowShowing.this, moviesUrlAddress, listView).execute();

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
        ListView listView1;


        private MoviesDownloader(NowShowing c, String urlAddress, ListView listView1) {

            this.moviesUrlAddress = urlAddress;
            this.listView1 = listView1;
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
        Context c;
        ListView listView1;
        String jsonData;

        ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();

        private MoviesDataParser(Context c, ListView listView1, String jsonData) {
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
                    Log.d("result response: ", "> " + moviesObject);
                    int moviesId = moviesObject.getInt("MoviesId");
                    String moviesName = moviesObject.getString("MoviesName");

                    String moviesImageUrl = moviesObject.getString("MoviesImageUrl");




                    //  flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;

                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setMoviesId(moviesId);
                    mySQLDataBase.setMoviesName(moviesName);
                    mySQLDataBase.setMoviesImageUrl(moviesImageUrl);

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
                convertView = inflater.inflate(R.layout.nowshowing_listview, parent, false);
            }

            TextView moviename =(TextView) convertView.findViewById(R.id.textViewURL);
            ImageView img = (ImageView) convertView.findViewById(R.id.imageDownloaded);


            //BIND DATA
            MySQLDataBase mySQLDataBase = (MySQLDataBase) this.getItem(position);
            final String url = mySQLDataBase.getMoviesImageUrl();
            final String finalUrl = Config.mainUrlAddress + url;

            final String movieName = mySQLDataBase.getMoviesName();

            //IMG
            PicassoClient.downloadImage(c, finalUrl, img);

            moviename.setText(movieName);

            convertView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openDetailNewsActivity(finalUrl,movieName);
                }
            });

            return convertView;
        }
        private void openDetailNewsActivity(String...details){
            Intent intent = new Intent(c, MoviesBooking.class);

            c.startActivity(intent);
        }
    }
}



*/