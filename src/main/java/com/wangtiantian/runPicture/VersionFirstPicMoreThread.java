package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_Picture;
import com.wangtiantian.entity.picture.PictureVersion;
import com.wangtiantian.entity.price.SaleModData;
import com.wangtiantian.mapper.PictureDataBase;
import com.wangtiantian.runPrice.CarPriceMethod;

import java.util.List;

public class VersionFirstPicMoreThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public VersionFirstPicMoreThread(List<Object> list, String savePath) {
        this.list = list;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        PictureDataBase pictureDataBase = new PictureDataBase();
        for (Object bean : list) {

            try {
                String mainUrl = "https://car.autohome.com.cn/photolist/spec/" + ((PictureVersion) bean).get_C_VersionID() + "/p1/#pvareaid=3454554";
                if (T_Config_File.method_访问url获取网页源码普通版(mainUrl, "GBK", savePath, ((PictureVersion) bean).get_C_VersionID() + "_1.txt")) {
                    pictureDataBase.update_修改下载了第一页的版本状态(((PictureVersion) bean).get_C_VersionID());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
