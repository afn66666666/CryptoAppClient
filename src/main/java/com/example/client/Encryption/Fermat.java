package com.example.client.Encryption;

import java.math.BigInteger;
import java.util.Random;

public class Fermat implements PrimeNumberTest {
    @Override
    public boolean primalTest(BigInteger p, int probability) {
        if (p.equals(BigInteger.TWO) || p.equals(BigInteger.valueOf(3))) {
            return true;
        }
        for (int i = 0; i < probability; i++) {
            BigInteger a = new BigInteger(p.bitLength(), new Random());
            a = a.mod(p.subtract(BigInteger.TWO)).add(BigInteger.TWO);
            System.out.println(a);
            if (!sFermaTheorem(a, p)) {
                return false;
            }
        }
        return true;
    }

    private static boolean sFermaTheorem(BigInteger a, BigInteger p) {
//        var tmp1 = a.pow(p.subtract(BigInteger.ONE).intValue());
//        System.out.println(tmp1);
//        var tmp2 = tmp1.mod(p);
//        System.out.println("res " + tmp2);
        var temp = (a.mod(p)).subtract(a);
        var result = temp.mod(p);
//        BigInteger result = a.modPow(p.subtract(BigInteger.ONE), p);
        return result.intValue() == 0;
    }

}