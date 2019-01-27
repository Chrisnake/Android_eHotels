package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

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

    }
}
