package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.picture.ModelCategroy;
import com.wangtiantian.entity.picture.PictureHtml;
import com.wangtiantian.mapper.DataBaseConnectPic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThreadPictureHtml implements Runnable {
    private List<Object> list;
    private String filePath;
    private CountDownLatch latch;

    public ThreadPictureHtml(List<Object> list, String filePath, CountDownLatch latch) {
        this.list = list;
        this.filePath = filePath;
        this.latch = latch;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        DataBaseConnectPic dataBaseConnectPic = new DataBaseConnectPic();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        List<String> htmlList = new ArrayList<>();
        for (Object o : list) {
            String html = ((PictureHtml) o).get_C_PictureHtml();
            String fileName = ((PictureHtml) o).get_C_PictureHtmlCode() + ".txt";
            if (!T_Config_File.method_判断文件是否存在(filePath + fileName)) {
                if (fileName.startsWith("imgs")){
                    if (T_Config_File.method_访问url获取网页源码普通版(html, "utf-8", filePath, fileName)) {
                        htmlList.add(html);
                    }
                }else {
                    if (T_Config_File.method_访问url获取网页源码普通版(html, "GBK", filePath, fileName)) {
                        htmlList.add(html);
                    }
                }
            } else {
                htmlList.add(html);
            }
        }
        StringBuffer htmls = new StringBuffer();
        for (int i = 0; i < htmlList.size(); i++) {
            htmls.append("'").append(htmlList.get(i)).append("',");
        }
        dataBaseConnectPic.updatePictureHtmlStatus(htmls.toString().substring(0,htmls.length()-1));
        latch.countDown();
    }
}