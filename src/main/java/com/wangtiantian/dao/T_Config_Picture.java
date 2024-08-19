package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_Config_Picture extends T_Config_Father {
    public T_Config_Picture(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }

    public void update_修改已下载数据的版本状态(String versionID) {
        String sql = "update " + tableName + " set C_IsFinish =1 where C_VersionID = " + versionID;
        method_i_d_u(sql);
    }

    public void update_修改已下载分页数据的版本状态(String fenYeUrl) {
        String sql = "update " + tableName + " set C_IsFinish =1 where C_FenYeUrl = '" + fenYeUrl + "'";
        method_i_d_u(sql);
    }

    public void update_修改下载图片具体页面的下载状态(String html) {
        String sql = "update " + tableName + " set C_IsFinish =1 where C_PictureHtmlUrl = '" + html + "'";
        method_i_d_u(sql);
    }
}
