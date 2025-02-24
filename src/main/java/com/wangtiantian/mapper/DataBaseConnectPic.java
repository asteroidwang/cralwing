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
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,2);
        picture.insertForeach(dataList);
    }

    public ArrayList<Object> carShortInfo(){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,2);
        return picture.findPictureShortInfo();
    }

    public ArrayList<Object> findPictureByModel(String modelCode){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,2);
        return picture.findPictureByModel(modelCode);
    }
    public ArrayList<Object> findBaseInfoCar(){
        T_Config_Picture picture = new T_Config_Picture(3,0,8);
        String sql ="select vm.C_VersionID,vm.C_VersionName,vm.C_ModelID,vm.C_ModelName,factory.C_FactoryID,factory.C_FactoryName,brand.C_BrandID,brand.C_BrandName from (\n" +
                "select version.C_VersionID,version.C_VersionName,model.C_ModelID,model.C_ModelName,model.C_BrandID,model.C_FactoryID from [T_汽车之家_版本表_20250224] version \n" +
                "left join [T_汽车之家_车型表_20250224] model on version.C_ModelID = model.C_ModelID) vm \n" +
                "left join [T_汽车之家_厂商表_20250224] factory on vm.C_FactoryID = factory.C_FactoryID and vm.C_BrandID = factory.C_BrandID\n" +
                "left join [T_汽车之家_品牌表_20250224] brand on vm.C_BrandID = brand.C_BrandID order by C_BrandID,C_FactoryID,C_ModelID,C_VersionID\n";
        return picture.method_有条件的查询(sql);
    }

    public void updateImgStatus(String codeList){
        T_Config_Picture picture = new T_Config_Picture(chooseDataBaseType,chooseDataBase,2);
        picture.update_修改下载图片的状态(codeList);
    }


}
