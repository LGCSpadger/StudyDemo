package com.test.common.utils;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import java.util.Map;
import org.codehaus.groovy.control.CompilerConfiguration;

public class GroovyUtil {

  /**
   * 动态执行java脚本，返回脚本执行结果
   * @param classText   脚本内容（字符串形式）
   * @param methodName  脚本中调用的方法名称
   * @param params      调用脚本中方法的参数
   * @return
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public static Object execScript(String classText,String methodName,Map<String,Object> params) throws IllegalAccessException, InstantiationException {
    CompilerConfiguration config = new CompilerConfiguration();
    GroovyClassLoader groovyClassLoader;
    config.setSourceEncoding("UTF-8");
    groovyClassLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);
    //加载解析脚本内容
    Class<?> groovyClass = groovyClassLoader.parseClass(classText);
    GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
    //执行脚本中的某个方法
    Object execResult = groovyObject.invokeMethod(methodName, new Object[]{params});
    return execResult;
  }

}
