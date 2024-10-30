package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_Config_AutoHome_new extends T_Config_Father {

    public T_Config_AutoHome_new(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }

    public ArrayList<Object> method_根据数据的获取路径和状态查找数据(String pathType, int status) {
        return method_有条件的查询("select * from " + tableName + " where C_" + pathType + " = " + status);
    }

    public void method_修改车型表中已下载的车型id状态(String modId, String type, int status) {
        method_i_d_u("update " + tableName + " set C_" + type + " = " + status + " where C_ModelID=" + modId);
    }

    public ArrayList<Object> method_根据数据类型获取未下载的数据(String dataType, int status) {

        return method_有条件的查询("select * from " + tableName + " where " + dataType + " = " + status);
    }

    public ArrayList<Object> method_根据组号查询版本id(int group) {
        return method_有条件的查询("select * from " + tableName + " where C_Group = " + group);
    }

    public void method_修改下载车辆版本配置的状态(int group, String type, int status) {
        method_i_d_u("update " + tableName + " set " + type + " = " + status + " where C_Group=" + group);
    }

    public ArrayList<Object> method_获取组号() {
        return method_有条件的查询("select distinct C_Group from " + tableName);
    }


    // 1.创建品牌表
    public void create_品牌表(String createTableName) {
        if (method_判断要创建的表是否已存在(createTableName)) {
            method_删除已存在的表(createTableName);
        }
        String sql = "create table " + createTableName + " (\n" +
                "C_ID int not null  identity(1,1) primary key,\n" +
                "C_BrandID nvarchar(200),\n" +
                "C_BrandName nvarchar(200),\n" +
                "C_BrandURL nvarchar(200),\n" +
                "C_BrandLogo nvarchar(200),\n" +
                "factoryNumber int,\n" +
                "C_UpdateTime nvarchar(200))";
        method_i_d_u(sql);


    }

    public void create_厂商表(String createTableName) {
        if (method_判断要创建的表是否已存在(createTableName)) {
            method_删除已存在的表(createTableName);
        }
        String sql = "create table " + createTableName + " (\n" +
                "C_ID int not null  identity(1,1) primary key,\n" +
                "C_FactoryID nvarchar(200),\n" +
                "C_FactoryName nvarchar(200),\n" +
                "C_FactoryURL nvarchar(200),\n" +
                "C_BrandID nvarchar(200),\n" +
                "modNumber int,\n" +
                "C_UpdateTime nvarchar(200)\n" +
                ")";
        method_i_d_u(sql);

    }

    public void create_车型表(String createTableName) {
        if (method_判断要创建的表是否已存在(createTableName)) {
            method_删除已存在的表(createTableName);
        }
        String sql = "create table " + createTableName + " (\n" +
                "C_ID int not null  identity(1,1) primary key,\n" +
                "C_ModelID nvarchar(200),\n" +
                "C_ModelName nvarchar(200),\n" +
                "C_ModelURL nvarchar(200),\n" +
                "C_BrandID nvarchar(200),\n" +
                "C_FactoryID nvarchar(200),\n" +
                "C_在售 INT,\n" +
                "C_停售 int ,\n" +
                "C_图片页面在售 int ,\n" +
                "C_图片页面停售 int,\n" +
                "C_即将销售 int,\n" +
                "C_IsFinish int,\n" +
                "C_UpdateTime nvarchar(200)\n" +
                ")";
        method_i_d_u(sql);

    }

    public void create_版本表(String createTableName) {
        if (method_判断要创建的表是否已存在(createTableName)) {
            method_删除已存在的表(createTableName);
        }
        String sql = "create table " + createTableName + " (\n" +
                "C_ID int not null  identity(1,1) primary key,\n" +
                "C_VersionID nvarchar(200),\n" +
                "C_VersionName nvarchar(200),\n" +
                "C_VersionURL nvarchar(200),\n" +
                "C_VersionStatus nvarchar(200),\n" +
                "C_ModelID nvarchar(200),\n" +
                "C_Group int,\n" +
                "params int,\n" +
                "config int,\n" +
                "bag int,\n" +
                "C_UpdateTime nvarchar(200)\n" +
                ")";
        method_i_d_u(sql);
    }


    public void create_版本Id表(String createTableName) {
        if (method_判断要创建的表是否已存在(createTableName)) {
            method_删除已存在的表(createTableName);
        }
        String sql = "create table " + createTableName + " (\n" +
                "C_ID int not null  identity(1,1) primary key,\n" +
                "C_Ids nvarchar(200),\n" +
                "C_Group int,\n" +
                "params int,\n" +
                "config int,\n" +
                "bag int,\n" +
                "C_UpdateTime nvarchar(200)\n" +
                ")";
        method_i_d_u(sql);
    }


    public void create_选装包表(String createTableName) {
        if (method_判断要创建的表是否已存在(createTableName)) {
            method_删除已存在的表(createTableName);
        }
        String sql = "create table " + createTableName + " (\n" +
                "C_ID int not null IDENTITY(1,1) PRIMARY key ,\n" +
                "C_PID nvarchar(200),\n" +
                "C_BagID nvarchar(200),\n" +
                "C_Price nvarchar(200),\n" +
                "C_Name nvarchar(200),\n" +
                "C_PriceDesc nvarchar(200),\n" +
                "C_Description text,\n" +
                "C_UpdateTime nvarchar(200)\n" +
                ")\n";
        method_i_d_u(sql);
    }

    public void update_修改版本id的分组() {
        String sql = "update " + tableName + " set C_Group = (C_ID-1)/10+1 ";
        System.out.println(sql);
        method_i_d_u(sql);
    }
}
