package cn.com.simple.sensitive.config;

import cn.com.simple.sensitive.aspect.SensitiveAspect;
import org.springframework.context.annotation.Bean;

/**
 * @author Carson
 * @date 2021年09月29日 15:43
 */
public abstract class SimpleSensitiveConfig implements SimpleSensitiveConfigAdapter {
    @Override
    @Bean
    public SensitiveAspect sensitiveAspect() {
        return new SensitiveAspect();
    }
}
