package com.example.client.Encryption;

import java.math.BigInteger;
import java.util.Random;

public class TestMillerRabin implements PrimeNumberTest {
    @Override
    public boolean primalTest(BigInteger n, int probability) {
        if (n.compareTo(BigInteger.valueOf(2)) < 0) {
            return false;
        }
        if (n.compareTo(BigInteger.valueOf(2)) == 0 || n.compareTo(BigInteger.valueOf(3)) == 0) {
            return true;
        }
        if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            return false;
        }

        int s = 0;
        BigInteger d = n.subtract(BigInteger.ONE);
        while (d.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            s++;
            d = d.divide(BigInteger.valueOf(2));
        }

        Random rnd = new Random();
        for (int i = 0; i < probability; i++) {
            BigInteger a = new BigInteger(n.bitLength() - 1, rnd).add(BigInteger.ONE);
            BigInteger x = a.modPow(d, n);
            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
                continue;
            }
            boolean prime = false;
            for (int r = 1; r < s; r++) {
                x = x.modPow(BigInteger.valueOf(2), n);
                if (x.equals(BigInteger.ONE)) {
                    return false;
                }
                if (x.equals(n.subtract(BigInteger.ONE))) {
                    prime = true;
                    break;
                }
            }
            if (!prime) {
                return false;
            }
        }
        return true;
    }

}
