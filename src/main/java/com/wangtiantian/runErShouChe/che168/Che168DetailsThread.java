package com.wangtiantian.runErShouChe.che168;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.ershouche.che168.Bean_CarHtml;
import com.wangtiantian.entity.ershouche.che168.Che168_CityData;
import com.wangtiantian.mapper.ErShouCheDataBase;

import java.util.List;

public class Che168DetailsThread implements Runnable {

    private List<Object> list;
    private String filePath;

    public Che168DetailsThread(List<Object> list, String filePath) {
        this.list = list;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object o : list) {
            String mainUrl = ((Bean_CarHtml) o).get_C_CarHtml();
            if (!mainUrl.equals("-")) {
                String fileName = mainUrl.substring(mainUrl.indexOf("dealer") + 7, mainUrl.indexOf(".html")).replace("/", "_");
                if (new MainChe168().method_访问url获取网页源码普通版(mainUrl, "GBK", filePath, fileName + ".txt")){
                    ErShouCheDataBase e = new ErShouCheDataBase();
                    e.update_修改che168车辆详情页下载状态(mainUrl);
                }
            }
        }
    }
}