package cn.com.simple.dict.config;

import cn.com.simple.dict.aspect.DictAspect;
import org.springframework.context.annotation.Bean;

/**
 * 在配置文件中继承此配置文件
 * @author Carson
 * @date 2021年09月27日 18:10
 */
public abstract class SimpleDictConfig  implements SimpleDictConfigAdapter{
    @Override
    @Bean
    public DictAspect dictAspect(){
        return new DictAspect();
    }

}
