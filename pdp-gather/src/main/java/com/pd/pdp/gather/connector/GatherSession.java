package com.pd.pdp.gather.connector;

import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.gather.constant.Constant;
import com.pd.pdp.gather.okhttp.FastHttpClient;
import com.pd.pdp.gather.okhttp.Response;
import com.pd.pdp.gather.utils.Base64Util;
import com.pd.pdp.gather.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/12
 */
@Service
public class GatherSession {
    private Logger logger = LoggerFactory.getLogger(GatherSession.class);

    private ConcurrentHashMap<String, String> dolphinSessionMap = new ConcurrentHashMap<>();

    public String getDolphinSession( String url, String username, String password) {
        String key = MD5Util.getMD5Code(url + username + password);
        String sessionId = dolphinSessionMap.get(key);
        boolean isSuccessful = testDolphinConn(sessionId, url);
        if (!isSuccessful) {
            String loginUrl = String.format(Constant.DOLPHIN_LOGIN_URL, url);
            ObjectParam param = new ObjectParam();
            param.userName = username;
            param.userPassword = Base64Util.getFromBase64(password);
            String resp = null;
            try {
                resp = FastHttpClient.post().
                        url(loginUrl).
                        addParams(param).
                        build().
                        execute().string();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //{"code":0,"msg":"login success","data":{"sessionId":"268a8781-ff54-42d6-a340-b09b3c14a077"}}
            if (JSONObject.parseObject(resp).getInteger(Constant.CODE) == 0) {
                sessionId = Constant.SESSION_ID + "=" + JSONObject.parseObject(resp).getJSONObject("data").get("sessionId").toString();
                dolphinSessionMap.put(key, sessionId);
            }
        }

        return sessionId;
    }

    public boolean testDolphinConn(String sessionId, String url) {
        if (sessionId == null || sessionId.isEmpty()) {
            return false;
        }
        String testUrl = String.format(Constant.DOLPHIN_TEST_CONN_URL, url);
        Response response = null;
        try {
            response = FastHttpClient.get().
                    addHeader("cookie", sessionId).
                    url(testUrl).
                    build().
                    execute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return response.isSuccessful();
    }

    @SuppressWarnings("unused")
    private static class ObjectParam {
        public String userName;
        public String userPassword;
    }
}
