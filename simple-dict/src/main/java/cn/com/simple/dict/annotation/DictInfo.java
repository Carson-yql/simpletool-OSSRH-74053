package cn.com.simple.dict.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典回显字段注解
 * @author Carson
 * @date 2021年09月28日 09:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DictInfo {
    /**
     * 字典分类
     * @return
     */
    String type();

    /**
     * 回显目标字段
     * @return
     */
    String target();
}
