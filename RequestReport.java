package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RequestReport extends AppCompatActivity
{
    private Context mContext = RequestReport.this;
    private static final int ACTIVITY_NUM = 2;
    private String userKey;
    private String requestType = "Report";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_report);
        setupBottomNavigation();
        getKey();
        updateTowels();
    }

    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    protected void updateTowels()
    {
        final EditText getInformation = findViewById(R.id.bathTowels);
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity.
        final DatabaseReference ref = database.getReference("Requests");
        Button confirm = findViewById(R.id.towelsButton);
        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String bookingRoomNumber = getIntent().getStringExtra("booking_roomnumber");
                String hotel = getIntent().getStringExtra("image_name");
                HashMap<String, String> requestData = new HashMap<String, String>(); //Putting data in a hashmap with key and values.
                requestData.put("userKey", userKey);
                requestData.put("requestType", requestType);
                requestData.put("requestInformation",getInformation.getText().toString());
                requestData.put("roomNumber", bookingRoomNumber);
                requestData.put("hotel", hotel);
                ref.push().setValue(requestData).addOnCompleteListener(new OnCompleteListener<Void>()  //Pushing the data with respect to oncompletelistener for errors.
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {

                        }
                        else
                        {
                            Toast.makeText(RequestReport.this, "Oops, please contact user support...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Intent ConfirmIntent = new Intent(RequestReport.this, RequestConfirmed.class);
                ConfirmIntent.putExtra("request_type", "Your " + requestType);
                startActivity(ConfirmIntent);
            }
        });
    }

    protected void getKey() //Returns the userKey depending on the email address of the user.
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query key = reference.child("Users").orderByChild("userEmail").equalTo(ProfilePage.getUserEmail);
        key.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot datas: dataSnapshot.getChildren())
                {
                    String keys = datas.getKey();
                    userKey = keys;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
