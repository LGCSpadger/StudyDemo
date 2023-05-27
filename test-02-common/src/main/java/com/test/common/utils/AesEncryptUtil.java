package com.test.common.utils;

/**
 * AES 128bit 加密解密工具类
 * @author dufy
 */

import org.apache.commons.codec.binary.Base64;

import java.io.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptUtil {

	//使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同！

    private static String KEY = "skf%0jfl@ssff#_7";

    private static String IV = "0102030405060708";

    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv){
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");//"算法/模式/补码方式"
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new Base64().encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String desEncrypt(String data, String key, String iv){
        try {
            byte[] encrypted1 = new Base64().decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,"UTF-8");
            return originalString.trim();// 去除空格！！！
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用默认的key和iv加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data){
        return encrypt(data, KEY, IV);
    }

    /**
     * 使用默认的key和iv解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String data){
        return desEncrypt(data, KEY, IV);
    }

    /**
    * 测试
    */
    public static void main(String args[]) throws Exception {

        String test = "gztb##77";

        String data = null;

        String path = "E:\\test\\aaa.xml";
        File file = new File(path);
        test = fileToString(file,"UTF-8");


        String key = "skf%0jfl@ssff#_7";//skf%0jfl@ssff#_7//taotao170920java
        String iv = "0102030405060708";//0102030405060708//taotao170920java

        data = encrypt(test, key, iv);

        System.out.println(data);
        System.out.println(desEncrypt(data, key, iv));
    }

    /**
     * 将File转字符串
     * @param file  要转换的file文件
     * @param encoding  字符编码
     * @return
     */
    public static String fileToString(File file,String encoding) {
		String fileStr = null;
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file));
			} else {
				reader = new InputStreamReader(new FileInputStream(file),encoding);
			}
			char[] buffer = new char[1024];
			int n = 0;
			while(-1 != (n = reader.read(buffer))) {
				writer.write(buffer,0,n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (writer != null) {
			fileStr = writer.toString();
		}
		return fileStr;
	}

    /**
     * 将字符串转换成file文件
     * @param fileStr   文件字符串
     * @param filePath  转换成文件后存储的路径
     * @return
     */
	public static File stringToFile(String fileStr,String filePath) {
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        File file = null;
        try {
            file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            bufferedReader = new BufferedReader(new StringReader(fileStr));
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            char buf[] = new char[1024];
            int len;
            while ((len = bufferedReader.read(buf)) != -1) {
                bufferedWriter.write(buf,0,len);
            }
            bufferedWriter.flush();
            bufferedReader.close();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

}

