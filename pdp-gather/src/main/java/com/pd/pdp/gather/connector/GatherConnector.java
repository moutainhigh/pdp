package com.pd.pdp.gather.connector;

import com.pd.pdp.gather.connector.impl.ConnectionProviderHikariCP;
import com.pd.pdp.gather.utils.Base64Util;
import com.pd.pdp.gather.utils.MD5Util;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/12
 */
@Service
public class GatherConnector {
    private ConcurrentHashMap<String, ConnectionProviderHikariCP> poolMap = new ConcurrentHashMap<>();

    public ConnectionProviderHikariCP getConnInPool(String driver, String url, String username, String password) {
        ConnectionProviderHikariCP jdbcConn = null;

        String key = MD5Util.getMD5Code(driver + url + username + password);

        if (!poolMap.containsKey(key)) {
            ConnectionProviderHikariCP connectionProvider = ConnectionProviderHikariCP.getInstance();
            connectionProvider.init(driver, url, username, Base64Util.getFromBase64(password));
            poolMap.put(key, connectionProvider);
        }
        jdbcConn = poolMap.get(key);
        return jdbcConn;
    }
}
