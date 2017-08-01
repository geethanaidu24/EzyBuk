package com.atwyn.sys3.ezybuk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

public class Rating extends AppCompatActivity {
    String movieposter,moviebigposter;
    String finalurl11;

    ImageView imageView;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnRate,btnRateLater;
    String ratedValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Intent i = this.getIntent(); //
        movieposter = i.getExtras().getString("Movie_poster");
        moviebigposter=i.getExtras().getString("Movie_BigPosterurl");


        finalurl11 = Config.mainUrlAddress + moviebigposter;
        imageView=(ImageView)findViewById(R.id.rateid) ;
        com.bumptech.glide.Glide.with(this)
                .load(finalurl11)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)

                .crossFade()
                .into(imageView);

        btnRateLater=(Button)findViewById(R.id.button6) ;
        btnRateLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Rating.this,ScrollingActivity.class);
                finish();
            }
        });
        addListenerOnRatingBar();
        addListenerOnButton();
    }
    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.simpleRatingBar);
        txtRatingValue = (TextView) findViewById(R.id.imageView6);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtRatingValue.setText(String.valueOf(rating));
                ratedValue = String.valueOf(ratingBar.getRating());
                txtRatingValue.setText("Rate  : "
                        + ratedValue + "/5.");
            }
        });


    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.simpleRatingBar);
        btnRate = (Button) findViewById(R.id.button5);

        //if click on me, then display the current rating value.
        btnRate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(Rating.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();

            }

        });

    }
}

