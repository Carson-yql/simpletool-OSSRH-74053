import cn.com.simple.sensitive.base.SensitiveProvide;
import cn.com.simple.sensitive.provide.DfaProvide;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Carson
 * @date 2021年09月29日 16:12
 */
public class IndexTest {
    @Test
    public void sensitive(){
        List<String> list= Arrays.asList("习大","共产党");
        SensitiveProvide sensitiveProvide = new DfaProvide()
                .setMatchType(false)
                .setSensitiveWord(list);
        String stage = sensitiveProvide.stage("习大五共产党万岁");
        System.out.println(stage);
    }
}
