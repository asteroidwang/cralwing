package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_DataBase_KouBei extends T_Config_Father {
    public T_DataBase_KouBei(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }

    public ArrayList<String> getDataByOneColumn(String columnName) {
        String sql = "select distinct " + columnName + " from " + tableName + " where C_Isfinish = 0";
        return method_单列数据的查询(sql);
    }

    public void update_下载状态(String columnName, String columnValue, int status) {
        String sql = "update " + tableName + " set C_IsFinish = " + status + " where " + columnName + " = " + columnValue;
        method_i_d_u(sql);
    }

    public void update_修改已下载的分页数据状态(String modelId,int page, int status) {
        String sql = "update " + tableName + " set C_IsFinish = " + status + " where C_ModelID = " + modelId+" and C_Page="+page;
        method_i_d_u(sql);
    }
    public void update_修改已下载的口碑详情页数据(String showIds) {
        String sql = "update " + tableName + " set C_IsFinish = 1 where C_ShowId  in ("+showIds+")";
        method_i_d_u(sql);
    }



}
