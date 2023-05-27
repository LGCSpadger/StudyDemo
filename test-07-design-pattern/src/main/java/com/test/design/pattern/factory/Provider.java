package com.test.design.pattern.factory;

/**
 * 短信和邮件工厂类的共同接口
 */
public interface Provider {

    public Sender provider();

}
