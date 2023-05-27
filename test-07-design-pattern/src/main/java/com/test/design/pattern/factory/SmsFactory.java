package com.test.design.pattern.factory;

/**
 * 短信 工厂类 -- 只负责生产 短信 实例
 */
public class SmsFactory implements Provider {

    @Override
    public Sender provider() {
        return new SmsSender();
    }

}
