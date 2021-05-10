package com.example.yardscapelistingprototype.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.LoginActivity;
import com.example.yardscapelistingprototype.R;
import com.example.yardscapelistingprototype.adapter.FirebaseListingAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    Button signoutButton;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mbase;
    private GridLayoutManager layoutManager;
    FirebaseListingAdapter mListingAdapter;

    @BindView(R.id.accountRecyclerView)
    RecyclerView mRecyclerView;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        signoutButton = (Button) view.findViewById(R.id.signoutButton);

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(requireContext(), "Signed Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.accountRecyclerView);
        layoutManager = new GridLayoutManager(requireContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);

        mbase = FirebaseDatabase.getInstance().getReference().child("listings");
        mbase.keepSynced(true);
        // Queries the database to return just the user's listings
        Query query = mbase.child("listing_user").equalTo(mAuth.getCurrentUser().toString());
        query.keepSynced(true);
        FirebaseRecyclerOptions<Listing> options = new FirebaseRecyclerOptions.Builder<Listing>().setQuery(query, Listing.class).build();
        mListingAdapter = new FirebaseListingAdapter(options);
        mRecyclerView.setAdapter(mListingAdapter);

        // Inflate the layout for this fragment
        mListingAdapter.startListening();

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
}