package com.test.design.pattern.factory;

/**
 * 邮件 工厂类 -- 只负责生产 邮件 实例
 */
public class MailFactory implements Provider{

    @Override
    public Sender provider() {
        return new MailSender();
    }

}
