package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_AutoHome_new;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.*;
import com.wangtiantian.entity.configData.Bean_VersionIds;
import com.wangtiantian.entity.price.SaleModData;

import java.text.SimpleDateFormat;
import java.util.*;

public class DataBaseMethod {
    //选择数据库与
    private static int chooseDataBase = 0;
    private static int chooseDataBaseType = 0;
    public void method_入库品牌数据(ArrayList<Bean_Brand> dataList) {
        T_Config_AutoHome_new brandDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 0);
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<Bean_Brand> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = brandDao.getColumnList(dataList.get(i));
            for (Bean_Brand bean : batchList) {
                valueBuffer.append(brandDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            brandDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("汽车之家品牌数据入库");
        }
    }

    public void method_入库厂商数据(ArrayList<Bean_Factory> dataList) {
        T_Config_AutoHome_new brandDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 1);
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<Bean_Factory> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = brandDao.getColumnList(dataList.get(i));
            for (Bean_Factory bean : batchList) {
                valueBuffer.append(brandDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            brandDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("汽车之家厂商数据入库");
        }
    }

    public void method_入库车型数据(ArrayList<Bean_Model> dataList) {
        T_Config_AutoHome_new modDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 2);
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<Bean_Model> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = modDao.getColumnList(dataList.get(i));
            for (Bean_Model bean : batchList) {
                valueBuffer.append(modDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            modDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("汽车之家车型数据入库");
        }
    }

    public ArrayList<Object> method_查找车型表中未下载的含版本数据的数据(String pathType, int status) {
        T_Config_AutoHome_new modDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 2);
        return modDao.method_根据数据的获取路径和状态查找数据(pathType, status);
    }

    public void method_修改车型表中下载的id状态(String modID, String type, int status) {
        T_Config_AutoHome_new modDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 2);
        modDao.method_修改车型表中已下载的车型id状态(modID, type, status);
    }

    public void method_入库版本数据(ArrayList<Bean_Version> dataList) {
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 3);
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<Bean_Version> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = verDao.getColumnList(dataList.get(i));
            for (Bean_Version bean : batchList) {
                valueBuffer.append(verDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            verDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("汽车之家车型数据入库");
        }
    }

    public ArrayList<Object> method_根据数据类型获取未下载的数据(String dataType, int status) {
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 4);
        return verDao.method_根据数据类型获取未下载的数据(dataType, status);
    }

    public String method_根据组号查询版本id(int group) {
        StringBuffer stringBuffer = new StringBuffer();
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 3);
        ArrayList<Object> result = verDao.method_根据组号查询版本id(group);
        for (Object o : result) {
            String verId = ((Bean_Version) o).get_C_VersionID();
            stringBuffer.append(verId + ",");
        }

        return stringBuffer.toString().substring(0,stringBuffer.toString().length()-1);
    }

    public void update_修改车辆配置的数据文件下载状态(int group, String dataType, int status) {
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 4);
        verDao.method_修改下载车辆版本配置的状态(group, dataType, status);
    }

    public void insert_批量插入版本ids(){
        T_Config_AutoHome_new verDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 3);
        ArrayList<Object> result = verDao.method_获取组号();
        ArrayList<Bean_VersionIds> idsDataList = new ArrayList<>();
        for(int i =1;i< result.size()+1;i++){
            Bean_VersionIds beanVersionIds = new Bean_VersionIds();
            String ids =  method_根据组号查询版本id(i);
            beanVersionIds.set_C_Ids(ids);
            beanVersionIds.set_C_Group(i);
            beanVersionIds.set_bag(0);
            beanVersionIds.set_config(0);
            beanVersionIds.set_params(0);
            beanVersionIds.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            idsDataList.add(beanVersionIds);
        }
        int batchSize = 100;
        T_Config_AutoHome_new verIdsDao = new T_Config_AutoHome_new(chooseDataBaseType,chooseDataBase, 4);
        for (int i = 0; i < idsDataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, idsDataList.size());
            List<Bean_VersionIds> batchList = idsDataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = verIdsDao.getColumnList(idsDataList.get(i));
            for (Bean_VersionIds bean : batchList) {
                valueBuffer.append(verIdsDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            verIdsDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("汽车之家版本id数据入库");
        }

    }


    public void method_批量插入配置数据(ArrayList<Object> dataList, String type) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            if (type.equals("params")) {
                T_Config_AutoHome paramsDao = new T_Config_AutoHome(chooseDataBaseType,chooseDataBase, 5);
                System.out.println("参数表操作");
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = paramsDao.getColumnList(dataList.get(i));
                for (Object params : batchList) {
                    valueBuffer.append(paramsDao.getValueList(params)).append(",");
                }
                String tempString = valueBuffer.toString();
                paramsDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            } else if (type.equals("config")) {
                T_Config_AutoHome configDao = new T_Config_AutoHome(chooseDataBaseType,chooseDataBase, 6);
                System.out.println("配置表操作");
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = configDao.getColumnList(dataList.get(i));
                for (Object config : batchList) {
                    valueBuffer.append(configDao.getValueList(config)).append(",");
                }
                String tempString = valueBuffer.toString();
                configDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            } else if (type.equals("bag")) {
                System.out.println("选装包表操作");
                T_Config_AutoHome bagDao = new T_Config_AutoHome(chooseDataBaseType,chooseDataBase, 7);
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = bagDao.getColumnList(dataList.get(i));
                for (Object bag : batchList) {
                    valueBuffer.append(bagDao.getValueList(bag)).append(",");
                }
                String tempString = valueBuffer.toString();
                bagDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            }
        }
    }
}
