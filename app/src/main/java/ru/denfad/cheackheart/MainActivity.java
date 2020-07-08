package ru.denfad.cheackheart;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        bottomNavigationView = findViewById(R.id.navigation);
        loadFragment(PulseFragment.newInstance());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pulse_menu:
                        loadFragment(PulseFragment.newInstance());
                        return true;
                    case R.id.profile_menu:
                        loadFragment(ProfileFragment.newInstance());
                        return true;
                    case R.id.diagnose_menu:
                        loadFragment(DiagnoseFragment.newInstance());
                        return true;
                }
                return false;
            }});

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
}
