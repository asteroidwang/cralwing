package com.wangtiantian.runConfigData;

import com.wangtiantian.dao.T_Config_AutoHome_new;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.koubei.KouBeiInfo;
import com.wangtiantian.mapper.DataBaseMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;

public class ModelMoreThread implements Runnable {
    private List<Object> list;
    private String filePath;
    private int type;

    public ModelMoreThread(List<Object> list, String filePath, int type) {
        this.list = list;
        this.filePath = filePath;
        this.type = type;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        DataBaseMethod dataBaseMethod = new DataBaseMethod();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            String modId = ((Bean_Model) bean).get_C_ModelID();
            String url = "";
            if (type == 0) {
                url = "https://car-web-api.autohome.com.cn/car/series/getspeclistresponse?seriesid=" + modId + "&tagid=1&tagname=%E5%9C%A8%E5%94%AE";
                if (T_Config_File.method_访问url获取Json普通版(url, "GBK", filePath, modId + "_在售.txt")) {
                    dataBaseMethod.method_修改车型表中下载的id状态(modId, "在售", 1);
                }
            } else if (type == 1) {
                url = "https://www.autohome.com.cn/" + modId + "/sale.html";
                if (T_Config_File.method_访问url获取网页源码普通版(url, "GBK", filePath, modId + "_停售.txt")) {
                    dataBaseMethod.method_修改车型表中下载的id状态(modId, "停售", 1);
                }
            } else if (type == 2) {
                url = "https://car.autohome.com.cn/pic/series/" + modId + ".html#pvareaid=3454438";
                if (T_Config_File.method_访问url获取网页源码普通版(url, "GBK", filePath, modId + "_图片页面在售.txt")) {
                    dataBaseMethod.method_修改车型表中下载的id状态(modId, "图片页面在售", 1);
                }
            } else if (type == 3) {
                url = "https://car.autohome.com.cn/pic/series-t/" + modId + ".html";
                if (T_Config_File.method_访问url获取网页源码普通版(url, "GBK", filePath, modId + "_图片页面停售.txt")) {
                    dataBaseMethod.method_修改车型表中下载的id状态(modId, "图片页面停售", 1);
                }
            } else if (type == 4) {
                url = "https://car-web-api.autohome.com.cn/car/series/getspeclistresponse?seriesid=" + modId + "&tagid=2&tagname=%E5%8D%B3%E5%B0%86%E9%94%80%E5%94%AE";

                if (T_Config_File.method_访问url获取Json普通版(url, "UTF-8", filePath, modId + "_即将销售.txt")) {
                    dataBaseMethod.method_修改车型表中下载的id状态(modId, "即将销售", 1);
                }
            }
        }
    }
}