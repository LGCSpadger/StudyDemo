package com.test.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListUtil {

  public static List<Map<String,Object>> listMapDistByKey(List<Map<String,Object>> dataList,String fieldName) {
    log.info("方法名：listMapDistByKey，去重之前：{}" + dataList + "，去重字段：" + fieldName);
    //最终去重后的ListMap
    List<Map<String, Object>> listMap = new ArrayList<>();
    // msp存放过滤后的Map 将ID当key 将去掉的ID的map当value。 因为Map 中的key(键)是唯一的，刚好可以去重。
    Map<String, Map> msp = new HashMap<>();
    //approveRecordList 是要去重的List,这里用的是从后面先取出来。那就是说会去重后面的值，保留前面的值，因为先存后面的value吗，然后前面有的话就会将前面的覆盖掉，就实现了去重。
    for(int i = dataList.size()-1 ; i>=0; i--){
      //从后获取 Map
      Map map = dataList.get(i);
      //我这里指定的是根据ClueID去重。你们可以换成你们要去重复的字段。
      String id = (String)map.get(fieldName);
      //删除现有Map中你所指定的字段。
      map.remove(fieldName);
      //存入msp 中，以你所指定的字段值当key，去掉的所指定的那个字段当值。
      msp.put(id, map);
    }
    //其实上面👆🏻已经做好去重啦。我们只需要将key再次循环送到Map中就好啦
    Set<String> mspKey = msp.keySet();
    for(String key: mspKey){
      //获取没有指定字段的Map
      Map newMap = msp.get(key);
      //然后将指定字段塞入Map
      newMap.put(fieldName, key);
      //最后返回我们上面写好的ListMap返回。
      listMap.add(newMap);
    }
    log.info("方法名：listMapDistByKey，去重之后：{}" + listMap);
    return listMap;
  }

  /**
   *
   * 按照指定对象的字段排序(正序)
   *
   * @param list
   * @param param 排序字段
   * @param <T>
   * @throws IntrospectionException
   */
  public static final <T> void ObjSort(List<T> list , String param) throws IntrospectionException {
    Collections.sort(list, new Comparator<T>() {
      @Override
      public int compare(T o1, T o2) {
        Class<?> type = o1.getClass();
        PropertyDescriptor descriptor1 = null;
        try {
          descriptor1 = new PropertyDescriptor( param, type );
          Method readMethod1 = descriptor1.getReadMethod();

          PropertyDescriptor descriptor2 = new PropertyDescriptor( param, type );
          Method readMethod2 = descriptor2.getReadMethod();
          return readMethod1.invoke(o1).toString().compareTo(readMethod2.invoke(o2).toString());
        } catch (IntrospectionException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
        return -1;
      }
    });
  }

  /**
   *
   * 按照指定对象的字段倒叙排序
   *
   * @param list
   * @param param 排序字段
   * @param <T>
   * @throws IntrospectionException
   */
  public static final <T> void ObjSortReversed(List<T> list, String param) throws IntrospectionException {
    Collections.sort(list, new Comparator<T>() {
      @Override
      public int compare(T o1, T o2) {
        Class<?> type = o1.getClass();
        PropertyDescriptor descriptor1 = null;
        try {
          descriptor1 = new PropertyDescriptor( param, type );
          Method readMethod1 = descriptor1.getReadMethod();

          PropertyDescriptor descriptor2 = new PropertyDescriptor( param, type );
          Method readMethod2 = descriptor2.getReadMethod();

          return readMethod1.invoke(o1).toString().compareTo(readMethod2.invoke(o2).toString());
        } catch (IntrospectionException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
        return -1;
      }
    }.reversed());
  }

  public static final Boolean  isEmpty(Collection<?> collection){
    return (collection == null || collection.isEmpty());
  }

}
