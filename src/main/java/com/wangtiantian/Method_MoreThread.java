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
        T_Config_Father t_config_father = new T_Config_Father(0, 7, 3);
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            String configAPI = "https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + ((Bean_P_C_B_URL) bean).get_C_VersionIds() + "&_=1704953414626&_callback=__config3";
            boolean isDownload = DownLoadData.method_config(configAPI, savePath, ((Bean_P_C_B_URL) bean).get_C_Group());
            if (isDownload) {
                t_config_father.method_修改下载状态("config", 1, ((Bean_P_C_B_URL) bean).get_C_Group());
            }
            String paramsAPI = "https://carif.api.autohome.com.cn/Car/v3/Param_ListBySpecIdList.ashx?speclist=" + ((Bean_P_C_B_URL) bean).get_C_VersionIds() + "&_appid=test&_=1708497025742&_callback=__param1";
            boolean isDownload_params = DownLoadData.method_params(paramsAPI, savePath, ((Bean_P_C_B_URL) bean).get_C_Group());
            if (isDownload_params) {
                t_config_father.method_修改下载状态("params", 1, ((Bean_P_C_B_URL) bean).get_C_Group());
            }
            String bagAPI ="https://carif.api.autohome.com.cn/Car/Config_BagBySpecIdListV2.ashx?speclist=" +  ((Bean_P_C_B_URL) bean).get_C_VersionIds() + "&_=1704953414627&_callback=__bag4";
            boolean isDownload_bag = DownLoadData.method_bag(bagAPI, savePath, ((Bean_P_C_B_URL) bean).get_C_Group());
            if (isDownload_bag) {
                t_config_father.method_修改下载状态("bag", 1, ((Bean_P_C_B_URL) bean).get_C_Group());
            }

        }
    }
}
