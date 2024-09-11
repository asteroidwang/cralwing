package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.picture.PictureHtmlUrl;
import com.wangtiantian.mapper.PictureDataBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PictureHtmlUrlThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public PictureHtmlUrlThread(List<Object> list, String savePath) {
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
                String mainUrl = ((PictureHtmlUrl) bean).get_C_PictureHtmlUrl();
                String fileName = ((PictureHtmlUrl) bean).get_C_BrandId() + "_" + ((PictureHtmlUrl) bean).get_C_ModelId() + "_" + ((PictureHtmlUrl) bean).get_C_VersionId() + "_" + ((PictureHtmlUrl) bean).get_C_ImgId() + "_" + ((PictureHtmlUrl) bean).get_C_ImgType();
                if (T_Config_File.method_访问url获取网页源码普通版(mainUrl, "GBK", savePath, fileName + ".txt")) {
//                    T_Config_File.method_重复写文件_根据路径创建文件夹(savePath.replace("图片的具体页面/",""),"已下载的图片具体页面.txt",mainUrl+"\n");
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
