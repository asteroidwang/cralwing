package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_AutoHome_new;
import com.wangtiantian.dao.T_Config_Price;

import java.util.ArrayList;

public class ModelDealerPriceDataBase {
    //选择数据库与
    private static int chooseDataBaseType = 0;
    private static int chooseDataBase = 5;

    // 1.获取未下载的车型id
    public ArrayList<Object> get_获取未下载首页经销商数据的车型数据() {
        T_Config_AutoHome_new modelDao = new T_Config_AutoHome_new(0, 0, 2);
        return modelDao.get_查找未下载的数据();
    }

    // 2.车型页面经销商分页json数据入库
    public void insert_车型页面经销商分页json数据入库(ArrayList<Object> dataList) {
        T_Config_Price price = new T_Config_Price(chooseDataBaseType, chooseDataBase, 0);
        price.insertForeach(dataList);
    }

    // 3.获取车型经销商的未下载分页数据
    public ArrayList<Object> get_车型经销商未下载的分页数据() {
        T_Config_Price price = new T_Config_Price(chooseDataBaseType, chooseDataBase, 0);
        return price.get_查找未下载的数据();
    }

    // 2.修改已下载首页的车型id 的下载状态
    public void update_修改已下载首页的车型id的下载状态(String url) {
        T_Config_Price price = new T_Config_Price(chooseDataBaseType, chooseDataBase, 0);
        price.update_修改所有经销商分页url表的数据的下载状态(url);
    }

    // 4.车型页面获得的经销商数据入库
    public void insert_车型页面经销商信息数据入库(ArrayList<Object> dataList) {
        T_Config_Price price = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        price.insertForeach(dataList);
    }

    //
    public ArrayList<Object> getNoFinishModelDealerData() {
        return new T_Config_Price(chooseDataBaseType, chooseDataBase, 1).findDealerCityNotFinish();
    }

    public void insertConfirmCarPriceFileModel(ArrayList<Object> dataList) {
        T_Config_Price modelDealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        modelDealerDao.insertForeach(dataList);
    }
    // 5.修改已下载的车辆价格信息的状态
    public void update_修改已下载的车辆价格信息的状态() {
        T_Config_Price price = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        price.update_修改已下载的车辆价格信息的状态();
    }

    public ArrayList<Object> get_dataListDealer() {
        T_Config_Price modelDealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        return modelDealerDao.method_查找();
    }
    public void insert_carPriceDataModel(ArrayList<Object> dataList) {
        T_Config_Price carPriceDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 3);
        carPriceDao.insertForeach(dataList);
    }
}

