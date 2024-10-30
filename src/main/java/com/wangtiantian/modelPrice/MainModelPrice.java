package com.wangtiantian.modelPrice;

import com.wangtiantian.dao.T_Config_File;

import java.util.ArrayList;

public class MainModelPrice {
    public static void main(String[] args) {
        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/经销商数据/20241030/";
//        String filePath ="/Users/wangtiantian/MyDisk/汽车之家/经销商数据/20241010/";
//        String filePath ="/Users/wangtiantian/MyDisk/汽车之家/经销商数据/20241030/";
        ModelPriceMethod modelPriceMethod = new ModelPriceMethod();
        // 1
        // modelPriceMethod.method_根据车型id获取经销商数据并获取经销商首页数据();
        // 2     4
         modelPriceMethod.method_下载首页数据(filePath + "车型页面的经销商数据分页/");
        // 3
        // modelPriceMethod.parse_解析车型经销商首页数据(filePath+"车型页面的经销商数据分页/");
        // 5
        // modelPriceMethod.method_解析所有经销商分页数据(filePath + "车型页面的经销商数据分页/");
        // 6
        // modelPriceMethod.method_根据经销商id获取下载车辆价格信息文件(filePath + "车辆价格信息/");

        // 7 记得修改sql语句 20240909   注释这个
        // modelPriceMethod.method_补充未下载的车辆价格信息页面(filePath + "车辆价格信息/");
        // 8   执行这个
        // modelPriceMethod.method_解析车辆价格信息数据(filePath + "车辆价格信息/");

        // 修改已下载的经销商分页数据
//        ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath+"车型页面的经销商数据分页/");
//        for(String fileName:fileList){
////            System.out.println(fileName);
//            String dealerId = fileName.replace(".txt","").split("_")[0];
//            String page = fileName.replace(".txt","").split("_")[1];
//            System.out.println(dealerId+"\t"+page);
//        }


        // modelPriceMethod.method_根据车型id获取经销商数据(filePath + "车型页面的经销商数据/");
        // modelPriceMethod.method_解析上一步的车型得到的经销商数据(filePath+"车型页面的经销商数据/");
        // modelPriceMethod.method_根据经销商id获取下载车辆价格信息文件(filePath + "车辆价格信息/");
        // modelPriceMethod.method_补充未下载的车辆价格信息页面(filePath + "车辆价格信息/");
        // modelPriceMethod.method_解析车辆价格信息数据(filePath + "车辆价格信息/");
    }
}
