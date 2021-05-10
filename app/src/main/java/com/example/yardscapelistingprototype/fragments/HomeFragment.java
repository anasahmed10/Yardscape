package com.example.yardscapelistingprototype.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.R;
import com.example.yardscapelistingprototype.adapter.FirebaseListingAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private GridLayoutManager layoutManager;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    DatabaseReference mbase;
    FirebaseListingAdapter mListingAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mListingAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mListingAdapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        layoutManager = new GridLayoutManager(requireContext(), 2);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        mbase = FirebaseDatabase.getInstance().getReference().child("listings");
        mbase.keepSynced(true);
        FirebaseRecyclerOptions<Listing> options = new FirebaseRecyclerOptions.Builder<Listing>().setQuery(mbase, Listing.class).build();
        mListingAdapter = new FirebaseListingAdapter(options);
        mRecyclerView.setAdapter(mListingAdapter);

        // Inflate the layout for this fragment
        mListingAdapter.startListening();
    }
}