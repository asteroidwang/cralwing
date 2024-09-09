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
        return method_有条件的查询("select * from "+tableName+" where C_IsFinish=0");
    }

//    public ArrayList<Object> findDealerCityNotFinish() {
//        return method_有条件的查询("select * from "+tableName+" where C_IsFinish=0");
//    }

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
        String sql = "update " + tableName + " set C_IsFinish = 1 where C_DealerId=" + dealerID+" and C_ModelId = "+modID;
        method_i_d_u(sql);
    }


    /**
     * 以下为新增方法
     */
    public void update_修改所有经销商分页url表的数据的下载状态(String fenyeUrl) {
        String sql = "update " + tableName + " set C_IsFinish = 1 where C_DealerFenYeUrl='" + fenyeUrl+"'";
        method_i_d_u(sql);
    }
    public ArrayList<Object> get_所有下载有重复经销商数据的城市名称(){
        String sql ="select distinct C_CityName from "+tableName+" where C_DealerID in( select C_DealerID from "+tableName+" GROUP BY C_DealerID having count(*) >1)";
        return method_有条件的查询(sql);
    }
    public void update_修改有重复经销商数据的下载状态(String cityName){
        String sql ="update "+tableName+" set C_IsFinish = 0 where C_DealerFenYeUrl like '%https://dealer.autohome.com.cn/"+cityName+"/%'";
        method_i_d_u(sql);
    }
    public void update_修改已下载的车辆价格信息的状态(){
        String sql ="update "+tableName+" set C_IsFinish = 1 where concat(C_DealerId,'_',C_ModelId) in (select distinct concat(C_DealerId,'_',C_ModelId) from T_ConfirmCarPriceFile_20240827)";
        method_i_d_u(sql);
    }


}

