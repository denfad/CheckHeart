package ru.denfad.cheackheart;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.denfad.cheackheart.models.User;
import ru.denfad.cheackheart.network.NetworkService;
import ru.denfad.cheackheart.services.BluetoothService;
import ru.denfad.cheackheart.services.BluetoothWorker;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences mSharedPreferences;
    private BluetoothWorker bluetoothWorker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        bottomNavigationView = findViewById(R.id.navigation);
        loadFragment(PulseFragment.newInstance());

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User user = new Gson().fromJson(mSharedPreferences.getString("user", ""), User.class);
        NetworkService.getInstance()
                .getJSONApi()
                .authUser(user.getLogin(), user.getPassword())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        mSharedPreferences.edit().putString("user", new Gson().toJson(response.body())).apply();
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
            }
        });

        bluetoothWorker = BluetoothWorker.getInstance(mHandler);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bluetoothWorker.getmState()==BluetoothAdapter.STATE_CONNECTED) startService(new Intent(this, BluetoothService.class));
        else bluetoothWorker.stopAllThreads();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(bluetoothWorker.getmState()==BluetoothAdapter.STATE_CONNECTED) {
            stopService(new Intent(this, BluetoothService.class));
            bluetoothWorker = BluetoothWorker.getInstance(mHandler);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bluetoothWorker.getmState()==BluetoothAdapter.STATE_CONNECTED) startService(new Intent(this, BluetoothService.class));
        else bluetoothWorker.stopAllThreads();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothAdapter.STATE_OFF:
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    break;
                case BluetoothAdapter.STATE_ON:
                    bluetoothWorker.start();
                    GetDeviceDialog dialog = new GetDeviceDialog(MainActivity.this);
                    dialog.show();
                    break;
                case BluetoothAdapter.ERROR:
                    Toast.makeText(getApplicationContext(),"Bluetooth не поддерживается на вашем устройстве!",Toast.LENGTH_LONG).show();
                    break;
                case BluetoothAdapter.STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(),"Успешное подключение",Toast.LENGTH_LONG).show();
                    break;
            }
        };
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            bluetoothWorker.start();
            Toast.makeText(getApplicationContext(),"Bluetooth включен",Toast.LENGTH_SHORT).show();
            GetDeviceDialog dialog = new GetDeviceDialog(MainActivity.this);
            dialog.show();
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class GetDeviceDialog extends Dialog {


        public GetDeviceDialog(@NonNull Context context) {
            super(context);

            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes( params);
            this.setContentView(R.layout.connect_device_dialog);

            final Set<BluetoothDevice> deviceSet = bluetoothWorker.getPairedDeviceSet();
            List<String> devicesName = new ArrayList<>();
            for(BluetoothDevice device: deviceSet) {
                Log.e("fwef",device.getName() + "\n" + device.getAddress());
                devicesName.add(device.getName() + "\n" + device.getAddress());
            }
            final ListView list = findViewById(R.id.paired_devices);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,devicesName);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String  itemValue = (String) list.getItemAtPosition(position);
                    String MAC = itemValue.substring(itemValue.length() - 17); // Вычленяем MAC-адрес
                    bluetoothWorker.connect(MAC);
                    GetDeviceDialog.this.cancel();
                }
            });

        }
    }


}
