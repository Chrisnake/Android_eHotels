package com.example.android.ehotelsapp;

import java.util.Calendar;

import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
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

public class RequestDisturb extends AppCompatActivity
{
    private Context mContext = RequestDisturb.this;
    private static final int ACTIVITY_NUM = 2;
    protected String userKey;
    protected String requestType = "Do Not Disturb";
    protected String selectedTimeMain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_disturb);
        setupBottomNavigation();
        initialiseTime();
        getKey();
        updateDoNotDisturb();
        showDoNotDisturb();
    }
    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    protected void initialiseTime()
    {
        final EditText timeSelected = findViewById(R.id.activeInactive);
        timeSelected.setEnabled(false);
        Button setTime = findViewById(R.id.settimebutton);
        setTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RequestDisturb.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                    {
                        timePicker.setIs24HourView(true);
                        timeSelected.setText("Until " + selectedHour + ":" + selectedMinute);
                        selectedTimeMain = (String.valueOf(selectedHour) + String.valueOf(selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
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

    protected void updateDoNotDisturb()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity.
        final DatabaseReference ref = database.getReference("Requests");
        Button confirm = findViewById(R.id.towelsButton);
        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HashMap<String, String> requestData = new HashMap<String, String>(); //Putting data in a hashmap with key and values.
                String hotel = getIntent().getStringExtra("image_name");
                requestData.put("userKey", userKey);
                requestData.put("requestType", requestType);
                requestData.put("requestInformation", selectedTimeMain);
                requestData.put("roomNumber", getIntent().getStringExtra("booking_roomnumber"));
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
                            Toast.makeText(RequestDisturb.this, "Oops, please contact user support...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Intent ConfirmIntent = new Intent(RequestDisturb.this, RequestConfirmed.class);
                ConfirmIntent.putExtra("request_type", "Your " + "Privacy");
                startActivity(ConfirmIntent);
            }
        });
    }

    protected void showDoNotDisturb() //Shows the time of do not disturb mode.
    {
        final EditText activeInactive = findViewById(R.id.activeInactive);
        activeInactive.setEnabled(false);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query key = reference.child("Requests");
        key.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot user : dataSnapshot.getChildren())
                {
                    String requestType = (String) user.child("requestType").getValue();
                    String usercurrent = (String) user.child("userKey").getValue();
                    if(requestType == null)
                    {
                        break;
                    }
                    else if(requestType.equals("Do Not Disturb") && usercurrent.equals(userKey))
                    {
                        String disturbTime = (String) user.child("requestInformation").getValue();
                        activeInactive.setText("Until " + disturbTime);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
