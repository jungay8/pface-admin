package com.pface.admin.modules.jiekou.sock;

import com.pface.admin.modules.jiekou.utils.BizUtils;

import javax.websocket.*;
import java.io.IOException;

@ClientEndpoint()
public class Client {

//	@OnOpen
//	public void onOpen(Session session) {
//		try {
//			System.out.println("Client onOpen: ");
//			key 通过api接口获取
//			String key = BizUtils.getWebsockKey();
//			session.getBasicRemote().sendText("{\"key\":\""+key+"\",\"msg_id\":\"776\"}");
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	@OnMessage
	public void onMessage(String message) {

		System.out.println("Client onMessage: " + message);
	}
	@OnClose
	public void onClose() {

	}
}
