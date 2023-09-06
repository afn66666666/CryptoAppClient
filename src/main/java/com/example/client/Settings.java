package com.example.client;

public class Settings {
    private static Definitions.EncryptionMods encryptionMode;
    private static String sessionKey;
    private static String publicKey;
    private static String encryptedPKey;
    private static int keyLength;

    public static Definitions.EncryptionMods getEncryptionMode() {
        return encryptionMode;
    }

    public static void setEncryptionMode(Definitions.EncryptionMods encryptionMode) {
        Settings.encryptionMode = encryptionMode;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static void setPublicKey(String publicKey) {
        Settings.publicKey = publicKey;
    }

    public static String getEncryptedPKey() {
        return encryptedPKey;
    }

    public static void setEncryptedPKey(String encryptedPKey) {
        Settings.encryptedPKey = encryptedPKey;
    }

    public static int getKeyLength() {
        return keyLength;
    }

    public static void setKeyLength(int keyLength) {
        Settings.keyLength = keyLength;
    }

    public static String getSessionKey() {
        return sessionKey;
    }

    public static void setSessionKey(String sessionKey) {
        Settings.sessionKey = sessionKey;
    }
}
