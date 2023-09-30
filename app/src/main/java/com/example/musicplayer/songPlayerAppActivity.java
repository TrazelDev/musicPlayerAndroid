package com.example.musicplayer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class songPlayerAppActivity extends AppCompatActivity {

    private HomePage songPlayerFragment = null;
    private UserProfileDisplay userProfileFragment = null;
    private AddSongs addSongsFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);

        setContentView(R.layout.activity_main);

        // Initialize the fragments
        if (songPlayerFragment == null) {songPlayerFragment = new HomePage(); }
        if (userProfileFragment == null) {userProfileFragment = new UserProfileDisplay(); }
        if (addSongsFragment == null) {addSongsFragment = new AddSongs(); }

        // Load the initial fragment (PlaySongsFragment)
        loadFragment(songPlayerFragment);

        // Initialize the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the initial selected item
        bottomNavigationView.setSelectedItemId(R.id.navigation_play);

        // Set up item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                if (item.getItemId() == R.id.navigation_play)
                {
                    loadFragment(songPlayerFragment);
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_profile)
                {
                    loadFragment(userProfileFragment);
                    return true;
                }
                else if (item.getItemId() == R.id.navigation_addSongs)
                {
                    loadFragment(addSongsFragment); // Load the AddSongs fragment when the item is selected
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        // adding the transaction to the back stack which make it so the fragment state is being state and the functions
        // of onCreate and onCreateView are not recalled when the fragment is switched to again:
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
