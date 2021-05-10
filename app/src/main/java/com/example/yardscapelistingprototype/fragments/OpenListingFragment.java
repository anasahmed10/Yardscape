package com.example.yardscapelistingprototype.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.R;
import com.example.yardscapelistingprototype.adapter.SliderAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpenListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenListingFragment extends Fragment {

    public TextView listing_title, listing_day, listing_time, listing_desc;
    public ImageView listing_image;
    private Listing sourceListing;
    final long ONE_MEGABYTE = 1024 * 1024;


    private List<Listing> listingList;
    private SliderAdapter sliderAdapter;
    private SliderView sliderView;
    FirebaseFirestore db;

    // Listing that is bundled on click
    private Bundle listingBundle;

    public OpenListingFragment(Bundle bundle) {
        sourceListing = bundle.getParcelable("openListing");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bundle Parameter 1.
     * @return A new instance of fragment OpenListingFragment.
     */
    public static OpenListingFragment newInstance(Bundle bundle) {
        OpenListingFragment fragment = new OpenListingFragment(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sourceListing = listingBundle.getParcelable("openListing");
        }
/*        this.db = FirebaseFirestore.getInstance();
        loadImages();*/

/*        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                listing_image.setImageBitmap(bitmap);
            }
        });*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.open_listing, container, false);

        listing_title = (TextView) v.findViewById(R.id.open_listing_title);
        listing_day = (TextView) v.findViewById(R.id.open_listing_date);
        listing_time = (TextView) v.findViewById(R.id.open_listing_time);
        listing_desc = (TextView) v.findViewById(R.id.open_listing_desc);
        listing_image = (ImageView) v.findViewById(R.id.imagePreview);

        // Sets Textview values according to listing
        listing_title.setText(sourceListing.getListing_title());
        listing_day.setText("Listing Date: " + sourceListing.getListing_date());
        listing_time.setText("Listing Time: " + sourceListing.getListing_time());
        listing_desc.setText("Listing Description: " + sourceListing.getListing_description());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(sourceListing.getListing_image_path());
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                listing_image.setImageBitmap(bitmap);
            }
        });



        // Inflate the layout for this fragment
        return v;
    }

    // Loads images from Firebase Storage and ports them into SliderView
    private void loadImages() {
        this.db.collection("images").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@Nullable QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Listing listing = documentSnapshot.toObject(Listing.class);
                    Listing model = new Listing();

                    model.setListing_image_path(sourceListing.getListing_image_path());
                    listingList.add(model);

                    // Setup SliderAdapter with settings
                    sliderAdapter = new SliderAdapter(requireContext(), listingList);
                    sliderView.setSliderAdapter(sliderAdapter);
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    sliderView.setScrollTimeInSec(3);
                    sliderView.setAutoCycle(true);
                    sliderView.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Fail to load slider data..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}