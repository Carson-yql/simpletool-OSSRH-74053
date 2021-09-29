package cn.com.simple.dict.service;

/**
 * @author Carson
 * @date 2021年09月26日 16:13
 */
public interface DictDetailService {
    /**
     * 根据类型和字典值获取字典的映射
     * @param type
     * @param value
     * @return
     */
    Object getLabelByValue(String type,String value);
}
