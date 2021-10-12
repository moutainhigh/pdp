package com.pd.pdp.gather.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pd.pdp.gather.constant.Constant;
import com.pd.pdp.gather.constant.DataSoureceType;
import com.pd.pdp.gather.okhttp.FastHttpClient;
import com.pd.pdp.gather.okhttp.Response;
import com.pd.pdp.gather.utils.JdbcPool;
import com.pd.pdp.gather.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.pd.pdp.gather.constant.DataSoureceType.JDBC;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/11
 */

@Service
public class GatherDolphinServer {
    private Logger logger = LoggerFactory.getLogger(GatherDolphinServer.class);

    private ConcurrentHashMap<String, JdbcPool> poolMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> dolphinSessionMap = new ConcurrentHashMap<>();

    public List<Map<String, String>> selectDBByDatasource(String driver, String url, String username, String password, String dataSourceType) {

        DataSoureceType type = DataSoureceType.getType(dataSourceType);
        List<Map<String, String>> list = null;
        switch (type) {
            case JDBC:
            case HIVE:
                list = selectDBByDatasource(driver, url, username, password);
                break;
            case HTTP:
                try {
                    list = getDolphinProject(url, username, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dataSourceType);
        }

        return list;
    }

    public List<Map<String, String>> selectDBByDatasource(String driver, String url, String username, String password) {

        String key = MD5Util.getMD5Code(driver + url + username + password);
        JdbcPool jdbcConn = getConnInPool(key, driver, url, username, password);
        List<Map<String, Object>> dbs = jdbcConn.excuteQuery(Constant.SHOW_DATABASES);

        List<Map<String, String>> list = new ArrayList<>();

        //hive/mysql读取的字段名不一致，将字段都替换为dbName
        for (Map<String, Object> map : dbs) {
            HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put("dbName", map.values().toArray()[0].toString());
            list.add(hashMap);
        }

        return list;
    }

    public List<Map<String, String>> selectTablesByDB(String db, String driver, String url, String username, String password) {
        String key = MD5Util.getMD5Code(driver + url + username + password);
        JdbcPool jdbcConn = getConnInPool(key, driver, url, username, password);
        List<Map<String, Object>> tables = jdbcConn.excuteQuery(String.format(Constant.SHOW_TABLES, db));
        List<Map<String, String>> list = new ArrayList<>();

        //hive/mysql读取的字段名不一致，将字段都替换为tableName
        for (Map<String, Object> map : tables) {
            HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put("tableName", map.values().toArray()[0].toString());
            list.add(hashMap);
        }

        return list;
    }


    public List<Map<String, String>> getDolphinProject(String url, String username, String password) throws Exception {
        List<Map<String, String>> resultList = new ArrayList<>();

        String key = MD5Util.getMD5Code(url + username + password);
        String allProjectUrl = String.format(Constant.DOLPHIN_ALL_PROJECT_URL, url);
        String sessionId = getDolphinSession(key, url, username, password);
        String response = FastHttpClient.
                get().
                addHeader("cookie", sessionId).
                url(allProjectUrl).
                build().
                execute().string();

        //{"code":0,"msg":"success","data":{"totalList":[{"id":8,"userId":6,"userName":"wangsb","name":"report_data_sync","description":"日志数据同步","createTime":"2021-09-08T17:30:42.000+0800","updateTime":"2021-09-08T17:30:42.000+0800","perm":7,"defCount":7,"instRunningCount":0}],"total":6,"currentPage":1,"totalPage":1}}

        JSONArray jsonArray = JSON.parseObject(response).getJSONObject("data").getJSONArray("totalList");
        for (int i = 0; i < jsonArray.size(); i++) {
            HashMap<String, String> resultMap = new HashMap<>();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            resultMap.put("dbName", name);
            resultList.add(resultMap);
        }

        return resultList;
    }

    private JdbcPool getConnInPool(String key, String driver, String url, String username, String password) {
        JdbcPool jdbcConn = null;
        if (!poolMap.containsKey(key)) {
            jdbcConn = new JdbcPool(driver, url, username, password).getInstance();
            poolMap.put(key, jdbcConn);
        }
        jdbcConn = poolMap.get(key);
        return jdbcConn;
    }


    public String getDolphinSession(String key, String url, String username, String password) throws Exception {
        String sessionId = dolphinSessionMap.get(key);
        boolean isSuccessful = testDolphinConn(sessionId, url);
        if (!isSuccessful) {
            String loginUrl = String.format(Constant.DOLPHIN_LOGIN_URL, url);
            ObjectParam param = new ObjectParam();
            param.userName = username;
            param.userPassword = password;
            String resp = FastHttpClient.post().
                    url(loginUrl).
                    addParams(param).
                    build().
                    execute().string();

            //{"code":0,"msg":"login success","data":{"sessionId":"268a8781-ff54-42d6-a340-b09b3c14a077"}}
            sessionId = Constant.SESSION_ID + "=" + JSONObject.parseObject(resp).getJSONObject("data").get("sessionId").toString();
            dolphinSessionMap.put(key, sessionId);
        }

        return sessionId;
    }

    public boolean testDolphinConn(String sessionId, String url) throws Exception {
        if (sessionId == null || sessionId.isEmpty()) {
            return false;
        }
        String testUrl = String.format(Constant.DOLPHIN_TEST_CONN_URL, url);
        Response response = FastHttpClient.get().
                addHeader("cookie", sessionId).
                url(testUrl).
                build().
                execute();
        return response.isSuccessful();
    }

    @SuppressWarnings("unused")
    private static class ObjectParam {
        public String userName;
        public String userPassword;
    }
}
