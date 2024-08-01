package com.ccc.cpx.CharRoom.Client;

import java.util.Scanner;

public class ClientStart {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClientManager cm = new ClientManager();
        String cin;
        cm.start();
        while (true) {
            //System.out.println("请输入信息");
            cin = sc.nextLine();
            cm.send(cin);
            //System.out.println("信息发送完成");
        }
    }
}
