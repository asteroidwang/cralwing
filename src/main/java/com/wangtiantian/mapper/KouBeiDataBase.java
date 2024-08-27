package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.koubei.*;
import com.wangtiantian.entity.price.SaleModData;

import java.util.ArrayList;
import java.util.List;

public class KouBeiDataBase {
    //选择数据库和连接的数据类型
    private static int chooseDataBase = 2;
    private static int chooseDataBaseType = 0;

    // 获取车型id列表
    public ArrayList<Object> getModIDList() {
        T_Config_AutoHome modDao = new T_Config_AutoHome(chooseDataBaseType, 1, 2);
        return modDao.method_查找();
    }

    //入库车型ID拼接的url
    public void modelUrlList(ArrayList<Object> dataList) {
        T_Config_KouBei modDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 0);
        modDataDao.insertForeach(dataList);

    }

    // 获取各个车型的第一页的口碑url 未下载 并下载
    public ArrayList<Object> getModFirstKouBei() {
        T_Config_KouBei modDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 0);
        return modDataDao.getUrl未下载();
    }

    // 修改下载状态
    public void update下载状态(String url) {
        T_Config_KouBei modDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 0);
        modDataDao.method修改车型口碑页面的下载状态(url);
    }

    public void insetForeachKouBeiInfo(ArrayList<Object> dataList) {
        T_Config_KouBei kouBeiInfoDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 1);
        kouBeiInfoDataDao.insertForeach(dataList);

    }

    public ArrayList<Object> findDescKouBeiUrl() {
        T_Config_KouBei kouBeiInfoDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 1);
        return kouBeiInfoDataDao.getUrl未下载();
    }

    public ArrayList<Object> findAllKouBeiShowId() {
        T_Config_KouBei kouBeiInfoDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 1);
        return kouBeiInfoDataDao.method_查找();
    }


    public void insetForeachKouBeiData(ArrayList<Object> dataList) {
        T_Config_KouBei kouBeiDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 2);
        kouBeiDataDao.insertForeach(dataList);

    }

    // 获取未下载口碑帖子回复的口碑id
    public ArrayList<Object> getReplyKouBei(int begin) {
        T_Config_KouBei replyDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 2);
        return replyDataDao.method_查找所有未下载回复的口碑id(begin);
    }

    // 获取表中数据总数
    public int get_回复表中未完成的数量() {
        T_Config_KouBei replyDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 2);
        return replyDataDao.get_一级评论未完成的数量();
    }
    public int getCount() {
        T_Config_KouBei replyDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 2);
        return replyDataDao.get_获取表中数据数量();
    }

    // 修改一级评论回复文件的下载状态
    public void update_修改一级评论的文件下载状态(String kbId) {
        T_Config_KouBei replyDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 2);
        replyDataDao.update修改一级评论的下载状态(kbId);
    }

    public void insetForeachKouBeiTest(ArrayList<Object> dataList) {
        T_Config_KouBei kouBeiDataDao = new T_Config_KouBei(2, chooseDataBase, 3);
        kouBeiDataDao.insertForeach(dataList);
    }


    public void insetForeachKouBeiReplyData(ArrayList<Object> dataList) {
        T_Config_KouBei kouBeiDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 4);
        kouBeiDataDao.insertForeach(dataList);

    }


    public void insetForeachKouBeiReplyDat2(ArrayList<Object> dataList) {
        T_Config_KouBei kouBeiDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 6);
        kouBeiDataDao.insertForeach(dataList);

    }
    // 查询还未解析的一级评论的口碑id
    public ArrayList<Object> getNotParseFirstPingLunKouBeiId(int begin) {
        T_Config_KouBei modDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 2);
        return modDataDao.method_查找所有未下载回复的口碑id(begin);
    }

    // 查询未下载的最后层级评论
    public ArrayList<Object> get_查询未下载的最后层级评论(int begin) {
        T_Config_KouBei modDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 4);
        return modDataDao.method_分页查询未下载的有二级评论的数据10000条每次(begin);
    }

    public void insert_确认已下载的二级评论数据(ArrayList<Object> dataList) {
        T_Config_KouBei kouBeiDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 5);
        kouBeiDataDao.insertForeach(dataList);

    }

    public int get_二级回复文件应有数量(){
        T_Config_KouBei kouBeiDataDao = new T_Config_KouBei(chooseDataBaseType, chooseDataBase, 4);
        return kouBeiDataDao.get_回复表中数据总量();
    }
}
