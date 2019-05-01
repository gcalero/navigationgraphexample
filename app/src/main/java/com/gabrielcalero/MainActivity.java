package com.gabrielcalero;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, NavController.OnDestinationChangedListener {

    private static final String TAG = MainActivity.class.getName();
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mBottomNavView;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();
    }

    private void setupNavigation() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mBottomNavView = findViewById(R.id.bottom_nav);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mToolbar, mNavController, mDrawerLayout);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        NavigationUI.setupWithNavController(mBottomNavView, mNavController);
        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavView.setOnNavigationItemSelectedListener(this);

        mNavController.addOnDestinationChangedListener(this);
    }


    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mDrawerLayout);
    }

    public void forwardToSecond(View view) { mNavController.navigate(R.id.secondFragment); }

    public void forwardToThird(View view) { mNavController.navigate(R.id.thirdFragment); }

    public void backwardArrow(View view) {
        mNavController.navigateUp();
    }

    public void play(View view) {
        mNavController.navigate(R.id.action_play);
    }

    public void stop(View view) {
        mNavController.navigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_drawer_home:
                while (mNavController.navigateUp()){}
                mNavController.navigate(R.id.homeFragment);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_drawer_arrows:
                // this is needed to keep the bottom bar in sync
                mBottomNavView.setSelectedItemId(R.id.nav_arrows);
                break;
            case R.id.nav_drawer_playstop:
                // this is needed to keep the bottom bar in sync
                mBottomNavView.setSelectedItemId(R.id.nav_play_stop);
                break;
            case R.id.nav_arrows:
                Log.d(TAG, "onNavigationItemSelected: nav_arrows" );
                while (mNavController.navigateUp()){}
                mNavController.navigate(R.id.action_arrows);
                mNavigationView.setCheckedItem(R.id.nav_drawer_arrows);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_play_stop:
                Log.d(TAG, "onNavigationItemSelected: nav_play_stop" );
                while (mNavController.navigateUp()){}
                mNavController.navigate(R.id.action_playstop);
                mNavigationView.setCheckedItem(R.id.nav_drawer_playstop);
                mDrawerLayout.closeDrawers();
                break;
            default:
                    Log.d(TAG,"Unknown navigation item selected " + menuItem.getItemId());


        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    public void menuOptionSelected(View view) {
        // Clean the back stack
        while (mNavController.navigateUp()) {}

        switch (view.getId()) {
            case R.id.arrows:
                // This is needed to keep bottom bar synced when user enters through home fragment or navigation drawer
                mBottomNavView.setSelectedItemId(R.id.nav_arrows);
                break;
            case R.id.play:
                mBottomNavView.setSelectedItemId(R.id.nav_play_stop);
                break;
            default:
                    Log.d(TAG,"Unknown menu option selected " + view.getId());
        }
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        Log.d(TAG, "onDestinationChanged");
        if (destination.getId() == R.id.homeFragment) {
            mBottomNavView.setVisibility(View.GONE);
            mNavigationView.setCheckedItem(R.id.nav_drawer_home);
        } else {
            mBottomNavView.setVisibility(View.VISIBLE);
        }
    }
}
