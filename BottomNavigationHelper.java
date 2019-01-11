package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;


public class BottomNavigationHelper
{
    public static void enableNavigation(final Context context, BottomNavigationView view)
    {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.navigation_home:
                        Intent HomeIntent = new Intent(context, HomePage.class);
                        context.startActivity(HomeIntent);
                        return true;

                    case R.id.navigation_search:
                        Intent SearchIntent = new Intent(context, SearchPage.class);
                        context.startActivity(SearchIntent);
                        return true;

                    case R.id.navigation_profile:
                        Intent ProfileIntent = new Intent(context, ProfilePage.class);
                        context.startActivity(ProfileIntent);
                        return true;
                }
                return false;
            }
        });
    }
}
