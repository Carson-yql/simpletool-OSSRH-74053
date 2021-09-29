package cn.com.simple.desensitized.provide;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Carson
 * @date 2021年09月28日 11:14
 */
public class DesensitizedProvide {
    private Map<String, Function<String, String>> map=new HashMap<>();


    public DesensitizedProvide() {
        map.put("mobile",t -> DesensitizedUtil.mobilePhone(t));
        map.put("password",t -> DesensitizedUtil.password(t));
        map.put("idCard",t -> DesensitizedUtil.idCardNum(t,1,2));
        map.put("bankCard",t -> DesensitizedUtil.bankCard(t));
        map.put("email",t -> DesensitizedUtil.email(t));
        map.put("fixedPhone",t -> DesensitizedUtil.fixedPhone(t));
        map.put("address",t -> DesensitizedUtil.address(t,5));
        map.put("chineseName",t -> DesensitizedUtil.chineseName(t));
        map.put("carLicense",t -> DesensitizedUtil.carLicense(t));

    }
    public DesensitizedProvide addStrategy(String key,Function<String, String> value) {
        map.put(key, value);
        return this;
    }
    public String strategy(String type,String value) {
        if (StrUtil.isBlank(type)||StrUtil.isBlank(value)){
            return null;
        }
        Function<String, String> function = map.get(type);
        if (null==function){
            return null;
        }
        String apply = function.apply(value);
        return apply;
    }
}
