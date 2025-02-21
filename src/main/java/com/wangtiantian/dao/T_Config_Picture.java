package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_Config_Picture extends T_Config_Father {
    public T_Config_Picture(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }



    public void update_修改下载车型分类页面的状态(String html) {
        String sql = "update " + tableName + " set C_IsFinish =1 where C_PictureCategoryMoreHtml = '" + html + "'";
        method_i_d_u(sql);
    }

    public void update_修改下载图片html页面的状态(String htmls) {
        String sql = "update " + tableName + " set C_IsFinish =1 where C_PictureHtml in (" + htmls + ")";
            method_i_d_u(sql);
    }

    public void update_修改下载图片html页面的状态为不能下载(String htmlCode) {
        String sql = "update " + tableName + " set C_IsFinish =5 where C_PictureHtmlCode in (" + htmlCode + ")";
        method_i_d_u(sql);
    }











    public void update_修改已下载数据的版本状态(String versionID) {
        String sql = "update " + tableName + " set C_IsFinish =1 where C_VersionID = " + versionID;
        method_i_d_u(sql);
    }

    public void update_修改已下载分页数据的版本状态(String fenYeUrl) {
        String sql = "update " + tableName + " set C_IsFinish =1 where C_FenYeUrl = '" + fenYeUrl + "'";
        method_i_d_u(sql);
    }

    public void update_修改下载图片具体页面的下载状态(String html) {
        String sql = "update " + tableName + " set C_IsFinish =1 where C_PictureHtmlUrl = '" + html + "'";
        method_i_d_u(sql);
    }

    public  int get_获取表中未下载的数据总数(){
        String sql = "select count(*) from "+tableName+ " where C_IsFinish = 0";
        return get_获取表中数据数量_有查询条件(sql);
    }

    public  int get_获取表中已下载的图片具体页面数据总数(){
        String sql = "select count(*) from "+tableName+ " where C_IsFinish = 0";
        return get_获取表中数据数量_有查询条件(sql);
    }

    public void update_修改厂商id(){
        String sql ="update T_PictureUrl set T_PictureUrl.C_FactoryID = T_Picture_Version.C_FactoryId from T_Picture_Version where  T_PictureUrl.C_VersionId = T_Picture_Version.C_VersionID";
        method_i_d_u(sql);
    }
}
