import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.feiqu.common.utils.AESUtil;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Administrator on 2019/1/26.
 */
public class Test1 {

    private final static String                LOG_FORMAT = "<web><OpEmpServiceGroupController><{}><{}><{}>{}";

    @Test
    public void test6(){
        Console.log(DigestUtil.md5Hex("123456"));
    }

    @Test
    public void test5(){
        String url = "https://www.toutiao.com/api/pc/feed/?max_behot_time=1549084277&category=__all__&utm_source=toutiao&widen=1&tadrequire=true&as=A1A51C4545349B8&cp=5C55E4D94B78FE1&_signature=UxIsnRASD0gEJWTG7L02QlMSLI";
        String result= HttpUtil.get(url);
        Console.log(result);
        Console.log(new JSONObject(result).toStringPretty());
    }
    @Test
    public void test4(){
    System.out.print(System.currentTimeMillis());

    }
    @Test
    public void test3(){
        Console.log(StrUtil.format(LOG_FORMAT,"doAddMember","1","2","操作入参：撒啊啊啊啊啊啊啊啊啊"));
    }

    @Test
    public void test1(){
        Console.log(AESUtil.aesEncode("foobared"));
    }

    @Test
    public void test2(){
        String[] keys = {
                "root", "123456", ""
        };
        System.out.println("key | AESEncode | AESDecode");
        for (String key : keys) {
            System.out.print(key + " | ");
            String encryptString = aesEncode(key);
            System.out.print(encryptString + " | ");
            String decryptString = aesDecode(encryptString);
            System.out.println(decryptString);
        }
    }

}
