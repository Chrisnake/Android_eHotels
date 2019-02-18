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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.relex.circleindicator.CircleIndicator;

public class LondonEuston extends AppCompatActivity
{
    private Context mContext = LondonEuston.this;
    private static final int ACTIVITY_NUM = 1;
    public static ArrayAdapter arrayAdapter;
    protected ListView roomList;
    protected Button checkIn, checkOut, bookButton;
    protected DatePickerDialog.OnDateSetListener dateSetListener_in;
    protected DatePickerDialog.OnDateSetListener dateSetListener_out;
    protected int dateIn, dateOut, monthIn, monthOut;
    protected int basePrice = 0;
    public String hotel = "London Euston";
    public String room = "";
    public String finalCheckIn = "";
    public String finalCheckOut = "";
    public String roomLeft = "";
    protected int Price = 20;
    protected ViewPager viewpager;
    protected ViewPagerAdapter viewPagerAdapter;
    private Integer[] hotelImages = {R.drawable.london_euston_lobby, R.drawable.london_euston_room, R.drawable.london_euston_gym};
    private ArrayList<Integer> imagesArray = new ArrayList<Integer>();
    protected boolean bookingValid = true;


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
        setContentView(R.layout.activity_london_euston);
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
                DecimalFormat dateFormat = new DecimalFormat("00");
                month = month + 1; //January starts at 0.
                String returnMonth = dateFormat.format(month); //Change format to DD, and make month a string.
                String returnDay = dateFormat.format(day);
                String date = returnDay + "/" + returnMonth + "/" + year;

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
                monthOut = month;

                DecimalFormat dateFormat = new DecimalFormat("00");
                String returnMonth = dateFormat.format(month); //Change format to DD, and make month a string.
                String returnDay = dateFormat.format(day);
                String date = returnDay + "/" + returnMonth + "/" + year;
                finalCheckOut = date;
                displayOutDate.setText(date);

                dateOut = day;
                if(displayInDate != null) //If the user has inputted a check in date.
                {
                    daysDifference = dateOut - dateIn;
                    Price *= daysDifference;
                    priceView.setText("£" + Price);
                    Animation animation = AnimationUtils.loadAnimation(LondonEuston.this, R.anim.fadein);
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
                DatePickerDialog dialog = new DatePickerDialog(LondonEuston.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener_out, inYear, inMonth, inDay);
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
                DatePickerDialog dialog = new DatePickerDialog(LondonEuston.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener_in, year, month, day);
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
                    Toast toast = Toast.makeText(getApplicationContext(),"Error: Please create an account or log in before you book a room.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(!bookingValid)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Error: There are no more availabilities for that room.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(dateIn >= dateOut)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Error: Please check out at least one day after you check in.", Toast.LENGTH_LONG);
                    toast.show();
                }
                // else if(dateIn <= GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH) || monthIn < GregorianCalendar.getInstance().get(Calendar.MONTH))
                // {
                //    Toast toast = Toast.makeText(getApplicationContext(),"Error: Please check in after today's date.", Toast.LENGTH_LONG);
                //      toast.show();
                // }
                else if(!room.equals("") && !finalCheckIn.equals("") && !finalCheckOut.equals("")) //Error handling for users that press book without any acceptable details.
                {
                    Intent ConfirmIntent = new Intent(LondonEuston.this, ConfirmBookingPage.class);
                    ConfirmIntent.putExtra("Hotel", hotel);
                    ConfirmIntent.putExtra("Room", room);
                    ConfirmIntent.putExtra("CheckIn",  finalCheckIn);
                    ConfirmIntent.putExtra("CheckOut", finalCheckOut);
                    ConfirmIntent.putExtra("Price",  "£" + Price);
                    ConfirmIntent.putExtra("Room_Availability", roomLeft);
                    startActivity(ConfirmIntent);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Error: Please fill in all the fields before you confirm", Toast.LENGTH_LONG);
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
        viewPagerAdapter = new ViewPagerAdapter(LondonEuston.this, imagesArray);
        viewpager.setAdapter(viewPagerAdapter);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);
    }

    protected void hotelLogic()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //Arraylist containing the room types available for the hotel.
        final ArrayList<String> hotelList = new ArrayList<>();

        hotelList.add("Single Room");
        hotelList.add("Double Room");
        hotelList.add("Family Room");
        hotelList.add("Large Family Room");
        hotelList.add("Couple Duplex Room");

        // Initialize an array adapter
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, hotelList)
        {
            Typeface mTypeface = ResourcesCompat.getFont(getContext(), R.font.roboto_light);
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                TextView item = (TextView) super.getView(position, convertView, parent);
                item.setTypeface(mTypeface);
                item.setTextColor(Color.parseColor("#191919"));
                item.setTypeface(item.getTypeface(), Typeface.NORMAL);
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                item.setPadding(80,0,0,0);
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
                        room = "Single Rooms";
                        roomAvailabilities("Single Rooms");
                        break;
                    case 1: basePrice = 60; //Single Room Base Price
                        room = "Double Rooms";
                        roomAvailabilities("Double Rooms");
                        break;
                    case 2: basePrice = 80; //Family Room Base Price
                        room = "Family Rooms";
                        roomAvailabilities("Family Rooms");
                        break;
                    case 3: basePrice = 120; //Large Family Room Base Price
                        room = "Large Family Rooms";
                        roomAvailabilities("Large Family Rooms");
                        break;
                    case 4: basePrice = 120; //Couple Duplex Room Base Price
                        room = "Couple Duplex Rooms";
                        roomAvailabilities("Couple Duplex Rooms");
                        break;
                }
            }
        });
    }

    protected void roomAvailabilities(final String userRoom) //Check firebase Hotels child for hotel room availabilities depending on the hotel the user has clicked. Parameter input is user room that they chose.
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query key = reference.child("Hotels").child("London Euston").child(userRoom);
        key.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.getValue().toString().equals("0"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Sorry, there are no more " + userRoom + " Available.", Toast.LENGTH_LONG);
                    bookingValid = false;
                    toast.show();
                }
                else
                {
                    bookingValid = true;
                    roomLeft = dataSnapshot.getValue().toString();
                    String roomAvailability = dataSnapshot.getValue().toString() + " " + userRoom + " Available.";
                    Toast toast = Toast.makeText(getApplicationContext(),roomAvailability, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
