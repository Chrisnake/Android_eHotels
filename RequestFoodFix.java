package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;

public class RequestFoodFix extends AppCompatActivity
{
    private Context mContext = RequestFoodFix.this;
    private static final int ACTIVITY_NUM = 2;
    private Integer[] hotelImages = {R.drawable.hotel_food_1, R.drawable.hotel_food_2, R.drawable.hotel_food_3};
    private ArrayList<Integer> imagesArray = new ArrayList<Integer>();
    private String userKey;
    protected ViewPager viewpager;
    protected ViewPagerAdapter viewPagerAdapter;
    protected String requestType = "Food";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_food_fix);
        setupBottomNavigation();
        getKey();
        slidingImages();

        Spinner starter = findViewById(R.id.starterSpinner);
        Spinner main = findViewById(R.id.mainSpinner);
        Spinner dessert = findViewById(R.id.dessertsSpinner);
        Spinner drinks = findViewById(R.id.drinksSpinner);

        ArrayAdapter starterAdapter = new ArrayAdapter(this, R.layout.spinner_item, Food.convertToArray(YourRoom.starterArray));
        starterAdapter.setDropDownViewResource(R.layout.spinner_item);
        starter.setAdapter(starterAdapter);

        ArrayAdapter mainAdapter = new ArrayAdapter(this, R.layout.spinner_item, Food.convertToArray(YourRoom.mainArray));
        mainAdapter.setDropDownViewResource(R.layout.spinner_item);
        main.setAdapter(mainAdapter);

        ArrayAdapter dessertsAdapter = new ArrayAdapter(this, R.layout.spinner_item, Food.convertToArray(YourRoom.dessertArray));
        dessertsAdapter.setDropDownViewResource(R.layout.spinner_item);
        dessert.setAdapter(dessertsAdapter);

        ArrayAdapter drinksAdapter = new ArrayAdapter(this, R.layout.spinner_item, Food.convertToArray(YourRoom.drinksArray));
        drinksAdapter.setDropDownViewResource(R.layout.spinner_item);
        drinks.setAdapter(drinksAdapter);

        updateOrder(starter, main, dessert, drinks);
    }

    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    protected void getKey() //Returns the userKey depending on the email address of thue user.
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query key = reference.child("Users").orderByChild("userEmail").equalTo(ProfilePage.getUserEmail);
        key.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    String keys = datas.getKey();
                    userKey = keys;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void updateOrder(final Spinner starter, final Spinner main, final Spinner dessert, final Spinner drinks)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity.
        final DatabaseReference ref = database.getReference("Requests");
        Button confirm = findViewById(R.id.towelsButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String starterOrder = starter.getSelectedItem().toString();
                String mainOrder = main.getSelectedItem().toString();
                String dessertOrder = dessert.getSelectedItem().toString();
                String drinksOrder = drinks.getSelectedItem().toString();
                String hotel = getIntent().getStringExtra("image_name");

                if (starterOrder.equals("No Starters Selected")) {
                    starterOrder = "";
                }
                if (mainOrder.equals("No Main Selected")) {
                    mainOrder = "";
                }
                if (dessertOrder.equals("No Desserts Selected")) {
                    dessertOrder = "";
                }
                if (drinksOrder.equals("No Drinks Selected")) {
                    drinksOrder = "";
                }

                String finalOrder = starterOrder + " " + mainOrder + " " + dessertOrder + " " + drinksOrder + " ";
                HashMap<String, String> requestData = new HashMap<String, String>(); //Putting data in a hashmap with key and values.
                requestData.put("userKey", userKey);
                requestData.put("requestType", requestType);
                requestData.put("requestInformation", finalOrder);
                requestData.put("roomNumber", getIntent().getStringExtra("booking_roomnumber"));
                requestData.put("hotel", hotel);
                ref.push().setValue(requestData).addOnCompleteListener(new OnCompleteListener<Void>()  //Pushing the data with respect to oncompletelistener for errors.
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {
                            Toast.makeText(RequestFoodFix.this, "Oops, please contact user support...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Intent ConfirmIntent = new Intent(RequestFoodFix.this, RequestConfirmed.class);
                ConfirmIntent.putExtra("request_type", "Your " + requestType);
                startActivity(ConfirmIntent);
            }
        });
    }

    protected void slidingImages()
    {
        for(int i = 0; i < hotelImages.length; i++)
        {
            imagesArray.add(hotelImages[i]);
        }
        //Adding viewpager for the swiping hotel images.
        viewpager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(RequestFoodFix.this, imagesArray);
        viewpager.setAdapter(viewPagerAdapter);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
    }
}
