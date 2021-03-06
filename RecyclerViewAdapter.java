package com.example.android.ehotelsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mcheckinout = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> images, ArrayList<String> checkinout)
    {
        mImageNames = imageNames;
        mImages = images;
        mContext = context;
        mcheckinout = checkinout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.imageName.setText(mImageNames.get(position));
        holder.checkinout.setText(mcheckinout.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(mContext, BookingPage.class);
                intent.putExtra("image_url", mImages.get(position));
                intent.putExtra("image_name", mImageNames.get(position));
                intent.putExtra("booking_in", SuccessfulProfilePage.bookingsList.get(position).getDateIn());
                intent.putExtra("booking_out", SuccessfulProfilePage.bookingsList.get(position).getDateOut());
                intent.putExtra("booking_qr", SuccessfulProfilePage.bookingsList.get(position).getIdQR());
                intent.putExtra("booking_roomtype", SuccessfulProfilePage.bookingsList.get(position).getRoomType());
                intent.putExtra("booking_price", SuccessfulProfilePage.bookingsList.get(position).getPrice());
                intent.putExtra("booking_roomnumber", SuccessfulProfilePage.bookingsList.get(position).getUserRoomNumber());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mImageNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView image;
        TextView imageName;
        TextView checkinout;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.towels_button);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            checkinout = itemView.findViewById(R.id.checkincheckout);
        }
    }
}