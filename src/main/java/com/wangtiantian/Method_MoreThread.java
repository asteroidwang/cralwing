package com.wangtiantian;

import com.wangtiantian.controller.DownLoadData;
import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.entity.Bean_P_C_B_URL;
import com.wangtiantian.mapper.DataBaseMethod;

import java.util.List;

public class Method_MoreThread implements Runnable {

    private List<Object> list;
    private String savePath;
    private int group;

    public Method_MoreThread(List<Object> list, String savePath, int group) {
        this.list = list;
        this.savePath = savePath;
        this.group = group;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        T_Config_Father t_config_father = new T_Config_Father(0, 10, 3);
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            String configAPI = "https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + ((Bean_P_C_B_URL) bean).get_C_VersionIds() + "&_=1704953414626&_callback=__config3";
            boolean isDownload = DownLoadData.method_config(configAPI, savePath, ((Bean_P_C_B_URL) bean).get_C_Group());
            if (isDownload) {
//                DataBaseMethod.updateDownLoadStatus(1,((Bean_P_C_B_URL) bean).get_C_Group(),"config");
                t_config_father.method_修改下载状态("config",1,((Bean_P_C_B_URL) bean).get_C_Group());
            }
            String paramsAPI = "https://carif.api.autohome.com.cn/Car/v3/Param_ListBySpecIdList.ashx?speclist=" + ((Bean_P_C_B_URL) bean).get_C_VersionIds() + "&_appid=test&_=1708497025742&_callback=__param1";
            boolean isDownload_params = DownLoadData.method_params(paramsAPI, savePath, ((Bean_P_C_B_URL) bean).get_C_Group());
            if (isDownload_params) {
//                t_config_father.Method_UpdateGroup(String.valueOf(((Bean_P_C_B_URL) bean).get_C_Group()));
                                t_config_father.method_修改下载状态("params",1,((Bean_P_C_B_URL) bean).get_C_Group());

//                DataBaseMethod.updateDownLoadStatus(1,((Bean_P_C_B_URL) bean).get_C_Group(),"params");
            }

        }
    }
}
