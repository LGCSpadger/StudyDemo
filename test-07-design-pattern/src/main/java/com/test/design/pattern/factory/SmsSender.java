package com.test.design.pattern.factory;

/**
 * 短信发送
 */
public class SmsSender implements Sender {

    @Override
    public void send() {
        System.out.println("短信发送程序  ****  正在发送短信。。。。。。");
    }

}
