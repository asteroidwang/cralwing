package com.wangtiantian.runErShouChe.che168;

import com.wangtiantian.entity.ershouche.che168.Che168_CityData;
import com.wangtiantian.entity.ershouche.che168.Che168_FenYeUrl;
import com.wangtiantian.mapper.ErShouCheDataBase;

import java.util.List;

public class Che168AllFenYeThread  implements Runnable {

    private List<Object> list;
    private String savePath;

    public Che168AllFenYeThread(List<Object> list, String savePath) {
        this.list = list;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object o:list) {
            String mainUrl = ((Che168_FenYeUrl)o).get_C_FenYeUrl();
            String cityPinYin =mainUrl.split("/")[3];
            String fileName =cityPinYin+"_"+((Che168_FenYeUrl)o).get_C_Page()+".txt";
            if (MainChe168.method_访问二手车url获取网页源码(mainUrl,"GBK",savePath,fileName)){
                new ErShouCheDataBase().update_修改已下载的分页数据下载状态(mainUrl);
            }
        }
    }
}
