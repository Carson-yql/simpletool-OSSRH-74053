package cn.com.simple.desensitized.config;

import cn.com.simple.desensitized.aspect.DesensitizedAspect;
import cn.com.simple.desensitized.provide.DesensitizedProvide;

/**
 * @author Carson
 * @date 2021年09月28日 14:17
 */
public interface SimpleDesensitizedAdapter {
    /**
     * 配置脱敏切面
     * @return
     */
     DesensitizedAspect desensitizedAspect();
    /**
     * 配置DesensitizedProvide的bean对象
     * @return
     */
    DesensitizedProvide desensitizedProvide();
}
