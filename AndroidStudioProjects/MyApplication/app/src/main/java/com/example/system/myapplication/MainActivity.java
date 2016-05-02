package com.example.system.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Main class for MARS SmartCar
 * @author  Emanuel Mellblom
 */

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_ENABLE_BT = 12;
    String raspberryIP = "172.20.10.5";
    int raspberryPort = 6666;
    static ImageView newImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (BtConnection.blueTooth()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    //Test wificonnection
    public void connectWifi(View view) {
        ImageView images = (ImageView) findViewById(R.id.images);
        wifiConnector rasp = new wifiConnector(raspberryIP, raspberryPort, images);
        rasp.setIP(raspberryIP);
        rasp.connect(raspberryIP, raspberryPort);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.buttonmode) {
            Intent intent = new Intent(this, buttonControl.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_jo) {
            Intent intent = new Intent(this, JoystickControl.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.connectToBluetooth) {
            BtConnection.setBluetoothData();
            BtConnection.blueTooth();
            return true;
        }
        if (id == R.id.action_video) {
            Intent intent = new Intent(this, VideoDisplay.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

