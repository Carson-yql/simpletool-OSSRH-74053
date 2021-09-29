package cn.com.simple.sensitive.provide;

import cn.com.simple.sensitive.base.SensitiveProvide;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;

import java.util.*;

/**
 * @author Carson
 * @date 2021年09月28日 17:58
 */
public class DfaProvide implements SensitiveProvide {
    private HashMap sensitiveWordMap;
    private String replaceStr = "*";
    private Boolean matchType = false;


    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：
     */
    private void addSensitiveWord(List<String> words) {
        sensitiveWordMap = new HashMap(words.size());
        Iterator<String> iterator = words.iterator();
        Map<String, Object> now = null;
        Map now2 = null;
        while (iterator.hasNext()) {
            now2 = sensitiveWordMap;
            String word = iterator.next().trim(); //敏感词
            for (int i = 0; i < word.length(); i++) {
                char key_word = word.charAt(i);
                //carson 考虑下now2.get会不会空指针
                Object obj = now2.get(key_word);
                if (obj != null) {
                    //存在
                    now2 = (Map) obj;
                } else {
                    //不存在
                    now = new HashMap<String, Object>();
                    now.put("isEnd", false);
                    now2.put(key_word, now);
                    now2 = now;
                }
                if (i == word.length() - 1) {
                    now2.put("isEnd", true);
                }
            }
        }
    }

    /**
     * 获取内容中的敏感词
     *
     * @param text 内容
     * @return
     */
    public List<String> getSensitiveWord(String text) {
        List<String> words = new ArrayList<String>();
        Map now = sensitiveWordMap;
        int count = 0; //初始化敏感词长度
        int start = 0; //标志敏感词开始的下标
        for (int i = 0; i < text.length(); i++) {
            char key = text.charAt(i);
            now = (Map) now.get(key);
            if (now != null) {
                //存在
                count++;
                if (count == 1) {
                    start = i;
                }
                if (BooleanUtil.isTrue(BooleanUtil.toBoolean(now.get("isEnd").toString()))) { //敏感词结束
                    //重新获取敏感词库
                    now = sensitiveWordMap;
                    //取出敏感词，添加到集合
                    words.add(text.substring(start, start + count));
                    //初始化敏感词长度
                    count = 0;
                }
            } else {
                //不存在
                //重新获取敏感词库
                now = sensitiveWordMap;
                if (count != 0) {
                    //前面匹配上了
                    if (matchType) {
                        //最佳匹配
                        words.add(text.substring(start, start + count));
                    }
                    //不最佳匹配
                    count = 0;

                }

            }
        }
        return words;
    }


    @Override
    public String stage(String text) {
        List<String> words = getSensitiveWord(text);
        if (ArrayUtil.isNotEmpty(words)) {
            for (String word :
                    words) {
                String re = "";
                for (int i = 0; i < word.length(); i++) {
                    re += replaceStr;
                }
                text = text.replaceAll(word, re);
            }
        }
        return text;
    }

    @Override
    public SensitiveProvide setSensitiveWord(List<String> words) {
        addSensitiveWord(words);
        return this;
    }

    @Override
    public SensitiveProvide setReplaceStr(String str) {
        if (StrUtil.isNotBlank(str)) {
            this.replaceStr = str;
        }
        return this;
    }

    @Override
    public SensitiveProvide setMatchType(Boolean matchType) {
        if (BooleanUtil.isTrue(matchType)) {
            this.matchType = matchType;
        }
        return this;
    }
}
