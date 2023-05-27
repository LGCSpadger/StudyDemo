package com.test.pub.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Cron表达式的组成：
 * cron 表达式包含六个部分，从左到右依次是：
 * 秒（0~59） 分钟（0~59） 小时（0~23） 月中的天（1~31） 月（1~12） 周中的天（填写MON，TUE，WED，THU，FRI，SAT,SUN，或数字1~7 1表示MON，依次类推）
 * Cron表达式中的特殊字符：
 * 1、“/” 字符表示指定数值的增量  例如：0/3 * * * * * 0秒的时候触发，以后每隔3秒执行一次，0 0/5 * * * * 0分的时候触发，以后每隔5分钟执行一次，0 '*'/5 * * * * 任意时刻触发，以后每隔五分钟执行一次
 * 2、“*” 字符表示所有可能的值（通配符）
 * 3、“-”字符表示区间范围  例如：0 0 10 ? * 1-3 每周 周一到周三的10点执行
 * 4、"," 字符表示列举   例如：0 0 10 5,6 * ? 每月 5号和6号的10点执行
 * 5、“？”字符仅被用于月中的天和周中的天两个子表达式，表示不指定值  例如：0 0 10 5 * ? 每月 5号的10点执行
 * Cron表达式使用实例：
 * 1、0 6 10 5 * 2   每月的五号，并且是周二的10点6分执行（每月的五号和周二要同时满足）
 * 2、0 11 10 * * 2    每周二的10点11分执行
 * 3、0 11 10 ? * 2    每周二的10点11分执行
 * 4、0 0/5 * * * *    首次触发时间为程序启动后距离最近的一次5的整数倍时间，之后每隔五分钟执行一次（比如说在 2022-06-30 19:22:00 启动程序，则第一次执行时间为 2022-06-30 19:25:00，之后每隔五分钟就会执行一次；如果在 2022-06-30 19:58:00 启动程序，则第一次执行时间为 2022-06-30 18:00:00）
 */
@Slf4j
@Component
public class TestTask {

    @Scheduled(cron = "0 25 14 * * 3")
    public void test01() {
        log.info("test01............" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 25 14 * * 3")
    public void test02() {
        log.info("test02............" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 25 14 * * 3")
    public void test03() {
        log.info("test03............" + System.currentTimeMillis());
    }

    @Scheduled(cron = "0 25 14 * * 3")
    public void test04() {
        log.info("test04............" + System.currentTimeMillis());
    }

}
