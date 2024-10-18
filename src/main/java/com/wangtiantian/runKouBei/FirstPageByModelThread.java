package com.wangtiantian.runKouBei;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.koubei.KouBeiInfo;
import com.wangtiantian.mapper.KouBei_DataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

// 车型查询口碑的首页多线程数据下载
public class FirstPageByModelThread implements Runnable {
    private List<String> list;
    private String filePath;
    private CountDownLatch latch;
    public FirstPageByModelThread(List<String> list, String filePathh, CountDownLatch latch) {
        this.list = list;
        this.filePath = filePath;
        this.latch = latch;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (String modelId : list) {
            String modelKouBeiUrl = "https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=" + modelId + "&pageIndex=1&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0";
            T_Config_File.method_访问url获取Json普通版(modelKouBeiUrl, "UTF-8", filePath, modelId + "_1.txt");
            KouBei_DataBase kouBeiDataBase = new KouBei_DataBase();
            kouBeiDataBase.update_下载状态(modelId, 1);
        }
        latch.countDown();
    }
}