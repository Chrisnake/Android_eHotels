package com.example.android.ehotelsapp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateRooms
{
    private String hotel;
    private String roomType;

    public UpdateRooms(String Hotel, String Room)
    {
        hotel = Hotel;
        roomType = Room;
    }

    public String getHotel()
    {
        return hotel;
    }

    public void setHotel(String hotel)
    {
        this.hotel = hotel;
    }

    public String getRoomType()
    {
        return roomType;
    }

    public void setRoomType(String roomType)
    {
        this.roomType = roomType;
    }

    public static void updateRoomSpace(ArrayList<UpdateRooms> updatelist)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        for(int i = 0; i < updatelist.size(); i++)
        {
            switch(updatelist.get(i).getHotel())
            {
                case "London Marylebone":
                    final Query key = reference.child("Hotels").child("London Marylebone").child(updatelist.get(i).getRoomType());
                    key.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            long roomlong = (Long) dataSnapshot.getValue();
                            long updateRoom = roomlong + 1;
                            Log.i("RoomUpdated", updateRoom + "hey");
                            ((DatabaseReference) key).setValue(updateRoom);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {

                        }
                    });
                    break;

                case "London Picadilly":
                    final Query key_picadilly = reference.child("Hotels").child("London Picadilly").child(updatelist.get(i).getRoomType());
                    key_picadilly.addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            long roomlong = (Long) dataSnapshot.getValue();
                            long updateRoom = roomlong + 1;
                            Log.i("RoomUpdated", updateRoom + "hey");
                            ((DatabaseReference) key_picadilly).setValue(updateRoom);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {

                        }
                    });
                    break;

                case "London Hammersmith":
                final Query key_hammersmith = reference.child("Hotels").child("London Hammersmith").child(updatelist.get(i).getRoomType());
                key_hammersmith.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        long roomlong = (Long) dataSnapshot.getValue();
                        long updateRoom = roomlong + 1;
                        Log.i("RoomUpdated", updateRoom + "hey");
                        ((DatabaseReference) key_hammersmith).setValue(updateRoom);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
                break;
            }
        }
    }
}
