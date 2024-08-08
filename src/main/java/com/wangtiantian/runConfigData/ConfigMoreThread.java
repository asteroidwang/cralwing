package com.wangtiantian.runConfigData;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.configData.Bean_VersionIds;
import com.wangtiantian.mapper.DataBaseMethod;

import java.util.List;

public class ConfigMoreThread implements Runnable {
    private List<Object> list;
    private String filePath;
    private int type;

    public ConfigMoreThread(List<Object> list, String filePath, int type) {
        this.list = list;
        this.filePath = filePath;
        this.type = type;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        DataBaseMethod dataBaseMethod = new DataBaseMethod();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object o : list) {
            String ids = ((Bean_VersionIds)o).get_C_Ids();
            int group = ((Bean_VersionIds)o).get_C_Group();
            if (type == 0) {
               if(T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/v3/Param_ListBySpecIdList.ashx?speclist="+ids+"&_appid=test&_=1723037336504&_callback=__param1","gb2312",filePath,group+"_params.txt")){
                   dataBaseMethod.update_修改车辆配置的数据文件下载状态(group,"params",1);
               }else {
                   dataBaseMethod.update_修改车辆配置的数据文件下载状态(group,"params",0);
               }
            } else if (type == 1) {
                if(T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist="+ids+"&_=1723037336505&_callback=__config3","gb2312",filePath,group+"_config.txt")){
                    dataBaseMethod.update_修改车辆配置的数据文件下载状态(group,"config",1);
                }else {
                    dataBaseMethod.update_修改车辆配置的数据文件下载状态(group,"config",0);
                }

            } else if (type == 2) {
                if(T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/Config_BagBySpecIdListV2.ashx?speclist="+ids+"&_=1723037336505&_callback=__bag4","gb2312",filePath,group+"_bag.txt")){
                    dataBaseMethod.update_修改车辆配置的数据文件下载状态(group,"bag",1);
                }else {
                    dataBaseMethod.update_修改车辆配置的数据文件下载状态(group,"bag",0);
                }

            }

        }

    }
}