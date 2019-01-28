package com.example.android.ehotelsapp;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProfilePage extends AppCompatActivity
{
    private Context mContext = ProfilePage.this;
    private static final int ACTIVITY_NUM = 2;
    private EditText userEmail;
    private EditText userPassword;
    public static boolean isLoggedIn;
    public static String userName;
    public static String getUserEmail;

    private void setupBottomNavigation()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView); //Enable the logic of the navigation bar.
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void signUp()
    {
        TextView signupView = (TextView) findViewById(R.id.signupText);
        signupView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent SignUpIntent = new Intent(ProfilePage.this, SIgnUpPage.class);
                startActivity(SignUpIntent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        setupBottomNavigation();
        signUp();

        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to login activity.
        final DatabaseReference ref = database.getReference("Users");

        Button loginButton = findViewById(R.id.confirmButton);
        userEmail = findViewById(R.id.emailField);
        userPassword = findViewById(R.id.passwordField);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Query query = ref.orderByChild("userEmail").equalTo(userEmail.getText().toString()); //Creating a Query to find a match between username and password in database.
                query.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists()) //If it exists then the email is in the database.
                        {
                            for (DataSnapshot user : dataSnapshot.getChildren())
                            {
                                if (userPassword.getText().toString().equals(user.child("userPassword").getValue().toString())) //Finds user password fields and checks to see if match.
                                {
                                    isLoggedIn = true;
                                    getUserEmail = userEmail.getText().toString();
                                    Intent LoginIntent = new Intent(ProfilePage.this, SuccessfulProfilePage.class);
                                    startActivity(LoginIntent);
                                }
                                else
                                {
                                    Toast.makeText(ProfilePage.this, "Password is wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(ProfilePage.this, "The email you have entered is incorrect.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        throw databaseError.toException(); // don't ignore errors
                    }
                });
            }
        });
    }
}
