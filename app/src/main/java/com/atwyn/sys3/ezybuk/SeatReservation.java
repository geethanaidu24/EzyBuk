package com.atwyn.sys3.ezybuk;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SeatReservation extends AppCompatActivity /*implements AdapterView.OnItemClickListener */ {
    Button book;
    TextView toolbartext;
    GridView gridView;
    final static String SeatUrlAddress = Config.SeatLayout;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;
    public Bitmap seatIcon;
    public Bitmap seatSelect, seatEmpty;
    WebView WebViewWithCSS;
    String showdate, showtime, selecetedlanguage, selectedformat, movietitle,theatername;
    Integer movieid, screenid;
    Spinner mspin;
    int numcount, norows, nocolumns;
    final ArrayList<MySQLDataBase> mySQLDataBases = new ArrayList<>();
    String noseatnos, norownames;
    int count = 0;
    int lettercount = 0;
    WebView webview;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_reservation);
        Intent i = this.getIntent(); //
        movieid = i.getExtras().getInt("Movie_Id");
        movietitle = i.getExtras().getString("Title");
        showdate = i.getExtras().getString("Show_Date");
        showtime = i.getExtras().getString("Show_Time");
        selecetedlanguage = i.getExtras().getString("Selected_language");
        selectedformat = i.getExtras().getString("Selected_format");
            theatername = i.getExtras().getString("Theater_Name");
        screenid = i.getExtras().getInt("Screen_Id");
        Log.d("SCREEn_id", String.valueOf(screenid));

          //  Log.d("theater nameee",movietitle);

        toolbartext = (TextView) findViewById(R.id.tooltext1);
        toolbartext.setText(theatername);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(SeatReservation.this, ScrollingActivity.class);
                    //startActivity(in);
                    finish();
                }
            });

            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.location);
            toolbar.setOverflowIcon(drawable);

            book = (Button) findViewById(R.id.bookticket);
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(SeatReservation.this, Payment.class);
                    startActivity(in);
                }
            });
        }
            mspin=(Spinner) findViewById(R.id.spinner);
            Integer[] items = new Integer[]{1,2,3,4,6};
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
            mspin.setAdapter(adapter);

            webview=(WebView)findViewById(R.id.webView1);
        webview.setWebViewClient(new MyWebViewClient());
           // webview.getSettings().setBuiltInZoomControls(true);
          /*  webview.setInitialScale(1);

            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);*/
        openURL();
       /* seatIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.seat);
        seatSelect = BitmapFactory.decodeResource(this.getResources(), R.mipmap.location);
        seatEmpty = BitmapFactory.decodeResource(this.getResources(), R.mipmap.creditcard);

        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.seat_gridview, gridArray);
        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
*/
    }
    private void openURL() {
        webview.loadUrl(Config.SeatLayout+screenid);
        webview.requestFocus();
    }
}

   /* public void onStart() {
        super.onStart();
        BackTask4 bt = new BackTask4();
        bt.execute();



    }

    private class BackTask4 extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.SeatLayout + screenid);
                String h1 = Config.SeatLayout + screenid;
                Log.d("hello gettha", " >" + h1);
                org.apache.http.HttpResponse response = httpclient.execute(httppost);
                org.apache.http.HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                is.close();
                //result=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // parse json data
            try {


               *//* JSONArray ja = new JSONArray(result);
                JSONObject jo = null;
                mySQLDataBases.clear();
                MySQLDataBase mySQLDataBase;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    // add interviewee name to arraylist
                    int layoutid = jo.getInt("LayoutId");
                  norows = jo.getInt("Rows");
                   nocolumns = jo.getInt("Columns");
                    noseatnos = jo.getString("SeatNos");
                    norownames = jo.getString("RowNames");
                    mySQLDataBase = new MySQLDataBase();
                    mySQLDataBase.setLayoutId(layoutid);
                    mySQLDataBase.setRows(norows);
                    mySQLDataBase.setColumns(nocolumns);
                    mySQLDataBase.setSeatNos(noseatnos);
                    mySQLDataBase.setRowNames(norownames);
                    mySQLDataBases.add(mySQLDataBase);

                   Log.d("Rows", String.valueOf(norows));*//*

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
*//*
numcount=noseatnos.split(",").length;
            Log.d("Count no", String.valueOf(numcount));

            String s = norownames;
                       *//*
*//*  myList = new ArrayList<String>(Arrays.asList(s.split(",")));
                        Log.d("LIstll", String.valueOf(myList));*//**//*

            final String[] myList=s.split(",");
            Log.d("LIstll", String.valueOf(myList));

            for (int i = 0; i <= norows; i++)
            {
                if(myList[lettercount]=="GAP")
                {
                    gridArray.add(new Item(seatEmpty, "" + i));
                }
                else
                {

                }
                gridArray.add(new Item(seatIcon, "" + i));

            }

*//*

            }
        }
    }*/


            /*seatIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.seat);
            seatSelect = BitmapFactory.decodeResource(this.getResources(), R.mipmap.location);
            totalSeat(100);

            gridView = (GridView) findViewById(R.id.gridView1);
            customGridAdapter = new CustomGridViewAdapter(this, R.layout.seat_gridview, gridArray);
            gridView.setAdapter(customGridAdapter);
            gridView.setOnItemClickListener(this);*//*


   *//* public void totalSeat(int n)
    {
        for (int i = 1; i <= n; ++i)
        {
            gridArray.add(new Item(seatIcon, "" + i));

        }
    }

    public void seatSelected(int pos)
    {
        gridArray.remove(pos);
        gridArray.add(pos, new Item(seatSelect, ""));
        customGridAdapter.notifyDataSetChanged();
    }

    public void seatDeselcted(int pos)
    {

        gridArray.remove(pos);
        int i = pos + 1;
        gridArray.add(pos, new Item(seatIcon, "" + i));
        customGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        Item item = gridArray.get(position);
        Bitmap seatcompare = item.getImage();
        if (seatcompare == seatIcon)
        {
            seatSelected(position);
        }
        else
        {
            seatDeselcted(position);

        }

    }

}
            mspin=(Spinner) findViewById(R.id.spinner);
            Integer[] items = new Integer[]{1,2,3,4,6};
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
            mspin.setAdapter(adapter);

            WebViewWithCSS = (WebView) findViewById(R.id.webView1);

            WebSettings webSetting = WebViewWithCSS.getSettings();
            webSetting.setJavaScriptEnabled(true);

            WebViewWithCSS.setWebViewClient(new WebViewClient());
            WebViewWithCSS.loadUrl("file:///android_asset/seat-layout.html");
        }

        class WebViewClient extends android.webkit.WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }

    }
}
*/