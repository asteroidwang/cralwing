package com.wangtiantian.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class T_ZiDuan_Params_Config {
    public static HashMap<String, String> params_字段() {
        HashMap<String, String> paramsMapList = new HashMap<>();        paramsMapList.put("C_UpdateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return paramsMapList;
    }

    public static HashMap<String, String> config_字段() {
        HashMap<String, String> configMapList = new HashMap<>();        configMapList.put("C_UpdateTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return configMapList;
    }
}