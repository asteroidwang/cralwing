package com.wangtiantian.runKouBei;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_DataBase_KouBei;
import com.wangtiantian.entity.koubei.Bean_KouBei_KouBeiShortInfo;
import com.wangtiantian.mapper.KouBei_DataBase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DetailsKouBeiInfoThread implements Runnable {
    private List<Object> list;
    private String filePath;

    public DetailsKouBeiInfoThread(List<Object> list, String filePath) {
        this.list = list;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object o : list) {
            String showId = ((Bean_KouBei_KouBeiShortInfo) o).get_C_ShowId();
            String kbId = ((Bean_KouBei_KouBeiShortInfo) o).get_C_KouBeiId();
            String mainUrl = "https://k.autohome.com.cn/detail/view_" + showId + ".html#pvareaid=2112108";
            T_Config_File.method_访问url获取网页源码普通版(mainUrl, "UTF-8", filePath, showId + "_" + kbId + ".txt");
        }
    }
}
