package com.test.design.pattern.factory;

/**
 * 工厂类 -- 生产短信和邮件发送的产品
 *
 * 多个工厂方法模式：是对普通工厂方法模式的改进，在普通工厂方法模式中，如果传递的字符串出错，则不能正确创建对象，而多个工厂方法模式是提供多个工厂方法，分别创建对象
 */
public class SendFactoryMore {

    public Sender produceMail() {
        return new MailSender();
    }

    public Sender produceSms() {
        return new SmsSender();
    }

}
