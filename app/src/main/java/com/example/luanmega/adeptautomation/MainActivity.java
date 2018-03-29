package com.example.luanmega.adeptautomation;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter myBluetooth = null;
    Set<BluetoothDevice> pairedDevices;
    Button btnPaired;
    ListView deviceList;
    public static String EXTRA_ADDRESS = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPaired = (Button) findViewById(R.id.button);
        deviceList = (ListView) findViewById(R.id.listView);
        ActiveBluetooth();

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList(); //method that will be called
            }
        });
    }

    private void ActiveBluetooth(){
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(myBluetooth == null){
            //Show error message
            Toast.makeText(getApplicationContext(), "Bluetooth no disponible", Toast.LENGTH_LONG).show();;
            finish();
        }
        else{
            if(myBluetooth.isEnabled()){

            }
            else{
                //Ask user to turn Bluetooth on
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon, 1);
            }
        }
    }

    private void pairedDevicesList(){
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        BluetoothDevice bt;
        if(pairedDevices.size() > 0){
            for(Object  bt2 : pairedDevices ){
                bt = (BluetoothDevice) bt2;
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device name and the address
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            Intent i = new Intent(MainActivity.this, ControlActivity.class);
            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };
}
