package com.example.client;

public class Settings {
    public String encryptionMode;
    public String publicKey;
    public String privateKey;

    public Settings(String enc,String publicKey, String privateKey){
        encryptionMode = enc;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
