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

    public ArrayList<Object> get_所有未下载的分页url() {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 1);
        return tConfigPicture.get_查找未下载的数据();
    }

    public void update_修改下载分页数据的版本状态(String fenYeUrl) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 1);
        tConfigPicture.update_修改已下载分页数据的版本状态(fenYeUrl);
    }

    public ArrayList<Object> get_图片分页表中的所有数据() {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 1);
        return tConfigPicture.method_查找();
    }

    public void insert_图片具体页面的文件下载相关数据(ArrayList<Object> dataList) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 2);
        tConfigPicture.insertForeach(dataList);
    }

    public ArrayList<Object> get_分页查询所有未下载的图片具体页面的url(int begin) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 2);
        return tConfigPicture.method_分页查询未下载的数据10000条每次(begin);
    }

    public void update_修改下载图片具体页面的下载状态(String htmlUrl) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 2);
        tConfigPicture.update_修改下载图片具体页面的下载状态(htmlUrl);
    }

    public int get_图片具体页面表中的数据总数() {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 2);
        return tConfigPicture.get_获取表中数据数量();
    }

    public void insert_图片具体页面已下载的文件数据(ArrayList<Object> dataList) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 3);
        tConfigPicture.insertForeach(dataList);
    }

    public ArrayList<Object> get_已经下载的图片具体页面的数据(int begin) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 2);
        return tConfigPicture.method_分页查询已下载的数据10000条每次(begin);
    }

    public void insert_下载图片的url数据入库(ArrayList<Object> dataList) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 4);
        tConfigPicture.insertForeach(dataList);
    }

    public ArrayList<Object> get_未下载的图片的url() {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 4);
        return tConfigPicture.get_查找未下载的数据();
    }

    public int get_下载图片具体页面中还需下载的数量() {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 2);
        return tConfigPicture.get_获取表中未下载的数据总数();
    }

    public int get_下载图片表中还需下载的数量() {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 4);
        return tConfigPicture.get_获取表中未下载的数据总数();
    }

    public ArrayList<Object> get_分页查询所有未下载的图片的url(int begin) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 4);
        return tConfigPicture.method_分页查询未下载的数据10000条每次(begin);
    }

    public void insert_确认下载图片的url数据入库(ArrayList<Object> dataList) {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 5);
        tConfigPicture.insertForeach(dataList);
        T_Config_Picture tConfigPicture2 = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 4);
        tConfigPicture2.method_i_d_u("update T_PictureUrl set C_IsFinish = 1 where concat(C_VersionId,'_',C_ImgType,'_',C_ImgId) in (select distinct C_ImgUrl from T_ConfirmPictureUrl)");
    }

    public void update_修改厂商id() {
        T_Config_Picture tConfigPicture = new T_Config_Picture(chooseDataBaseType, chooseDataBase, 4);
        tConfigPicture.update_修改厂商id();
    }

}
