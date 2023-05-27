package com.test.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串转为驼峰格式 构建工具类
 * @author LYY
 * @date 2022/07/06
 */

public class RegexUtils {

  private static Pattern UNDERLINE_PATTERN = Pattern.compile("_([a-z])");


  public static void main(String[] args) {
    String a = "safe_level_test";
    String result = underlineToHump(a);
    System.out.println(result);
  }

  public static String underlineToHump(String str){
    //正则匹配下划线及后一个字符，删除下划线并将匹配的字符转成大写
    Matcher matcher = UNDERLINE_PATTERN.matcher(str);
    StringBuffer sb = new StringBuffer(str);
    if (matcher.find()) {
      sb = new StringBuffer();
      //将当前匹配的子串替换成指定字符串，并且将替换后的子串及之前到上次匹配的子串之后的字符串添加到StringBuffer对象中
      //正则之前的字符和被替换的字符
      matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
      //把之后的字符串也添加到StringBuffer对象中
      matcher.appendTail(sb);
    } else {
      //去除除字母之外的前面带的下划线
      return sb.toString().replaceAll("_", "");
    }
    return underlineToHump(sb.toString());
  }


}
