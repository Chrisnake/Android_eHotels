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
    private String userKey;
    protected List<UserBookings> bookingsList;

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
        getKey();
        findBookings();
        initImageBitmaps();
    }

    protected void findBookings() //Finds bookings made by the user and place each object in arraylist of userBookings
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query key = reference.child("Bookings").orderByChild("userKey").equalTo("-LXKqTjvdcu1nYEfa3Um"); //Finds children userKey and checks if they are equal to userkey.
        key.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot datas: dataSnapshot.getChildren())
                {
                    bookingsList = new ArrayList<>();

                    String hotel = (String) datas.child("Hotel").getValue();
                    String price = (String) datas.child("Price").getValue();
                    String dateIn = (String) datas.child("dateIn").getValue();
                    String dateOut = (String) datas.child("dateOut").getValue();
                    String roomType = (String) datas.child("roomType").getValue();

                    UserBookings userBookings = new UserBookings();
                    userBookings.setHotel(hotel);
                    userBookings.setPrice(price);
                    userBookings.setDateIn(dateIn);
                    userBookings.setDateOut(dateOut);
                    userBookings.setRoomType(roomType);
                    bookingsList.add(userBookings);

                    Toast.makeText(SuccessfulProfilePage.this, userBookings.getPrice(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

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
                    //Toast.makeText(SuccessfulProfilePage.this, keys, Toast.LENGTH_LONG).show();
                    userKey = keys;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
    private void initImageBitmaps()
    {
        //TODO: Learn how to search user key in bookings database to find all bookings that the user has made.
        //TODO: After finding all their bookings, add mNames from their hotel fields and add imageURLs depending on which hotel they have booked.

        mImageUrls.add("https://media-cdn.tripadvisor.com/media/photo-s/06/4b/0b/d9/london-bridge-hotel.jpg");
        mNames.add("London Marylebone");
        mImageUrls.add("https://doubletree3.hilton.com/resources/media/dt/LONLKDI/en_US/img/shared/full_page_image_gallery/main/HL_exterior_677x380_FitToBoxSmallDimension_Center.jpg");
        mNames.add("London Paddington");
        mImageUrls.add("https://doubletree3.hilton.com/resources/media/dt/LONLKDI/en_US/img/shared/full_page_image_gallery/main/HL_exterior_677x380_FitToBoxSmallDimension_Center.jpg");
        mNames.add("London Paddington");
        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");
        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");
        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");
        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");
        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");
        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");

        initRecyclerView();
    }

    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
