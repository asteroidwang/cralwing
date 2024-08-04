package com.wangtiantian;

import com.wangtiantian.controller.DownLoadData;
import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.entity.Bean_P_C_B_URL;
import com.wangtiantian.entity.koubei.ModelKouBei;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;

public class CommonMoreThread implements Runnable {
    private List<Object> list;
    private String filePath;

    public CommonMoreThread(List<Object> list, String filePath) {
        this.list = list;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            String modFirstUrl = ((ModelKouBei) bean).get_C_ModelKouBeiUrl();
            Document mainDoc = null;
            try {
                mainDoc = Jsoup.parse(new URL(modFirstUrl).openStream(), "UTF-8", modFirstUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mainDoc != null) {
                T_Config_File.method_写文件_根据路径创建文件夹(filePath + ((ModelKouBei) bean).get_C_ModelID() + "/", ((ModelKouBei) bean).get_C_ModelID() + "_" + modFirstUrl.split("&")[2].replace("pageIndex=","") + ".txt", mainDoc.text());
                T_Config_KouBei kouBeiDataBase = new T_Config_KouBei(2, 1, 0);
                kouBeiDataBase.method修改车型口碑页面的下载状态(((ModelKouBei) bean).get_C_ModelKouBeiUrl());
            }


        }
    }
}
