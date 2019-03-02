package com.example.android.ehotelsapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BookingConfirmedPage extends AppCompatActivity
{
    protected String hotelName, roomType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed_page);
        TextView hotelText = findViewById(R.id.requestTypeText);
        hotelName = getIntent().getStringExtra("hotel_name");
        roomType = getIntent().getStringExtra("hotel_room");
        hotelText.setText(hotelName);
        updateRoomAvailability(getIntent().getStringExtra("hotel_availability"));
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

    protected void updateRoomAvailability(String roomsLeft) //Updates database on the amount of room availabilities left.
    {
        int numberooms = Integer.parseInt(roomsLeft) - 1; //Take away one from the number of rooms left to confirm the booking.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Hotels").child(hotelName).child(roomType);
        ref.setValue(numberooms);
    }
}
