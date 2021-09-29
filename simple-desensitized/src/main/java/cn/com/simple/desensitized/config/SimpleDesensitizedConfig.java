package cn.com.simple.desensitized.config;

import cn.com.simple.desensitized.aspect.DesensitizedAspect;
import org.springframework.context.annotation.Bean;

/**
 * 在配置文件中继承此配置文件
 * @author Carson
 * @date 2021年09月27日 18:10
 */
public abstract class SimpleDesensitizedConfig implements SimpleDesensitizedAdapter{
    @Override
    @Bean
    public DesensitizedAspect desensitizedAspect(){
        return new DesensitizedAspect();
    }


}
