package cn.com.simple.sensitive.aspect;

import cn.com.simple.sensitive.base.SensitiveProvide;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author Carson
 * @date 2021年09月29日 14:52
 */
@Aspect
public class SensitiveAspect {
    @Autowired
    private SensitiveProvide sensitiveProvide;
    @Pointcut("@annotation(cn.com.simple.sensitive.annotation.Sensitive)")
    public void annotationPointCut() {
    }
    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint point){
        Object result=null;
        try {
            result=point.proceed();
            formatMethod(result);
        }catch (Throwable e){
            e.printStackTrace();
        }
        return result;
    }

    private void formatMethod(Object obj) {
        if (obj == null || ClassUtil.isBasicType(obj.getClass())) {
            //为空或基础数据类型,
            return;
        }
        if ( String.class.isAssignableFrom(obj.getClass())){
            //字符串处理
            obj=stage(String.valueOf(obj));
        }else if (ArrayUtil.isArray(obj)) {
            for (Object object : (Object[]) obj) {
                formatMethod(object);
            }
        } else if (Collection.class.isAssignableFrom(obj.getClass())) {
            for (Object o : ((Collection) obj)) {
                formatMethod(o);
            }
        } else if (Map.class.isAssignableFrom(obj.getClass())) {
            for (Object o : ((Map) obj).values()) {
                formatMethod(o);
            }
        } else {
            objFormat(obj);
        }
    }

    /**
     * 对象才格式化数据
     *
     * @param obj
     */
    private void objFormat(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            //设置可操作
            ReflectionUtils.makeAccessible(field);
            Object fieldValue = ReflectionUtils.getField(field, obj);
            if (fieldValue != null) {
                if (String.class.isAssignableFrom(field.getType())) {
                    //字符串
                    Object stage = stage(String.valueOf(fieldValue));
                    ReflectionUtils.setField(field,obj,stage);
                }else if (ClassUtil.isBasicType(field.getType()) ){
                    //基础类型不进行处理
                    continue;
                }else {
                    //对象类型进行下一级处理
                    //获取去字段值
                    formatMethod(fieldValue);
                }

            }

        }
    }


    private String stage(String text){
        if (null!=sensitiveProvide){
            text=sensitiveProvide.stage(text);
        }
        return  text;
    }




}
