package com.test.design.pattern.singleton;

/**
 * 单例模式的特点：
 * 1.单例类只能有一个实例
 * 2.单例类必须自己创建自己的唯一实例
 * 3.单例类必须给所有其他对象提供这一实例
 * 单例模式保证了全局对象的唯一性，比如系统启动读取配置文件就需要单例保证配置的一致性
 *
 * 单例模式 -- 懒汉式（线程不安全）【不可用】（懒汉式：需要用的时候再创建对象）
 * 这种写法起到了Lazy Loading的效果，但是只能在单线程下使用。如果在多线程下，一个线程进入了if (singleton == null)判断语句块，还未来得及往下执行，
 * 另一个线程也通过了这个判断语句，这时便会产生多个实例。所以在多线程环境下不可使用这种方式。
 */
public class Singleton03 {

    private static Singleton03 singleton03;

    private Singleton03() {}

    public static Singleton03 getInstance() {
        if (singleton03 == null) {
            singleton03 = new Singleton03();
        }
        return singleton03;
    }

}
