package com.wangtiantian.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.impl.oa.poa.AOMEntry;
import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_AutoHome_new;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.*;
import com.wangtiantian.entity.configData.Bean_VersionIds;
import com.wangtiantian.entity.price.SaleModData;

import javax.jws.soap.SOAPBinding;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataBaseMethod {
    //选择数据库与
    private static int chooseDataBase = 0;
    // xuanze lianjie
    private static int chooseDataBaseType = 2;

    public void method_入库品牌数据(ArrayList<Object> dataList) {
        T_Config_AutoHome_new brandDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 0);
        brandDao.insertForeach(dataList);
    }

    public void method_入库厂商数据(ArrayList<Object> dataList) {
        T_Config_AutoHome_new fctDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 1);
        fctDao.insertForeach(dataList);
    }

    public void method_入库车型数据(ArrayList<Object> dataList) {
        T_Config_AutoHome_new modDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 2);
        modDao.insertForeach(dataList);
    }

    public ArrayList<Object> method_查找车型表中未下载的含版本数据的数据(String pathType, int status) {
        T_Config_AutoHome_new modDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 2);
        return modDao.method_根据数据的获取路径和状态查找数据(pathType, status);
    }

    public void method_修改车型表中下载的id状态(String modID, String type, int status) {
        T_Config_AutoHome_new modDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 2);
        modDao.method_修改车型表中已下载的车型id状态(modID, type, status);
    }

    public void method_入库版本数据(ArrayList<Object> dataList) {
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 3);
        verDao.insertForeach(dataList);
        // 修改版本id的分组10个一组
        verDao.update_修改版本id的分组();

    }

    public ArrayList<Object> method_根据数据类型获取未下载的数据(String dataType, int status) {
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 4);
        return verDao.method_根据数据类型获取未下载的数据(dataType, status);
    }

    public String method_根据组号查询版本id(int group) {
        StringBuffer stringBuffer = new StringBuffer();
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 3);
//        verDao.update_修改版本id的分组();
        ArrayList<Object> result = verDao.method_根据组号查询版本id(group);
        for (Object o : result) {
            String verId = ((Bean_Version) o).get_C_VersionID();
            stringBuffer.append(verId + ",");
        }

        return stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
    }

    public void update_修改车辆配置的数据文件下载状态(int group, String dataType, int status) {
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 4);
        verDao.method_修改下载车辆版本配置的状态(group, dataType, status);
    }

    public void insert_批量插入版本ids() {
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 3);
        ArrayList<Object> result = verDao.method_获取组号();
        System.out.println(result.size());
        ArrayList<Object> idsDataList = new ArrayList<>();
        for (int i = 1; i < result.size() + 1; i++) {
            Bean_VersionIds beanVersionIds = new Bean_VersionIds();
            String ids = method_根据组号查询版本id(i);
            beanVersionIds.set_C_Ids(ids);
            beanVersionIds.set_C_Group(i);
            beanVersionIds.set_bag(0);
            beanVersionIds.set_config(0);
            beanVersionIds.set_params(0);
            beanVersionIds.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            idsDataList.add(beanVersionIds);
        }
        System.out.println(idsDataList.size());
        T_Config_AutoHome_new verIdsDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 4);
        verIdsDao.insertForeach(idsDataList);
    }


    public void method_批量插入配置数据(ArrayList<Object> dataList, String type) {
        if (type.equals("params")) {
            T_Config_AutoHome paramsDao = new T_Config_AutoHome(chooseDataBaseType, chooseDataBase, 5);
            paramsDao.insertForeach(dataList);
        } else if (type.equals("config")) {
            T_Config_AutoHome configDao = new T_Config_AutoHome(chooseDataBaseType, chooseDataBase, 6);
            configDao.insertForeach(dataList);
        } else if (type.equals("bag")) {
            System.out.println("选装包表操作");
            T_Config_AutoHome bagDao = new T_Config_AutoHome(chooseDataBaseType, chooseDataBase, 7);
            bagDao.insertForeach(dataList);
        }
    }

    public int get_版本表中组数() {
        T_Config_AutoHome_new verIdsDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 3);
        return verIdsDao.get_获取表中组数();
    }


    // 创建爬取汽车之家配置数据所需要的表
    public void method_创建爬取汽车之家配置数据所需要的表(String currentTime) {
        T_Config_AutoHome_new tableDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 0);
        String brandTableName = "T_汽车之家_品牌表_" + currentTime;
        String factoryTableName = "T_汽车之家_厂商表_" + currentTime;
        String versionTableName = "T_汽车之家_版本表_" + currentTime;
        String modelTableName = "T_汽车之家_车型表_" + currentTime;
        String versionIdTableName = "T_汽车之家_版本id表_" + currentTime;
        String bagTableName = "T_汽车之家_选装包_" + currentTime;

        tableDao.create_品牌表(brandTableName);
        tableDao.create_厂商表(factoryTableName);
        tableDao.create_车型表(modelTableName);
        tableDao.create_版本表(versionTableName);
        tableDao.create_版本Id表(versionIdTableName);
        tableDao.create_选装包表(bagTableName);
        String content = T_Config_File.method_读取文件内容("config.json");
        String tempTime = JSON.parseArray(content).getJSONObject(0).getJSONArray("dbItems").getJSONObject(0).getJSONArray("db_table").getJSONObject(0).getString("tableName").replace("T_汽车之家_品牌表", "").replace("_", "");
        String brand_lastTableName = "T_汽车之家_品牌表_" + tempTime;
        String factory_lastTableName = "T_汽车之家_厂商表_" + tempTime;
        String version_lastTableName = "T_汽车之家_版本表_" + tempTime;
        String model_lastTableName = "T_汽车之家_车型表_" + tempTime;
        String versionId_lastTableName = "T_汽车之家_版本id表_" + tempTime;
        String bag_lastTableName = "T_汽车之家_选装包_" + tempTime;
        String updateContent = content.replace(brand_lastTableName, brandTableName).replace(factory_lastTableName, factoryTableName).replace(model_lastTableName, modelTableName).replace(version_lastTableName, versionTableName).replace(versionId_lastTableName, versionIdTableName).replace(bag_lastTableName, bagTableName).replace("T_汽车之家_参数表_" + tempTime, "T_汽车之家_参数表_" + currentTime).replace("T_汽车之家_配置表_" + tempTime, "T_汽车之家_配置表_" + currentTime);
        T_Config_File.method_写文件("config.json", updateContent);
    }

    public void method_创建爬取汽车之家配置数据表(String currentTime, String paramsSql, String configSql) {
        T_Config_AutoHome_new tableDao = new T_Config_AutoHome_new(chooseDataBaseType, chooseDataBase, 0);
        String paramsTableName = "T_汽车之家_参数表_" + currentTime;
        String configTableName = "T_汽车之家_配置表_" + currentTime;
        String bagTableName = "T_汽车之家_选装包_" + currentTime;
        tableDao.create_参数表(paramsTableName, paramsSql);
        tableDao.create_配置表(configTableName, configSql);
        tableDao.create_选装包表(bagTableName);
    }
}
