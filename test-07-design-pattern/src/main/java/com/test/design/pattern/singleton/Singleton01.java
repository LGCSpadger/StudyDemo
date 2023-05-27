package com.test.design.pattern.singleton;

/**
 * 单例模式的特点：
 * 1.单例类只能有一个实例
 * 2.单例类必须自己创建自己的唯一实例
 * 3.单例类必须给所有其他对象提供这一实例
 * 单例模式保证了全局对象的唯一性，比如系统启动读取配置文件就需要单例保证配置的一致性
 *
 * 单例模式 -- 饿汉式（静态常量）【可用】（饿汉式：在类加载的时候就把对象创建出来）
 * 优点：这种写法比较简单，就是在类装载的时候就完成实例化。避免了线程同步问题。
 * 缺点：在类装载的时候就完成实例化，没有达到Lazy Loading的效果。如果从始至终从未使用过这个实例，则会造成内存的浪费。
 */
public class Singleton01 {

    private static final Singleton01 singleton01 = new Singleton01();

    private Singleton01() {}

    public static Singleton01 getInstance() {
        return singleton01;
    }

}