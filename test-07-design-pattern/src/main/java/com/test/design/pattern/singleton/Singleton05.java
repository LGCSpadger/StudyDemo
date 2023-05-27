package com.test.design.pattern.singleton;

/**
 * 单例模式的特点：
 * 1.单例类只能有一个实例
 * 2.单例类必须自己创建自己的唯一实例
 * 3.单例类必须给所有其他对象提供这一实例
 * 单例模式保证了全局对象的唯一性，比如系统启动读取配置文件就需要单例保证配置的一致性
 *
 * 单例模式 -- 懒汉式（线程安全，同步代码块）【不可用】（懒汉式：需要用的时候再创建对象）
 * 由于第四种实现方式同步效率太低，所以摒弃同步方法，改为同步产生实例化的的代码块。但是这种同步并不能起到线程同步的作用。跟第3种实现方式遇到的情形一致，
 * 假如一个线程进入了if (singleton == null)判断语句块，还未来得及往下执行，另一个线程也通过了这个判断语句，这时便会产生多个实例。
 */
public class Singleton05 {

    private static Singleton05 singleton05;

    private Singleton05() {}

    public static Singleton05 getInstance() {
        if (singleton05 == null) {
            synchronized (Singleton05.class) {
                singleton05 = new Singleton05();
            }
        }
        return singleton05;
    }

}
