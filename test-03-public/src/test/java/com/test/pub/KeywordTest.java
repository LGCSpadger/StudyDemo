package com.test.pub;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * java 常用关键字测试
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class KeywordTest {

  private static final char HexCharArr[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

  private static final String HexStr = "0123456789abcdef";

  /**
   * assignable 关键字测试
   */
  @Test
  public void test01() {
    String decode = URLDecoder.decode(
        "%E4%B8%AD%E5%9B%BD%E4%BA%BA%E5%AF%BF%E4%BF%9D%E9%99%A9%E8%82%A1%E4%BB%BD%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E6%9C%94%E5%B7%9E%E5%88%86%E5%85%AC%E5%8F%B8");
    System.out.println(decode);
    byte[] bytes = hexToByteArr(decode);
    String customName = new String(bytes);
    System.out.println(customName);
  }

  @Test
  public void test02() throws UnsupportedEncodingException {
    System.out.println(URLDecoder.decode("%D6%D0%B9%FA%BD%A8%C9%E8%D2%F8%D0%D0%B9%C9%B7%DD%D3%D0%CF%DE%B9%AB%CB%BE%BD%FA%B3%C7%B7%D6%D0%D0.","gbk"));
  }

  @Test
  public void test03() {
    String a = "Nice to meet you";
    String result = a.substring(a.lastIndexOf(" ") + 1, a.length());
    System.out.println(result.length());
  }

  public static String byteArrToHex(byte[] btArr) {
    char strArr[] = new char[btArr.length * 2];
    int i = 0;
    for (byte bt : btArr) {
      strArr[i++] = HexCharArr[bt>>>4 & 0xf];
      strArr[i++] = HexCharArr[bt & 0xf];
    }
    return new String(strArr);
  }

  public static byte[] hexToByteArr(String hexStr) {
    char[] charArr = hexStr.toCharArray();
    byte btArr[] = new byte[charArr.length / 2];
    int index = 0;
    for (int i = 0; i < charArr.length; i++) {
      int highBit = HexStr.indexOf(charArr[i]);
      int lowBit = HexStr.indexOf(charArr[++i]);
      btArr[index] = (byte) (highBit << 4 | lowBit);
      index++;
    }
    return btArr;
  }

}
