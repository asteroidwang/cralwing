package com.wangtiantian;

import com.wangtiantian.controller.AnalysisData;
import com.wangtiantian.controller.Analysis_AutoHome;
import com.wangtiantian.controller.Analysis_AutoHome_Pic;
import com.wangtiantian.controller.DownLoadData;
import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.entity.Bean_Brand;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        DownLoadData downLoadData = new DownLoadData();
        String websiteURL = "https://car.autohome.com.cn";
        String saveFilePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/配置数据/20240807/";
        System.out.println(System.getProperty("os.name").toLowerCase());
        // 1.下载品牌厂商车型数据
        // downLoadData.downLoadBrandFactoryModel(saveFilePath);
        // 1.解析入库 品牌厂商车型
        // AnalysisData.method_解析品牌厂商车型(saveFilePath);
        // 2.下载车型版本页面
        // downLoadData.downLoadModel(saveFilePath+"车型版本页面/");
        // 2.解析入库 版本数据
        // AnalysisData.method_解析车型入库版本(saveFilePath+"车型版本页面/");
        // 入库版本id集合
        // AnalysisData.insertVersionIds();
        // 3.下载配置数据
        // downLoadData.downLoadParams_Config_Bag(saveFilePath+"params/");
        // 3.取列名
        // AnalysisData.method_解析params_config_bag_列名(saveFilePath+"params/");
        // AnalysisData.method_取列名(saveFilePath+"列名/");
        // 3.解析配置数据
        // AnalysisData.method_解析params_config_bag(saveFilePath+"params/");
        // 3.解析单个版本id配置
        // AnalysisData.method_解析params_config_bag_One(saveFilePath+"params/");




        //AutoHome.method_AutoHome();

//        T_Config_AutoHome brandDao = new T_Config_AutoHome(0, 7, 0);
//        ArrayList<Object> list = brandDao.method_查找();
//
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(((Bean_Brand) list.get(i)).get_C_BrandName());
//        }
    }
}