package cn.com.simple.desensitized.aspect;

import cn.com.simple.desensitized.annotation.DesensitizedInfo;
import cn.com.simple.desensitized.provide.DesensitizedProvide;
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
 * 数据脱敏切面
 * @author Carson
 */
@Aspect
public class DesensitizedAspect {
    @Autowired
    private DesensitizedProvide desensitizedProvide;
    @Pointcut("@annotation(cn.com.simple.desensitized.annotation.DesensitizedApi)")
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
                setStrategy(obj, field);
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
    private void setStrategy(Object object,Field field){
        try {
            boolean hasAnnotation = field.isAnnotationPresent(DesensitizedInfo.class);
            if (hasAnnotation){
                DesensitizedInfo annotation = field.getAnnotation(DesensitizedInfo.class);
                String type = annotation.type();
                if (StrUtil.isBlank(type)){
                    return;
                }
                if (!String.class.isAssignableFrom(field.getType())){
                    //非字符串不脱敏
                    return;
                }
                ReflectionUtils.makeAccessible(field);
                Object value = ReflectionUtils.getField(field, object);
                if (null==value){
                    //值不存在
                    return;
                }
                String strategy = desensitizedProvide.strategy(type, String.valueOf(value));
                ReflectionUtils.setField(field,object,strategy);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
