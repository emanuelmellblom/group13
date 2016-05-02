package com.example.system.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.IOException;

/**
 * @author Emanuel Mellblom
 * This class handels the buttonControl option as well as reading from the
 * input stream and checking if there is an obstacle in front of the car.
 */


public class buttonControl extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * This is the different button events for the button control of the car.
     * It writes the output on the bluetooth outputstream and sends it to the car.
     */
    public void goForward(View view) {
        String forward = "c50|0";
        try {
            BtConnection.btOutputStream.write(forward.getBytes());
            System.out.println(forward);
            //New read sensor data
            //sensordata();
            //readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goBackwards(View view){
        String back = "c-50|0";
        try {
            BtConnection.btOutputStream.write(back.getBytes());
            System.out.println(back);
            //New read sensor data
            //sensordata();
            //readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void stopMovement(View view){
        String stop = "c0|0";

        try {
            BtConnection.btOutputStream.write(stop.getBytes());
            System.out.println(stop);
            //New read sensor data
            //sensordata();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void goLeft(View view){
        String left = "c50|-180";//-90
        try {
            BtConnection.btOutputStream.write(left.getBytes());
            System.out.println(left);
            //New read sensor data
            //sensordata();
            //readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }
    public void goRight(View view){
        String right = "c50|180"; //90
        try {
            BtConnection.btOutputStream.write(right.getBytes());
            System.out.println(right);
            //New read the sensor
            //sensordata();
            //readInput();
        } catch (IOException io) {
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    public void connectToBluetooth(View view){
        BtConnection.setBluetoothData();
        BtConnection.blueTooth();
    }


    //Read input from the bluetooth
    public void readInput(){
        try {
            boolean isShown = false;
            //char in = (char)BtConnection.btInputStream.read();
            //if (in == '1') {
            if(BtConnection.btInputStream.read() == 49){
                System.out.println("Obstacle detected");
                System.out.println("" + BtConnection.btInputStream.read());
                if(!JoystickControl.obstacleAlertisShown){
                    AlertBoxes.obstacleDetected(this);
                    isShown = true;
                }
            }else{}
        }catch(IOException io){
            //System.out.println("Error reading input");
            AlertBoxes.bluetoothAlert(this);
        }catch (IllegalStateException il){
            AlertBoxes.bluetoothAlert(this);
        }catch(NullPointerException nu){
            AlertBoxes.bluetoothAlert(this);
        }
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.buttonmode) {
            Intent intent = new Intent(this, buttonControl.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_jo){
            Intent intent = new Intent(this, JoystickControl.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.connectToBluetooth){
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
