package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_AutoHome_new;
import com.wangtiantian.dao.T_Config_Picture;

import java.util.ArrayList;

public class DataBaseConnectPic {
    private static int chooseDataBaseType = 3;
    private static int chooseDataBase = 3;

    public void insertModelCategoryHtml(ArrayList<Object> dataList){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,0);
        picture.insertForeach(dataList);
    }
    public ArrayList<Object> findModelCategoryHtml(){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,0);
        return picture.get_查找未下载的数据();
    }

    public void updateModelCategoryHtmlStatus(String html){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,0);
        picture.update_修改下载车型分类页面的状态(html);
    }

    public void insertPictureHtml(ArrayList<Object> dataList){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,1);
        picture.insertForeach(dataList);
    }

    public ArrayList<Object> findPictureHtml(){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,1);
        return picture.get_查找未下载的数据();
    }

    public int pictureHtmlCount(){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,1);
        return picture.get_获取表中数据数量();
    }
    public ArrayList<Object> findPictureHtmlForeach(int begin,int number){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,1);
        return picture.method_分页查询未下载的数据(begin,number);
    }
    public void updatePictureHtmlStatus(String html){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,1);
        picture.update_修改下载图片html页面的状态(html);
    }

    public void updatePictureHtmlStatusForNot(String html){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,1);
        picture.update_修改下载图片html页面的状态为不能下载(html);
    }

    public ArrayList<Object> findModelInfoList() {
        T_Config_AutoHome_new modDao = new T_Config_AutoHome_new(3, 0, 2);
        return modDao.method_查找();
    }

    public void insertPictureInfoList(ArrayList<Object> dataList) {
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,1);
        picture.insertForeach(dataList);

    }

}
