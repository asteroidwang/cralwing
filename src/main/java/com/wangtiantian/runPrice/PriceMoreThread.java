package com.wangtiantian.runPrice;

import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.price.SaleModData;
import com.wangtiantian.mapper.PriceDataBase;
import com.wangtiantian.runPrice.MainPrice;

import java.util.List;

public class PriceMoreThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public PriceMoreThread(List<Object> list, String savePath) {
        this.list = list;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            PriceDataBase priceDataBase = new PriceDataBase();
            try {
                String mainUrl = ((SaleModData) bean).get_C_PriceDataUrl();
                String fileName = ((SaleModData) bean).get_C_DealerID() + "_" + ((SaleModData) bean).get_C_ModelID() + ".txt";
                String dealerID = ((SaleModData) bean).get_C_DealerID();
                String modId = ((SaleModData) bean).get_C_ModelID();
                T_Config_File.method_访问url获取Json普通版(mainUrl, "UTF-8", savePath, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

