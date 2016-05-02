package com.example.system.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 * @authors Emanuel Mellblom, John Sundling.
 * Class that handels the task of showing the video in the GUI
 * it also handels the initial connection to a UDP socket on the RaspberryPi.
 * info: http://developer.android.com/reference/android/widget/VideoView.html
 */

public class VideoDisplay extends AppCompatActivity {

    boolean connected = false;
    String ipAdress = "172.20.10.5";
    int port = 6666;
    Socket socket;
    BufferedReader in = null;
    File tmpVideo;
    DatagramSocket dataSocket;
    //static ImageView images;
    public static ImageView images;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView tempImage = (ImageView) findViewById(R.id.imageBox);
        images=tempImage;
        connect();
    }

    public static void setImages(Drawable drawable){
        images.setImageDrawable(drawable);
    }

    private InetAddress setInetaddres(){
        java.net.InetAddress inetaddres = null;
        try {
            inetaddres = java.net.InetAddress.getByName(ipAdress);
        }catch (IOException io){
            System.out.println("wifi failed");
        }
        return inetaddres;
    }


    private DatagramSocket declareSocket(){
        try {
            dataSocket = new DatagramSocket();
        }catch (IOException io){
            System.out.println(io);
        }
        return dataSocket;
    }

    public static boolean canWriteToStorage(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return  true;
        }
        return false;
    }

    public void connect(){
        connected = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                SocketAddress distanceAdress = new InetSocketAddress(port);
                try {
                    //testrtp.startSession();
                    System.out.println(distanceAdress.toString());
                    DatagramSocket newDatagramSocket = declareSocket();
                    //newDatagramSocket.bind(distanceAdress);
                    SocketAddress peerAdress = new InetSocketAddress(ipAdress, port);
                    newDatagramSocket.connect(peerAdress);
                    System.out.println("Connect");
                    //System.out.println(canWriteToStorage());
                    //New Test write to file

                 /*   File temp = new File(Environment.getExternalStorageDirectory()+"/testDirectory/");
                    temp.mkdir();
                    File newTemp = new File(temp.getAbsolutePath(),"textTest.txt");
                    temp.createNewFile();
                    temp.mkdir();
                    FileOutputStream out = new FileOutputStream(newTemp);
                    //OutputStreamWriter writer = new OutputStreamWriter(out);
                    //writer.append(ch);
                    String testData = "hej det är ett test";
                    out.write(testData.getBytes());
                    out.close();*/


                    //Send initial  data
                    byte[] initial = new byte[DataHandeler.packetSize];
                    DatagramPacket data = new DatagramPacket(initial, initial.length);
                    newDatagramSocket.send(data);

                    DatagramPacket recieved = new DatagramPacket(initial, initial.length);
                    int outdata = 0;
                    int counter = 0;
                    final DataHandeler dataHandeler = new DataHandeler(200, images);
                    final BooleanC decoderBusy=new BooleanC(false);

                    while(true) {
                        newDatagramSocket.receive(recieved);
                        System.out.println(counter++);
                        dataHandeler.push(Arrays.copyOf(recieved.getData(), DataHandeler.packetSize));
                        if(!decoderBusy.getBool()) {
                            decoderBusy.setBool(true);
                            VideoDisplay.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dataHandeler.decodeBuffer();
                                    decoderBusy.setBool(false);
                                }
                            });
                        }
                        recieved = new DatagramPacket(initial, initial.length);
                        //counter++;
                    }
                }
                catch (NullPointerException nu){
                    System.out.println(nu);
                    System.out.println("Null error in connect");
                }
                catch (IOException io){
                    System.out.println(io);
                    System.out.println("Io error in connect");
                }
            }
        }).start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        if(id == R.id.buttonmode){
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
        return super.onOptionsItemSelected(item);
    }
}





