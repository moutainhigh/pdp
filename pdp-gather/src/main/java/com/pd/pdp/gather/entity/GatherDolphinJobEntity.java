package com.pd.pdp.gather.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author zz
 * @date 2021/10/9
 */

@Data
public class GatherDolphinJobEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String systemName;
    private String gatherJobId;
    //输入数据源
    private String driverInput;
    private String urlInput;
    private String usernameInput;
    private String passwordInput;
    private String databaseNameInput;
    private String tableNameInput;
    //输出数据源
    private String driverOutput;
    private String urlOutput;
    private String usernameOutput;
    private String passwordOutput;

    private int syncType;

    //dolphin
    private String urlDolphin;
    private String usernameDolphin;
    private String passwordDolphin;
    private String dolphinProjectName;
    private String gatherJobInfo;

    private boolean createHiveTable;
    private String crontab;


    private List<Map<String, String>> columnsOfInputTable;
    private String tableCommentOfInputTable;

    private HashMap<String, String> hiveTableSqls;

}
