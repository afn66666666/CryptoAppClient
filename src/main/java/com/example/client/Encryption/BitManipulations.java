package com.example.client.Encryption;

public class BitManipulations {
    public static byte[] intToBits(int val) {
        byte[] bits = new byte[32];
        for (int i = 0, j = 31; i < 32; ++i, --j) {
            bits[j] = (byte) ((val >> i) & 1);
        }
        return bits;
    }

    public static int bitsToInt(byte[] val) {
        int result = 0;
        for (int i = 0; i < 32; ++i) {
            result = result | val[i];
            if (i != 31) {
                result = result << 1;
            }
        }
        return result;
    }

    public static int bytesToInt(byte[] val) {
        return (val[0] & 0xff) | (val[1] & 0xFF) << 8 | (val[2] & 0xFF) << 16 | (val[3] & 0xFF) << 24;
    }

    public static byte[] intToBytes(int val) {
        return new byte[]{
                (byte) (val >> 24),
                (byte) (val >> 16),
                (byte) (val >> 8),
                (byte) val};
    }

    public static byte[] longToBits(long val){
        byte[] bits = new byte[64];
        for (int i = 0, j = 63; i < 64; ++i, --j) {
            bits[j] = (byte) ((val >> i) & 1);
        }
        return bits;
    }

    public static long bitsToLong(byte[] val) {
        long result = 0;
        for (int i = 0; i < 64; ++i) {
            result = result | val[i];
            if (i != 63) {
                result = result << 1;
            }
        }
        return result;
    }

    public static long bytesToLong(byte[] val){
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (val[i] & 0xFF);
        }
        return result;
    }

    public static byte[] longToBytes(long val){
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(val & 0xFF);
            val >>= 8;
        }
        return result;
    }

    /***
     * block MUST be same-sized!
     * @param val1
     * @param val2
     * @return
     */
    public static byte[] byteArrXor(byte[] val1, byte[] val2){
        var res = new byte[val1.length];
        for(int i = 0;i<val1.length;++i){
            res[i] = (byte) ((byte)val1[i]^val2[i]);
        }
        return res;
    }
}
