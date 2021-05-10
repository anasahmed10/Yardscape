package com.example.yardscapelistingprototype;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Code that opens the listing page after clicking on a listing card on the main UI

public class OpenListing extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_listing);
        Intent intent = getIntent();
        Listing sourceListing = intent.getExtras().getParcelable("openListing");

        getSupportActionBar().setTitle(sourceListing.getListing_title());

        // If the Listing image is not the default image, then set the image to the uploaded image
        ImageView listing_image = (ImageView) findViewById(R.id.imagePreview);
        if(sourceListing.getListing_image_path() != "none")
            listing_image.setImageBitmap(BitmapFactory.decodeFile(sourceListing.getListing_image_path()));
        else {
            listing_image.setImageResource(R.drawable.profile_icon);
        }

        TextView listing_title, listing_day, listing_time, listing_desc;

        listing_title = (TextView) findViewById(R.id.open_listing_title);
        listing_day = (TextView) findViewById(R.id.open_listing_date);
        listing_time = (TextView) findViewById(R.id.open_listing_time);
        listing_desc = (TextView) findViewById(R.id.open_listing_desc);

        // Sets Textview values according to listing
        listing_title.setText(sourceListing.getListing_title());
        listing_day.setText(sourceListing.getListing_date());
        listing_time.setText(sourceListing.getListing_time());
        listing_desc.setText(sourceListing.getListing_description());
    }
}
