package com.example.android.ehotelsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Random;

public class BookingPage extends AppCompatActivity
{
    private Context mContext = BookingPage.this;
    private static final int ACTIVITY_NUM = 2;
    protected int totalDays;
    protected int daysLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        setupBottomNavigation();
        String bookingIn = getIntent().getStringExtra("booking_in");
        String bookingOut = getIntent().getStringExtra("booking_out");
        getIncomingIntent(bookingIn, bookingOut);
        progressBar(bookingIn, bookingOut);
    }

    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void getIncomingIntent(String bookingIn, String bookingOut)
    {
        Random rand = new Random();
        int qrID = rand.nextInt(10000) + 1; //Generate a random QRID for the QR code.
        TextView textIn = findViewById(R.id.CheckIn);
        TextView textOut = findViewById(R.id.CheckOut);
        TextView textHotel = findViewById(R.id.Hotel);
        ImageView QR = findViewById(R.id.QRCode);
        if(getIntent().hasExtra("image_name"))
        {
            String imageName = getIntent().getStringExtra("image_name");
            textIn.setText("Check In: " + bookingIn);
            textOut.setText("Check Out: " + bookingOut);
            textHotel.setText(imageName);

            //TODO: Store the qrID to the Bookings table in the database and access that ID through the database to generate the QR.
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try
            {
                BitMatrix bitMatrix = multiFormatWriter.encode(String.valueOf(qrID), BarcodeFormat.QR_CODE, 500,500);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                QR.setImageBitmap(bitmap);

            } catch (WriterException e) { e.printStackTrace(); }
        }
    }

    protected void progressBar(String bookingIn, String bookingOut)
    {
        int checkIn = Integer.parseInt(bookingIn.substring(0,1)); //Converting string bookingIn to primitive int.
        int checkOut = Integer.parseInt(bookingOut.substring(0,1)); //Converting string bookingIn to primitive int.
        TextView activeText = findViewById(R.id.activeText);
        totalDays = checkOut - checkIn; //Number of days
        daysLeft = checkOut - Calendar.getInstance().get(Calendar.DAY_OF_MONTH); //Days left = check out day (27th) - current day (25th) = 2

        if(daysLeft <= 0) //If the days are less than 0 then this booking is no longer available.
        {
            activeText.setText("Booking Finished");
        }
        else
        {
            Toast.makeText(BookingPage.this, daysLeft + "daysLeft", Toast.LENGTH_SHORT).show();
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setMax(totalDays);
            progressBar.setProgress(daysLeft);

        }
    }

}

