package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_Config_Price extends T_Config_Father {
    public T_Config_Price(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }

    // 修改经销商数据下载完成的城市的状态
    public void updateDealerCityStatus(String cityID) {
        String sql = "update " + tableName + " set C_IsFinish = 1 where C_CityID=" + cityID;
        method_i_d_u(sql);
    }

    // 查找未下载的城市数据
    public ArrayList<Object> findDealerCityNotFinish() {
        System.out.println("select * from "+tableName+" where C_IsFinish=0");
        return method_有条件的查询("select * from "+tableName+" where C_IsFinish=0");
    }

    // 修改经销商数据下载完成的城市的状态
    public void updateCarPriceStatus(String dealerID) {
        String sql = "update " + tableName + " set C_IsFinish = 1 where C_DealerID=" + dealerID;
        method_i_d_u(sql);
    }

    // 查找无车辆报价页面的经销商
    public ArrayList<Object> findNoCarPriceDealer_20240802(){
        return method_有条件的查询("select * from T_DealerData_20240806 where C_DealerID not in (select distinct C_DealerID from T_SaleModData_20240806)");
    }
    // 获取未下载的车辆信息的数据的url
    public ArrayList<Object> findNoDealerModel(){
        return method_有条件的查询("select * from "+tableName+" where C_IsFinish = 0");
    }

    // 修改未下载的车辆信息的数据的url的状态
    public void updateNoDealerModelStatus(String dealerID,String modID) {
        String sql = "update " + tableName + " set C_IsFinish = 1 where C_DealerID=" + dealerID+" and C_ModelID = "+modID;
        method_i_d_u(sql);
    }
}

