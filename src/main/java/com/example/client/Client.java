package com.example.client;

import com.example.client.Controllers.MainViewController;
import com.example.client.Controllers.ProgressBarController;
import com.example.client.Encryption.*;
import com.google.gson.Gson;
import javafx.application.Platform;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class Client {
    public static final int FILE_BUF_SIZE = 1048576;
    public static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static Encryptor encryptor;
    private static byte[] fileBuf = null;
    private static int byteCounter = 0;

    public static File loadDir;


    public static void startSession() {
        try {
            clientSocket = new Socket("localhost", 4004);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            var encryptionMode = Settings.getEncryptionMode();
            if (encryptionMode == Definitions.EncryptionMods.MARS_EBC) {
                encryptor = new Mars();
            } else if (encryptionMode == Definitions.EncryptionMods.MARS_CBC) {
                encryptor = new MarsCBC();
            }
            writeSystemMessage(String.valueOf(Definitions.CREATE_SESSION_MACROS));
            writeSystemMessage(Definitions.encryptionModeName(Settings.getEncryptionMode()));

            //send initVector (if it needs)
            var initVector = encryptor.getInitVector();
            if (initVector.length != 0) {
                var b64 = Base64.getEncoder().encodeToString(initVector);
                writeSystemMessage(b64);
            }
            //log init info from server
            var id = readMessage();


            var xtr = new XTR(Entities.TestMode.Ferma, 45);
            var publicKey = xtr.getPublicKey();
            writePublicKey(publicKey);

            var stringMessage = in.readLine();
            var msg = new Gson().fromJson(stringMessage, Message.class);
            msg.data = Base64.getDecoder().decode(msg.data);
            var sessionKey = xtr.decrypt(msg.data);
            Settings.setSessionKey(new String(sessionKey));
            MainViewController.log("GET PRIVATE KEY : " + new String(sessionKey));

//            var userPublicKey = new XTR.PublicKey(userPublicKeyArr[0],userPublicKeyArr[1],
//                    new GFP2(userPublicKeyArr[0],userPublicKeyArr[2],userPublicKeyArr[3]),
//                    new GFP2(userPublicKeyArr[0],userPublicKeyArr[4],userPublicKeyArr[5]));
//            var encryptedKey = xtr.encrypt(Settings.getSessionKey().getBytes(),userPublicKey,userPublicKeyArr[6]);
//            writeEncryptionData("encryptedKey",encryptedKey);
        } catch (IOException e) {
            MainViewController.log("cant connect to server 4004. Try restart client application");
        }

    }

    public static void joinToSession(int id) {
        try {
            clientSocket = new Socket("localhost", 4004);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            writeSystemMessage("0");
            writeSystemMessage(String.valueOf(id));
            var encryptionMode = readMessage();
            if (encryptionMode.equals(Definitions.encryptionModeName(Definitions.EncryptionMods.MARS_CBC))) {
                encryptor = new MarsCBC();
                var iv = readMessage();
                var decrypted = Base64.getDecoder().decode((iv));
                encryptor.setInitVector(decrypted);
                Settings.setEncryptionMode(Definitions.EncryptionMods.MARS_CBC);
            }
            if (encryptionMode.equals(Definitions.encryptionModeName(Definitions.EncryptionMods.MARS_EBC))) {
                encryptor = new Mars();
                Settings.setEncryptionMode(Definitions.EncryptionMods.MARS_EBC);
            }
            var res = readMessage();

            // sending encryption info
        } catch (Exception e) {
            MainViewController.log("join to session error");
        }
    }

    public static String readMessage() {
        String result = "";
        try {
            var stringMessage = in.readLine();
            var msg = new Gson().fromJson(stringMessage, Message.class);
            msg.data = Base64.getDecoder().decode(msg.data);
            switch (msg.type) {
                case "system":
                    result = new String(msg.data);
                    break;
                case "text":
                case "file":

                    switch (msg.status) {
                        case "init":
                            if (msg.size != 0) {
                                msg.data = encryptor.decrypt(msg.data);
                                fileBuf = new byte[msg.size];
                                System.arraycopy(msg.data, 0, fileBuf, byteCounter, msg.data.length);
                                byteCounter += msg.data.length;
                            }
                            break;
                        case "loading":
                            msg.data = encryptor.decrypt(msg.data);
                            System.arraycopy(msg.data, 0, fileBuf, byteCounter, msg.data.length);
                            byteCounter += msg.data.length;
                            break;
                        case "ready":
                            if (fileBuf == null) {
                                fileBuf = new byte[msg.size];
                            }
                            msg.data = encryptor.decrypt(msg.data);
                            System.arraycopy(msg.data, 0, fileBuf, byteCounter, msg.data.length);
                            byteCounter += msg.data.length;
                            var file = new File("C:/Users/china/IdeaProjects/Client/loads/" + clientSocket.getLocalPort(), generateUniqueFileName(msg.name));
                            try (var writer = new FileOutputStream(file)) {
                                writer.write(fileBuf);
                            }
                            result = "ready";
                            byteCounter = 0;
                            break;
                    }
                    break;

            }
            System.out.println(result);
            MainViewController.log(result);
            return result;
        } catch (Exception e) {
            MainViewController.log("receiving message error.");
        }
        return null;
    }






    public static void closeResources() {
        writeSystemMessage("closed");
        try {
            clientSocket.close();
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            MainViewController.log("closing resources error " + e.getMessage());

        }
    }

    public static void sendFile(String fileName, byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    var paddingSize = 16 - data.length % 16;
                    var size = data.length + paddingSize;
                    int percent = size / 100;
                    int step = 1;
                    int currentPercents = 0;
                    encryptor.setKey(Settings.getSessionKey().getBytes());
                    for (int i = 0; i < data.length; i += FILE_BUF_SIZE) {
                        byte[] encryptedData;
                        String status;
                        if (i + FILE_BUF_SIZE >= data.length) {
                            var buf = new byte[data.length - i];
                            System.arraycopy(data, i, buf, 0, data.length - i);
                            encryptedData = encryptor.encrypt(buf);
                            status = "ready";
                        } else {
                            var buf = new byte[FILE_BUF_SIZE];
                            System.arraycopy(data, i, buf, 0, FILE_BUF_SIZE);
                            encryptedData = encryptor.encrypt(buf);
                            if (i == 0) {
                                status = "init";
                            } else {
                                status = "loading";
                            }
                        }
                        var b64 = Base64.getEncoder().encode(encryptedData);
                        var msg = new Message(fileName, "file", status, size, b64);
                        if (i == 0) {
                            progress(1);
                        }
                        if (i > percent * step) {
                            // progress for 1 iteration
                            step = i / percent;
                            currentPercents = step;
                            if (step >= 100) {
                                currentPercents = 100;
                            }
                            step += step;
                            MainViewController.log("loading file " + fileName + " : [" + currentPercents + "%]");
                            progress(currentPercents);
                        }
                        writeMessage(msg);
                    }
                    progress(100);
                    MainViewController.log("loading file " + fileName + " : [" + 100 + "%]");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private static void progress(int percentage){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ProgressBarController.updateProgress(percentage);
            }
        });
    }

    public static void uploadFile(File file) {
        String status = "";
        var msg = new Message(file.getName(), "load", "", 0, new byte[0]);
        //send uploading request to server
        writeMessage(msg);
        while (status != "ready") {
            status = readMessage();
        }
    }

    private static String generateUniqueFileName(String fileName) {
        var dir = new File("loads/" + clientSocket.getLocalPort());
        var buf = new ArrayList<String>();
        var files = Arrays.stream(dir.list()).toList();
        int counter = 1;
        String result = fileName;
        if (!files.contains(fileName)) {
            result = fileName;
        } else {
            while (files.contains(result)) {
                var st = new StringBuilder(result);
                var i = fileName.lastIndexOf(".");
                result = st.insert(i, "(" + Integer.toString(counter) + ")").toString();
                ++counter;
            }
        }
        return result;
    }

    private static void suffer() throws IOException {

        String gfg = "hello";
        var b = gfg.getBytes();
        var gfg2 = new String(b);
        var xtr = new XTR(Entities.TestMode.Ferma, 45);
        var publicKey = xtr.getPublicKey();
        for(int i = 0;i<7;++i) {
            sendBI(publicKey[i]);
            out.flush();
        }

        var stringMessage = in.readLine();
        var msg = new Gson().fromJson(stringMessage, Message.class);
        msg.data = Base64.getDecoder().decode(msg.data);

        var decr = xtr.decrypt(msg.data);




        var xtr2 = new XTR(Entities.TestMode.Ferma, 45);
        var receivedPKey = new XTR.PublicKey(publicKey[0],publicKey[1],
                new GFP2(publicKey[0],publicKey[2],publicKey[3]),
                new GFP2(publicKey[0],publicKey[4],publicKey[5]));
        var eData = xtr2.encrypt(new String("hello").getBytes(),receivedPKey,publicKey[6]);
        var resssuka = new String(xtr.decrypt(eData));
    }

    private static void sendBI(BigInteger val){
        var sVal = val.toString(10);
        System.out.println("send BI : " + sVal);
        var rawBytes = sVal.getBytes();
        var b64 = Base64.getEncoder().encode(sVal.getBytes());
        var msg = new Message("", "system", "ready", 0, b64);
        var msgString = new Gson().toJson(msg);
        try {

            out.write(msgString + "\n"); // отправляем сообщение на сервер

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void exchangeEncryptionData() {
        encryptor.setKey(Settings.getSessionKey().getBytes());
        var xtr = new XTR(Entities.TestMode.Ferma, 45);
//        var publicKey = xtr.getPublicKey();
//
//        writePublicKey(publicKey);
        var hostPublicKey = readPublicKey();
        var hostPKey = new XTR.PublicKey(hostPublicKey[0], hostPublicKey[1],
                new GFP2(hostPublicKey[0], hostPublicKey[2], hostPublicKey[3]),
                new GFP2(hostPublicKey[0], hostPublicKey[4], hostPublicKey[5]));

        var encryptedSessionKey = xtr.encrypt(Settings.getSessionKey().getBytes(), hostPKey,hostPublicKey[6]);
        writeSystemMessage(encryptedSessionKey);
        System.out.println("send message " + new String(encryptedSessionKey));
    }

    public static void writeMessage(Message msg) {
        try {
            if (out != null) {
                if (msg.type == "text") {
                    encryptor.setKey(Settings.getSessionKey().getBytes());
                    MainViewController.log("You send a message : " + new String(msg.data));
                    msg.data = encryptor.encrypt(msg.data);
                    msg.data = Base64.getEncoder().encode(msg.data);
                }
                var msgString = new Gson().toJson(msg);
                out.write(msgString + "\n"); // отправляем сообщение на сервер
                out.flush();
            } else {
                MainViewController.log("write message error : write buffer not initialized");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MainViewController.log(e.getMessage());
        }
    }

    public static void writeSystemMessage(String data) {
        var b64 = Base64.getEncoder().encode(data.getBytes());
        var msg = new Message("", "system", "ready", 0, b64);
        writeMessage(msg);
    }

    public static void writeSystemMessage(byte[] data){
        var b64 = Base64.getEncoder().encode(data);
        var msg = new Message("", "system", "", 0, b64);
        writeMessage(msg);
    }

    public static void writePublicKey(BigInteger[] key){
        for(int i = 0;i<7;++i){
            System.out.println("send BI : " + key[i].toString(10));
            writeSystemMessage(key[i].toString(10));
        }
    }

    public static BigInteger[] readPublicKey(){
        BigInteger[] arr = new BigInteger[7];
        for(int i = 0;i<7;++i){
            var msg = readMessage();
            var bigInteger = new BigInteger(msg);
//            System.out.println("get BI : " + msg);
            arr[i] = bigInteger;
        }
        return arr;
    }

    public static void writeEncryptionData(String type, byte[] data) {
        var b64 = Base64.getEncoder().encode(data);
        var msg = new Message("", type, "ready", 0, b64);
        writeMessage(msg);
    }
}
