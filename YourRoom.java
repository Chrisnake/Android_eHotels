package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class YourRoom extends AppCompatActivity
{
    private Context mContext = YourRoom.this;
    private static final int ACTIVITY_NUM = 2;
    protected ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_room);
        setupBottomNavigation();
        initialisebuttons();
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
                Intent nextIntent = new Intent(YourRoom.this, RequestFoodFix.class);
                startActivity(nextIntent);
            }
        });

        towelsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent nextIntent = new Intent(YourRoom.this, RequestTowels.class);
                startActivity(nextIntent);
            }
        });

        problemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent nextIntent = new Intent(YourRoom.this, RequestReport.class);
                startActivity(nextIntent);
            }
        });

        donotdisturbButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent nextIntent = new Intent(YourRoom.this, RequestDisturb.class);
                startActivity(nextIntent);
            }
        });
    }
}
