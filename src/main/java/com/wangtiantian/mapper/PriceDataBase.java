package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.price.CityData;
import com.wangtiantian.entity.price.DealerData;
import com.wangtiantian.entity.price.SaleModData;

import java.util.ArrayList;
import java.util.List;

public class PriceDataBase {
    //选择数据库与
    private static int chooseDataBase = 0;
    //选择要操作的数据库表
    private static T_Config_Price cityDataDao = new T_Config_Price(2, chooseDataBase, 0);
    private static T_Config_Price dealerDao = new T_Config_Price(2, chooseDataBase, 1);
    private static T_Config_Price saleModDao = new T_Config_Price(2, chooseDataBase, 2);
    // 入库城市数据
    public void price_cityData(ArrayList<CityData> dataList) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<CityData> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = cityDataDao.getColumnList(dataList.get(i));
            for (CityData bean : batchList) {
                valueBuffer.append(cityDataDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            cityDataDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("城市数据入库操作");
        }

    }
    // 查询城市数据
    public ArrayList<Object> allDataList(String type){
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            if (type.equals("经销商城市数据")){
                dataList = cityDataDao.method_查找();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataList;
    }

    // 查询 还有 哪些城市的经销商列表数据没下载完成
    public ArrayList<Object> findDealerCityNotFinish(){
      return cityDataDao.findDealerCityNotFinish();
    }

    // 修改经销商数据下载完成的城市的状态
    public void updateDealerCityStatus(String cityID) {
        cityDataDao.updateDealerCityStatus(cityID);
    }
    //  经销商数据入库
    public void dealerData(ArrayList<DealerData> dataList) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<DealerData> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = dealerDao.getColumnList(dataList.get(i));
            for (DealerData bean : batchList) {
                valueBuffer.append(dealerDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            dealerDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("经销商数据入库操作");
        }

    }

    // 查询还有哪些经销商的车型报价页面没有下载
    public ArrayList<Object> findCarPriceNotFinish(){
        return dealerDao.findDealerCityNotFinish();
    }

    // 下载经销商的在售车型数据
    public ArrayList<Object> findNoCarPriceDealer_20240802(){
        return dealerDao.findNoCarPriceDealer_20240802();
    }
    // 修改经销商数据下载完成的城市的状态
    public void updateCarPriceStatus(String dealerID) {
        dealerDao.updateCarPriceStatus(dealerID);
    }
    // 价格数据信息下载
    public void saleModData(ArrayList<SaleModData> dataList) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<SaleModData> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = saleModDao.getColumnList(dataList.get(i));
            for (SaleModData bean : batchList) {
                valueBuffer.append(saleModDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            saleModDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("下载价格数据Url入库操作");
        }

    }

    // 获取未下载的车辆信息的数据的url
    public ArrayList<Object> findNoDealerModel(){
        return saleModDao.findNoDealerModel();
    }
    // 修改经销商数据下载完成的城市的状态
    public void updateNoDealerModelStatus(String dealerID,String modelId) {
        saleModDao.updateNoDealerModelStatus(dealerID,modelId);
    }


}
