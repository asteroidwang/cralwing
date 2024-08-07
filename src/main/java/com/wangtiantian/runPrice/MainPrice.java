package com.wangtiantian.runPrice;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.price.CarPrice;
import com.wangtiantian.entity.price.CityData;
import com.wangtiantian.entity.price.DealerData;
import com.wangtiantian.entity.price.SaleModData;
import com.wangtiantian.mapper.PriceDataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainPrice {
    //    private String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/经销商数据/20240802/";
    public static void main(String[] args) {
        String filePath = "/Users/wangtiantian/MyDisk/汽车之家/经销商数据/20240806/";
        String cityDataName = "cityData.json";
        CarPriceMethod carPriceMethod = new CarPriceMethod();
        // 数据准备->入库城市数据
        // carPriceMethod.cityData(filePath+cityDataName);
        // 数据准备-> 下载各省市的经销商列表数据 https://dealer.autohome.com.cn/beijing/0/0/0/0/4/1/0/0.html
        // carPriceMethod.getDealerFile(filePath);
        // 解析经销商数据
        // carPriceMethod.parseDealerFile(filePath+"经销商信息/");
        // 下载车型报价页面
         carPriceMethod.getCarPriceFile(filePath+ "车型报价页面_html/");
         carPriceMethod.getCarPriceFile2( filePath+ "车型报价页面/");
        // 解析车型报价页面 获取该店的在售车型Id 拼接下载地址获取 该店在售车型的车辆信息
        // new MainPrice().parseCarPriceFile();
        // carPriceMethod.parseCarPriceFile2( filePath+ "车型报价页面/");
        // 下载车辆信息数据
        // carPriceMethod.getCarDataFile(filePath);
        // 解析数据
        // new CarPriceMethod().parseCarPriceData();
    }


}