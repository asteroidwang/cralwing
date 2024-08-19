package com.wangtiantian.runPrice;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.price.DealerData;
import com.wangtiantian.mapper.PriceDataBase;

import java.util.List;

public class JsonDealerModelThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public JsonDealerModelThread(List<Object> list, String savePath) {
        this.list = list;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        PriceDataBase priceDataBase = new PriceDataBase();
        for (Object bean : list) {
            try {
                String dealerId = ((DealerData) bean).get_C_DealerID();
                String mainUrl = "https://dealer.autohome.com.cn/handler/other/getdata?__action=dealer.getfacseriesinfobydealerid&dealerId=" + dealerId + "&show0Price=1";
                if (T_Config_File.method_访问url获取Json普通版(mainUrl, "UTF-8", savePath, dealerId + ".txt")) {
                    priceDataBase.update_修改下载了的经销商报价页面的下载状态(dealerId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
