package com.wangtiantian.mapper;

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
    public void dealerData(ArrayList<Object> dataList) {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        dealerDao.insertForeach(dataList);

    }

    // 查询还有哪些经销商的车型报价页面没有下载
    public ArrayList<Object> findCarPriceNotFinish() {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        return dealerDao.findDealerCityNotFinish();
    }

    // 下载经销商的在售车型数据
    public ArrayList<Object> findNoCarPriceDealer_20240802() {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        return dealerDao.findNoCarPriceDealer_20240802();
    }

    // 修改经销商数据下载完成的城市的状态
    public void updateCarPriceStatus(String dealerID) {
        T_Config_Price dealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 1);
        dealerDao.updateCarPriceStatus(dealerID);
    }

    // 价格数据信息下载
    public void saleModData(ArrayList<Object> dataList) {
        T_Config_Price saleModDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 2);
        saleModDao.insertForeach(dataList);

    }

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

    // 经销商版本价格数据入库
    public void carPriceData(ArrayList<Object> dataList) {
        T_Config_Price carPriceDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 4);
        carPriceDao.insertForeach(dataList);
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


    public void insertConfirmCarPriceFile(ArrayList<Object> dataList) {
        T_Config_Price modelDealerDao = new T_Config_Price(2, chooseDataBase, 5);
        modelDealerDao.insertForeach(dataList);
    }

    public void insertConfirmCarPriceFileModel(ArrayList<Object> dataList) {
        T_Config_Price modelDealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 6);
        modelDealerDao.insertForeach(dataList);
    }
    public ArrayList<Object> dataListDealer(){
        T_Config_Price modelDealerDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 3);
        return modelDealerDao.method_查找();
    }

    public void carPriceDataModel(ArrayList<Object> dataList) {
        T_Config_Price carPriceDao = new T_Config_Price(chooseDataBaseType, chooseDataBase, 7);
        carPriceDao.insertForeach(dataList);
    }



}
