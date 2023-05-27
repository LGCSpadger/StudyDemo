package com.test.design.pattern.factory;

/**
 * 工厂类 -- 生产短信和邮件发送的产品
 *
 * 普通工厂模式：就是建立一个工厂类，对实现了同一接口的一些类进行实例的创建
 */
public class SendFactory {

    public Sender produce(String type) {
        if ("MAIL".equals(type)) {
            return new MailSender();
        } else if ("SMS".equals(type)) {
            return new SmsSender();
        } else {
            System.out.println("请输入正确的类型！");
            return null;
        }
    }

}
