package com.example.yardscapelistingprototype.adapter;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.OpenListing;
import com.example.yardscapelistingprototype.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListingAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "ListingAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<Listing> mListingList;

    public ListingAdapter(List<Listing> ListingList) {
        mListingList = ListingList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
        View v = holder.itemView;

        // Opens Listing when user clicks on it in a new activity OpenListing
        CardView cardView = (CardView) v.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("openListing", mListingList.get(position));

                Intent intent = new Intent(v.getContext(), OpenListing.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mListingList != null && mListingList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mListingList != null && mListingList.size() > 0) {
            return mListingList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Listing> ListingList) {
        mListingList.addAll(ListingList);
        notifyDataSetChanged();
    }

    // Overloaded addItems function to also support adding a single Listing variable instead of a List of Listings
    public void addItems(Listing listing) {
        mListingList.add(listing);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    // Holds view for Listing Cards
    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.thumbnail)
        ImageView coverImageView;

        @BindView(R.id.listing_title)
        TextView titleTextView;

        @BindView(R.id.listing_time)
        TextView timeTextView;

        @BindView(R.id.listing_date)
        TextView dateTextView;

        @BindView(R.id.listing_desc)
        TextView descTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("inListing", mListingList.get(getCurrentPosition()));
                    Intent intent = new Intent(v.getContext(), OpenListing.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }

        protected void clear() {
            coverImageView.setImageDrawable(null);
            titleTextView.setText("");
            timeTextView.setText("");
            dateTextView.setText("");
            descTextView.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Listing mListing = mListingList.get(position);

            // Sets XML attributes to Listing values

            if (mListing.getListing_image_path() != "none") {
                Glide.with(itemView.getContext())
                        .load(BitmapFactory.decodeFile(mListing.getListing_image_path()))
                        .into(coverImageView);
            }
            else {
                coverImageView.setImageResource(R.drawable.profile_icon);
            }

            titleTextView.setText(mListing.getListing_title());



            if (mListing.getListing_time() != null) {
                timeTextView.setText(mListing.getListing_time());
            }

            if (mListing.getListing_date() != null) {
                dateTextView.setText(mListing.getListing_date());
            }

            if (mListing.getListing_description() != null) {
                descTextView.setText(mListing.getListing_description());
            }

            itemView.setOnClickListener(v -> {
                if (mListing.getListing_image_path() != null) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(mListing.getListing_image_path()));
                        itemView.getContext().startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: Image url is not correct");
                    }
                }
            });


        }
    }

    public class EmptyViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_message)
        TextView messageTextView;
        @BindView(R.id.buttonRetry)
        TextView buttonRetry;

        EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            buttonRetry.setOnClickListener(v -> mCallback.onEmptyViewRetryClick());
        }

        @Override
        protected void clear() {

        }
    }
}










/*
package com.example.yardscapelistingprototype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] titles = {"Listing One",
            "Listing Two",
            "Listing Three",
            "Listing Four",
            "Listing Five",
            "Listing Six",
            "Lisitng Seven",
            "Lisitng Eight"
    };

    private String[] details = {"Listing One details",
            "Listing Two details",
            "Listing Three details",
            "Listing Four details",
            "Listing Five details",
            "Listing Six details",
            "Lisitng Seven details",
            "Lisitng Eight details"
    };

    private int[] images = {R.drawable.android_image_1,
            R.drawable.android_image_2,
            R.drawable.android_image_3,
            R.drawable.android_image_4,
            R.drawable.android_image_5,
            R.drawable.android_image_6,
            R.drawable.android_image_7,
            R.drawable.android_image_8,
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        public int currentItem;
        public ImageView listingImage;
        public TextView listingTitleText;
        public TextView listingDetail;

        public ViewHolder(View listingView) {
            super(listingView);
            listingImage = (ImageView) listingView.findViewById(R.id.listing_image);
            listingTitleText = (TextView) listingView.findViewById(R.id.listing_title);
            listingDetail = (TextView) listingView.findViewById(R.id.listing_content);

            listingView.setOnClickListener((v) -> {
                int position = getAdapterPosition();

                Snackbar.make(v, "Click detected on item" + position, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            });
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.listingTitleText.setText(titles[position]);
        holder.listingDetail.setText(details[position]);
        holder.listingImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
*/
