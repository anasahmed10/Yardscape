package com.example.yardscapelistingprototype.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.R;
import com.example.yardscapelistingprototype.fragments.OpenListingFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

// Firebase version of the recycler adapter which fetches listings from a JSON file

public class FirebaseListingAdapter extends FirebaseRecyclerAdapter<Listing, FirebaseListingAdapter.listingViewholder> {

    public FirebaseListingAdapter(@NonNull FirebaseRecyclerOptions<Listing> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull listingViewholder holder, int position, @NonNull Listing model) {
        final long ONE_MEGABYTE = 1024 * 1024;
        View view = holder.itemView;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(model.getListing_image_path());

        // Sets Textviews to proper Listing values
        holder.itemTitle.setText(model.getListing_title());
        holder.itemDesc.setText("Description: " + model.getListing_description());
        holder.itemDate.setText("Date: " + model.getListing_date());
        holder.itemTime.setText("Time: " + model.getListing_time());
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.itemPreview.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 282, 159, false));
            }
        });

        // Opens Listing when user clicks on it in a new activity OpenListing
        CardView cardView = (CardView) view.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("openListing", model);
                OpenListingFragment openFrag = new OpenListingFragment(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, openFrag).addToBackStack(null).commit();
            }
        });

    }

    @NonNull
    @Override
    public listingViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new FirebaseListingAdapter.listingViewholder(view);
    }



    class listingViewholder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemDesc, itemDate, itemTime;
        ImageView itemPreview;
        public listingViewholder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.listing_title);
            itemDesc = itemView.findViewById(R.id.listing_desc);
            itemDate = itemView.findViewById(R.id.listing_date);
            itemTime = itemView.findViewById(R.id.listing_time);
            itemPreview = itemView.findViewById(R.id.thumbnail);
        }
    }
}


