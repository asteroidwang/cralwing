package com.wangtiantian.runKouBei;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.koubei.ModelKouBei;
import com.wangtiantian.mapper.KouBeiDataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class MainKouBei {



    public static void main(String[] args) {
        // 1.https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=3170&pageIndex=1&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0
        // new KouBeiMethod().getModelKouBeiFirstFileUrl();
        // new KouBeiMethod().getModelKouBeiFileDataAndUrl();
        // new KouBeiMethod().method_获取总页数并拼接url入库();
        // new KouBeiMoreThread().method_获取上一步入库的未下载的url下载口碑页面数据();
        // new KouBeiMethod().parseKouBeiToGetShoeId();
        // new KouBeiMethod().getKouBeiDescUrl();
        new KouBeiMoreThread().method_获取上一步入库的未下载的url下载具体口碑页面数据();
    }



}
