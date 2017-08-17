package com.atwyn.sys3.ezybuk;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class SeatReservation extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Button book;
    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;
    public Bitmap seatIcon;
    public Bitmap seatSelect;
    WebView WebViewWithCSS;
    String showdate,showtime;
    Integer movieid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_reservation);
        Intent i = this.getIntent(); //
        movieid = i.getExtras().getInt("Movie_Id");
        showdate=i.getExtras().getString("Show_Date");
        showtime=i.getExtras().getString("Show_Time");

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
            seatIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.seat);
            seatSelect = BitmapFactory.decodeResource(this.getResources(), R.mipmap.location);
            totalSeat(100);

            gridView = (GridView) findViewById(R.id.gridView1);
            customGridAdapter = new CustomGridViewAdapter(this, R.layout.seat_gridview, gridArray);
            gridView.setAdapter(customGridAdapter);
            gridView.setOnItemClickListener(this);
        }
    }

    public void totalSeat(int n)
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

      /*  WebViewWithCSS = (WebView)findViewById(R.id.webView1);

        WebSettings webSetting = WebViewWithCSS.getSettings();
        webSetting.setJavaScriptEnabled(true);

        WebViewWithCSS.setWebViewClient(new WebViewClient());
        WebViewWithCSS.loadUrl("file:///android_asset/ex.html");
    }

    private class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

}*/