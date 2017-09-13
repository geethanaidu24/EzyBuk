package com.atwyn.sys3.ezybuk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
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
import java.util.Arrays;
import java.util.Date;

public class Filter extends AppCompatActivity {
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean loggedIn = false;
    final static String moviesUrlAddress = Config.moviesUrlAddress;

    TextView tx1;
    CheckBox ch1;
    CheckBox checkBox;
    Button apply1;
    ArrayList<String> list;
    static ArrayList<String> checkBoxData = null;
    String selected;
    boolean ch;



    LanguageListAdapter dataAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        final ListView listView = (ListView) findViewById(R.id.langlist);
        new LanguageDownloader(Filter.this, moviesUrlAddress, listView).execute();

        apply1 = (Button) findViewById(R.id.apply);
        apply1.setEnabled(false);

        apply1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Check> checks = dataAdapter.checks;

                for(int i=0;i<checks.size();i++)
                {
                    Check check = checks.get(i);

                    if(check.isSelected())
                    {
                        Intent in=new Intent(Filter.this,NowShowing_movies.class);
                        in.putExtra("Array_languages",check.getName());
                        startActivity(in);
                       Log.d("ghb" , check.getName());
                    }
                }





            }
        });

        //
        //  checkBoxData=new ArrayList<String>(Arrays.asList(myStrings));

        //checkBoxNumberListView.setAdapter(new CheckBoxListViewAdapter(EcConferenceNumber.this, adapter, checkBoxData));

        //System.out.println(""+checkBoxData);


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


    private class LanguageDownloader extends AsyncTask<Void, Void, String> {

        Context c;
        String moviesUrlAddress;
        ListView listView1;


        private LanguageDownloader(Filter c, String moviesUrlAddress, ListView listView) {
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
                LanguageDataParser parser = new LanguageDataParser(c, listView1, s);
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

    private class LanguageDataParser extends AsyncTask<Void, Void, Integer> {
        private final ListView listView1;
        Context c;

        String jsonData;

        ArrayList<Check> checks = new ArrayList<>();

        private LanguageDataParser(Context c, ListView listView1, String jsonData) {
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

                dataAdapter = new LanguageListAdapter(c, checks);
                listView1.setAdapter(dataAdapter);
            }
        }

        private int parseData() {
            try {
                JSONArray moviesArray = new JSONArray(jsonData);
                JSONObject moviesObject = null;
                checks.clear();
                Check mySQLDataBase;
                for (int i = 0; i < moviesArray.length(); i++) {
                    moviesObject = moviesArray.getJSONObject(i);
                    //   Log.d("result movies response: ", "> " + moviesObject);

                    String movieslanguage = moviesObject.getString("MLanguage");


                    mySQLDataBase = new Check();

                    mySQLDataBase.setName(movieslanguage);

                    checks.add(mySQLDataBase);

                }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    private class LanguageListAdapter extends BaseAdapter {

        Context c;
        ArrayList<Check> checks;
        LayoutInflater inflater;
        public ArrayList<Check> mySQLDataBase;
        //  public ArrayList<Check> countryList;


        private LanguageListAdapter(Context c, ArrayList<Check> mySQLDataBases) {
            this.c = c;
            this.checks = mySQLDataBases;
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return checks.size();
        }

        @Override
        public Object getItem(int position) {
            return checks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.language_list, parent, false);
            }

            // final TextView movielanguage1 =(TextView) convertView.findViewById(R.id.langtext);
            //  movielanguage1.setSelected(true);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    apply1.setEnabled(true);
                    CheckBox cb = (CheckBox) v;
                    Check ch = (Check) cb.getTag();
                    Toast.makeText(getApplicationContext(),
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                    ch.setSelected(cb.isChecked());
                    Log.d("cc", String.valueOf(cb.isChecked()));
                }
            });

            //BIND DATA
            final Check ch = (Check) this.getItem(position);

            final String movielanguage = ch.getName();


            //   holder.code.setText(" (" +  country.getCode() + ")");
            checkBox.setText(ch.getName());
            checkBox.setChecked(ch.isSelected());
            Log.d("cc1", String.valueOf(checks.get(position).isSelected()));
            checkBox.setTag(ch);


            return convertView;
        }
    }
}


/*
          checkBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {
                        ch = Boolean.parseBoolean(String.valueOf(((CheckBox) v).isChecked()));
                        apply1.setEnabled(true);
                        //  Log.d("checkbox_statearray",checkBox.getText().toString() );
                      //  Toast.makeText(Filter.this, checkBox.getText().toString(), Toast.LENGTH_SHORT).show();
                        //  vendor += CheckBox.getText();

                       // list.add(checkBox.getTag().toString());
                       // Log.d("checkbox_statearray", String.valueOf(list));


                        checkBox.setOnCheckedChangeListener(myCheckChangList);
                        checkBox.setTag(position);
Log.d("Po" , String.valueOf(position));


                    } else {

                    }
                }
            });*/


 /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        apply1.setEnabled(true);
                        list.add(checkBox.getText().toString());
              //list=checkBox.getText().toString();
                        Log.d("Selected string", String.valueOf(list));
                        checkBox.setTag(position);
                        Log.d("Po" , String.valueOf(position));
                    }else{

                    }


                }

            });
*/





