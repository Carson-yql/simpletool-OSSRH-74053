package cn.com.simple.dict.aspect;

import cn.com.simple.dict.annotation.DictInfo;
import cn.com.simple.dict.provide.DictProvide;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author Carson
 * @date 2021年09月27日 14:13
 */
@Aspect
public class DictAspect {
    @Autowired
    private DictProvide dictProvide;
    @Pointcut("@annotation(cn.com.simple.dict.annotation.DictApi)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint point) {
        Object obj = null;
        try {
            obj = point.proceed();
            formatMethod(obj);
        }catch (Throwable e){
            e.printStackTrace();
        }

        return obj;
    }

    private void formatMethod(Object obj) {
        if (obj == null || ClassUtil.isBasicType(obj.getClass()) || String.class.isAssignableFrom(obj.getClass()) ) {
            //为空,基础数据类型,字符串都不处理
            return;
        }
        if (ArrayUtil.isArray(obj)) {
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
     * 只有对象才格式化数据
     *
     * @param obj
     */
    private void objFormat(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (ClassUtil.isBasicType(field.getType()) || String.class.isAssignableFrom(field.getType())) {
                //基础类型进行处理
                setDict(obj, field);
            } else {
                //对象类型进行下一级处理
                //设置可操作
                ReflectionUtils.makeAccessible(field);
                //获取去字段值
                Object fieldValue = ReflectionUtils.getField(field, obj);
                if (fieldValue != null) {
                    formatMethod(fieldValue);
                }
            }
        }
    }
    private void setDict(Object object,Field field){
       try {
           boolean hasAnnotation = field.isAnnotationPresent(DictInfo.class);
           if (hasAnnotation){
               DictInfo dictInfo = field.getAnnotation(DictInfo.class);
               String type = dictInfo.type();
               String target = dictInfo.target();
               if (StrUtil.isBlank(type)||StrUtil.isBlank(target)){
                   return;
               }
               ReflectionUtils.makeAccessible(field);
               Field targetField = object.getClass().getDeclaredField(target);
               if (null==targetField){
                   //目标字段不存在
                   return;
               }
               Object value = ReflectionUtils.getField(field, object);
               if (null==value){
                   //字典值不存在
                   return;
               }
               Object label = dictProvide.getLabel(type, String.valueOf(value));
               ReflectionUtils.makeAccessible(targetField);
               ReflectionUtils.setField(targetField,object,label);
           }
       }catch (Exception e){
           e.printStackTrace();
       }


    }

}
