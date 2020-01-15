package com.pface.admin.modules.jiekou.sock;

import com.pface.admin.modules.jiekou.Constants.Faceconstant;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

public class Main {
//    private static String uri = "ws://192.168.2.10/ws/"; //换成实际websocket地址
    private static Session session;

    private void start() {
        WebSocketContainer container = null;
        try {
            container = ContainerProvider.getWebSocketContainer();
        } catch (Exception ex) {
            System.out.println("error" + ex);
        }
        try {
            URI r = URI.create(Faceconstant.websock_uri);
            session = container.connectToServer(Client.class, r);
            session.setMaxTextMessageBufferSize(1024 * 1024);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Main client = new Main();
        client.start();
    }
}