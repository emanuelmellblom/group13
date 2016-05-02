package com.example.system.myapplication;

/**
 * Created by System on 2016-04-26.
 */
public class ListC {

    private byte[] array;
    private int size;
    private byte[] tpmArr;


    public ListC(){
        this.array = new byte[0];
        this.size = 0;
    }

    public void add(byte bytes){
        byte[] tempArr = new byte[size()+1];
        for(int i = 0; i<size(); i++){
            tempArr[i] = array[i];
        }
        tempArr[size()] = bytes;
        array = tempArr;
        size++;
    }

    public int size(){
        return size;
    }

    public void remove(){
        array = new byte[size()];
    }

    public void deleteLast(int numElements){
        int numElementsLeft = size()-numElements;
        byte[] tempArr = new byte[numElementsLeft];
        for(int i = 0; i<numElementsLeft; i++){
            tempArr[i] = array[i];
        }
        array = tempArr;
        size = numElementsLeft;
    }

    public byte[] toArray(){
        return array;
    }

}
