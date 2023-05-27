package com.test.design.pattern.singleton;

/**
 * 单例模式的特点：
 * 1.单例类只能有一个实例
 * 2.单例类必须自己创建自己的唯一实例
 * 3.单例类必须给所有其他对象提供这一实例
 * 单例模式保证了全局对象的唯一性，比如系统启动读取配置文件就需要单例保证配置的一致性
 *
 * 单例模式 -- 饿汉式（静态代码块）【可用】（饿汉式：在类加载的时候就把对象创建出来）
 * 优点：这种写法比较简单，就是在类装载的时候就完成实例化。避免了线程同步问题。
 * 缺点：在类装载的时候就完成实例化，没有达到Lazy Loading的效果。如果从始至终从未使用过这个实例，则会造成内存的浪费。
 * 这种方式和上面的方式其实类似，只不过将类实例化的过程放在了静态代码块中，也是在类装载的时候，就执行静态代码块中的代码，初始化类的实例。优缺点和上面是一样的。
 */
public class Singleton02 {

    private static Singleton02 singleton02;

    static {
        singleton02 = new Singleton02();
    }

    private Singleton02() {}

    public static Singleton02 getInstance() {
        return singleton02;
    }

}
