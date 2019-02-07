package com.example.android.ehotelsapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class BookingConfirmedPage extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed_page);
        TextView hotelText = findViewById(R.id.hotelSuccessTitle);
        hotelText.setText(getIntent().getStringExtra("hotel_name"));

        animation();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent BookingsIntent = new Intent (BookingConfirmedPage.this, SuccessfulProfilePage.class);
                startActivity(BookingsIntent);
            }

        }, 2900L);

    }

    protected void animation()
    {
        ImageView completeImage = findViewById(R.id.completeImage);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim);
        completeImage.setAnimation(animation);
    }
}
