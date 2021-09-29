package cn.com.simple.sensitive.base;

import java.util.List;

/**
 * @author Carson
 * @date 2021年09月29日 11:39
 */
public interface SensitiveProvide {
    /**
     * 过滤
     * @param text
     * @return
     */
    String stage(String text);
    /**
     * 设置需要过滤的敏感词集合
     * @param words
     * @return
     */
    SensitiveProvide setSensitiveWord(List<String> words);


    /**
     * 设置替换字符
     * @param str
     * @return
     */
    SensitiveProvide setReplaceStr(String str);

    /**
     * 是否最佳匹配
     * @param matchType
     * @return
     */
    SensitiveProvide setMatchType(Boolean matchType);
}
