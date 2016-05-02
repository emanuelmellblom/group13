package com.example.system.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author Emanuel Mellblom
 * Class for making different alert boxes.
 */
public class AlertBoxes extends MainActivity{

    //Display an error message if the bluetooth is disconnected
    public static void bluetoothAlert(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Bluetooth disconnected")
                .setMessage("Do you want to connect?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BtConnection.blueTooth();
                        BtConnection.setBluetoothData();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //Display error message if bluetooth is not activated on the device
    public static void turnOnBluetooth(Context context){
        AlertDialog turnOnBluetooth = new AlertDialog.Builder(context).create();
        turnOnBluetooth.setTitle("No Bluetooth");
        turnOnBluetooth.setMessage("Make sure that you have bluetooth running on your device.");
        turnOnBluetooth.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        turnOnBluetooth.show();
    }

    //New Display message when an obstacle is detected
    public static void obstacleDetected(Context context){
        AlertDialog turnOnBluetooth = new AlertDialog.Builder(context).create();
        turnOnBluetooth.setTitle("Obstacle detected");
        turnOnBluetooth.setMessage("The car detected an obstacle in front of the car be careful");
        turnOnBluetooth.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        JoystickControl.obstacleAlertisShown = false;
                    }
                });
        turnOnBluetooth.show();
    }
}



