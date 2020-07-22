package ru.denfad.cheackheart.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import ru.denfad.cheackheart.MainActivity;

public class BluetoothService extends Service {
    private BluetoothWorker bluetoothWorker;
    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothWorker = BluetoothWorker.getInstance(mHandler);
        Log.d("service","start service");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service","stop service");
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        };
    };
}
