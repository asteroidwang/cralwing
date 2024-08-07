package com.wangtiantian.runPrice;

import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.entity.price.SaleModData;
import com.wangtiantian.runPrice.MainPrice;

import java.util.List;

public class PriceMoreThread implements Runnable {

    private List<Object> list;
    private String savePath;
    private int group;

    public PriceMoreThread(List<Object> list, String savePath) {
        this.list = list;
        this.savePath = savePath;
//        this.group = group;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            try {
                new CarPriceMethod().method_下载车辆信息数据文件(((SaleModData) bean).get_C_PriceDataUrl(), ((SaleModData) bean).get_C_DealerID() + "_" + ((SaleModData) bean).get_C_ModelID() + ".txt",savePath);
                new T_Config_Father(2, 0, 2).updateNoDealerModelStatus(((SaleModData) bean).get_C_DealerID(), ((SaleModData) bean).get_C_ModelID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
