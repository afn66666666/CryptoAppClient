package com.example.client;

public class Message {

    public Message(String name, String type, String status, int size, byte[] data){
        this.name = name;
        this.type = type;
        this.status = status;
        this.data = data;
        this.size = size;
    }
    public int size;
    public String name;
    /**text,binary,etc*/
    public String type;
    /**start of file,middle or end*/
    public String status;
    public byte[] data;
}