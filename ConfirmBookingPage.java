package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class ConfirmBookingPage extends AppCompatActivity
{
    private Context mContext = ConfirmBookingPage.this;
    private static final int ACTIVITY_NUM = 1;
    protected String hotelConfirm, roomConfirm, inConfirm, outConfirm, priceConfirm, roomsLeft;
    private String userKey;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking_page);
        setupBottomNavigation();

        hotelConfirm = getIntent().getStringExtra("Hotel");
        roomConfirm = getIntent().getStringExtra("Room");
        inConfirm = getIntent().getStringExtra("CheckIn");
        outConfirm = getIntent().getStringExtra("CheckOut");
        priceConfirm = getIntent().getStringExtra("Price");
        hotelConfirm = getIntent().getStringExtra("Hotel");
        roomsLeft = getIntent().getStringExtra("Room_Availability");
        getKey();
        getDetails();
        confirmDetails();
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

        hotel.setText("Hotel: " + hotelConfirm);
        room.setText("Room: " + roomConfirm);
        checkin.setText("Check In: " + inConfirm);
        checkout.setText("Check Out: " + outConfirm);
        price.setText(priceConfirm);
    }

    protected void confirmDetails()
    {
        Random rand = new Random();
        final String qrID = String.valueOf(rand.nextInt(100000) + 1); //Generate a random QRID for the QR code.
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity.
        final DatabaseReference ref = database.getReference("Bookings");
        Button confirm = findViewById(R.id.confirmButton);
        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HashMap<String, String> bookingsData = new HashMap<String, String>(); //Putting data in a hashmap with key and values.
                bookingsData.put("userKey", userKey);
                bookingsData.put("Hotel", hotelConfirm);
                bookingsData.put("roomType", roomConfirm);
                bookingsData.put("dateIn", inConfirm);
                bookingsData.put("dateOut", outConfirm);
                bookingsData.put("Price", priceConfirm);
                bookingsData.put("idQR", qrID);
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
                ConfirmIntent.putExtra("hotel_name", hotelConfirm);
                ConfirmIntent.putExtra("hotel_availability", roomsLeft);
                ConfirmIntent.putExtra("hotel_room", roomConfirm);
                startActivity(ConfirmIntent);
            }
        });
    }

    protected void getKey() //Returns the userKey depending on the email address of thue user.
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query key = reference.child("Users").orderByChild("userEmail").equalTo(ProfilePage.getUserEmail);
        key.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot datas: dataSnapshot.getChildren())
                {
                    String keys = datas.getKey();
                    Toast.makeText(ConfirmBookingPage.this, keys, Toast.LENGTH_LONG).show();
                    userKey = keys;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
