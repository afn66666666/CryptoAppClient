package com.example.client;

import com.example.client.Controllers.MainViewController;
import com.example.client.Encryption.*;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class Client {
    public static final int FILE_BUF_SIZE = 1024;
    public static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static Encryptor encryptor;
    private static byte[] fileBuf = null;
    private static int byteCounter = 0;
    private static int sessionId;
    public static File loadDir;


    public static void startSession(Settings settings) {
        try {
            clientSocket = new Socket("localhost", 4004);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            if (settings.encryptionMode.equals("MARS(EBC)")) {
                encryptor = new Mars();
            } else if (settings.encryptionMode.equals("MARS(CBC)")) {
                encryptor = new MarsCBC();
            } else if (settings.encryptionMode.equals("XTR")) {
                encryptor = new XTR(Entities.TestMode.Ferma, 15);
            }
            encryptor.setKey(settings.privateKey.getBytes());
//            publicKey = settings.publicKey;
//            privateKey = settings.privateKey;
            writeSystemMessage("1");
            writeSystemMessage(settings.encryptionMode);
            if (settings.encryptionMode.equals("MARS(CBC)")) {
                var b64 = Base64.getEncoder().encodeToString(((MarsCBC) encryptor).iv);
                writeSystemMessage(b64);
            }
            writeSystemMessage(settings.privateKey);
            readMessage();
            var id = readMessage();
            sessionId = Integer.valueOf(id);
        } catch (IOException e) {
            MainViewController.log("cant connect to server 4004. Try restart client application");
        }
    }


    public static void joinToSession(int id) {
        try {
            sessionId = id;
            clientSocket = new Socket("localhost", 4004);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            writeSystemMessage("0");
//            writeMessage(Integer.toString(sessionId));
            writeSystemMessage(Integer.toString(id));
            var encryptionMode = readMessage();
            if (encryptionMode.equals("MARS(CBC)")) {
                encryptor = new MarsCBC();
                var iv = readMessage();
                var decrypted = Base64.getDecoder().decode((iv));
                ((MarsCBC) encryptor).iv = decrypted;
            }
            var key = readMessage();
            if (encryptionMode.equals("MARS(EBC)")) {
                encryptor = new Mars();
            } else if (encryptionMode.equals("XTR")) {
                encryptor = new XTR(Entities.TestMode.Ferma, 15);
            }
            encryptor.setKey(key.getBytes());
            readMessage();

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

    public static void writeMessage(Message msg) {
        try {
            if (out != null) {
                if (msg.type == "text") {
                    MainViewController.log("You send a message : " + new String(msg.data));
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
                    int byteCounter = 0;
                    int percentage = 0;
                    for (int i = 0; i < data.length; i += FILE_BUF_SIZE) {
                        byte[] encryptedData;
                        String status;
                        if (i + FILE_BUF_SIZE >= data.length) {
                            var buf = new byte[data.length-i];
                            System.arraycopy(data,i,buf,0,data.length-i);
                            encryptedData = encryptor.encrypt(buf);
//                            buf = new byte[size - i];
//                            System.arraycopy(encryptedData, i, buf, 0, size - i);
                            status = "ready";
                        } else {
                            var buf = new byte[FILE_BUF_SIZE];
                            System.arraycopy(data,i,buf,0,FILE_BUF_SIZE);
                            encryptedData = encryptor.encrypt(buf);
//                            System.arraycopy(encryptedData, i, buf, 0, FILE_BUF_SIZE);
                            if (i == 0) {
                                status = "init";
                            } else {
                                status = "loading";
                            }
                        }
                        var b64 = Base64.getEncoder().encode(encryptedData);
                        var msg = new Message(fileName, "file", status, size, b64);
                        if (i > percent) {
                            ++percentage;
                            MainViewController.log("loading file " + fileName + " : [" + percentage + "%]");
                            percent += percent;
                        }
                        writeMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public static void uploadFile(File file) {
        String status = "";
        var msg = new Message(file.getName(), "load", "", 0, new byte[0]);
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
}
