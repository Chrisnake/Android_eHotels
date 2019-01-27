package com.example.android.ehotelsapp;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

public class SearchPage extends AppCompatActivity
{
    private Context mContext = SearchPage.this;
    private static final int ACTIVITY_NUM = 1;
    public static ArrayAdapter arrayAdapter;
    public static String homeQuery = "";
    protected SearchView searchView;

    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void implementSearch()
    {
        //TODO: Replace the data below with real database data from the users bookings.
        ListView userBookings = findViewById(R.id.ListView);
        final ArrayList<String> hotelList = new ArrayList<>();
        hotelList.add("London Marylebone");
        hotelList.add("London Euston");
        hotelList.add("London Hammersmith");
        hotelList.add("London Canning Town");
        hotelList.add("Birmingham Moor Street");
        hotelList.add("Birmingham Central");
        hotelList.add("Manchester Central");
        hotelList.add("Manchester Fox Street");
        hotelList.add("London Stratford");
        hotelList.add("London Picadilly");
        hotelList.add("London Kings Cross");
        hotelList.add("London Baker Street");
        hotelList.add("London Kensington");
        hotelList.add("London Westminister");
        Collections.sort(hotelList);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, hotelList)
        {
            Typeface mTypeface = ResourcesCompat.getFont(getContext(), R.font.roboto_light);
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                // Cast the list view each item as text view
                TextView item = (TextView) super.getView(position, convertView, parent);

                // Set the typeface/font for the current item
                item.setTypeface(mTypeface);

                // Set the list view item's text color
                item.setTextColor(Color.parseColor("#191919"));

                // Set the item text style to bold
                item.setTypeface(item.getTypeface(), Typeface.NORMAL);

                // Change the item text size
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                // return the view
                return item;
            }
        };

        userBookings.setAdapter(arrayAdapter);
        userBookings.setOnItemClickListener(new AdapterView.OnItemClickListener() //Set an itemonclick listener for the list view.
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String clickedHotel = hotelList.get(i).replaceAll("\\s",""); //Taking the white space out of the hotel search name.
                String packageName = "com.example.android.ehotelsapp." + clickedHotel;
                try //Fast, efficient way to load the hotel activity that the user clicked.
                {
                    Class<?> c = Class.forName(packageName);
                    Intent intent = new Intent(SearchPage.this, c);
                    startActivity(intent);
                }
                catch (ClassNotFoundException ignored) { }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        setupBottomNavigation();
        implementSearch();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.SearchView);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });

        if(!homeQuery.equals("")) //If there was an input from the homepage search bar, then set the query as that string.
        {
            searchView.setQuery(homeQuery, true);
        }

        return super.onCreateOptionsMenu(menu);
    }
}
