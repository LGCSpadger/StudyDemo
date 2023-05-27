package com.test.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 此工具类用于校验一些东西的合法性，比如说ip是否合法
 */
@Slf4j
public class CheckedLegitimacyUtil {

    /**
     * 判断IP地址是否合法
     * @param ipAddr    IP地址
     * @return  返回判断结果。true or false
     */
    public static boolean checkIpAddr(String ipAddr) {
        if (ipAddr != null && !ipAddr.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (ipAddr.matches(regex)) {
                // 返回判断信息
                return true;
            } else {
                // 返回判断信息
                return false;
            }
        }
        return false;
    }

}
