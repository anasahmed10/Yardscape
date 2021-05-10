package com.example.yardscapelistingprototype;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.yardscapelistingprototype.fragments.AccountFragment;
import com.example.yardscapelistingprototype.fragments.CreateFragment;
import com.example.yardscapelistingprototype.fragments.HomeFragment;
import com.example.yardscapelistingprototype.fragments.LoginFragment;
import com.example.yardscapelistingprototype.fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HostActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView nvDrawer;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Yardscape");
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(nvDrawer);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView nvDrawer) {
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = HomeFragment.class;

        // Creates a new fragment based on which
        switch(item.getItemId()) {
            case R.id.home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.create:
                fragmentClass = CreateFragment.class;
                break;
            case R.id.profile:
                if(mUser == null) {
                    fragmentClass = LoginFragment.class;
                } else {
                    fragmentClass = AccountFragment.class;
                }
                break;
            case R.id.settings:
                fragmentClass = SettingsFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            Log.e(String.valueOf(e), "Error fragment was not properly selected");
        }


        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        // Sync the toggle state after onRestoreInstanceState has occured
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}