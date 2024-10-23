package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_Config_ErShouChe extends T_Config_Father{
    public T_Config_ErShouChe(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }
    public void update_修改已下载的城市状态(String pinyin){
        String sql ="update "+tableName+ " set C_IsFinish = 1 where C_CityPinYin='"+pinyin+"'";
        method_i_d_u(sql);
    }
    public void update_修改已下载的分页数据城的下载状态(String url){
        String sql ="update "+tableName+ " set C_IsFinish = 1 where C_FenYeUrl='"+url+"'";
        method_i_d_u(sql);
    }

    public ArrayList<Object> method_查询易车未下载首页的城市数据() {
        String sql = "SELECT * FROM " + tableName + " where C_IsFinish = 0 and C_CityLevel = 1";
        return method_有条件的查询(sql);
    }

    public void update_修改已下载的首页数据的下载状态(String cityId){
        String sql ="update "+tableName+ " set C_IsFinish = 1 where C_CityId='"+cityId+"'";
        method_i_d_u(sql);
    }

    public ArrayList<Object> get_根据城市拼音获取其他数据(String pinYin){
        String sql = "SELECT * FROM " + tableName + " where  C_CityLevel = 1 and C_EngName = '"+pinYin+"'";
        return method_有条件的查询(sql);
    }

    public void yiche_update_修改已下载详情页面的车辆状态(String ucarId,String cityName){
        String sql ="update "+tableName+ " set C_IsFinish = 1 where C_uCarId='"+ucarId+"' and C_cityName ='"+cityName+"'";
        method_i_d_u(sql);
    }

    public void yiche_update_修改已下载详情页面的数据状态(){
        String sql ="update "+tableName+ " set C_IsFinish = 1 where   concat(C_uCarId,'_',substring(C_FileName,1,charindex('_',C_FileName)-1)) in (select concat(C_uCarId,'_',C_CityPinYin) from T_yiche_ConfirmDetails)";
        method_i_d_u(sql);
    }

    public ArrayList<Object> get_文件命名错误数据(){
        String sql ="select * from "+tableName+" where C_quanpin != C_listName";
        return method_有条件的查询(sql);
    }
    public void update_修改完成下载任务的城市状态(String cityId){
        String sql ="update "+tableName+ " set C_IsFinish = 1 where C_city_id='"+cityId+"'";
        method_i_d_u(sql);
    }
    public void update_修改che168车辆详情页下载状态(String C_CarHtml){
        String sql ="update "+tableName+ " set C_IsFinish = 1 where C_CarHtml='"+C_CarHtml+"'";
        method_i_d_u(sql);
    }


}
