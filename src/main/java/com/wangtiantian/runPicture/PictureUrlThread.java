package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.picture.PictureHtmlUrl;
import com.wangtiantian.entity.picture.PictureUrl;
import com.wangtiantian.mapper.PictureDataBase;

import java.util.List;

public class PictureUrlThread implements Runnable {

    private List<Object> list;
    private String savePath;

    public PictureUrlThread(List<Object> list, String savePath) {
        this.list = list;
        this.savePath = savePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            try {
                String mainUrl = ((PictureUrl) bean).get_C_GaoQingImgUrl().equals("无高清图") ? ((PictureUrl) bean).get_C_ImgUrl() : ((PictureUrl) bean).get_C_GaoQingImgUrl();
                String tempFilePath = ((PictureUrl) bean).get_C_BrandId()+"/"+((PictureUrl)bean).get_C_FactoryId() + "/" + ((PictureUrl) bean).get_C_ModelId() + "/" + ((PictureUrl) bean).get_C_VersionId() + "/" + ((PictureUrl) bean).get_C_ImgType() + "/";
                String fileName = ((PictureUrl) bean).get_C_ImgId();
                if (T_Config_File.downloadImage(mainUrl, savePath + tempFilePath, fileName+".jpg")) {
                    T_Config_File.method_重复写文件_根据路径创建文件夹(savePath.replace("图片/", ""), "已下载的图片Url.txt", mainUrl + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
