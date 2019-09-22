package com.websarva.wings.android.mapsecond;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
//コミットしたかった

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new MapsFragment();
    final Fragment fragment3 = new pinnedmapsFragment();

    Fragment active = fragment2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        BottomNavigationView.OnNavigationItemSelectedListener nav_handler = new BottomNavigationView.OnNavigationItemSelectedListener() {

            int saved_state = 0;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        if (saved_state == 0 || active != fragment1) {
                            active = fragment1;
                        }
                        break;

                    case R.id.action_favorites:
                        if (saved_state == 0 || active != fragment2) {
                            active = fragment2;
                        }
                        Toast.makeText(MainActivity.this, "now", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_nearby:
                        if (saved_state == 0 || active != fragment3) {
                            active = fragment3;
                        }
                        Toast.makeText(MainActivity.this, "pin", Toast.LENGTH_SHORT).show();
                        break;
                }


                if (savedInstanceState == null) {

                    saved_state = 1;

                    Toast.makeText(MainActivity.this, "fragment reloaded", Toast.LENGTH_SHORT).show();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame_layout, active);

                    transaction.commit();



                }

                return true;

            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(nav_handler);
    }
}