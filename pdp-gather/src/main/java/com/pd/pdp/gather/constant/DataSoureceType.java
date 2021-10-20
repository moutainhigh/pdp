package com.pd.pdp.gather.constant;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/11
 */
public enum DataSoureceType {
    //jdbc
    JDBC("JDBC"),
    //hive
    HIVE("HIVE"),
    //http
    HTTP("HTTP");


    private String value;

    private DataSoureceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DataSoureceType getType(String dataTypeCode) {
        for (DataSoureceType enums : DataSoureceType.values()) {
            if (enums.value.equalsIgnoreCase(dataTypeCode)) {
                return enums;
            }
        }
        return null;
    }
}
