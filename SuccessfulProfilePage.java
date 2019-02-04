package com.example.android.ehotelsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuccessfulProfilePage extends AppCompatActivity
{
    private Context mContext = SuccessfulProfilePage.this;
    private static final int ACTIVITY_NUM = 2;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    public static ArrayList<UserBookings> bookingsList = new ArrayList<>();
    protected String userKey;

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
        setContentView(R.layout.activity_successful_profile_page);
        setupBottomNavigation();
        readKey(new MyCallback()
        {
            @Override
            public void onCallback(String value)
            {
                userKey = value;
                findBookings(value);
                Log.i("List test", "Value:  " + value);
                Log.i("List test", "userKey:  " + value);
            }
        });

    }

    protected void findBookings(final String userKey) //Finds bookings made by the user and place each object in arraylist of userBookings
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query key = reference.child("Bookings").orderByChild("userKey").equalTo(userKey); //Finds children userKey and checks if they are equal to userkey.
        key.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    String hotel = (String) datas.child("Hotel").getValue();
                    String price = (String) datas.child("Price").getValue();
                    String dateIn = (String) datas.child("dateIn").getValue();
                    String dateOut = (String) datas.child("dateOut").getValue();
                    String roomType = (String) datas.child("roomType").getValue();

                    UserBookings userBookings = new UserBookings(userKey, hotel, price, dateIn, dateOut, roomType);

                    Log.i("List test", "ID:  " + userBookings.getUserID());
                    Log.i("List test", "Hotel:  " + userBookings.getHotel());
                    Log.i("List test", "Price: " + userBookings.getPrice());
                    Log.i("List test", "Date In: " + userBookings.getDateIn());
                    Log.i("List test", "Date Out: " + userBookings.getDateOut());
                    Log.i("List test", "Room Type: " + userBookings.getRoomType());
                    bookingsList.add(userBookings);

                    //TODO: Find a way to use link an image to the hotel depending on its name.

                    mImageUrls.add("https://media-cdn.tripadvisor.com/media/photo-s/06/4b/0b/d9/london-bridge-hotel.jpg");
                    mNames.add(userBookings.getHotel());
                }

                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initRecyclerView()
    {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void readKey(final MyCallback myCallback)
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
                    String key = datas.getKey();
                    myCallback.onCallback(key);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    public interface MyCallback
    {
        void onCallback(String userKey);
    }
}
