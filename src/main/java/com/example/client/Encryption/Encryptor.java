package com.example.client.Encryption;

import com.example.client.LoggedException;

public interface Encryptor {
    byte[] encrypt (byte[] Block) throws LoggedException;
    byte[] decrypt(byte[] block) throws LoggedException;
    void setKey(byte[] key);
}
