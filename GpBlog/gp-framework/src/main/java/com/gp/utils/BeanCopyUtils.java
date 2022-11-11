package com.gp.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}

    public static <v> v copyBean(Object s,Class<v> c) {
        //创建目标对象
        v o = null;
        try {
            o = c.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(s, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return o;
    }

    public static <o,v>List<v> copyBeanList(List<o> list,Class<v> c){
        return list.stream()
                .map(o -> copyBean(o,c))
                .collect(Collectors.toList());
    }
}
