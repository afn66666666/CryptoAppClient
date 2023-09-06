package com.example.client.Encryption;

import java.util.Arrays;

public class MarsCBC extends Mars {
    private byte[] initVector;


    public MarsCBC() {
        initVector = generateIV();
    }

    @Override
    public byte[] encrypt(byte[] in)  {
        byte[] prevEncrBlock = null;
        var totalSize = in.length;
        var paddingSize = 0;
        var padding = new byte[0];
        int i = 0;
        if (totalSize % 16 != 0) {
            paddingSize = 16 - totalSize % 16;
            padding = new byte[paddingSize];
            padding[0] = (byte) 0x80;
            for (i = 1; i < paddingSize; i++)
                padding[i] = 0;
        }
        var tempBuf = new byte[16];
        var resultBlock = new byte[totalSize + paddingSize];
        int paddingIndex = 0;
        for (i = 0; i < totalSize + paddingSize; ++i) {
            if (i != 0 && i % 16 == 0) {
                if (i == 16) {
                    prevEncrBlock = super.encryptBlock(BitManipulations.byteArrXor(tempBuf, initVector));
                    tempBuf = Arrays.copyOf(prevEncrBlock,16);
                } else {
                    tempBuf = super.encryptBlock(BitManipulations.byteArrXor(tempBuf, prevEncrBlock));
                    prevEncrBlock = Arrays.copyOf(tempBuf,16);
                }
                System.arraycopy(tempBuf, 0, resultBlock, i - 16, tempBuf.length);
            }

            if (i < totalSize) {
                tempBuf[i % 16] = in[i];
            } else {
                tempBuf[i % 16] = padding[paddingIndex % 16];
                paddingIndex++;

            }
        }


        if (prevEncrBlock == null) {
            prevEncrBlock = super.encryptBlock(BitManipulations.byteArrXor(tempBuf, initVector));
            tempBuf = prevEncrBlock;
        } else {
            tempBuf = super.encryptBlock(BitManipulations.byteArrXor(tempBuf, prevEncrBlock));
        }
        //can I go here???
//            tempBuf = super.encryptBlock(BitManipulations.byteArrXor(tempBuf, prevBlock));
        System.arraycopy(tempBuf, 0, resultBlock, i - 16, tempBuf.length);

        prevEncrBlock = null;
        return resultBlock;
    }

    @Override
    public byte[] decrypt(byte[] in) {
        byte[] p1 = null;
        byte[] prevEncBlock = null;
        var totalSize = in.length;
        byte[] resultBlock = new byte[totalSize];
        byte[] tempBuf = new byte[16];
        int i = 0;
        for (i = 0; i < totalSize; i++) {
            if (i > 0 && i % 16 == 0) {
                if (i == 16) {
                    prevEncBlock = Arrays.copyOf(tempBuf,16);
                    p1 = BitManipulations.byteArrXor(initVector, decryptBlock(tempBuf));
                    tempBuf = Arrays.copyOf(p1,16);
                } else {
                    var temp = Arrays.copyOf(tempBuf,16);
                    tempBuf = BitManipulations.byteArrXor(prevEncBlock, decryptBlock(tempBuf));
                    prevEncBlock = Arrays.copyOf(temp,16);
//                    prevBlock = Arrays.copyOf(tempBuf,16);
                }
                System.arraycopy(tempBuf, 0, resultBlock, i - 16, tempBuf.length);
            }
            if (i < totalSize) {
                tempBuf[i % 16] = in[i];
            }
        }
        if (p1 == null) {
            p1 = BitManipulations.byteArrXor(initVector, decryptBlock(tempBuf));
            tempBuf = p1;
        } else {
            tempBuf = BitManipulations.byteArrXor(prevEncBlock, decryptBlock(tempBuf));
        }
        System.arraycopy(tempBuf, 0, resultBlock, i - 16, tempBuf.length);
        resultBlock = deletePadding(resultBlock);
        return resultBlock;
    }

    @Override
    public byte[] getInitVector(){
        return initVector;
    }

    @Override
    public void setInitVector(byte[] bytes){
        initVector = bytes;
    }
}
