package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_ErShouChe;
import com.wangtiantian.dao.T_Config_Price;

import java.util.ArrayList;

public class ErShouCheDataBase {
    //选择数据库和连接的数据类型
    private static int chooseDataBase = 4;
    private static int chooseDataBaseType = 0;

    // 插入城市数据
    public void insert_cityData(ArrayList<Object> dataList) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 0);
        cityDataDao.insertForeach(dataList);
    }
    // 获取未下载分页第一页数据的城市
    public ArrayList<Object> get_CityData() {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 0);
        return cityDataDao.method_查询未下载的数据();
    }
    // 修改已下载分页第一页的城市下载状态
    public void update_修改已下载的城市状态(String pinyin){
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 0);
       cityDataDao.update_修改已下载的城市状态(pinyin);
    }
    // 分页url入库
    public void insert_新增二手车数据分页(ArrayList<Object> dataList){
        T_Config_ErShouChe fenYeDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 1);
        fenYeDao.insertForeach(dataList);
    }
    // 获取未下载的分页url
    public ArrayList<Object> get_获取未下载的分页url(){
        T_Config_ErShouChe fenYeDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 1);
        return  fenYeDao.get_查找未下载的数据();
    }
    // 修改分页数据下载状态
    public void update_修改已下载的分页数据下载状态(String  url){
        T_Config_ErShouChe fenYeDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 1);
        fenYeDao.update_修改已下载的分页数据城的下载状态(url);
    }
}
