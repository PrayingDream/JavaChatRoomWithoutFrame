package com.ccc.cpx.CharRoom.Client;

import java.io.*;
import java.net.Socket;

public class ClientManager extends Thread {
    Socket socket;
    String ip = "127.0.0.1";
    BufferedReader reader;//定义读取数据的输入流
    PrintWriter writer;//定义写数据的输出流

    @Override
    public void run() {
        try {
            socket = new Socket(ip, 3000);
            System.out.println("成功连接服务器" + ip);
            writer = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream(), "UTF-8"));
            reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(), "UTF-8"));
            String line;
            //点读取到的数据不为空时，把读取的数据输出到窗口文本区中
            while ((line = reader.readLine()) != null) {
                show(line);
            }
            writer.close();//关闭输入输出流
            reader.close();
        } catch (IOException e) {
            writer = null;
            reader = null;
            System.out.println("当前服务器连接已断开");
        }
    }

    //发送信息处理
    public void send(String out) {
        if (writer != null) {
            writer.write(out + "\n");
            writer.flush();//写完数据后必须刷新缓存区，才能发出去
        } else {
            System.out.println("当前服务器连接已断开");
        }
    }

    public void show(String line) {
        System.out.println(line);
    }
}
