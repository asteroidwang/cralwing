package com.wangtiantian.mapper;

import com.sun.corba.se.spi.ior.ObjectKey;
import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.price.*;

import java.util.ArrayList;
import java.util.List;

public class PriceDataBase {
    //选择数据库与
    private static int chooseDataBaseType = 0;
    private static int chooseDataBase = 1;

    //选择要操作的数据库表
    // 入库城市数据
    public void price_cityData(ArrayList<Object> dataList) {
        T_Config_Price cityDataDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 0);
        cityDataDao.insertForeach(dataList);
    }

    // 查询城市数据
    public ArrayList<Object> allDataList(String type) {
        T_Config_Price cityDataDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 0);
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            if (type.equals("经销商城市数据")) {
                dataList = cityDataDao.method_查找();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    // 入库所有的经销商分页url
    public void insert_入库经销商的分页url数据(ArrayList<Object> dataList) {
        T_Config_Price dealerFenYeDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        dealerFenYeDao.insertForeach(dataList);
    }

    // 查询所有经销商分页url表的数据
    public ArrayList<Object> get_所有未下载的经销商分页url数据() {
        T_Config_Price dealerFenYeDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        return dealerFenYeDao.get_查找未下载的数据();
    }

    // 修改所有经销商分页url表的数据的下载状态
    public void update_修改所有经销商分页url表的数据的下载状态(String fenyeUrl) {
        T_Config_Price dealerFenYeDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        dealerFenYeDao.update_修改所有经销商分页url表的数据的下载状态(fenyeUrl);
    }

    // 查询还有哪些经销商的车型报价页面没有下载
    public ArrayList<Object> get_没有下载的经销商的车型报价页面html() {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        return dealerDao.findDealerCityNotFinish();
    }

    // 下载经销商的在售车型数据
    public ArrayList<Object> get_没有下载的经销商的车型报价页面接口() {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        return dealerDao.findDealerCityNotFinish();
    }

    public void update_修改下载了的经销商报价页面的下载状态(String dealerID) {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        dealerDao.updateCarPriceStatus(dealerID);
    }

    // 经销商的车型信息入库
    public void insert_经销商的车型信息数据入库(ArrayList<Object> dataList) {
        T_Config_Price saleModDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 3);
        saleModDao.insertForeach(dataList);

    }

    public ArrayList<Object> get_未下载的经销商车型数据() {
        T_Config_Price saleModDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 3);
        return saleModDao.findDealerCityNotFinish();
    }

    public void update_修改车型经销商数据下载状态(String dealerID, String modelId) {
        T_Config_Price saleModDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 3);
        saleModDao.updateNoDealerModelStatus(dealerID, modelId);
    }

    public void insert_确认已下载的数据(ArrayList<Object> dataList) {
        T_Config_Price modelDealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 6);
        modelDealerDao.insertForeach(dataList);
    }

    // 经销商版本价格数据入库
    public void insert_车辆的价格信息数据入库(ArrayList<Object> dataList) {
        T_Config_Price carPriceDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 5);
        carPriceDao.insertForeach(dataList);
    }

    public void method_修改经销商分页的下载状态为未下载() {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);

        T_Config_Price dealerFenYeDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        ArrayList<Object> fenYeDataList = dealerFenYeDao.get_所有下载有重复经销商数据的城市名称();
        for (Object o : fenYeDataList) {
            String cityName = ((DealerData) o).get_C_CityName();
            System.out.println(cityName);
        }
    }

    /***
     *自此以上代码为修改过的可以直接用 20240813，以下不可以
     */


    // 查询 还有 哪些城市的经销商列表数据没下载完成
    public ArrayList<Object> findDealerCityNotFinish() {
        T_Config_Price cityDataDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 0);
        return cityDataDao.findDealerCityNotFinish();
    }

    // 修改经销商数据下载完成的城市的状态
    public void updateDealerCityStatus(String cityID) {
        T_Config_Price cityDataDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 0);
        cityDataDao.updateDealerCityStatus(cityID);
    }

    //  经销商数据入库
    public void insert_经销商数据入库(ArrayList<Object> dataList) {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        dealerDao.insertForeach(dataList);

    }


    // 修改经销商数据下载完成的城市的状态
    public void updateCarPriceStatus(String dealerID) {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        dealerDao.updateCarPriceStatus(dealerID);
    }

    // 价格数据信息下载


    // 获取未下载的车辆信息的数据的url
    public ArrayList<Object> findNoDealerModel() {
        T_Config_Price saleModDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        return saleModDao.findNoDealerModel();
    }

    // 修改经销商数据下载完成的城市的状态
    public void updateNoDealerModelStatus(String dealerID, String modelId) {
        T_Config_Price saleModDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        saleModDao.updateNoDealerModelStatus(dealerID, modelId);
    }


    // 20240806
    public ArrayList<Object> getModelData() {
        return new T_Config_AutoHome(chooseDataBaseType, 1, 3).getSaleModelData();
    }

    // 车型页面经销商数据入库
    public void modelDealerDataInsert(ArrayList<Object> dataList) {
        T_Config_Price modelDealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 3);
        modelDealerDao.insertForeach(dataList);
    }

    public ArrayList<Object> getNoFinishModelDealerData() {
        return new T_Config_Price(chooseDataBaseType, chooseDataBase, 3).findDealerCityNotFinish();
    }

    public void method_车型经销商数据修改下载状态(String dealerID, String modelId) {
        T_Config_Price saleModDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 3);
        saleModDao.updateNoDealerModelStatus(dealerID, modelId);
    }


    public void insertConfirmCarPriceFileModel(ArrayList<Object> dataList) {
        T_Config_Price modelDealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 6);
        modelDealerDao.insertForeach(dataList);
    }

    public ArrayList<Object> dataListDealer() {
        T_Config_Price modelDealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 3);
        return modelDealerDao.method_查找();
    }

    public void carPriceDataModel(ArrayList<Object> dataList) {
        T_Config_Price carPriceDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 7);
        carPriceDao.insertForeach(dataList);
    }


}
