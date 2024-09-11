package com.wangtiantian.runErShouChe.yiChe;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.ershouche.yiChe.YiChe_CarInfo;
import com.wangtiantian.entity.ershouche.yiChe.YiChe_FenYeUrl;
import com.wangtiantian.mapper.ErShouCheDataBase;

import java.util.List;

public class YiCheDetailsThread implements Runnable {

    private List<Object> list;
    private String filePath;

    public YiCheDetailsThread(List<Object> list, String filePath) {
        this.list = list;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (int i = 0; i < list.size(); i++) {
            String carId = ((YiChe_CarInfo)list.get(i)).get_C_uCarId();
            String cityPinYin =((YiChe_CarInfo)list.get(i)).get_C_FileName().split("_")[0];
            String cityName = ((YiChe_CarInfo)list.get(i)).get_C_cityName();
            String mainUrl ="https://yiche.taocheche.com/detail/"+carId+"?city="+cityPinYin;
            if(T_Config_File.method_访问url获取网页源码普通版(mainUrl,"UTF-8",filePath,carId+"_"+cityPinYin+".txt")){
                new ErShouCheDataBase().yiche_update_修改已下载详情页面的车辆状态(carId,cityName);
            }
        }
    }
}