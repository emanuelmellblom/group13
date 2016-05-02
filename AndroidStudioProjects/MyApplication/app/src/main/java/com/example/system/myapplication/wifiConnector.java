package com.example.system.myapplication;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



/**
 * @authors Emanuel Mellbom, John Sundling
 * This class handels the connection to the RaspberryPi over wifi
 *
 * info TCP: http://developer.android.com/reference/java/net/Socket.html
 * info UDP: http://developer.android.com/reference/java/net/DatagramSocket.html
 */

public class wifiConnector extends MainActivity{
        String Wifiadress;
        int Wifiport;
        ImageView NewimageView;


    public wifiConnector(String adress, int port, ImageView imageView){
        Wifiadress = adress;
        Wifiport = port;
        NewimageView = imageView;
    }


    boolean connected = false;
    String IpAdress;
    int raspberryPort = 6666;
    Socket socket;
    BufferedReader in = null;
    int port;

    public void setIP(String ip){
       IpAdress = ip;
    }


    private InetAddress setInetaddres(){
        InetAddress inetaddres = null;
        try {
            inetaddres = InetAddress.getByName(IpAdress);
        }catch (IOException io){
            System.out.println("wifi failed");
        }
        return inetaddres;
    }


    private Socket declareSocket(){
        try {
            System.out.println("test");
            System.out.println(setInetaddres().toString());
            socket = new Socket(setInetaddres(), 6666);
        }catch (IOException io){
            System.out.println("No socket created");
            System.out.println(io);
        }
        return socket;
    }



    public void closeSocket(){
        try {
            socket.close();
        }catch (IOException io){
            System.out.println("Socket could not close");
        }
    }

    public void connect(String ip, int port){
        new Thread(new Runnable() {
            @Override
            public void run() {
        InetSocketAddress distanceAdress = new InetSocketAddress(IpAdress,6666);
        try {
            System.out.println(distanceAdress.toString());
            declareSocket();
            connected = true;
            readTheChar();
        }
        catch (NullPointerException nu){
            //System.out.println(nu);
            System.out.println("null in connect");
                }
            }
        }).start();
    }

    public void readTheChar(){
            BufferedReader read;
            List<Integer> ints = new ArrayList<Integer>();
            boolean test = false;
            int pos = 0;
            if(socket.isConnected()) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                while (true) {
                   byte[] newBytes =  inData(buffer);
                    if(newBytes.length>0) {
                        System.out.println(newBytes.length);
                        buffer = new ByteArrayOutputStream();
                        Bitmap im = BitmapFactory.decodeByteArray(newBytes, 0, newBytes.length);
                        final Drawable theimage = new BitmapDrawable(Resources.getSystem(), im);
                        final ImageView theImageView = NewimageView;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            theImageView.setImageDrawable(theimage);
                            }
                        });
                    }
                    if(!socket.isConnected()){
                        break;
                    }
                }
            }
        System.out.println("out of loop");
    }

    public byte[] inData(ByteArrayOutputStream buff){

        //String filename = "/data/data/11600-11600/com.example.system.MARS/test.txt";
        //System.out.println("first");
        //System.out.println("hej "+ getCacheDir());
        //System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath());
        //System.out.println(this.getFilesDir());
        //File tmpVideo = new File(filename);//new File(this.getApplicationContext().getFilesDir() + "/tmp.png"); //filename

        //File tmpFile;

       /* File f = new File("data/data/");
        File[] files = f.listFiles();
        for(File inFile : files){
            System.out.println(inFile.getAbsolutePath());
        }

        FileOutputStream outputStream;
        System.out.println(Environment.getDataDirectory());*/
        try {
            //FileOutputStream outputStream;

            //System.out.println("second");
            //outputStream = openFileOutput(filename, Context.MODE_PRIVATE);

            //writer = new FileWriter(tmpFile);
        /*    MediaPlayer mediaPlayer = new MediaPlayer();
            FileDescriptor fd;
            FileInputStream input = (FileInputStream)socket.getInputStream();

            mediaPlayer.setDataSource(input.getFD());
            input.

            mediaPlayer.prepare();
            mediaPlayer.start();*/



            //BufferedOutputStream out = new BufferedOutputStream();
            boolean waitingConnection = false;

            while(true){
                if(socket.getInputStream().available() <= 0 && waitingConnection) {
                    break;
                }
                waitingConnection = true;
                int ch = socket.getInputStream().read();
                buff.write(ch);
            }
            System.out.println("finished");
            buff.close();
            return buff.toByteArray();
       }catch (IOException io){
           System.out.println(io);
       }
        return buff.toByteArray();
    }


}





