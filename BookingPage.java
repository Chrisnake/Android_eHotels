package com.example.android.ehotelsapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Random;

public class BookingPage extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        getIncomingIntent();
        getBooking();
    }

    private void getIncomingIntent()
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
            String bookingIn = getIntent().getStringExtra("booking_in");
            String bookingOut = getIntent().getStringExtra("booking_out");
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

    protected void getBooking()
    {
        for(UserBookings bookings : SuccessfulProfilePage.bookingsList)
        {
            Log.i("BookingPage", bookings.getHotel());
        }
    }

}

