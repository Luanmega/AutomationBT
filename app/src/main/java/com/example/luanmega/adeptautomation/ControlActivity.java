package com.example.luanmega.adeptautomation;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;

public class ControlActivity extends AppCompatActivity {
    ToggleButton toggleBtn;
    ToggleButton toggleBtnSala;
    ToggleButton togleBtnJardin;
    ToggleButton toggleBtnCafeteria;
    ToggleButton toggleBtnRiego;
    Button btnDisconnect;
    String address = "";
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    boolean isBTConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newInt = getIntent();
        address = newInt.getStringExtra(MainActivity.EXTRA_ADDRESS);
        setContentView(R.layout.activity_control);
        ConnectBT bt = new ConnectBT() ;
        bt.execute();
        toggleBtn = findViewById(R.id.toggleButton);
        toggleBtnCafeteria = findViewById(R.id.toggleBtnCafetera);
        toggleBtnSala = findViewById(R.id.toggleBtnSala);
        toggleBtnRiego = findViewById(R.id.toggleBtnRiego);
        btnDisconnect = findViewById(R.id.btnDis);
        toggleBtn.setTextOff("OFF");
        toggleBtn.setTextOn("ON");
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    turnOnLED("1");
                } else {
                    // The toggle is disabled
                    turnOffLED("2");
                }
            }
        });

        toggleBtnSala.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    turnOnLED("3");
                } else {
                    // The toggle is disabled
                    turnOffLED("4");
                }
            }
        });

        toggleBtnRiego.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    turnOnLED("5");
                } else {
                    // The toggle is disabled
                    turnOffLED("6");
                }
            }
        });

        toggleBtnCafeteria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    turnOnLED("7");
                } else {
                    // The toggle is disabled
                    turnOffLED("8");
                }
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Disconnect();
            }
        });


    }


    public class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ControlActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBTConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBTConnected = true;
            }
            progress.dismiss();
        }
    }

    public void turnOnLED(String valOn){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(valOn.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    public void turnOffLED(String valOff){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(valOff.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout
    }
}


