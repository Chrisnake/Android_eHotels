package com.example.android.ehotelsapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter
{
    Activity activity;
    ArrayList<Integer> images;
    LayoutInflater inflater;

    public ViewPagerAdapter(Activity activity, ArrayList<Integer> images)
    {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount()
    {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);
        ImageView image;
        image = itemView.findViewById(R.id.viewpagerImage);
        DisplayMetrics display = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(display);
        int height = display.heightPixels;
        int width = display.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        try{
            Picasso.with(activity.getApplicationContext())
                    .load(images.get(position))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher_round)
                    .into(image);
        } catch (Exception ex) { }

        container.addView(itemView);
        return itemView;
    }
}
