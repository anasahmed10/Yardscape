package com.example.yardscapelistingprototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yardscapelistingprototype.adapter.FirebaseListingAdapter;
import com.example.yardscapelistingprototype.adapter.ListingAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ListingAdapter.Callback {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    DatabaseReference mbase;
    FirebaseListingAdapter mListingAdapter;
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);

        mbase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Fetches UI elements from Firebase Database
        FirebaseRecyclerOptions<Listing> options = new FirebaseRecyclerOptions.Builder<Listing>().setQuery(mbase, Listing.class).build();

        mListingAdapter = new FirebaseListingAdapter(options);
        mRecyclerView.setAdapter(mListingAdapter);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        toggle.setDrawerIndicatorEnabled(true);
        ButterKnife.bind(this);

        // Syncs the navigation bar to the main activity
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int item_id = item.getItemId();
                switch (item_id) {
                    case R.id.home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }
            }
        });

        // Floating Action Button will go to the CreateListing activity when clicked
/*        fab = (FloatingActionButton) findViewById(R.id.newListingButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateListing.class);
            startActivityForResult(intent, 1);
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        mListingAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mListingAdapter.stopListening();
    }

    // After a listing is received from CreateListing, gets the Listing variable and adds it to mListingAdapter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle createData = data.getExtras();
                Listing resultListing = (Listing) createData.getParcelable("new_listing");
/*                mListingAdapter.addItems(resultListing);
                mRecyclerView.setAdapter(mListingAdapter);
                mListingAdapter.*/
                // Some unworking code to create a new listing item in the UI, was previously using a grid view instead of Recyclerview
                // As you can see, a GridLayout is a lot more unoptimized than a RecyclerView

/*                Listing resultListing = data.getParcelableExtra("new_listing");

                CardView newListing = new CardView(new ContextThemeWrapper(MainActivity.this, R.style.newListingStyle), null, 0);
                RelativeLayout cardInner = new RelativeLayout(new ContextThemeWrapper(MainActivity.this, R.style.newListingStyle));

                GridLayout gridLayout = (GridLayout) findViewById(R.id.listingGrid);
                GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);

                TextView newTitle = new TextView(this);
                newTitle.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                newTitle.setTextAppearance(this, R.style.newListingText);
                newTitle.setText((CharSequence) resultListing.getTitle());

                newListing.addView(newTitle);
                gridLayout.addView(newListing, gridParam);*/
            }
        }
    }

    @Override
    public void onEmptyViewRetryClick() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}

// Previously used methods to load UI elements before updating it with Firebase

/*
    // Takes contents of List of Listings and creates cards out of them
    private void createCard(ArrayList<Listing> addList) {
        mListingAdapter.addItems(addList);
        mRecyclerView.setAdapter(mListingAdapter);
    }

    // Creates the layout and dividers between the cards
    private void setUp() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        mListingAdapter = new ListingAdapter(new ArrayList<>());

        prepareDemoContent();
    }

    // Prepares
    private void prepareDemoContent() {
            ArrayList<Listing> demoList = new ArrayList<>();
            demoList.add(new Listing(getString(R.string.demoTitle), getString(R.string.demoDesc), getString(R.string.demoDate), getString(R.string.demoTime)));
            createCard(demoList);
    }*/