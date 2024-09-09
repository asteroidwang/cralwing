package com.wangtiantian.modelPrice;

import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.price.ModelDealerData;
import com.wangtiantian.entity.price.SaleModData;
import com.wangtiantian.mapper.PriceDataBase;
import com.wangtiantian.runPrice.CarPriceMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;

public class ModelPriceMoreThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public ModelPriceMoreThread(List<Object> list, String savePath) {
        this.list = list;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            try {
                String dealerId = ((ModelDealerData) bean).get_C_DealerId();
                String modId = ((ModelDealerData) bean).get_C_ModelId();
                String mainUrl = "https://dealer.autohome.com.cn/handler/other/getdata?__action=dealerlq.getdealerspeclist&dealerId=" + dealerId + "&seriesId=" + modId + "&show0Price=1";
                Document mainDoc =null;
                try {
                    mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "UTF-8", mainUrl);
                    T_Config_File.method_写文件_根据路径创建文件夹(savePath, dealerId + "_" + modId + ".txt", mainDoc.text());
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
