package com.test.pub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

/**
 * IO流基础知识及常见用法测试
 *
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)//不加这个注解 userService 就无法注入
@SpringBootTest
public class IoTest {

  //获取resources目录下路径的几种方法
  @Test
  public void test01() throws FileNotFoundException {
    //1.获取项目的根路径
    String xmglj = this.getClass().getResource("/").getPath();
    log.info("项目根路径：" + xmglj);
    //2.获取项目中 resources 目录下的 templates 目录的绝对路径（方式一）
    String temPath01 = this.getClass().getResource("/templates").getPath();
    log.info("项目中 resources 目录下的 templates 目录的绝对路径（方式一）：" + temPath01);
    String temPath02 = this.getClass().getClassLoader().getResource("templates").getPath();
    log.info("项目中 resources 目录下的 templates 目录的绝对路径（方式二）：" + temPath02);
    String url = ResourceUtils.getURL("src" + File.separator + "main" + File.separator + "resources" + File.separator +  "static" + File.separator + "txtfile").getPath();
    log.info("使用 ResourceUtils 工具类获取url："+url);
  }

  //FileOutputStream：文件输出流用于将数据写入文件File
  @Test
  public void test02() throws IOException {
    FileOutputStream fos01 = null;
    try {
      String basePath = ResourceUtils.getURL("src" + File.separator + "main" + File.separator + "resources" + File.separator +  "static" + File.separator + "txtfile").getPath();
      log.info("使用 ResourceUtils 工具类获取url："+basePath);
      if (StringUtils.isNotEmpty(basePath)) {
        File dicFile = new File(basePath);
        //exists()判断文件或者文件夹是否存在
        if (!dicFile.exists()) {
          dicFile.mkdir();//mkdir()创建单个文件夹，要确保它的上级文件夹存在
  //        dicFile.mkdirs();//mkdirs()创建多个文件夹，并且不需要保证它的上级文件夹存在
        }
        String fileName01 = "testFile01.txt";
        File file01 = new File(basePath + File.separator + fileName01);
        if (!file01.exists()) {
          file01.createNewFile();//createNewFile()创建一个新的文件
        }
        /**
         * new FileOutputStream(String name,boolean append)
         * 参数一：name 文件完整名称
         * 参数二：append 是否是追加写入。默认为false，会从文件的开头写入内容，如果为true，则会从文件的末尾写入内容
         * 注意：如果 append 为 false 时，新写入的数据会把文件中原有的内容给覆盖掉，而 append 为 true 时，新写入的数据在原有内容的后面，不会覆盖掉文件中原有的内容
         */
        fos01 = new FileOutputStream(basePath + File.separator + fileName01);
        fos01.write(98);//向文件中写入内容（一次写一个字节数据 ASCII码 98对应为b）
        fos01.write("\n".getBytes(StandardCharsets.UTF_8));//  \n换行
        byte[] bys = {97,98,99,100,101};
        fos01.write(bys);//向文件中写入内容（一次写一个字节数组数据）
        fos01.write("\r".getBytes(StandardCharsets.UTF_8));//  \r回车
        fos01.write("\n".getBytes(StandardCharsets.UTF_8));//  \n换行
        fos01.write("\r\n".getBytes(StandardCharsets.UTF_8));//  \r\n回车换行
        byte[] bs1 = "abcdefghijk".getBytes(StandardCharsets.UTF_8);
        fos01.write(bs1,3,4);//从指定的字节数组bs1的索引为3的地方开始写入，一次写入4个字节，也就是 defg
        byte[] bs2 = "我们是好朋友".getBytes(StandardCharsets.UTF_8);
        fos01.write(bs2,0,bs2.length);//从指定的字节数组bs1的索引为0的地方开始写入，一次写入bs2.length个字节，也就是 我们是好朋友
        fos01.flush();//flush 是清空的意思，一般主要用在IO中，即清空缓冲区数据，就是说你用读写流的时候，其实数据是先被读到了内存中，然后把数据写到文件中，当你数据读完的时候不代表你的数据已经写完了，因为还有一部分有可能会留在内存这个缓冲区中。这时候如果你调用了 close()方法关闭了读写流，那么这部分数据就会丢失，所以应该在关闭读写流之前先flush()，先清空数据
      }
    } catch (Exception e) {
      log.error("异常信息：{}",e);
    } finally {
      if (fos01 != null) {
        try {
          fos01.close();//关闭流
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
    }
  }

  //FileOutputStream：文件输出流用于将数据写入文件File
  @Test
  public void test03() throws IOException {
    FileOutputStream fos02 = null;
    try {
      String basePath = ResourceUtils.getURL("src" + File.separator + "main" + File.separator + "resources" + File.separator +  "static" + File.separator + "txtfile").getPath();
      String fileName02 = "testFile02.txt";
      File file02 = new File(basePath + File.separator + fileName02);
      if (!file02.exists()) {
        file02.createNewFile();//createNewFile()创建一个新的文件
      }
      /**
       * new FileOutputStream(String name,boolean append)
       * 参数一：name 文件完整名称
       * 参数二：append 是否是追加写入。默认为false，会从文件的开头写入内容，如果为true，则会从文件的末尾写入内容
       * 注意：如果 append 为 false 时，新写入的数据会把文件中原有的内容给覆盖掉，而 append 为 true 时，新写入的数据在原有内容的后面，不会覆盖掉文件中原有的内容
       */
      fos02 = new FileOutputStream(basePath + File.separator + fileName02);
      byte[] bs3 = "撼不动这天地，".getBytes(StandardCharsets.UTF_8);
      byte[] bs4 = "我要这金箍有何用！".getBytes(StandardCharsets.UTF_8);
      fos02.write(bs3,0,bs3.length);//整个数组全部写入
      fos02.write(bs4,0,bs4.length);//整个数组全部写入
      fos02.flush();//flush 是清空的意思，一般主要用在IO中，即清空缓冲区数据，就是说你用读写流的时候，其实数据是先被读到了内存中，然后把数据写到文件中，当你数据读完的时候不代表你的数据已经写完了，因为还有一部分有可能会留在内存这个缓冲区中。这时候如果你调用了 close()方法关闭了读写流，那么这部分数据就会丢失，所以应该在关闭读写流之前先flush()，先清空数据
    } catch (Exception e) {
      log.error("异常信息：{}",e);
    } finally {
      if (fos02 != null) {
        try {
          fos02.close();//关闭流
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
    }
  }

  //FileOutputStream：文件输出流用于将数据写入文件File
  @Test
  public void test04() throws IOException {
    FileOutputStream fos02 = null;
    try {
      String basePath = ResourceUtils.getURL("src" + File.separator + "main" + File.separator + "resources" + File.separator +  "static" + File.separator + "txtfile").getPath();
      String fileName02 = "testFile03.txt";
      File file02 = new File(basePath + File.separator + fileName02);
      if (!file02.exists()) {
        file02.createNewFile();//createNewFile()创建一个新的文件
      }
      /**
       * new FileOutputStream(String name,boolean append)
       * 参数一：name 文件完整名称
       * 参数二：append 是否是追加写入。默认为false，会从文件的开头写入内容，如果为true，则会从文件的末尾写入内容
       * 注意：如果 append 为 false 时，新写入的数据会把文件中原有的内容给覆盖掉，而 append 为 true 时，新写入的数据在原有内容的后面，不会覆盖掉文件中原有的内容
       */
      fos02 = new FileOutputStream(basePath + File.separator + fileName02,true);
      byte[] bs3 = "撼不动这天地，".getBytes(StandardCharsets.UTF_8);
      byte[] bs4 = "我要这金箍有何用！".getBytes(StandardCharsets.UTF_8);
      fos02.write(bs3,0,bs3.length);//整个数组全部写入
      fos02.write(bs4,0,bs4.length);//整个数组全部写入
      fos02.flush();//flush 是清空的意思，一般主要用在IO中，即清空缓冲区数据，就是说你用读写流的时候，其实数据是先被读到了内存中，然后把数据写到文件中，当你数据读完的时候不代表你的数据已经写完了，因为还有一部分有可能会留在内存这个缓冲区中。这时候如果你调用了 close()方法关闭了读写流，那么这部分数据就会丢失，所以应该在关闭读写流之前先flush()，先清空数据
    } catch (Exception e) {
      log.error("异常信息：{}",e);
    } finally {
      if (fos02 != null) {
        try {
          fos02.close();//关闭流
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
    }
  }

  //FileOutputStream：文件输出流用于将数据写入文件File
  @Test
  public void test05() throws IOException {
    FileOutputStream fos02 = null;
    try {
      String basePath = ResourceUtils.getURL("src" + File.separator + "main" + File.separator + "resources" + File.separator +  "static" + File.separator + "txtfile").getPath();
      String fileName02 = "testFile04.txt";
      File file02 = new File(basePath + File.separator + fileName02);
      if (!file02.exists()) {
        file02.createNewFile();//createNewFile()创建一个新的文件
      }
      /**
       * new FileOutputStream(String name,boolean append)
       * 参数一：name 文件完整名称
       * 参数二：append 是否是追加写入。默认为false，会从文件的开头写入内容，如果为true，则会从文件的末尾写入内容
       * 注意：如果 append 为 false 时，新写入的数据会把文件中原有的内容给覆盖掉，而 append 为 true 时，新写入的数据在原有内容的后面，不会覆盖掉文件中原有的内容
       */
      fos02 = new FileOutputStream(basePath + File.separator + fileName02,false);
      byte[] bs3 = "撼不动这天地，".getBytes(StandardCharsets.UTF_8);
      byte[] bs4 = "我要这金箍有何用！".getBytes(StandardCharsets.UTF_8);
      fos02.write(bs3,0,bs3.length);//整个数组全部写入
      fos02.write(bs4,0,bs4.length);//整个数组全部写入
      fos02.flush();//flush 是清空的意思，一般主要用在IO中，即清空缓冲区数据，就是说你用读写流的时候，其实数据是先被读到了内存中，然后把数据写到文件中，当你数据读完的时候不代表你的数据已经写完了，因为还有一部分有可能会留在内存这个缓冲区中。这时候如果你调用了 close()方法关闭了读写流，那么这部分数据就会丢失，所以应该在关闭读写流之前先flush()，先清空数据
    } catch (Exception e) {
      log.error("异常信息：{}",e);
    } finally {
      if (fos02 != null) {
        try {
          fos02.close();//关闭流
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
    }
  }

  //字节输入流读数据
  @Test
  public void test06() throws IOException {
    FileInputStream fis = null;
    try {
      String basePath = ResourceUtils.getURL("src" + File.separator + "main" + File.separator + "resources" + File.separator +  "static" + File.separator + "txtfile").getPath();
      String fileName = "testFile01.txt";
      fis = new FileInputStream(basePath + File.separator + fileName);
      //一次读取一个字节
      int read = fis.read();
      log.info("一次读取一个字节的内容：" + read);
      log.info("------------------------------------------------------------------------");
      //循环读取整个文件的全部内容
      int readCurr;
      while ((readCurr = fis.read()) != -1) {
        log.info("当前字节内容：" + (char) readCurr);
      }
    } catch (Exception e) {
      log.error("异常信息：{}",e);
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
    }
  }

  //使用案例一：复制文本文件（将文件A中的内容复制到文件B中）
  @Test
  public void test07() {
    FileInputStream fis = null;
    FileOutputStream fos = null;
    try {
      String basePath = ResourceUtils.getURL("src" + File.separator + "main" + File.separator + "resources" + File.separator +  "static" + File.separator + "txtfile").getPath();
      //字节输入流读取数据
      String fileName04 = "testFile04.txt";
      fis = new FileInputStream(basePath + File.separator + fileName04);
      //字节输出流写入数据
      String fileName01 = "testFile01.txt";
      fos = new FileOutputStream(basePath + File.separator + fileName01,false);
      int read;
      while ((read = fis.read()) != -1) {
        //循环将 testFile04.txt 文件中的内容写入到 testFile01.txt 文件中
        fos.write(read);
      }
      fos.flush();
    } catch (Exception e) {
      log.error("异常信息：{}",e);
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
      if (fos != null) {
        try {
          fos.close();
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
    }
  }

  //使用案例二：把文件中的内容读取出来在控制台输出
  @Test
  public void test08() {
    FileReader fr = null;
    BufferedReader bf = null;
    try {
      String basePath = ResourceUtils.getURL("src" + File.separator + "main" + File.separator + "resources" + File.separator +  "static" + File.separator + "txtfile").getPath();
      String fileName04 = "testFile04.txt";
      //字符流按字符读取数据
      fr = new FileReader(new File(basePath + File.separator + fileName04));
      log.info("----------- 按字符读取数据 ------------");
      int read;
      while ((read = fr.read()) != -1) {
        char charCurr = (char) read;
        System.out.println(charCurr);
      }
      log.info("----------- 按字符读取数据 ------------");
      log.info("##################### 整行数据输出 #####################");
      bf = new BufferedReader(new FileReader(new File(basePath + File.separator + fileName04)));
      String readLine = null;
      while ((readLine = bf.readLine()) != null) {
        System.out.println(readLine);
      }
      log.info("##################### 整行数据输出 #####################");
    } catch (Exception e) {
      log.error("异常信息：{}",e);
    } finally {
      if (fr != null) {
        try {
          fr.close();
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
      if (bf != null) {
        try {
          bf.close();
        } catch (Exception e) {
          log.error("异常信息：{}",e);
        }
      }
    }
  }

}
