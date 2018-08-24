package sma.tech.ma5doom.rest;
import sma.tech.ma5doom.R;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;

public class CarouselViewActivity extends AppCompatActivity {

    CarouselView carouselView;
    ArrayList<String> images ;
    String BASE_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel_view);
        Fresco.initialize(this);


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
        }

        Intent intent = getIntent();
        images = intent.getStringArrayListExtra("images");
        BASE_URL = intent.getStringExtra("url");
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {

                View v = getLayoutInflater().inflate(R.layout.imageview,null);
                Picasso.with(CarouselViewActivity.this).
                        load(BASE_URL+images.get(position)).placeholder(R.drawable.logo).
                        into((ImageView)v.findViewById(R.id.image));

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                return v;
            }
        });
        carouselView.setPageCount(images.size());

        /*carouselView.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View customView = getLayoutInflater().inflate(R.layout.item_cover, null);
                Picasso.with(CarouselViewActivity.this).
                        load(BASE_URL+images.get(position)).fit().placeholder(R.drawable.logo).
                        into((ImageView) customView.findViewById(R.id.image_cover));


                return customView;
            }
        });*/
    }


    ImageListener imageListener =new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {


            Picasso.with(CarouselViewActivity.this).
                    load(BASE_URL+images.get(position)).fit().placeholder(R.drawable.logo).
                    into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
    };
}