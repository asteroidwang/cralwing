package com.wangtiantian;

import com.wangtiantian.controller.DownLoadData;
import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.entity.Bean_P_C_B_URL;
import com.wangtiantian.entity.koubei.KouBeiInfo;
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
//            String mainUrl = ((ModelKouBei) bean).get_C_ModelKouBeiUrl();
//            String midFolder =  ((ModelKouBei) bean).get_C_ModelID();
            String mainUrl = ((KouBeiInfo)bean).get_C_KouBeiUrl();
            String fileName = ((KouBeiInfo)bean).get_C_ShowID()+".txt";
            Document mainDoc = null;

            try {
                mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "UTF-8", mainUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mainDoc != null) {
                // 获取上一步入库的未下载的url下载口碑页面数据
//                T_Config_File.method_写文件_根据路径创建文件夹(filePath +midFolder + "/", midFolder + "_" + mainUrl.split("&")[2].replace("pageIndex=","") + ".txt", mainDoc.text());
//                T_Config_KouBei kouBeiDataBase = new T_Config_KouBei(2, 1, 0);
//                kouBeiDataBase.method修改车型口碑页面的下载状态(((ModelKouBei) bean).get_C_ModelKouBeiUrl());

                // 获取上一步入库的未下载的url下载具体口碑页面数据
//                T_Config_File.method_写文件_根据路径创建文件夹(filePath,fileName,mainDoc.toString());
                T_Config_KouBei kouBeiDataBase = new T_Config_KouBei(2, 1, 1);
                kouBeiDataBase.method修改具体口碑页面的下载状态(mainUrl);
            }
        }
    }
}
