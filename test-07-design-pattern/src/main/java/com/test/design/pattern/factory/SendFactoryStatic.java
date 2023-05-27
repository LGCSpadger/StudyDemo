package com.test.design.pattern.factory;

/**
 * 工厂类 -- 生产短信和邮件发送的产品
 *
 * 静态工厂模式：将上面的多个工厂方法模式里的方法置为静态的，不需要创建实例，直接调用即可
 */
public class SendFactoryStatic {

    public static Sender produceMail() {
        return new MailSender();
    }

    public static Sender produceSms() {
        return new SmsSender();
    }

}
