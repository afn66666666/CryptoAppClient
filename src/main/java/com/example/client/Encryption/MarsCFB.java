package com.example.client.Encryption;

import com.example.client.LoggedException;

public class MarsCFB extends Mars{
    public byte[] iv;
    public MarsCFB(){
        iv = generateIV();
    }
    @Override
    public byte[] encrypt(byte[] in)  {
        var blocks = splitInput(in);
        var result = new byte[blocks.length*16];
        for(int i = 0;i<blocks.length;++i){
            var encrypted = encryptBlock(blocks[i]);
            System.arraycopy(encrypted,0,result,i*16,16);
        }
        return result;
    }
}
