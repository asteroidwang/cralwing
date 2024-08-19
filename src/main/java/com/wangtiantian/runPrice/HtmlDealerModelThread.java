package com.wangtiantian.runPrice;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.price.Bean_DealerFenYeUrl;
import com.wangtiantian.entity.price.DealerData;
import com.wangtiantian.mapper.PriceDataBase;

import java.util.List;

public class HtmlDealerModelThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public HtmlDealerModelThread(List<Object> list, String savePath) {
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
                String mainUrl = "https://dealer.autohome.com.cn/" + dealerId + "/price.html";
                if (T_Config_File.method_访问url获取网页源码普通版(mainUrl, "GBK", savePath, dealerId + ".txt")) {
                    priceDataBase.update_修改下载了的经销商报价页面的下载状态(dealerId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
