package com.wangtiantian.runPrice;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.price.Bean_DealerFenYeUrl;
import com.wangtiantian.entity.price.SaleModData;
import com.wangtiantian.mapper.PriceDataBase;

import java.util.List;

public class DealerFenYeThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public DealerFenYeThread(List<Object> list, String savePath) {
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
                String mainUrl = ((Bean_DealerFenYeUrl) bean).get_C_DealerFenYeUrl();
                String pinyin = mainUrl.split("/")[3];
                String page = mainUrl.split("/")[8];
                if (T_Config_File.method_访问url获取网页源码普通版(mainUrl, "GBK", savePath, pinyin + "_" + page + ".txt")) {
                    priceDataBase.update_修改所有经销商分页url表的数据的下载状态(mainUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
