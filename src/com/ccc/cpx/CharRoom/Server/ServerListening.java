package com.ccc.cpx.CharRoom.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListening extends Thread {
    public static void main(String[] args) {
        //启动服务器监听
        new ServerListening().start();
        System.out.println("服务器已经成功启动!");
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(3000);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("有客户端连接3000端口");
                ChatSocket csChatSocket = new ChatSocket(socket);
                csChatSocket.start();
                ServerManager.add(csChatSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
