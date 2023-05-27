package com.test.design.pattern.factory;

/**
 * 邮件发送
 */
public class MailSender implements Sender {

    @Override
    public void send() {
        System.out.println("邮件发送程序  ****  正在发送邮件。。。。。。");
    }

}
