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
}
