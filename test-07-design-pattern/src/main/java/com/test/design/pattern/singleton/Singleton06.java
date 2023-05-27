package com.test.design.pattern.singleton;

/**
 * 单例模式的特点：
 * 1.单例类只能有一个实例
 * 2.单例类必须自己创建自己的唯一实例
 * 3.单例类必须给所有其他对象提供这一实例
 * 单例模式保证了全局对象的唯一性，比如系统启动读取配置文件就需要单例保证配置的一致性
 *
 * 单例模式 -- 双重检查【推荐使用】（懒汉式：需要用的时候再创建对象）
 * Double-Check概念对于多线程开发者来说不会陌生，如代码中所示，我们进行了两次if (singleton == null)检查，这样就可以保证线程安全了。这样，
 * 实例化代码只用执行一次，后面再次访问时，判断if (singleton == null)，直接return实例化对象。
 * 优点：线程安全；延迟加载；效率较高。
 */
public class Singleton06 {

    private static volatile Singleton06 singleton06;

    private Singleton06() {}

    public static Singleton06 getInstance() {
        if (singleton06 == null) {
            synchronized (Singleton06.class) {
                if (singleton06 == null) {
                    singleton06 = new Singleton06();
                }
            }
        }
        return singleton06;
    }

}
