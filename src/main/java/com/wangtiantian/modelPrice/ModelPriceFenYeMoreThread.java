package com.wangtiantian.modelPrice;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.ModelDealerPrice.ModelDealerPriceFenYe;
import com.wangtiantian.entity.price.ModelDealerData;
import com.wangtiantian.mapper.ModelDealerPriceDataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;

public class ModelPriceFenYeMoreThread implements Runnable {

    private List<Object> list;
    private String filePath;

    public ModelPriceFenYeMoreThread(List<Object> list, String filePath) {
        this.list = list;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            try {
                String modId =((ModelDealerPriceFenYe) bean).get_C_ModelID();
                String mainUrl =((ModelDealerPriceFenYe) bean).get_C_DealerFenYeUrl();
                int page = ((ModelDealerPriceFenYe)bean).get_C_Page();
                if (T_Config_File.method_访问url获取Json普通版(mainUrl, "UTF-8", filePath, modId + "_"+page+".txt")) {
                    new ModelDealerPriceDataBase().update_修改已下载首页的车型id的下载状态(mainUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
