package com.wangtiantian.runKouBei;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.koubei.ModelKouBei;
import com.wangtiantian.mapper.KouBeiDataBase;
import com.wangtiantian.runConfigData.MainConfigData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class MainKouBei {
    public static void main(String[] args) {
        // 第一版流程 20240804
        // 1.https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=3170&pageIndex=1&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0
        // new KouBeiMethod().getModelKouBeiFirstFileUrl();
        // new KouBeiMethod().getModelKouBeiFileDataAndUrl();
        // new KouBeiMethod().method_获取总页数并拼接url入库();
        // new KouBeiMoreThread().method_获取上一步入库的未下载的url下载口碑页面数据();
        // new KouBeiMethod().parseKouBeiToGetShoeId();
        // new KouBeiMethod().getKouBeiDescUrl();
        // new KouBeiMoreThread().method_获取上一步入库的未下载的url下载具体口碑页面数据();
        // new KouBeiMethod().getKouBeiDesc();
        // new KouBeiMethod().getKouBeiDescQueShi();
        // new KouBeiMethod().getReplyFile();
        // new KouBeiMethod().update_修改口碑的一级回复数据的下载状态();

        // 20240809 优化代码
//        String macFilePathMini = "/Users/wangtiantian/MyDisk/汽车之家/口碑数据/20240809/";
        String macFilePathAir = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/口碑评价数据/20240809/";
        String winFilePath = "E:/汽车之家/口碑评价数据/20240804/";
        String currentFilePath = "";
        String systemName = System.getProperty("os.name").toLowerCase();
        KouBeiMethod kouBeiMethod = new KouBeiMethod();

        // 1.下载以车型为单位的口碑数据
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            currentFilePath = macFilePathAir;
        } else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            currentFilePath = winFilePath;
        }
        // 数据准备 汽车之家最新的车型数据
        // new MainConfigData().method_下载品牌厂商车型数据(currentFilePath + "车型数据/");
        // kouBeiMethod.getModelKouBeiFirstFileUrl(currentFilePath);

        // new KouBeiMethod().update_修改口碑的一级回复数据的下载状态();
        // new KouBeiMethod().method_一级评论数据();
        // new KouBeiMethod().parse_一级评论无法解析("/Users/asteroid/所有文件数据/一级评论/");
        // new KouBeiMethod().downLoad_下载回复一级评论的数据文件(currentFilePath+"二级评论数据/");
        // new KouBeiMethod().confirm_确认已下载的二级评论数据(currentFilePath+"二级评论数据/");
        // new KouBeiMethod().method_解析二级评论数据(currentFilePath+"二级评论数据/");
        new KouBeiMethod().method_下载口碑数据中的图片(currentFilePath);
    }
}