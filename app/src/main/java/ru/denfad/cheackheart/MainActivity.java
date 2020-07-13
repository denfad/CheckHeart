package ru.denfad.cheackheart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.denfad.cheackheart.models.User;
import ru.denfad.cheackheart.network.NetworkService;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        bottomNavigationView = findViewById(R.id.navigation);
        loadFragment(PulseFragment.newInstance());

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = new Gson().fromJson(mSharedPreferences.getString("user",""),User.class);
        NetworkService.getInstance()
                .getJSONApi()
                .authUser(user.getLogin(),user.getPassword())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        mSharedPreferences.edit().putString("user",new Gson().toJson(response.body())).apply();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                    }
                });
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
                    case R.id.graph_menu:
                        loadFragment(GraphicFragment.newInstance());
                        return true;
                    case R.id.statistic_menu:
                        loadFragment(StatsFragment.newInstance());
                        return true;
                }
                return false;
            }});

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
}
