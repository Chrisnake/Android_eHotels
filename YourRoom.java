package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class YourRoom extends AppCompatActivity
{
    private Context mContext = YourRoom.this;
    private static final int ACTIVITY_NUM = 2;
    protected ViewPager viewpager;
    public static ArrayList<Food> dessertArray = new ArrayList<>();
    public static ArrayList<Food> starterArray = new ArrayList<>();
    public static ArrayList<Food> mainArray = new ArrayList<>();
    public static ArrayList<Food> drinksArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_room);
        setupBottomNavigation();
        initialisebuttons();
        readDesserts(getIntent().getStringExtra("image_name"));
        readStarter(getIntent().getStringExtra("image_name"));
        readMain(getIntent().getStringExtra("image_name"));
        readDrinks(getIntent().getStringExtra("image_name"));
    }

    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void initialisebuttons()
    {
        Button foodButton = findViewById(R.id.foodButton);
        Button towelsButton = findViewById(R.id.towelsButton);
        Button donotdisturbButton = findViewById(R.id.disturbButton);
        Button problemButton = findViewById(R.id.reportButton);

        foodButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String bookingRoomNumber = getIntent().getStringExtra("booking_roomnumber");
                String bookingHotel = getIntent().getStringExtra("image_name");
                Intent nextIntent = new Intent(YourRoom.this, RequestFoodFix.class);
                nextIntent.putExtra("booking_roomnumber", bookingRoomNumber);
                nextIntent.putExtra("image_name", bookingHotel);
                startActivity(nextIntent);
            }
        });

        towelsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String bookingRoomNumber = getIntent().getStringExtra("booking_roomnumber");
                String bookingHotel = getIntent().getStringExtra("image_name");
                Intent nextIntent = new Intent(YourRoom.this, RequestTowels.class);
                nextIntent.putExtra("booking_roomnumber", bookingRoomNumber);
                nextIntent.putExtra("image_name", bookingHotel);
                startActivity(nextIntent);
            }
        });

        problemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String bookingRoomNumber = getIntent().getStringExtra("booking_roomnumber");
                String bookingHotel = getIntent().getStringExtra("image_name");
                Intent nextIntent = new Intent(YourRoom.this, RequestReport.class);
                nextIntent.putExtra("booking_roomnumber", bookingRoomNumber);
                nextIntent.putExtra("image_name", bookingHotel);
                startActivity(nextIntent);
            }
        });

        donotdisturbButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String bookingRoomNumber = getIntent().getStringExtra("booking_roomnumber");
                String bookingHotel = getIntent().getStringExtra("image_name");
                Intent nextIntent = new Intent(YourRoom.this, RequestDisturb.class);
                nextIntent.putExtra("booking_roomnumber", bookingRoomNumber);
                nextIntent.putExtra("image_name", bookingHotel);
                startActivity(nextIntent);
            }
        });
    }

    public void readDesserts(String userHotel)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity
        final DatabaseReference ref = database.getReference("RestaurantOrders").child(userHotel).child("Desserts");
        ValueEventListener eventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Food empty = new Food("No Desserts Selected", Long.valueOf(0));
                dessertArray.add(empty);
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String dessertName = ds.getKey();
                    Long dessertValue = (Long) ds.getValue();
                    Food dessert = new Food(dessertName, dessertValue); //Add the type of food and its cost to a Food arraylist.
                    dessertArray.add(dessert);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    public void readStarter(String userHotel)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity
        final DatabaseReference ref = database.getReference("RestaurantOrders").child(userHotel).child("Starters");
        ValueEventListener eventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Food empty = new Food("No Starters Selected", Long.valueOf(0));
                starterArray.add(empty);
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String starterName = ds.getKey();
                    Long starterValue = (Long) ds.getValue();
                    Food starter = new Food(starterName, starterValue); //Add the type of food and its cost to a Food arraylist.
                    starterArray.add(starter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    public void readMain(String userHotel)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity
        final DatabaseReference ref = database.getReference("RestaurantOrders").child(userHotel).child("Main");
        ValueEventListener eventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Food empty = new Food("No Main Selected", Long.valueOf(0));
                mainArray.add(empty);
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String mainName = ds.getKey();
                    Long mainValue = (Long) ds.getValue();
                    Food main = new Food(mainName, mainValue); //Add the type of food and its cost to a Food arraylist.
                    mainArray.add(main);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }

    public void readDrinks(String userHotel)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity
        final DatabaseReference ref = database.getReference("RestaurantOrders").child(userHotel).child("Drinks");
        ValueEventListener eventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Food empty = new Food("No Drinks Selected", Long.valueOf(0));
                drinksArray.add(empty);
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    String drinksName = ds.getKey();
                    Long drinksValue = (Long) ds.getValue();
                    Food drinks = new Food(drinksName, drinksValue); //Add the type of food and its cost to a Food arraylist.
                    drinksArray.add(drinks);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);
    }
}
