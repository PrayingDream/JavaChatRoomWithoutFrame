package com.ccc.cpx.CharRoom.Server;

import java.io.*;
import java.net.Socket;

public class ChatSocket extends Thread {
    Socket socket;
    String name = null;

    public ChatSocket(Socket socket) {
        this.socket = socket;
    }

    public void out(String name, String out) {
        try {
            socket.getOutputStream().write((name + ":" + out + "\n").getBytes("utf-8"));
            socket.getOutputStream().flush();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    //重写out方法
    public void out(String out){
        try {
            socket.getOutputStream().write(("服务器" + ":" + out + "\n").getBytes("utf-8"));
            socket.getOutputStream().flush();
        } catch (IOException e) {
            ServerManager.getServerManager().remove(this);
        }
    }

    public void privateOut(String outName, String inName,String out) {
        try{
            socket.getOutputStream().write((outName + "-->" + inName + "(私聊):" + out + "\n").getBytes("utf-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        out("你已连接到本服务器");
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(), "UTF-8"));
            String line = null;
            out("请输入用户名:");
            //循环读取数据，当输入流的数据不为空时，把数据写发送到每一个客户端
            while ((line = br.readLine()) != null) {
                if (name == null) {
                    name = line;
                    out("您以" + name + "为用户名加入群聊");
                    ServerManager.getServerManager().publish("服务器", this ,name + "加入群聊");
                    continue;
                }
                //加入判断群聊还是私聊
                if (line.charAt(0) == '@') {
                    //查询全部在线的用户
                    if(line.length() == 2 && line.charAt(1) == '@'){
                        out(ServerManager.getServerManager().getAllClient());
                        continue;
                    }
                    String outName = null;
                    String outLine = null;
                    int space = -1;
                    for (int i = 1; i < line.length(); i++) {
                        if (line.charAt(i) == ' ') {
                            space = i;
                            break;
                        }
                    }
                    //System.out.println(space);
                    if (space > 1) {
                        outName = line.substring(1, space);
                        //System.out.println(outName);
                        outLine = line.substring(space + 1);
                        //System.out.println(outLine);
                        ChatSocket csOut = ServerManager.getServerManager().getChatSocket(outName);
                        //判断用户是否存在
                        if (csOut == null) {
                            out( "不存在该用户请重新@");
                        } else if (csOut == this){
                            out( "请误@自己");
                        }else {
                            ServerManager.getServerManager().privatePublish(this , csOut, outLine);
                        }
                    } else {
                        out("请按照正确格式私聊:@ 用户名 文字内容");
                    }
                } else {
                    ServerManager.getServerManager().publish(name, this, line);
                }
            }
            br.close();
            ServerManager.getServerManager().remove(this);
        } catch (IOException e) {
            //清除断开连接的客户端通信地址
            ServerManager.getServerManager().remove(this);
        }
    }
}
