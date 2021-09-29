package cn.com.simple.desensitized.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据脱敏字段注解
 * @author Carson
 * @date 2021年09月28日 11:08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DesensitizedInfo {
    String type();
}
