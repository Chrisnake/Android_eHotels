package com.example.android.ehotelsapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class LondonMarylebone extends AppCompatActivity
{
    private Context mContext = LondonMarylebone.this;
    private static final int ACTIVITY_NUM = 1;
    protected Button checkIn;
    protected Button checkOut;

    protected DatePickerDialog.OnDateSetListener dateSetListener_in;
    protected DatePickerDialog.OnDateSetListener dateSetListener_out;

    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_london_marylebone);

        setupBottomNavigation();

        //Connecting XML buttons and text to code.
        checkIn = findViewById(R.id.checkindateButton);
        checkOut = findViewById(R.id.checkoutdateButton);
        final TextView displayInDate = findViewById(R.id.checkinText);
        final TextView displayOutDate = findViewById(R.id.checkoutText);

        //Creating datelistene to listen to dates.
        dateSetListener_in = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1; //January starts at 0.
                String date = day + "/" + month + "/" + year;
                displayInDate.setText(date);
            }
        };

        dateSetListener_out = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1; //January starts at 0.
                String date = day + "/" + month + "/" + year;
                displayOutDate.setText(date);
            }
        };

        checkOut.setOnClickListener(new View.OnClickListener()
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

        checkIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH);
                int day = calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(LondonMarylebone.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener_out, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }
}
