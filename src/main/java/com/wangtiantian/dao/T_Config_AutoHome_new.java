package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_Config_AutoHome_new extends T_Config_Father{

    public T_Config_AutoHome_new(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }

    public ArrayList<Object> method_根据数据的获取路径和状态查找数据(String pathType,int status){
        return method_有条件的查询("select * from "+tableName+" where C_"+pathType+" = "+status);
    }

    public void method_修改车型表中已下载的车型id状态(String modId,String type,int status){
        method_i_d_u("update "+tableName+" set C_"+type+" = "+status+" where C_ModelID="+modId);
    }

    public ArrayList<Object> method_根据数据类型获取未下载的数据(String dataType,int status){

        return method_有条件的查询("select * from "+tableName+" where "+dataType+" = "+status);
    }
    public ArrayList<Object> method_根据组号查询版本id(int group){
        return method_有条件的查询("select * from "+tableName+" where C_Group = "+group);
    }

    public void method_修改下载车辆版本配置的状态(int group,String type,int status){
        method_i_d_u("update "+tableName+" set "+type+" = "+status+" where C_Group="+group);
    }

    public ArrayList<Object> method_获取组号(){
        return method_有条件的查询("select distinct C_Group from "+tableName);
    }
}
