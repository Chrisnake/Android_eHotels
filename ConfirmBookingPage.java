package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ConfirmBookingPage extends AppCompatActivity
{
    private Context mContext = ConfirmBookingPage.this;
    private static final int ACTIVITY_NUM = 1;
    private String hotelConfirm, roomConfirm, inConfirm, outConfirm, priceConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking_page);
        setupBottomNavigation();
        getDetails();
        confirmDetails();
        hotelConfirm = getIntent().getStringExtra("Hotel");
        roomConfirm = getIntent().getStringExtra("Room");
        inConfirm = getIntent().getStringExtra("CheckIn");
        outConfirm = getIntent().getStringExtra("CheckOut");
        priceConfirm = getIntent().getStringExtra("Price");
    }

    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    protected void getDetails() //Gets details prior to booking button being pressed using putExtra after an intent.
    {
        TextView hotel = findViewById(R.id.hotelText);
        TextView room = findViewById(R.id.roomText);
        TextView checkin = findViewById(R.id.checkInText);
        TextView checkout = findViewById(R.id.checkOutText);
        TextView price = findViewById(R.id.priceText);

        hotel.setText(hotelConfirm);
        room.setText(roomConfirm);
        checkin.setText(inConfirm);
        checkout.setText(outConfirm);
        price.setText(priceConfirm);
    }

    protected void confirmDetails()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity.
        final DatabaseReference ref = database.getReference("Bookings");
        Button confirm = findViewById(R.id.confirmButton);
        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HashMap<String, String> bookingsData = new HashMap<String, String>(); //Putting data in a hashmap with key and values.
                bookingsData.put("userKey", hotelConfirm);
                bookingsData.put("Hotel", hotelConfirm);
                bookingsData.put("roomType", roomConfirm);
                bookingsData.put("dateIn", inConfirm);
                bookingsData.put("dateOut", outConfirm);
                bookingsData.put("Price", priceConfirm);
                ref.push().setValue(bookingsData).addOnCompleteListener(new OnCompleteListener<Void>()  //Pushing the data with respect to oncompletelistener for errors.
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {

                        }
                        else
                        {
                            Toast.makeText(ConfirmBookingPage.this, "Oops, please contact user support...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Intent ConfirmIntent = new Intent(ConfirmBookingPage.this, BookingConfirmedPage.class);
                startActivity(ConfirmIntent);
            }
        });
    }
}
