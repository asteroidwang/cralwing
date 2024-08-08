package com.wangtiantian;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.Bean_ModelURL;
import com.wangtiantian.mapper.DataBaseMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InsertModelURL {
    public static void main(String[] args) {
//        ArrayList<Object> modelList = DataBaseMethod.findDataFromDataBase("model");
        ArrayList<Object> modelList =new ArrayList<>();
        ArrayList<Object> modelDataLits = new ArrayList<>();
        String filePath="/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/汽车之家_240801/车型版本页面/";
        String zaishoupath=filePath+"在售车型页面/";
        String tinghsoupath=filePath+"停售车型页面/";
        String piczaishoupath=filePath+"图片路径在售车型页面/";
        String pictingshoupath=filePath+"图片路径停售车型页面/";
        for (int i = 0; i < modelList.size(); i++) {
            String modelID = ((Bean_Model)modelList.get(i)).get_C_ModelID();
            String C_zaishou = "https://www.autohome.com.cn/"+modelID;
            String C_tingshou  = "https://www.autohome.com.cn/"+modelID+"/sale.html#pvareaid=3311673";
            String pic_zaishou ="https://car.autohome.com.cn/pic/series/"+modelID+".html";
            String pic_tingshou ="https://car.autohome.com.cn/pic/series-t/"+modelID+".html";
            String createTiem =new SimpleDateFormat("yyyy-MM-dd HH:mm;ss").format(new Date());
            String updateTime =new SimpleDateFormat("yyyy-MM-dd HH:mm;ss").format(new Date());
            Bean_ModelURL bean_modelURL = new Bean_ModelURL();
            bean_modelURL.set_C_ModelURL_停售(C_tingshou);
            bean_modelURL.set_C_ModelURL_在售(C_zaishou);
            bean_modelURL.set_C_ModelURL_图片页面停售(pic_tingshou);
            bean_modelURL.set_C_ModelURL_图片页面在售(pic_zaishou);
            bean_modelURL.set_C_CreateTime(createTiem);
            bean_modelURL.set_C_UpdateTime(updateTime);
            bean_modelURL.set_C_ModelID(modelID);
            if(T_Config_File.method_判断文件是否存在(tinghsoupath+modelID+"_t.txt")){
                bean_modelURL.set_C_停售(1);
            }else {
                bean_modelURL.set_C_停售(0);
            }
            if(T_Config_File.method_判断文件是否存在(pictingshoupath+modelID+"_t.txt")){
                bean_modelURL.set_C_图片页面停售(1);
            }else {
                bean_modelURL.set_C_图片页面停售(0);
            }
            if(T_Config_File.method_判断文件是否存在(zaishoupath+modelID+".txt")){
                bean_modelURL.set_C_在售(1);
            }else {
                bean_modelURL.set_C_在售(0);
            }
            if(T_Config_File.method_判断文件是否存在(piczaishoupath+modelID+".txt")){
                bean_modelURL.set_C_图片页面在售(1);
            }else {
                bean_modelURL.set_C_图片页面在售(0);
            }
//            bean_modelURL.set_C_图片在售(0);
//            bean_modelURL.set_C_图片停售(0);
//            bean_modelURL.set_C_在售(0);
            modelDataLits.add(bean_modelURL);
        }
//        DataBaseMethod.dataBase_i_d_u(modelDataLits,"modelURL");
    }
}
