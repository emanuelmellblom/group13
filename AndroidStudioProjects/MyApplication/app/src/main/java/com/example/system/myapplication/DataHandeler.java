package com.example.system.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @authors Emanuel Mellblom, John Sundling
 * This class handles the transfer of image data from the raspberry Pi to the app
 * It also handles displaying the images in the app.
 */

public class DataHandeler extends AppCompatActivity {
    static int collectorIndex;
    static int storageIndex;
    final  int size;
    static int memory;
    static byte[][] inData;
    static private boolean decoderBusy;
    static List<Byte> bytelist  = new ArrayList<Byte>();
    static BooleanC threadBusy = new BooleanC(false);
    static BooleanC test = new BooleanC(false);
    char[] endSequence={'9', '6', '0', '3', '1', '7', '2', '3', '1', '4'};
    int endSequenceIndex=0;
    char[] soi = {255, 216};
    int soiIndexValue = 0;
    ImageView raspImg;
    public static int packetSize = 2048;


    public DataHandeler(int size, ImageView image){
        this.size = size;
        memory = 0;
        this.collectorIndex = 0;
        this. storageIndex = 0;
        this.inData = new byte[size][size];
        this.decoderBusy = false;
        this.raspImg = image;
    }

    public boolean push(byte[] data){
        //System.out.println("index storage " + storageIndex);
        if((storageIndex+1)%size != collectorIndex){
            this.inData[storageIndex] = data;
            storageIndex++;
            storageIndex = storageIndex%size;
            return  true;
        }
        return false;
    }

    public byte[] poll(){
        //System.out.println("index collector "+collectorIndex);

        if(storageIndex!=collectorIndex){
            byte[] temp = inData[collectorIndex];
            this.collectorIndex++;
            this.collectorIndex = collectorIndex%size;
            return temp;
        }
        return null;
    }

    public byte[] peek(){
        if(storageIndex!=collectorIndex){
            return inData[collectorIndex];
        }
        return null;
    }

    public boolean toBoolean(int orginal){
        return orginal==1?true:false;
    }

    public boolean parseSequence(byte input) {
        //if(input=='9') System.out.println("parsing " + (char)this.endSequence[this.endSequenceIndex]+ " verses " + (char)input);
        if((this.endSequence[this.endSequenceIndex])== (input)){
            boolean endSequenceFound=endSequenceIndex==(9)?toBoolean((endSequenceIndex-=10)+2):false;
            //boolean test=this.endSequenceIndex==9?true:false;
            this.endSequenceIndex++;
            return endSequenceFound; //6
        }else{
            this.endSequenceIndex=0;
            return false;
        }
    }

    public boolean parseSOI(byte in){
        if(in==this.soi[this.soiIndexValue]){
            boolean soiFound=soiIndexValue==1?toBoolean((soiIndexValue=-1)+2):false;
            this.soiIndexValue++;
            return soiFound;
        }else {
            this.soiIndexValue = 0;
            return false;
        }
    }

    int count = 0;
    public void decodeBuffer(){
        if(!decoderBusy) {
            if(bytelist.size()>0){
                bytelist.clear();
            }
            decoderBusy = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    while(peek()!=null){
                        byte[] temp= poll();
                        for(int i = 0; i<packetSize; i++){
                            int tempInt=temp[i];
                            //if(tempInt!=0){
                            if(parseSOI(temp[i])){
                                System.out.println("Found beginning of image");
                            }
                            if(!parseSequence(temp[i])) {
                                bytelist.add((byte) tempInt);
                                count++;
                            }else{
                                //System.out.println("Endsequence = 0");
                                int size = bytelist.size();

                                //Remove the last bytes
                                if(size>=7){
                                    for (int j = (size - 1); j > size - 10; j--){
                                        bytelist.remove(j);
                                    }
                                    //displayImages(bytelist);
                                    bytelist.clear();
                                }
                                endSequenceIndex =0;
                                break;
                            }
                        }
                        if(peek()==null) {
                            try {
                                //System.out.println("peek");
                                Thread.sleep(500);
                            }catch (InterruptedException ie){
                                System.out.println(ie);
                            }
                        }
                        if(test.getBool()) {
                            break;
                        }
                    }
                    decoderBusy = false;
                    /*int size = bytelist.size();

                    //Remove the last bytes
                    if(size>=7){
                        for (int i = (size - 1); i > size - 8; i--){
                            bytelist.remove(i);
                        }
                        displayImages(bytelist);
                    }*/
                }
            }, 0);
        }
    }

    /*public byte[] signdByte(byte[] arr){
        byte[] newBytes = new byte[arr.length];
        int k;
        for(int i = 0; i<arr.length;i++){
            k = arr[i]&0xff;
            newBytes[i] = (byte)k;
        }
        return newBytes;
    }*/


    public void displayImages(List<Byte> list){
        //System.out.println("hej");
        byte[] imageList = constructByteArray(list);
        Bitmap im = BitmapFactory.decodeByteArray(imageList, 0, imageList.length);
        final Drawable theimage = new BitmapDrawable(Resources.getSystem(), im);
        VideoDisplay.setImages(theimage);

    }

    public byte[] constructByteArray(List<Byte> list){
        byte[] array = new byte[list.size()];
        for(int i = 0; i<array.length;i++){
           array[i] = list.get(i);
        }
        return array;
    }

    private void appendByteBuffer(byte[] bytes, FileOutputStream fileOut){
        try {
            OutputStreamWriter writer = new OutputStreamWriter(fileOut);
            for (int i = 0; i<bytes.length;i++) {
                writer.append((char)bytes[i]);
            }
            writer.close();
        }catch (IOException io){
            System.out.println(io);
        }
    }
}
