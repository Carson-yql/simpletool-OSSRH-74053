package cn.com.simple.dict.provide;


import cn.com.simple.dict.service.DictDetailService;

/**
 * @author Carson
 * @date 2021年09月23日 11:24
 */
public class DictProvide {

    private DictDetailService dictDetailService;

    public DictProvide(DictDetailService dictDetailService) {
        this.dictDetailService = dictDetailService;
    }

    public Object getLabel(String type, String value){
        if (null!=dictDetailService){
            return dictDetailService.getLabelByValue(type, value);
        }
        return null;
    }


}
