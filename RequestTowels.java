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

public class RequestTowels extends AppCompatActivity
{
    private Context mContext = RequestTowels.this;
    private static final int ACTIVITY_NUM = 2;
    private String userKey;
    private String requestType = "Towel";
    EditText bathTowelQuantity;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_towels);
        setupBottomNavigation();
        initialiseEditTexts();
        getKey();
        updateTowels();
    }

    protected void initialiseEditTexts()
    {
        EditText bathTowel = findViewById(R.id.bathTowels);
        EditText faceTowel = findViewById(R.id.faceTowel);
        EditText bathMat = findViewById(R.id.bathMat);
        EditText handTowel = findViewById(R.id.handTowel);

        bathTowel.setEnabled(false);
        faceTowel.setEnabled(false);
        bathMat.setEnabled(false);
        handTowel.setEnabled(false);
    }
    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    protected void updateTowels() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity.
        final DatabaseReference ref = database.getReference("Requests");
        Button confirm = findViewById(R.id.towelsButton);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookingRoomNumber = getIntent().getStringExtra("booking_roomnumber");
                String hotel = getIntent().getStringExtra("image_name");
                EditText bathTowel = findViewById(R.id.bathTowelQuantity);
                EditText faceTowel = findViewById(R.id.faceTowelQuantity);
                EditText bathMat = findViewById(R.id.bathMatQuantity);
                EditText handTowel = findViewById(R.id.handTowelQuantity);

                if (Integer.parseInt(bathTowel.getText().toString()) > 5 || Integer.parseInt(faceTowel.getText().toString()) > 5 || Integer.parseInt(bathMat.getText().toString()) > 5 || Integer.parseInt(handTowel.getText().toString()) > 5)
                {
                    Toast.makeText(RequestTowels.this, "Sorry, but you have requested too many towels. 5 is the most you can request for.", Toast.LENGTH_LONG).show();
                }
                else {
                    final String towelRequest = "Bath Towels: " + bathTowel.getText() + " Face Towels: " + faceTowel.getText() + " Bath Maths: " + bathMat.getText() + " Hand Towels: " + handTowel.getText();
                    HashMap<String, String> requestData = new HashMap<String, String>(); //Putting data in a hashmap with key and values.
                    requestData.put("userKey", userKey);
                    requestData.put("requestType", requestType);
                    requestData.put("requestInformation", towelRequest);
                    requestData.put("roomNumber", bookingRoomNumber);
                    requestData.put("hotel", hotel);
                    ref.push().setValue(requestData).addOnCompleteListener(new OnCompleteListener<Void>()  //Pushing the data with respect to oncompletelistener for errors.
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Toast.makeText(RequestTowels.this, "Oops, please contact user support...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Intent ConfirmIntent = new Intent(RequestTowels.this, RequestConfirmed.class);
                    ConfirmIntent.putExtra("request_type", "Your " + requestType);
                    startActivity(ConfirmIntent);
                }
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
