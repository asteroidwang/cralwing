package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.configData.Bean_VersionIds;
import com.wangtiantian.entity.picture.ModelCategroy;
import com.wangtiantian.mapper.DataBaseConnectPic;
import com.wangtiantian.mapper.DataBaseMethod;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThreadModelCategory implements Runnable {
    private List<Object> list;
    private String filePath;
    private CountDownLatch latch;

    public ThreadModelCategory(List<Object> list, String filePath, CountDownLatch latch) {
        this.list = list;
        this.filePath = filePath;
        this.latch = latch;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        DataBaseConnectPic dataBaseConnectPic = new DataBaseConnectPic();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object o : list) {
            String html = ((ModelCategroy) o).get_C_PictureCategoryMoreHtml();
            String fileNameTmp = html.replace("https://car.autohome.com.cn/pic/", "").replace("https://car.autohome.com.cn/pic/", "");
            String fileName = fileNameTmp.substring(0, fileNameTmp.indexOf(".html")).replace("/", "_") + ".txt";
            if (!T_Config_File.method_判断文件是否存在(filePath + fileName)) {
                if (T_Config_File.method_访问url获取网页源码普通版(html, "GBK", filePath, fileName)) {
                    dataBaseConnectPic.updateModelCategoryHtmlStatus(html);
                }
            } else {
                dataBaseConnectPic.updateModelCategoryHtmlStatus(html);
            }
        }
        latch.countDown();
    }
}