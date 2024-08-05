package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.koubei.KouBeiInfo;
import com.wangtiantian.entity.koubei.ModelKouBei;
import com.wangtiantian.entity.price.SaleModData;

import java.util.ArrayList;
import java.util.List;

public class KouBeiDataBase {
    //选择数据库与
    private static int chooseDataBase = 1;
    //选择要操作的数据库表
    private static T_Config_AutoHome modDao = new T_Config_AutoHome(0, 1, 2);
    private static T_Config_KouBei modDataDao = new T_Config_KouBei(2, chooseDataBase, 0);
    private static T_Config_KouBei kouBeiInfoDataDao = new T_Config_KouBei(2, chooseDataBase, 1);

    // 获取车型id列表
    public ArrayList<Object> getModIDList() {
        return modDao.method_查找();
    }

    //入库车型ID拼接的url
    public void modelUrlList(ArrayList<ModelKouBei> dataList) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<ModelKouBei> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = modDataDao.getColumnList(dataList.get(i));
            for (ModelKouBei bean : batchList) {
                valueBuffer.append(modDataDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            modDataDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("下载价格数据Url入库操作");
        }

    }

    // 获取各个车型的第一页的口碑url 未下载 并下载
    public ArrayList<Object> getModFirstKouBei() {
        return modDataDao.getUrl未下载();
    }
    // 修改下载状态
    public void update下载状态(String url){
        modDataDao.method修改车型口碑页面的下载状态(url);
    }

    public void insetForeachKouBeiInfo(ArrayList<KouBeiInfo> dataList) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<KouBeiInfo> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = kouBeiInfoDataDao.getColumnList(dataList.get(i));
            for (KouBeiInfo bean : batchList) {
                valueBuffer.append(kouBeiInfoDataDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            kouBeiInfoDataDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("下载价格数据Url入库操作");
        }

    }

    public ArrayList<Object> findDescKouBeiUrl(){
        return kouBeiInfoDataDao.getUrl未下载();
    }
}
