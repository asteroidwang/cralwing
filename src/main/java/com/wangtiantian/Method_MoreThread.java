package com.wangtiantian;

import com.wangtiantian.controller.DownLoadData;
import com.wangtiantian.entity.Bean_P_C_B_URL;

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
//            System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
//                ((Bean_P_C_B_URL) bean).get_C_VersionIds()+"\t"+
//                System.out.println(((Bean_P_C_B_URL) bean).get_C_Group());
//                System.out.println(bean.get_Url()+"\t"+bean.get_Group());
//               DownLoadData.method_params(url, savePath, group);
            String configAPI = "https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + ((Bean_P_C_B_URL) bean).get_C_VersionIds() + "&_=1704953414626&_callback=__config3";
            DownLoadData.method_config(configAPI, savePath, ((Bean_P_C_B_URL) bean).get_C_Group());
        }
    }
}
