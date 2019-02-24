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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class RequestFoodFix extends AppCompatActivity
{
    private Context mContext = RequestFoodFix.this;
    private static final int ACTIVITY_NUM = 2;
    String userKey;
    protected String requestType = "Food";
    protected String[] Starters = new String[] {"No Starter Selected", "Beans and Toasted Bread: £3.00", "Canarian Potatoes with Mojo: £4.50", "Beef Tapsilog and fried egg: £5.00", "Wedges seasoned with Salt, Pepper and Cumin: £3.50", "Garlic Bread: £2.00", "Chili Nachos with Pulled Pork: £4.00", "Bulalo Beef Soup: £5.00", "Assorted Chicken Wings: £5.00"};

    protected String[] Main = new String[] {"No Main Selected", "Pan Roasted Porkchop w/Potatoes, Vegetables : £10.00", "Duck and Waffle: £11.00", "Indian Curry w/Pata Bread, Basmati Rice: £10.00", "Chinese Style Vegetable Stir Fry: £8.00",
            "Crispy Pork Belly w/Fried Rice, Spinach: £10.00", "Filipino Chicken Adobo w/Fried Rice: £10.00", "British Sunday Roast w/Potatoes, Vegetables: £10.00", "Beef Spaghetti w/Meatballs, Garlic Bread : £9.50"};

    protected String[] Desserts = new String[] {"No Desserts Selected", "Chocolate Ice Cream Waffle: £4.00", "Vanilla Ice Cream Waffle: £4.00", "Strawberry Ice Cream Waffle: £4.00", "Chocolate Brownie Ice Cream: £4.00", "Heavenly Slice of Chocolate Cake: £3.00", "Soft Filipino Mamon Cake: £4.00"};

    protected String[] Refresments = new String[] {"No Refreshments Selected", "Walkers Ready Salted Crisps: £1.00", "Walkers Salt and Vinegar Crisps: £1.00", "Walkers Prawn Cocktail Crisps: £1.00", "Kit Kat Bar: £1.00", "Kinder Buneo Bar: £1.00", "Haribos Tangtastics: £2.00", "Skittles minipack: £1.00"};

    protected String[] Drinks = new String[] {"No Drinks Selected", "Bottled Mineral Water: £2.00", "Coca Cola Can: £1.00", "Sprite Can: £1.00", "Fanta Fruit Twist Can: £1.00", "Fresh Orange Juice: £2.00", "Fresh Apple Juice: £2.00"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_food);
        setupBottomNavigation();
        getKey();

        Spinner starter = findViewById(R.id.starterSpinner);
        Spinner main = findViewById(R.id.mainSpinner);
        Spinner dessert = findViewById(R.id.dessertsSpinner);
        Spinner refreshments = findViewById(R.id.refereshmentsSpinner);
        Spinner drinks = findViewById(R.id.drinksSpinner);

        ArrayAdapter starterAdapter = new ArrayAdapter(this,R.layout.spinner_item,Starters);
        starterAdapter.setDropDownViewResource(R.layout.spinner_item);
        starter.setAdapter(starterAdapter);

        ArrayAdapter mainAdapter = new ArrayAdapter(this,R.layout.spinner_item,Main);
        mainAdapter.setDropDownViewResource(R.layout.spinner_item);
        main.setAdapter(mainAdapter);

        ArrayAdapter dessertAdapter = new ArrayAdapter(this,R.layout.spinner_item,Desserts);
        dessertAdapter.setDropDownViewResource(R.layout.spinner_item);
        dessert.setAdapter(dessertAdapter);

        ArrayAdapter refreshmentAdapter = new ArrayAdapter(this,R.layout.spinner_item,Refresments);
        refreshmentAdapter.setDropDownViewResource(R.layout.spinner_item);
        refreshments.setAdapter(refreshmentAdapter);

        ArrayAdapter drinksAdapter = new ArrayAdapter(this,R.layout.spinner_item,Drinks);
        drinksAdapter.setDropDownViewResource(R.layout.spinner_item);
        drinks.setAdapter(drinksAdapter);

        updateOrder(starter, main, dessert, refreshments, drinks);
    }
    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    protected void getKey() //Returns the userKey depending on the email address of thue user.
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
    protected void updateOrder(final Spinner starter, final Spinner main, final Spinner dessert, final Spinner refreshments, final Spinner drinks)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to confirm activity.
        final DatabaseReference ref = database.getReference("Requests");
        Button confirm = findViewById(R.id.towelsButton);
        confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String starterOrder = starter.getSelectedItem().toString();
                String mainOrder = main.getSelectedItem().toString();
                String dessertOrder = dessert.getSelectedItem().toString();
                String refreshmentsOrder = refreshments.getSelectedItem().toString();
                String drinksOrder = drinks.getSelectedItem().toString();

                if(starterOrder.equals("No Starter Selected"))
                {
                    starterOrder = "";
                }
                if(mainOrder.equals("No Main Selected"))
                {
                    mainOrder = "";
                }
                if(dessertOrder.equals("No Desserts Selected"))
                {
                    dessertOrder = "";
                }
                if(refreshmentsOrder.equals("No Refreshments Selected"))
                {
                    refreshmentsOrder = "";
                }
                if(drinksOrder.equals("No Drinks Selected"))
                {
                    drinksOrder = "";
                }

                String finalOrder = starterOrder + " " + mainOrder + " " + dessertOrder + " " + refreshmentsOrder + " " + drinksOrder + " ";
                HashMap<String, String> requestData = new HashMap<String, String>(); //Putting data in a hashmap with key and values.
                requestData.put("userKey", userKey);
                requestData.put("requestType", requestType);
                requestData.put("requestInformation",finalOrder);
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
                            Toast.makeText(RequestFoodFix.this, "Oops, please contact user support...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Intent ConfirmIntent = new Intent(RequestFoodFix.this, RequestConfirmed.class);
                ConfirmIntent.putExtra("request_type", "Your " + requestType);
                startActivity(ConfirmIntent);
            }
        });
    }

}
