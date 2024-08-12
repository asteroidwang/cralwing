package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.koubei.KouBeiData;
import com.wangtiantian.entity.koubei.KouBeiInfo;
import com.wangtiantian.entity.koubei.KouBeiTest;
import com.wangtiantian.entity.koubei.ModelKouBei;
import com.wangtiantian.entity.price.SaleModData;

import java.util.ArrayList;
import java.util.List;

public class KouBeiDataBase {
    //选择数据库和连接的数据类型
    private static int chooseDataBase = 1;
    private static int chooseDataBaseType = 0;

    // 获取车型id列表
    public ArrayList<Object> getModIDList() {
        T_Config_AutoHome modDao = new T_Config_AutoHome(chooseDataBaseType, 1, 2);
        return modDao.method_查找();
    }

    //入库车型ID拼接的url
    public void modelUrlList(ArrayList<ModelKouBei> dataList) {
        int batchSize = 100;
        T_Config_KouBei modDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 0);
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
        T_Config_KouBei modDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 0);
        return modDataDao.getUrl未下载();
    }

    // 修改下载状态
    public void update下载状态(String url) {
        T_Config_KouBei modDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 0);
        modDataDao.method修改车型口碑页面的下载状态(url);
    }

    public void insetForeachKouBeiInfo(ArrayList<KouBeiInfo> dataList) {
        int batchSize = 100;
        T_Config_KouBei kouBeiInfoDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 1);
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

    public ArrayList<Object> findDescKouBeiUrl() {
        T_Config_KouBei kouBeiInfoDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 1);
        return kouBeiInfoDataDao.getUrl未下载();
    }

    public ArrayList<Object> findAllKouBeiShowId() {
        T_Config_KouBei kouBeiInfoDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 1);
        return kouBeiInfoDataDao.method_查找();
    }


    public void insetForeachKouBeiData(ArrayList<KouBeiData> dataList) {
        int batchSize = 100;
        T_Config_KouBei kouBeiDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 2);
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<KouBeiData> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = kouBeiDataDao.getColumnList(dataList.get(i));
            for (KouBeiData bean : batchList) {
                valueBuffer.append(kouBeiDataDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            kouBeiDataDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("下载价格数据Url入库操作");
        }

    }

    // 获取未下载口碑帖子回复的口碑id
    public ArrayList<Object> getReplyKouBei(int begin) {
        T_Config_KouBei replyDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 2);
        return replyDataDao.method_查找所有未下载回复的口碑id(begin);
    }

    // 获取表中数据总数
    public int getCount(){
        T_Config_KouBei replyDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 2);
        return replyDataDao.get_获取表中数据数量();
    }
    // 修改一级评论回复文件的下载状态
    public void update_修改一级评论的文件下载状态(String kbId){
        T_Config_KouBei replyDataDao = new T_Config_KouBei(chooseDataBaseType,chooseDataBase, 2);
        replyDataDao.update修改一级评论的下载状态(kbId);
    }

    public void insetForeachKouBeiTest(ArrayList<KouBeiTest> dataList) {
        int batchSize = 100;
        T_Config_KouBei kouBeiDataDao = new T_Config_KouBei(2, chooseDataBase, 3);
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<KouBeiTest> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = kouBeiDataDao.getColumnList(dataList.get(i));
            for (KouBeiTest bean : batchList) {
                valueBuffer.append(kouBeiDataDao.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            kouBeiDataDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("下载koubeiid入库操作");
        }

    }
}
