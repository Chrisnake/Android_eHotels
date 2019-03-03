package com.example.android.ehotelsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuccessfulProfilePage extends AppCompatActivity
{
    private Context mContext = SuccessfulProfilePage.this;
    private static final int ACTIVITY_NUM = 2;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mCheckinout = new ArrayList<>();
    public static ArrayList<UserBookings> bookingsList = new ArrayList<>();
    public static String userKey;

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
        setContentView(R.layout.activity_successful_profile_page);
        setupBottomNavigation();

        readKey(new MyCallback()
        {
            @Override
            public void onCallback(String value)
            {
                userKey = value;
                findBookings(value);
            }
        });
    }

    protected void findBookings(final String userKey) //Finds bookings made by the user and place each object in arraylist of userBookings
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query key = reference.child("Bookings").orderByChild("userKey").equalTo(userKey); //Finds children userKey and checks if they are equal to userkey.
        key.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ImageView noBookingsImage = findViewById(R.id.sorry);
                TextView noBookingsText = findViewById(R.id.noBookings);
                int i = 0;
                for (DataSnapshot datas : dataSnapshot.getChildren())
                {
                    if(datas.exists())
                    {
                        noBookingsImage.setVisibility(View.GONE);
                        noBookingsText.setVisibility(View.GONE);
                    }
                    String hotel = (String) datas.child("Hotel").getValue();
                    String price = (String) datas.child("Price").getValue();
                    String dateIn = (String) datas.child("dateIn").getValue();
                    String dateOut = (String) datas.child("dateOut").getValue();
                    String roomType = (String) datas.child("roomType").getValue();
                    String qrID = (String) datas.child("idQR").getValue();
                    String roomNumber = (String) datas.child("RoomNumber").getValue();

                    UserBookings userBookings = new UserBookings(userKey, hotel, price, dateIn, dateOut, roomType, qrID, roomNumber);
                    bookingsList.add(userBookings);
                    mNames.add(userBookings.getHotel());
                    mImageUrls.add(HashMapImages().get(userBookings.getHotel())); //Using hashmap where the key is the hotel name.
                    mCheckinout.add(dateIn + "   ➜   " + dateOut);

                    Log.i("rawdata?", "position " + i + " " + dateIn + " " + dateOut + " ");
                    Log.i("insidearray", "position " + i + " " + bookingsList.get(i).getDateIn() + " " + bookingsList.get(i).getDateOut() + " ");
                    i++;
                }
                initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initRecyclerView()
    {
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls, mCheckinout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void readKey(final MyCallback myCallback)
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
                    String key = datas.getKey();
                    myCallback.onCallback(key);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    public interface MyCallback
    {
        void onCallback(String userKey);
    }

    protected HashMap<String, String> HashMapImages() //Creates a hashmap of images where the key is the hotel name and the value is the hotel image link.
    {
        HashMap<String,String> imageMap = new HashMap<>();
        imageMap.put("London Marylebone", "https://media-cdn.tripadvisor.com/media/photo-s/06/4b/0b/d9/london-bridge-hotel.jpg");
        imageMap.put("London Picadilly", "https://t-ec.bstatic.com/images/hotel/max1024x768/413/41353407.jpg");
        imageMap.put("London Hammersmith", "https://s-ec.bstatic.com/images/hotel/max1024x768/142/142190398.jpg");
        imageMap.put("London Kensington", "http://www.hoteldirect.co.uk/images/The_Regency_Exterior.jpg");
        imageMap.put("London Euston", "https://www.londontoolkit.com/where-to-stay/images/novotel/hero-novotel-hotels.jpg");
        imageMap.put("London Westminster", "https://thumbnails.trvl-media.com/OSKOfyDLvQ8g0keZ_B_DbdLGq8w=/773x530/smart/filters:quality(60)/images.trvl-media.com/hotels/4000000/3120000/3113100/3113039/dd6865cc_z.jpg");
        imageMap.put("Birmingham Central", "https://s-ec.bstatic.com/xdata/images/hotel/270x200/18758426.jpg?k=c8de2afa4e05b71053ff72e2bbe1b0ae19f19462aca6609dcf8681405ae8f163&o=");
        return imageMap;
    }
}
