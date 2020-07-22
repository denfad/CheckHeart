package ru.denfad.cheackheart.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import ru.denfad.cheackheart.repository.Dao;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;

public class BluetoothWorker {

    private final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";
    private static final String NAME = "CheckHeart";
    private int mState;
    private static BluetoothWorker instance;
    private BluetoothAdapter bluetoothAdapter;
    private final UUID myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
    private ThreadConnectBTdevice myThreadConnectBTdevice;
    private ThreadConnected myThreadConnected;
    private AcceptThread accept;
    private final Handler mHandler;
    private Dao dao;

    private BluetoothWorker(Handler handler){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler=handler;
        dao = Dao.getInstance();


        if (bluetoothAdapter!=null) {
            // Bluetooth включен. Работаем.
            if (bluetoothAdapter.isEnabled()) {
                // Bluetooth включен. Работаем.
                Message msg = mHandler.obtainMessage(BluetoothAdapter.STATE_ON);
                mHandler.sendMessage(msg);
            }
            else
            {
                Message msg = mHandler.obtainMessage(BluetoothAdapter.STATE_OFF);
                mHandler.sendMessage(msg);
            }
        }
        else{
            Message msg = mHandler.obtainMessage(BluetoothAdapter.ERROR);
            mHandler.sendMessage(msg);
        }

    }

    public static BluetoothWorker getInstance(Handler handler){
        if(instance==null){
            instance = new BluetoothWorker(handler);
        }
        return instance;
    }

    public void start(){
        accept = new AcceptThread();
        accept.start();
    }
    public void connect(BluetoothDevice device){
        myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
        myThreadConnectBTdevice.start();
    }

    public void connect(String MAC){
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(MAC);
        myThreadConnectBTdevice = new ThreadConnectBTdevice(device);
        myThreadConnectBTdevice.start();
    }

    public Set<BluetoothDevice> getPairedDeviceSet(){
        return bluetoothAdapter.getBondedDevices();
    }


    public void stopAllThreads(){
        if(myThreadConnectBTdevice!=null) myThreadConnectBTdevice.cancel();
        if(myThreadConnected!=null) myThreadConnected.cancel();
        if(accept!=null) accept.cancel();
        mState = STATE_DISCONNECTED;
    }

    public void writeData(String data){
        if(myThreadConnected!=null) {
            myThreadConnected.write(data.getBytes());
        }
    }

    public int getmState() {
        return mState;
    }

    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp =bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, myUUID);
            } catch (IOException e) {
            }
            mmServerSocket = tmp;
        }

        @Override
        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            // Listen to the server socket if we're not connected

            while (true) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    myThreadConnected = new ThreadConnected(socket);
                    myThreadConnected.start(); // запуск потока приёма и отправки данных
                }
            }
        }


        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ThreadConnectBTdevice extends Thread { // Поток для коннекта с Bluetooth

        private BluetoothSocket bluetoothSocket = null;

        private ThreadConnectBTdevice(BluetoothDevice device) {

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
                Log.d("Bluetooth","create socket");
            }

            catch (IOException e) {
                e.printStackTrace();
                Log.e("Bluetooth","not create socket");

            }
        }


        @Override
        public void run() { // Коннект

            boolean success = false;

            try {
                bluetoothSocket.connect();
                success = true;
                Log.d("Bluetooth","connected");
                mState = STATE_CONNECTED;
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.e("Bluetooth","exception while trying connected");
                try {
                    bluetoothSocket.close();
                    mState = STATE_DISCONNECTED;
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(success) {  // Если законнектились, тогда открываем панель с кнопками и запускаем поток приёма и отправки данных
                myThreadConnected = new ThreadConnected(bluetoothSocket);
                myThreadConnected.start(); // запуск потока приёма и отправки данных
            }
        }


        public void cancel() {
            try {
                bluetoothSocket.close();
                mState = STATE_DISCONNECTED;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    } // END ThreadConnectBTdevice:

    private class ThreadConnected extends Thread {    // Поток - приём и отправка данных

        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;
        private final BluetoothSocket mmSocket;
        private String text;

        public ThreadConnected(BluetoothSocket socket) {
            mmSocket=socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = mmSocket.getInputStream();
                out = mmSocket.getOutputStream();
                Log.d("Bluetooth","Create streams");
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }


        @Override
        public void run() { // Приём данных
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = connectedInputStream.read(buffer);
                    final String strIncom = new String(buffer, 0, bytes);
                    if(strIncom!=null) dao.add(strIncom);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }


        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
                connectedOutputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void cancel(){
            try{
                mmSocket.close();
                mState = STATE_DISCONNECTED;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
