package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_DataBase_KouBei;

import java.util.ArrayList;

public class KouBei_DataBase {
    // 选择数据库对应驱动
    private static int chooseDataBaseType = 0;

    // 选择目标数据库
    private static int chooseDataBase = 2;

    // 获取最新的车型信息数据
    public ArrayList<String> getLatestModelIdData() {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(0, 0, 2);
        return dataBaseKouBei.getDataByOneColumn("C_ModelID");
    }

    // 修改已下载的车型首页数据状态
    public void update_下载状态(String modelId, int status) {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(0, 0, 2);
        dataBaseKouBei.update_下载状态("C_ModelID", modelId, status);
    }

    // 入库分页数据
    public void insertFenYeUrl(ArrayList<Object> dataList) {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 0);
        dataBaseKouBei.insertForeach(dataList);
    }

    // 查询未下载的分页数据
    public ArrayList<Object> getFenYeUrl() {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 0);
        return dataBaseKouBei.method_查询未下载的数据();
    }

    // 修改已下载的分页数据状态
    public void update_修改已下载的分页数据状态(String modelId,int page, int status) {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 0);
        dataBaseKouBei.update_修改已下载的分页数据状态(modelId, page, status);
    }

    //入库口碑的简单信息数据
    public void insertShortKouBeiInfo(ArrayList<Object> dataList) {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 1);
        dataBaseKouBei.insertForeach(dataList);
    }

    // 获取需要下载的口碑详情总数量
    public int getShortKouBeiDataCount() {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 1);
        return dataBaseKouBei.get_未下载口碑详情页的表中总数量();
    }

    // 分批查询未下载的口碑详情页数据
    public ArrayList<Object> getShortKouBeiDataForeach(int group) {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 1);
        return dataBaseKouBei.method_分页查询未下载的口碑详情页数据(group);
    }

    // 查询未下载的口碑详情页数据
    public ArrayList<Object> getShortKouBeiData() {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 1);
        return dataBaseKouBei.method_查询未下载的数据();
    }

    // 修改已下载的口碑详情页数据
    public void update_修改已下载的口碑详情页数据(String showIds) {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 1);
        dataBaseKouBei.update_修改已下载的口碑详情页数据(showIds);
    }

    // 新增口碑数据
    public void insertKouBeiInfo(ArrayList<Object> dataList) {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 2);
        dataBaseKouBei.insertForeach(dataList);
    }

    public void insertKouBeiImgUrl(ArrayList<Object> dataList) {
        T_DataBase_KouBei dataBaseKouBei = new T_DataBase_KouBei(chooseDataBaseType, chooseDataBase, 3);
        dataBaseKouBei.insertForeach(dataList);
    }
}
