package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_ErShouChe;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.ershouche.yiChe.YiChe_FenYeUrl;

import java.util.ArrayList;

public class ErShouCheDataBase {
    //选择数据库和连接的数据类型
    private static int chooseDataBase = 4;
    private static int chooseDataBaseType = 0;

    // che168
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
    public void update_修改已下载的城市状态(String pinyin) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 0);
        cityDataDao.update_修改已下载的城市状态(pinyin);
    }

    // 分页url入库
    public void insert_新增二手车数据分页(ArrayList<Object> dataList) {
        T_Config_ErShouChe fenYeDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 1);
        fenYeDao.insertForeach(dataList);
    }

    // 获取未下载的分页url
    public ArrayList<Object> get_获取未下载的分页url() {
        T_Config_ErShouChe fenYeDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 1);
        return fenYeDao.get_查找未下载的数据();
    }

    // 修改分页数据下载状态
    public void update_修改已下载的分页数据下载状态(String url) {
        T_Config_ErShouChe fenYeDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 1);
        fenYeDao.update_修改已下载的分页数据城的下载状态(url);
    }

    // yiche
    // 插入易车的城市数据
    public void insert_入库易车的城市数据(ArrayList<Object> dataList) {
        try {
            T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 2);
            cityDataDao.insertForeach(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取未下载分页首页的城市
    public ArrayList<Object> yiche_get_获取未下载首页分页的城市() {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 2);
        return cityDataDao.method_查询易车未下载首页的城市数据();
    }

    // 修改已下载分页首页的城市下载状态
    public void yiche_update_修改已下载的首页数据的下载状态(String cityId) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 2);
        cityDataDao.update_修改已下载的首页数据的下载状态(cityId);
    }

    // 根据城市拼音获取其他数据
    public ArrayList<Object> yiche_get_根据城市拼音获取其他数据(String pinYin) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 2);
        return cityDataDao.get_根据城市拼音获取其他数据(pinYin);
    }

    // 分页url数据入库
    public void yiche_insert_城市分页数据url入库(ArrayList<Object> dataList) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 3);
        cityDataDao.insertForeach(dataList);

    }

    // 获取未下载的分页url
    public ArrayList<Object> yiche_get_获取未下载的城市分页url() {
        T_Config_ErShouChe fenYeDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 3);
        return fenYeDao.get_查找未下载的数据();
    }

    // 修改分页数据下载状态
    public void yiche_update_修改已下载的分页数据下载状态(String url) {
        T_Config_ErShouChe fenYeDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 3);
        fenYeDao.update_修改已下载的分页数据城的下载状态(url);
    }

    // 车辆基本信息数据入库
    public void yiche_insert_车辆基本信息数据入库(ArrayList<Object> dataList) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 4);
        cityDataDao.insertForeach(dataList);
    }

    // 车辆基本信息数据入库
    public void yiche_insert_确认车辆基本信息数据下载数据入库(ArrayList<Object> dataList) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 8);
        cityDataDao.insertForeach(dataList);
        T_Config_ErShouChe dataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 4);
        dataDao.yiche_update_修改已下载详情页面的数据状态();
    }


    public ArrayList<Object> yiche_get_车辆基本信息数据() {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 4);
        return cityDataDao.get_查找未下载的数据();
    }

    public void yiche_update_修改已下载详情页面的车辆状态(String ucarId, String cityName) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 4);
        cityDataDao.yiche_update_修改已下载详情页面的车辆状态(ucarId, cityName);
    }


    // 人人车
    // 1.城市数据入库
    public void rrc_insert_城市数据入库(ArrayList<Object> dataList) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 5);
        cityDataDao.insertForeach(dataList);
    }

    public ArrayList<Object> rrc_get_获取未下载首页分页的城市() {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 5);
        return cityDataDao.method_查询未下载的数据();
    }

    // 修改已下载分页首页的城市下载状态
    public void rrc_update_修改已下载的首页数据的下载状态(String cityId) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 5);
        cityDataDao.update_修改已下载的首页数据的下载状态(cityId);
    }

    // 入库城市分页数据url
    public void rrc_insert_入库城市分页Url数据(ArrayList<Object> dataList) {
        T_Config_ErShouChe carInoDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 6);
        carInoDao.insertForeach(dataList);
    }

    // 获取未下载的分页数据
    public ArrayList<Object> rrc_get_获取未下载分页的url数据() {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 6);
        return cityDataDao.method_查询未下载的数据();
    }

    // 修改已下载分页首页的城市下载状态
    public void rrc_update_修改已下载的分页数据的下载状态(String url) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 6);
        cityDataDao.update_修改已下载的分页数据城的下载状态(url);
    }

    // 入库车辆基本信息数据
    public void rrc_insert_入库车辆的基本信息数据(ArrayList<Object> dataList) {
        T_Config_ErShouChe carInoDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 7);
        carInoDao.insertForeach(dataList);
    }
    /***************
     *
     * ******************/
    // 车300
    // 入库城市数据
    public void che300_insert_入库城市数据(ArrayList<Object> dataList) {
        T_Config_ErShouChe carInoDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 9);
        carInoDao.insertForeach(dataList);
    }
    // 获取未下载的分页数据
    public ArrayList<Object> che300_get_获取未下载首页的数据() {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 9);
        return cityDataDao.method_查询未下载的数据();
    }
    public void che300_update_修改已下载的分页数据的下载状态(String cityId) {
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 9);
        cityDataDao.update_修改完成下载任务的城市状态(cityId);
    }

    public void che300_insert_入库车辆基本信息数据(ArrayList<Object> dataList){
        T_Config_ErShouChe cityDataDao = new T_Config_ErShouChe(chooseDataBaseType, chooseDataBase, 10);
        cityDataDao.insertForeach(dataList);
    }
}
