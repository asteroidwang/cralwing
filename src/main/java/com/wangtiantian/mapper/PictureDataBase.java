package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_Picture;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.price.ConfirmCarPriceFile;

import java.util.ArrayList;
import java.util.List;

public class PictureDataBase {
    private static int chooseDataBaseType = 0;
    private static int chooseDataBase = 3;

    public ArrayList<Object> get_未下载的版本的第一页全图文件() {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 0);
        return tConfigPicture.get_查找未下载的数据();
    }

    public void update_修改下载了第一页的版本状态(String versionId) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 0);
        tConfigPicture.update_修改已下载数据的版本状态(versionId);
    }
    public void insertConfirmCarPriceFileModel(ArrayList<Object> dataList) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 1);
        tConfigPicture.insertForeach(dataList);
    }
    public ArrayList<Object> get_所有未下载的分页url(){
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 1);
        return tConfigPicture.get_查找未下载的数据();
    }
    public void update_修改下载分页数据的版本状态(String fenYeUrl) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 1);
        tConfigPicture.update_修改已下载分页数据的版本状态(fenYeUrl);
    }

    public ArrayList<Object> get_图片分页表中的所有数据(){
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 1);
        return tConfigPicture.method_查找();
    }
    public void insert_图片具体页面的文件下载相关数据(ArrayList<Object> dataList) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 2);
        tConfigPicture.insertForeach(dataList);
    }
}
