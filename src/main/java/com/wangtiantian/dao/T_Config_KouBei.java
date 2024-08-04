package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_Config_KouBei extends T_Config_Father {
    public T_Config_KouBei(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }
    public void method修改车型口碑页面的下载状态(String url){
        method_i_d_u("update "+tableName+" set C_IsFinish = 1 where C_ModelKouBeiUrl= '"+url+"'");
    }
    public ArrayList<Object> getUrl未下载(){
        return method_有条件的查询("select * from  "+tableName+ " where C_IsFinish = 0");
    }
}
