package com.ccc.cpx.CharRoom.Server;

import java.util.Vector;

public class ServerManager {
    private ServerManager() {
    }

    private static final ServerManager sm = new ServerManager();

    public static ServerManager getServerManager() {
        return sm;
    }

    static Vector<ChatSocket> vector = new Vector<ChatSocket>();

    public static void add(ChatSocket cs) {
        vector.add(cs);
    }

    public void remove(ChatSocket cs) {
        System.out.println("移除" + cs.name);
        vector.remove(cs);
        publish("服务器", cs.name + "已断开连接");
    }

    //按名字返回私聊对象
    public ChatSocket getChatSocket(String name) {
        for (int i = 0; i < vector.size(); i++) {
            ChatSocket csChatSocket = vector.get(i);
            if (csChatSocket.name.equals(name)) {
                return csChatSocket;
            }
        }
        return null;
    }

    //查看全部在线的用户
    public String getAllClient() {
        String name = "";
        for (int i = 0; i < vector.size(); i++) {
            name += vector.get(i).name + " ";
        }
        return  name;
    }

    //群发
    public void publish(String csOutName, ChatSocket cs, String out) {
        //System.out.println(vector.size());
        System.out.println(csOutName + "(群聊):" + out);

        for (int i = 0; i < vector.size(); i++) {
            ChatSocket csChatSocket = vector.get(i);
            if (!cs.equals(csChatSocket)) {
                //System.out.println("发送群聊信息");
                csChatSocket.out(csOutName, out);
            } else {
                csChatSocket.out("自己", out);
            }
        }
    }

    public void publish(String csOutName, String out) {
        System.out.println(csOutName + "(群聊):" + out);
        for (int i = 0; i < vector.size(); i++) {
            ChatSocket csChatSocket = vector.get(i);
            csChatSocket.out(csOutName, out);
        }
    }

    //私聊
    public void privatePublish(ChatSocket csOut, ChatSocket csIn, String out) {
        System.out.println(csOut.name + "-->" + csIn.name + ":" + out);
        csIn.privateOut(csOut.name, csIn.name, out);
        csOut.privateOut(csOut.name, csIn.name, out);
    }
}
