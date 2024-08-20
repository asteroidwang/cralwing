package com.wangtiantian.runErShouChe.che168;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.ershouche.che168.Che168_CityData;
import com.wangtiantian.entity.price.SaleModData;
import com.wangtiantian.mapper.ErShouCheDataBase;
import com.wangtiantian.mapper.PriceDataBase;

import java.util.List;

public class Che168FenYeThread  implements Runnable {

    private List<Object> list;
    private String savePath;

    public Che168FenYeThread(List<Object> list, String savePath) {
        this.list = list;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for(Object o:list){
            String pinyin =((Che168_CityData)o).get_C_CityPinYin();
            String mainUrl ="https://www.che168.com/nlist/"+pinyin+"/list/?pvareaid=100533";
            if (MainChe168.method_访问二手车url获取网页源码(mainUrl,"GBK",savePath,pinyin+"_1.txt")){
                new ErShouCheDataBase().update_修改已下载的城市状态(pinyin);
            }
        }
    }
}
