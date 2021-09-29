package cn.com.simple.dict.config;

import cn.com.simple.dict.aspect.DictAspect;
import cn.com.simple.dict.provide.DictProvide;

/**
 * @author Carson
 * @date 2021年09月28日 14:12
 */
public interface SimpleDictConfigAdapter {
    /**
     * 配置dict的切面bean对象
     * @return
     */
     DictAspect dictAspect();
    /**
     * 配置dictProvide的bean对象
     * @return
     */
    DictProvide dictProvide();
}
