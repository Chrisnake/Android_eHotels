package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class HomePage extends AppCompatActivity
{
    private Context mContext = HomePage.this;
    private static final int ACTIVITY_NUM = 0;
    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setupBottomNavigation();

        SearchView searchView = findViewById(R.id.searchViewHome);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Intent SearchIntent = new Intent (HomePage.this, SearchPage.class);
                SearchPage.homeQuery = query;
                startActivity(SearchIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                return false;
            }
        });

        updateRoomAvailabilities();
    }

    protected void updateRoomAvailabilities() //Goes through all user bookings and reviews if any of the bookings have passed the current date. If they have then add 1 to room availability.
    {
        //Get each individual child in the database, and store their key and values in a temporary arraylist.
        //Get the booking date out first, and check to see if it is past todays current day.
        //If the booking date out is past todays day, it is expired, thus get the hotel name, and then the room type and iterate it in a hashmap.
        //Then, delete that child to ensure it does not come up again to disrupt future results.
        //After going through all the possible bookings children, update the hotels and their respective roomtypes.

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH); //Gets the current date.
        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Query key = reference.child("Bookings");
        key.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ArrayList<UpdateRooms> updateRooms = new ArrayList<>();
                for (DataSnapshot user : dataSnapshot.getChildren())
                {
                    String dateOut = (String) user.child("dateOut").getValue();
                    String monthOut = dateOut.substring(3,5);
                    String dayOut = dateOut.substring(0,2); //Gets the day of the month of checkout.
                    String yearOut = dateOut.substring(6,10); //Gets the year of checkout date.

                    int monthoutInt = Integer.parseInt(monthOut);
                    int checkoutInt = Integer.parseInt(dayOut); //Converting it to an integer.
                    int yearoutInt = Integer.parseInt(yearOut);

                    Log.i("Current Date", currentDay + " " +  currentMonth + " " +  currentYear);
                    Log.i("Booking Date", "Day: " + dayOut + "Month: " + monthOut + " Year: " + yearOut);
                    if(checkoutInt < currentDay || monthoutInt < currentMonth || yearoutInt < currentYear) //If the user check out date is after the current day, the booking is finished, thus add one back to its child value.
                    {
                        Log.i("Expired Date", "Day: " + dayOut + "Month: " + monthOut + " Year: " + yearOut);
                        String hotel = (String) user.child("Hotel").getValue();
                        String roomType = (String) user.child("roomType").getValue();
                        UpdateRooms newUpdate = new UpdateRooms(hotel,roomType);
                        updateRooms.add(newUpdate);
                        user.getRef().removeValue();
                    }
                }
                UpdateRooms.updateRoomSpace(updateRooms);
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                throw databaseError.toException(); //don't ignore errors
            }
        });
    }
}
