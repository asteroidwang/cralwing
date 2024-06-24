package com.wangtiantian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.controller.AnalysisData;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Params;
import com.wangtiantian.mapper.DataBaseMethod;

import java.util.ArrayList;
import java.util.Date;

public class CheckData {
    public static void main(String[] args) {
        CheckData checkData = new CheckData();
        String filePath_new = "/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/汽车之家_240621/params/";
        String filePath_old = "/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/汽车之家_240620/params/";
        String config_new = "/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/汽车之家_240621/config/";
        String config_old = "/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/汽车之家_240621_2/config/";
        ArrayList<Object> groupList = DataBaseMethod.findAllGroup();
        for (int i = 1; i < groupList.size()+1; i++) {
            String newContent =  T_Config_File.method_读取文件内容(filePath_new+i+"_params.txt");
            String oldContent = T_Config_File.method_读取文件内容(filePath_old+i+"_params.txt");
            String con_new =  T_Config_File.method_读取文件内容(config_new+i+"_config.txt");
            String con_old = T_Config_File.method_读取文件内容(config_old+i+"_config.txt");
//            if (newContent.equals(oldContent)){
////                System.out.println("same");
//            }else {
//                System.out.println(i);
//            }
            if (con_new.equals(con_old)){
//                System.out.println("same");

            }else {
                ArrayList<Object> result_new = AnalysisData.method_解析config_One(con_new,con_new);
                ArrayList<Object> result_old =  AnalysisData.method_解析config_One(con_old,con_old);
//                DataBaseMethod
                T_Config_File.method_重复写文件_根据路径创建文件夹("/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/对比/","no_same_config.txt",result_new+"\n"+result_old+"\n");
//                DataBaseMethod.dataBase_i_对比数据(result_new,"config");
//                DataBaseMethod.dataBase_i_对比数据(result_old,"config");
            }
        }


    }
    public void analysisPrams(String content,String filePath){
        JSONObject jsonRoot = JSON.parseObject(content);
        System.out.println(filePath+"\t"+jsonRoot);

    }
}
