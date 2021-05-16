package com.app.vipsaffinity.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.vipsaffinity.R;
import com.app.vipsaffinity.databinding.ActivityMainBinding;
import com.app.vipsaffinity.fragments.HomeFragment;
import com.app.vipsaffinity.fragments.PollFragment;
import com.app.vipsaffinity.fragments.ProfileFragment;
import com.app.vipsaffinity.fragments.SearchFragment;
import com.app.vipsaffinity.fragments.TeachersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Field Declaration
    ActivityMainBinding binding;
    Map<Integer, Fragment> fragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.nav_home, new HomeFragment());
        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragmentMap.get(R.id.nav_home)).commit();
        setBottomNavigationViewOnClickListener();
    }

    private void setBottomNavigationViewOnClickListener() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (binding.bottomNavigation.getSelectedItemId() == itemId)
                    return false;

                switch (itemId) {
                    case R.id.nav_home:
                        if (fragmentMap.get(R.id.nav_home) == null)
                            fragmentMap.put(R.id.nav_home, new HomeFragment());
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragmentMap.get(R.id.nav_home)).commit();
                        break;

                    case R.id.nav_search:
                        if (fragmentMap.get(R.id.nav_search) == null)
                            fragmentMap.put(R.id.nav_search, new SearchFragment());
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragmentMap.get(R.id.nav_search)).commit();
                        break;

                    case R.id.nav_poll:
                        if (fragmentMap.get(R.id.nav_poll) == null)
                            fragmentMap.put(R.id.nav_poll, new PollFragment());
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragmentMap.get(R.id.nav_poll)).commit();
                        break;

                    case R.id.nav_teachers:
                        if (fragmentMap.get(R.id.nav_teachers) == null)
                            fragmentMap.put(R.id.nav_teachers, new TeachersFragment());
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragmentMap.get(R.id.nav_teachers)).commit();
                        break;
                    case R.id.nav_profile:
                        if (fragmentMap.get(R.id.nav_profile) == null)
                            fragmentMap.put(R.id.nav_profile, new ProfileFragment());
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragmentMap.get(R.id.nav_profile)).commit();
                        break;
                }
                return true;
            }
        });
    }
}