package com.example.yardscapelistingprototype;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.annotations.SerializedName;

public class Listing implements Parcelable {
    @SerializedName("listingTitle")
    private String listing_title = String.valueOf(R.string.demoTitle);
    @SerializedName("listingDesc")
    private String listing_description = String.valueOf(R.string.demoDesc);
    @SerializedName("listingDate")
    private String listing_date = String.valueOf(R.string.demoDate);
    @SerializedName("listingTime")
    private String listing_time = String.valueOf(R.string.demoTime);
    @SerializedName("listingPath")
    private String listing_image_path;
    @SerializedName("listingUser")
    private String listing_user;

    // Empty Constructor
    public Listing() {
        listing_title = "";
        listing_description = "";
        listing_date = "";
        listing_time = "";
        listing_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setDefaultImage();
    }

    public Listing(String inTitle, String inDesc, String inDate, String inTime) {
        listing_title = inTitle;
        listing_description = inDesc;
        listing_date = inDate;
        listing_time = inTime;
        listing_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        setDefaultImage();
    }

    public Listing(String intitle, String indesc, String indate, String intime, String inimage) {
        listing_title = intitle;
        listing_description = indesc;
        listing_date = indate;
        listing_time = intime;
        listing_image_path = inimage;
        listing_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // Enables the ability to read a Listing variable from a Parceable object
    protected Listing(Parcel in) {
        listing_title = in.readString();
        listing_description = in.readString();
        listing_date = in.readString();
        listing_time = in.readString();
        listing_image_path = in.readString();
        listing_user = in.readString();
    }

    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };

    public void setTitle(String titleInput) {
        listing_title = titleInput;
    }
    public void setDescription(String description1) {
        listing_description = description1;
    }
    public void setListing_date(String dateInput) {
        listing_date = dateInput;
    }
    public void setListing_time(String timeInput) {
        listing_time = timeInput;
    }
    public void setListing_image_path(String imageInput) {listing_image_path = imageInput;}
    public void setListing_user(String userInput) {listing_user = userInput;}

    public String getListing_title() {
        return listing_title;
    }
    public String getListing_description() {
        return listing_description;
    }
    public String getListing_date(){
         return listing_date;
    }
    public String getListing_time(){
        return listing_time;
    }
    public String getListing_image_path() {return listing_image_path;}
    public String getListing_user() {return listing_user;}

    @Override
    public int describeContents() {
        return 0;
    }

    public String getURLForResource(int resourceID) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + resourceID).toString();
    }

    public void setDefaultImage() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("images/emptyphoto.jpg");
        setListing_image_path(storageReference.toString());
    }

    // Enables the ability to write a Listing variable to a Parceable object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(listing_title);
        dest.writeString(listing_description);
        dest.writeString(listing_date);
        dest.writeString(listing_time);
        dest.writeString(listing_image_path);
        dest.writeString(listing_user);
    }
}
