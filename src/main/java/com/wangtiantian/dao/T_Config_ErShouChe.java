package com.wangtiantian.dao;

public class T_Config_ErShouChe extends T_Config_Father{
    public T_Config_ErShouChe(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }
    public void update_修改已下载的城市状态(String pinyin){
        String sql ="update "+tableName+ " set C_IsFinish = 1 where C_CityPinYin='"+pinyin+"'";
        method_i_d_u(sql);
    }
}
