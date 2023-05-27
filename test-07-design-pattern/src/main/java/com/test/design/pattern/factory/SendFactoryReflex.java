package com.test.design.pattern.factory;

/**
 * 普通工厂模式（也叫简单工厂模式） -- java 反射 实现方式
 */
public class SendFactoryReflex {

    public Sender produce(Class c) {
        Sender sender = null;
        try {
            String name = c.getName();
            System.out.println(name);
            sender = (Sender) Class.forName(c.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sender;
    }

}
