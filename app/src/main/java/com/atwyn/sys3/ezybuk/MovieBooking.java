package com.atwyn.sys3.ezybuk;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

import com.mancj.slideup.SlideUp;

public class MovieBooking extends Main2Activity implements NavigationView.OnNavigationItemSelectedListener {
    VideoView simpleVideoView;
    MediaController mediaControls;
    Button book1;

    Spinner sp1, sp2, sp3;
    ImageButton im1;
    private DrawerLayout drawer;
    private ViewPager viewPager;

    private SlideUp slideUp;
    private View dim;
    private View sliderView;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.backbutton);

            //  actionbar.setTitle(R.string.title_activity_settings);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MovieBooking.this, Main2Activity.class);
                    //startActivity(in);
                    finish();
                }
            });

            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.location);
            toolbar.setOverflowIcon(drawable);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);

            toggle.syncState();

            sp1 = (Spinner) findViewById(R.id.spinner);
            sp2 = (Spinner) findViewById(R.id.spinner2);
            sp3 = (Spinner) findViewById(R.id.spinner3);
            book1 = (Button) findViewById(R.id.button);
            // Find your VideoView in your video_main.xml layout

            im1 = (ImageButton) findViewById(R.id.imageButton);
            im1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MovieBooking.this, FullTrailer.class);
                    startActivity(in);
                }
            });

            book1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(MovieBooking.this, SeatReservation.class);
                    startActivity(in);
                }
            });


        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //add your switch statement
        //noinspection SimplifiableIfStatement
        if (id == R.id.h1) {
            Intent in = new Intent(MovieBooking.this, Search.class);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fr1) {

            viewPager.setCurrentItem(0);
        } else if (id == R.id.fr2) {

            viewPager.setCurrentItem(1);

//        } else if (id == R.id.go) {
//
//            Intent intent = new Intent(this, DesActivity.class);
//            intent.putExtra("string", "Go to other Activity by NavigationView item cliked!");
//            startActivity(intent);
//        }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
