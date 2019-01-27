package com.example.android.ehotelsapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import me.relex.circleindicator.CircleIndicator;

public class LondonMarylebone extends AppCompatActivity
{
    private Context mContext = LondonMarylebone.this;
    private static final int ACTIVITY_NUM = 1;
    public static ArrayAdapter arrayAdapter;
    protected ListView roomList;
    protected Button checkIn, checkOut, bookButton;
    protected DatePickerDialog.OnDateSetListener dateSetListener_in;
    protected DatePickerDialog.OnDateSetListener dateSetListener_out;
    protected int dateIn, dateOut, monthIn, monthOut;
    protected int basePrice = 0;
    public String hotel = "Hotel: London Marylebone";
    public String room = "";
    public String finalCheckIn = "";
    public String finalCheckOut = "";
    protected int Price = 20;
    protected ViewPager viewpager;
    protected ViewPagerAdapter viewPagerAdapter;
    private Integer[] hotelImages = {R.drawable.london_marylebone_kitchen, R.drawable.london_marylebone, R.drawable.london_marylebone_lobby};
    private ArrayList<Integer> imagesArray = new ArrayList<Integer>();


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
        setContentView(R.layout.activity_london_marylebone);
        roomList = findViewById(R.id.roomlistView);
        setupBottomNavigation();
        dateChanger();
        confirmBooking();
        slidingImages();
        hotelLogic();
    }

    protected void dateChanger() //Allows the user to input their check in and check out dates.
    {
        //Connecting XML buttons and text to code.
        checkIn = findViewById(R.id.checkindateButton);
        checkOut = findViewById(R.id.checkoutdateButton);
        final TextView displayInDate = findViewById(R.id.checkinText);
        final TextView displayOutDate = findViewById(R.id.checkoutText);
        final TextView priceView = findViewById(R.id.hotelpriceText);

        //DATA SEGMENT CODE: Creating datelistener to listen to dates.
        dateSetListener_in = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1; //January starts at 0.
                String date = day + "/" + month + "/" + year;
                displayInDate.setText(date);
                finalCheckIn = date;
                dateIn = day;
                monthIn = month;
            }
        };

        dateSetListener_out = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                Price = basePrice;
                int daysDifference;
                month = month + 1; //January starts at 0.
                String date = day + "/" + month + "/" + year;
                finalCheckOut = date;
                monthOut = month;
                displayOutDate.setText(date);

                dateOut = day;
                if(displayInDate != null) //If the user has inputted a check in date.
                {
                    daysDifference = dateOut - dateIn;
                    Price = Price * daysDifference;
                    priceView.setText("£" + Price);
                    Animation animation = AnimationUtils.loadAnimation(LondonMarylebone.this, R.anim.fadein);
                    priceView.setAnimation(animation);
                }
            }
        };

        checkOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calender = Calendar.getInstance();
                int inYear = calender.get(Calendar.YEAR);
                int inMonth = calender.get(Calendar.MONTH);
                int inDay = calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(LondonMarylebone.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener_out, inYear, inMonth, inDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        checkIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH);
                int day = calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(LondonMarylebone.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener_in, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    protected void confirmBooking()
    {
        bookButton = findViewById(R.id.bookButton);
        bookButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!ProfilePage.isLoggedIn)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Please create an account or log in before you book a room.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(dateIn >= dateOut)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Please check out atleast one day after you check in.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(!room.equals("") && !finalCheckIn.equals("") && !finalCheckOut.equals("")) //Error handling for users that press book without any acceptable details.
                {
                    Intent ConfirmIntent = new Intent(LondonMarylebone.this, ConfirmBookingPage.class);
                    ConfirmIntent.putExtra("Hotel", hotel);
                    ConfirmIntent.putExtra("Room", room);
                    ConfirmIntent.putExtra("CheckIn", "Check In: " + finalCheckIn);
                    ConfirmIntent.putExtra("CheckOut", "Check Out: " + finalCheckOut);
                    ConfirmIntent.putExtra("Price", "£" + Price);
                    startActivity(ConfirmIntent);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Please select the room type, check in date and check out date before booking.", Toast.LENGTH_LONG);
                    toast.show();
                }
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
        viewPagerAdapter = new ViewPagerAdapter(LondonMarylebone.this, imagesArray);
        viewpager.setAdapter(viewPagerAdapter);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
    }

    protected void hotelLogic()
    {
        //Arraylist containing the room types available for the hotel.
        final ArrayList<String> hotelList = new ArrayList<>();
        hotelList.add("    Single Room - 1 Adult");
        hotelList.add("    Double Room - 2 Adults");
        hotelList.add("    Family Room - 2 Adults, 2 Children");
        hotelList.add("    Large Family Room - 3 Adults, 3 Children");
        hotelList.add("    Couple Duplex - 2 Adults");

        // Initialize an array adapter
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
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

                // return the view
                return item;
            }
        };
        roomList.setAdapter(arrayAdapter);
        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() //Set an itemonclick listener for the list view.
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) //Once the user has chosen their roomtype, change the base price depending on the room.
            {
                switch(i)
                {
                    case 0: basePrice = 30; //Single Room Base Price
                        room = "Room: Single Room";
                        break;
                    case 1: basePrice = 60; //Single Room Base Price
                        room = "Room: Double Room";
                        break;
                    case 2: basePrice = 80; //Family Room Base Price
                        room = "Room: Family Room";
                        break;
                    case 3: basePrice = 120; //Large Family Room Base Price
                        room = "Room: Large Family Room";
                        break;
                    case 4: basePrice = 120; //Couple Duplex Room Base Price
                        room = "Room: Couple Duplex Room";
                        break;
                }
            }
        });
    }
}
