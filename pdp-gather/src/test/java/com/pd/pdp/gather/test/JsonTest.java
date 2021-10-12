package com.pd.pdp.gather.test;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/11
 */
public class JsonTest {
    @Test
    public void TestJson() {
        String resp = "{\"code\":0,\"msg\":\"login success\",\"data\":{\"sessionId\":\"268a8781-ff54-42d6-a340-b09b3c14a077\"}}";
        String s = JSONObject.parseObject(resp).getJSONObject("data").get("sessionId").toString();
        System.out.println(s);
    }
}
