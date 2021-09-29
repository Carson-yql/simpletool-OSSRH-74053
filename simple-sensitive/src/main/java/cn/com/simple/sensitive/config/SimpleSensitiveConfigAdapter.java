package cn.com.simple.sensitive.config;

import cn.com.simple.sensitive.aspect.SensitiveAspect;
import cn.com.simple.sensitive.base.SensitiveProvide;

/**
 * @author Carson
 * @date 2021年09月28日 14:12
 */
public interface SimpleSensitiveConfigAdapter {
    /**
     * 配置过滤的切面bean对象
     *
     * @return
     */
    SensitiveAspect sensitiveAspect();

    /**
     * 配置SensitiveProvide的bean对象
     *
     * @return
     */
    SensitiveProvide sensitiveProvide();
}
