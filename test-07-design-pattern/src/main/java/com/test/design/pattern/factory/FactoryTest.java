package com.test.design.pattern.factory;

/**
 * 工厂模式测试类
 */
public class FactoryTest {

    //测试普通工厂模式（通过逻辑判断实现）
//    public static void main(String[] args) {
//        //创建工厂实例
//        SendFactory factory = new SendFactory();
//        //工厂生产发送短信的实例
//        Sender sms = factory.produce("SMS");
//        //使用发送短信的实例来发送短信
//        sms.send();
//        //工厂生产发送邮件的实例
//        Sender mail = factory.produce("MAIL");
//        //使用发送邮件的实例来发送短信
//        mail.send();
//    }

    //测试简单工厂模式（通过java反射实现）
    public static void main(String[] args) {
        //创建工厂实例
        SendFactoryReflex factory = new SendFactoryReflex();
        //工厂生产发送短信的实例
        Sender sms = factory.produce(SmsSender.class);
        //使用发送短信的实例来发送短信
        sms.send();
        //工厂生产发送邮件的实例
        Sender mail = factory.produce(MailSender.class);
        //使用发送邮件的实例来发送短信
        mail.send();
    }

    //测试多个工厂方法模式（相比于普通工厂模式而言，多个工厂方法模式每个生产方法针对一个实例，不需要传参，不会有参数错误，无法创建实例的问题）
//    public static void main(String[] args) {
//        //创建工厂实例
//        SendFactoryMore factory = new SendFactoryMore();
//        //工厂生产发送短信的实例
//        Sender sms = factory.produceSms();
//        //使用发送短信的实例来发送短信
//        sms.send();
//        //工厂生产发送邮件的实例
//        Sender mail = factory.produceMail();
//        //使用发送邮件的实例来发送短信
//        mail.send();
//    }

    //测试静态工厂模式（静态工厂模式中的方法是静态的，可以直接调用，不需要创建工厂对象）
//    public static void main(String[] args) {
//        //工厂生产发送短信的实例
//        Sender sms = SendFactoryStatic.produceSms();
//        //使用发送短信的实例来发送短信
//        sms.send();
//        //工厂生产发送邮件的实例
//        Sender mail = SendFactoryStatic.produceMail();
//        //使用发送邮件的实例来发送短信
//        mail.send();
//    }

    //测试抽象工厂模式
//    public static void main(String[] args) {
//        //创建短信工厂对象
//        SmsFactory smsFactory = new SmsFactory();
//        //调用短信工厂对象的 provider() 方法生产 短信 实例对象
//        Sender sms = smsFactory.provider();
//        //调用短信实例的 send() 方法发送短信
//        sms.send();
//        //创建邮件工厂对象
//        MailFactory mailFactory = new MailFactory();
//        //调用邮件工厂对象的 provider() 方法生产 邮件 实例对象
//        Sender mail = mailFactory.provider();
//        //调用邮件实例的 send() 方法发送邮件
//        mail.send();
//    }

}
