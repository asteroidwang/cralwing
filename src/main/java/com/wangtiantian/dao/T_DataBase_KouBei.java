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
        System.out.println(sql);
        method_i_d_u(sql);
    }
    public void update_修改已下载的口碑图片数据(String showIds) {
        String sql = "update " + tableName + " set C_IsFinish = 1 where concat(C_ShowID,'_',C_KouBeiID,'_',C_Position)  in ("+showIds+")";
        System.out.println(sql);
        method_i_d_u(sql);
    }

    public int get_未下载口碑详情页的表中总数量(){
        String sql ="select count(*) from "+tableName+ "  where C_ShowId not in (select distinct C_ShowID from T_KouBeiInfo_20240804) and C_IsFinish = 0";
        return get_获取表中数据数量_有查询条件(sql);
    }

    public ArrayList<Object> method_分页查询未下载的口碑详情页数据(int begin) {
        String sql = "SELECT * FROM " + tableName + " where C_IsFinish = 0 and C_ShowId not in (select distinct C_ShowID from T_KouBeiInfo_20240804)  ORDER BY C_ID OFFSET " + begin + " ROWS FETCH NEXT 10000 ROWS ONLY";
        return method_有条件的查询(sql);
    }

    public ArrayList<Object> method_查询本轮下载中未下载的数据(String updateTime,int begin){
        String sql ="select * from "+tableName+" where C_IsFinish = 0 and C_UpdateTime like '%"+updateTime+"%' ORDER BY C_ID OFFSET " + begin + " ROWS FETCH NEXT 10000 ROWS ONLY";
        return method_有条件的查询(sql);
    }
    public int get_查询本轮下载中未下载的数据(String updateTime){
        String sql ="select count(*) from "+tableName+ "  where C_IsFinish = 0 and C_UpdateTime like '%"+updateTime+"%'";
        return get_获取表中数据数量_有查询条件(sql);
    }

}
