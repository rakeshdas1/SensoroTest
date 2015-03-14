package com.rakeshdas.sensorotest;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
private TextView mMainText;
private BeaconManagerListener beaconManagerListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainText = (TextView)findViewById(R.id.mainTextView);
        mMainText.setText("Looking for beacons...");
        startSensoroService();
        initSenoroListener();
    }
    private void startSensoroService() {
        //init the Sensoro manager
        SensoroManager sensoroManager = SensoroManager.getInstance(getApplicationContext());
        //Checks if BT is ON or OFF
        if(sensoroManager.isBluetoothEnabled()) {
            //Cloud services for Sensoro
            sensoroManager.setCloudServiceEnable(true);
            //start Sensoro SDK services
            try{
                sensoroManager.startService();
                Log.i("Service", "Started Sensoro Service");
            }
            //In case start fails
            catch (Exception e){
                e.printStackTrace();
            }
        }
        //Turn on BT if not on
        else{
            BluetoothAdapter mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            mBTAdapter.enable();
        }
        sensoroManager.setBeaconManagerListener(beaconManagerListener);
    }

    private void initSenoroListener() {
        //Detect or find Beacons
        beaconManagerListener = new BeaconManagerListener() {
            @Override
            public void onNewBeacon(Beacon beacon) {
                Log.i("INFO","Found a beacon with SN " + beacon.getSerialNumber());
                Toast.makeText(getApplicationContext(), "Found a beacon!", Toast.LENGTH_LONG).show();
                mMainText.setText("Found a beacon with SN " + beacon.getSerialNumber());
            }

            @Override
            public void onGoneBeacon(Beacon beacon) {
                Log.i("INFO","Lost a beacon!");
                Toast.makeText(getApplicationContext(), "Lost a beacon!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUpdateBeacon(ArrayList<Beacon> arrayList) {
                mMainText.setText("Looking for beacons...");

            }
        };
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
