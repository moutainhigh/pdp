package com.pd.pdp.utils;

import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author pdp
 * @Description 字符串工具类
 * @Date 2020-10-15 3:05 下午
 **/

public class StringUtil {
    public static final char  BLANK = ' ';

    public static final char  ZERO  = '0';

    private static StringUtil stringUtil;

    private StringUtil() {

    }

    public static synchronized StringUtil getInstance() {
        if (stringUtil == null) {
            stringUtil = new StringUtil();
        }
        return stringUtil;
    }

    /**
     * @Author pdp
     * @Description 右补位，左对齐
     * @Date 3:08 下午 2020/10/15
     * @Param [oriStr, len, alexin]
     * @return java.lang.String
     **/

    public String padRight(String oriStr, int len, char alexin) {
        oriStr = StringUtils.isEmpty(oriStr) == true ? "" : oriStr;
        StringBuilder str = new StringBuilder();
        int strlen = oriStr.length();
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str.append(alexin);
            }
        }
        str.insert(0, oriStr);
        return str.toString();
    }

    /**
     * @Author pdp左补位，右对齐
     * @Description
     * @Date 3:08 下午 2020/10/15
     * @Param [oriStr, len, alexin]
     * @return java.lang.String
     **/

    public String padLeft(String oriStr, int len, char alexin) {
        oriStr = StringUtils.isEmpty(oriStr) == true ? "" : oriStr;
        StringBuilder str = new StringBuilder();
        int strlen = oriStr.length();
        if (strlen < len) {
            for (int i = 0; i < len - strlen; i++) {
                str.append(alexin);
            }
        }
        str.append(oriStr);
        return str.toString();
    }

    /**
     * @Author pdp
     * @Description 金额转为12位字符串，单位分
     * @Date 3:08 下午 2020/10/15
     * @Param [value]
     * @return java.lang.String
     **/

    public String amount2String(BigDecimal value) {
        String v = new DecimalFormat(value.signum() >= 0 ? "0000000000.00" : "000000000.00").format(value);
        String str = String.format("%s%s", v.substring(0, 10), v.substring(11));
        return str;
    }

    /**
     * @Author pdp
     * @Description 字符串截取 半个中文字符用空格代替
     * @Date 3:07 下午 2020/10/15
     * @Param [value, length]
     * @return java.lang.String
     **/

    public String subStringValue(String value, int length) {
        StringBuilder sb = new StringBuilder();
        int strLength = value.length();
        int charLength = 0;
        for (int i = 0; i < strLength; i++) {
            int assicCode = value.codePointAt(i);
            if (assicCode > 0 && assicCode <= 255) {
                charLength += 1;
            } else {
                charLength += 2;
            }

            if (charLength < length) {
                sb.append(value.charAt(i));
            } else if (charLength == length) {
                sb.append(value.charAt(i));
                break;
            } else {
                sb.append(" ");
                break;
            }
        }
        return sb.toString();
    }

    /**
     * @Author pdp
     * @Description 判断字符串是否为空
     * @Date 3:07 下午 2020/10/15
     * @Param [str, isTrim]
     * @return boolean
     **/

    public static boolean isEmpty(Object str, boolean isTrim) {
        if (isTrim) {
            return (str == null || "".equals(str.toString().trim()));
        } else {
            return (str == null || "".equals(str));
        }
    }

    /**
     * @Author pdp
     * @Description 布尔类型转换成字符"0"或"1"
     * @Date 3:07 下午 2020/10/15
     * @Param [value]
     * @return java.lang.String
     **/

    public static String convertBooleanToStringNum(boolean value) {
        return value ? "1" : "0";
    }

    /**
     * @Author pdp
     * @Description 判断字符串是否为空并分割成list
     * @Date 3:07 下午 2020/10/15
     * @Param [str]
     * @return java.util.List<java.lang.Long>
     **/

    public static List<Long> isEmptyAndSplit(String str) {
        List<Long> list = null;
        String[] split = null;
        if (str != null && !"".equals(str)) {
            split = str.split(",");
            list = new ArrayList<Long>();
            for (int i = 0; i < split.length; i++) {
                list.add(Long.valueOf(split[i]));
            }
        } else {
            list = null;
        }
        return list;
    }

    /**
     * @Author pdp
     * @Description 把null转换成""(适用于不是实体类的值 如:String,Integer,List<String>等)
     * @Date 3:07 下午 2020/10/15
     * @Param [param]
     * @return java.lang.Object
     **/

    public static Object convertNullToEmpty(Object param) throws Exception {
        if (param == null) {
            return "";
        }
        return param;
    }

    /**
     * @Author pdp
     * @Description 把实体类中null转换成"" 处理List<Object> 类型的数据（如：List<TestVO>）
     * @Date 3:06 下午 2020/10/15
     * @Param [param, listMap]
     * @return void
     **/

    public static void convertNullToEmptyForEntity(Object param, List<Map<String, Object>> listMap) throws Exception {

        if (param == null) {
            return;
        }
        List<?> list1 = (List<?>) param;
        for (int j = 0; j < list1.size(); j++) {
            Object jec = list1.get(j);
            Map<String, Object> childMap = new HashMap<>();
            convertNullToEmptyForEntity(jec, childMap);

            if (!childMap.isEmpty()) {
                listMap.add(childMap);
            }
        }
    }

    /**
     * @Author pdp
     * @Description 把实体类中null转换成""
     * @Date 3:06 下午 2020/10/15
     * @Param [param, retMap]
     * @return void
     **/

    public static void convertNullToEmptyForEntity(Object param, Map<String, Object> retMap) throws Exception {

        if (param == null) {
            return;
        }
        if (!(param instanceof Integer || param instanceof String || param instanceof Double || param instanceof Float || param instanceof Long || param instanceof Boolean || param instanceof Date)) {

            Class<?> class1 = param.getClass();
            Field[] fields = class1.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];

                // 获取当前属性名称
                String name = field.getName();

                // 获取当前属性值
                PropertyDescriptor pd = new PropertyDescriptor(name, class1);
                // 获得get方法
                Method getMethod = pd.getReadMethod();
                // 执行get方法返回一个Object
                Object value = getMethod.invoke(param);

                if (value instanceof Collection) {

                    Collection<?> collection = (Collection<?>) value;

                    // 子集合返回Map类型
                    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
                    for (Object item : collection) {
                        Map<String, Object> childMap = new HashMap<>();
                        convertNullToEmptyForEntity(item, childMap);
                        if (!childMap.isEmpty()) {
                            listMap.add(childMap);
                        }
                    }

                    if (listMap != null && listMap.size() > 0) {
                        retMap.put(name, listMap);
                    } else {
                        retMap.put(name, value);
                    }

                } else {

                    // 如果值为空，返回一个""
                    if (value == null) {
                        retMap.put(name, "");
                    } else {
                        retMap.put(name, value);
                    }
                }
            }

        }
    }

}
