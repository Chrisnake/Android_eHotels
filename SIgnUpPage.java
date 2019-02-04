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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SIgnUpPage extends AppCompatActivity
{
    private Context mContext = SIgnUpPage.this;
    private static final int ACTIVITY_NUM = 2;
    public static String getuserKey;

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
        setContentView(R.layout.activity_sign_up_page);
        setupBottomNavigation();

        final FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connecting firebase to login activity.
        final DatabaseReference ref = database.getReference("Users");
        Button confirmButton = findViewById(R.id.confirmButton);
        final EditText userEmail = findViewById(R.id.emailField);
        final EditText userPassword = findViewById(R.id.passwordField);
        final EditText cuserPassword = findViewById(R.id.confirmpasswordField);
        final EditText userName = findViewById(R.id.nameField);
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!userPassword.getText().toString().equals(cuserPassword.getText().toString())) //If the user entered password is not the same as the confirm password.
                {
                    Toast.makeText(SIgnUpPage.this, "Oops, your passwords do not match, please try again...", Toast.LENGTH_SHORT).show();
                }
                else if(isEmpty(userEmail) || isEmpty(userPassword) || isEmpty(cuserPassword) || isEmpty(userName)) //Checks if the user has filled in all fields
                {
                    Toast.makeText(SIgnUpPage.this, "Oops, please fill in all the fields...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String key = ref.push().getKey(); //Obtaining key with empty data push.
                    getuserKey = key;
                    UserDetails newUser = new UserDetails(); //Creating new user with object.
                    newUser.setUserEmail(userEmail.getText().toString().trim());
                    newUser.setUserID(key);
                    newUser.setUserName(userName.getText().toString().trim());
                    newUser.setUserPassword(userPassword.getText().toString().trim());
                    ref.child(key).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>()  //Pushing the data with respect to oncompletelistener for errors.
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                ProfilePage.getUserEmail = userEmail.getText().toString();
                                ProfilePage.isLoggedIn = true;
                            }
                            else
                            {
                                Toast.makeText(SIgnUpPage.this, "Oops, please contact user support...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Intent LoginIntent = new Intent(SIgnUpPage.this, ProfilePage.class);
                    startActivity(LoginIntent);
                }
            }
        });
    }

    private boolean isEmpty(EditText etText)
    {
        if (etText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }



}
