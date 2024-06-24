package com.wangtiantian;

import com.wangtiantian.controller.Analysis_AutoHome;
import com.wangtiantian.controller.Analysis_AutoHome_Pic;

public class AutoHome {
    public static void method_AutoHome() {
        String websiteURL = "https://car.autohome.com.cn";
        String dataPath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/汽车之家_20240218/图片/";
        String koubeiFilePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/口碑数据/20240318/";
        String saveFilePath = "/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/汽车之家_240610/";
        /*汽车之家品牌数据的下载与解析入库*/
        //Analysis_AutoHome.method_下载品牌_厂商_车型数据(saveFilePath);
        //Analysis_AutoHome.method_解析品牌厂商车型(saveFilePath);
        //车型版本页面分两种
        //Analysis_AutoHome.method_下载版本(saveFilePath+"车型版本页面/");
        //Analysis_AutoHome.method_不在图片路径的车型页面(saveFilePath+"车型版本页面/");
        //解析版本两个
        //Analysis_AutoHome.method_解析版本(dataPath.replace("图片", "数据"));
        //Analysis_AutoHome.method_版本(dataPath.replace("图片", "数据")+"车型版本_另/");
        //Analysis_AutoHome.method_解析车型入库版本(saveFilePath+"车型版本页面/");
        //下载配置数据
        //Analysis_AutoHome.method_下载配置信息(saveFilePath+"params/");
        //Analysis_AutoHome.method_下载配置信息_补充(dataPath.replace("图片", "数据")+"params/");
        //Analysis_AutoHome.method_解析params_config_bag(saveFilePath+"params/");
//        Analysis_AutoHome.method_解析params_config_bag_One(saveFilePath+"params/");
//        Analysis_AutoHome.method_解析params_config_bag_列名(saveFilePath+"params/");
        //Analysis_AutoHome.method_取列名(saveFilePath+"列名/");

        /*汽车之家图片数据的下载*/
        //Analysis_AutoHome_Pic.method_下载图片页面html(dataPath);
        //Analysis_AutoHome_Pic.method_确认html页面数量并解析(dataPath);

        /*奔驰宝马奥迪口碑评分数据*/
        //Analysis_AutoHome.method_下载口碑评分(koubeiFilePath);
    }
}