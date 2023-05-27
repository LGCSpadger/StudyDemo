package com.test.design.pattern.factory;

/**
 * 普通工厂模式
 * 举例：发送邮件和短信
 *
 * 这是邮件和短信的共同接口，让邮件和短信发送类共同实现该接口
 */
public interface Sender {

    public void send();

}
