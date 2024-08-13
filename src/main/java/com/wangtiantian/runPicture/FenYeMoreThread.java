package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.picture.Picture_FenYeUrl;
import com.wangtiantian.mapper.PictureDataBase;

import java.util.List;

public class FenYeMoreThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public FenYeMoreThread(List<Object> list, String savePath) {
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
                String mainUrl =((Picture_FenYeUrl)bean).get_C_FenYeUrl();
                int page = Integer.parseInt(mainUrl.split("/")[6].replace("p",""));
                if (T_Config_File.method_访问url获取网页源码普通版(mainUrl, "GBK", savePath, ((Picture_FenYeUrl) bean).get_C_VersionId() + "_"+page+".txt")) {
                    pictureDataBase.update_修改下载分页数据的版本状态(((Picture_FenYeUrl) bean).get_C_FenYeUrl());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
