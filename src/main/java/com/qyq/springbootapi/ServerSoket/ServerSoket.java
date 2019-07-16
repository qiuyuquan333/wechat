package com.qyq.springbootapi.ServerSoket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSoket {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);

        while (true){
            Socket accept = serverSocket.accept();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStreamReader reader = new InputStreamReader(accept.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            System.out.println("服务器端接收：" + line);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }
}

