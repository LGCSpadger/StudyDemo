package com.test.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ServerEndpoint("/websocket/{name}")
public class WebSocket {

  private Session session;

  private String name;

  private static ConcurrentHashMap<String,WebSocket> webSocketSet = new ConcurrentHashMap<>();

  @OnOpen
  public void onOpen(Session session,@PathParam(value = "name") String name) {
    this.session = session;
    this.name = name;
    webSocketSet.put(name,this);
  }

  @OnClose
  public void onClose() {
    webSocketSet.remove(this.name);
  }

  @OnMessage
  public void onMessage(String message) {
    log.info("{} send {}",this.name,message);
  }

  /**
   * 群发
   * @param message 消息内容
   */
  public void groupSending(String message) {
    for (String name : webSocketSet.keySet()) {
      try {
        webSocketSet.get(name).session.getBasicRemote().sendText(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 指定发送消息
   * @param name 指定的客户端名称
   * @param message 消息内容
   */
  public void appointSending(String name,String message) {
    try {
      webSocketSet.get(name).session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
