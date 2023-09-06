package com.example.client.Encryption;

import com.example.client.LoggedException;

public interface Encryptor {
    byte[] encrypt (byte[] block) throws LoggedException;
    byte[] decrypt(byte[] block) throws LoggedException;
    void setKey(byte[] key);
    byte[] getInitVector();
    void setInitVector(byte[] block);
}
