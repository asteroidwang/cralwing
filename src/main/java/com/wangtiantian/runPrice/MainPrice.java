package com.wangtiantian.runPrice;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.dao.T_Config_File;
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
    private String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/经销商数据/20240802/";
    private String cityDataName = "cityData.json";
    private PriceDataBase priceDataBase = new PriceDataBase();

    public static void main(String[] args) {
        // 数据准备->入库城市数据
        // new MainPrice().cityData();
        // 数据准备-> 下载各省市的经销商列表数据 https://dealer.autohome.com.cn/beijing/0/0/0/0/4/1/0/0.html
        // new MainPrice().getDealerFile();
        // 解析经销商数据
        // new MainPrice().parseDealerFile();
        // 下载车型报价页面
        // new MainPrice().getCarPriceFile();
        // new MainPrice().getCarPriceFile2();
        // 解析车型报价页面 获取该店的在售车型Id 拼接下载地址获取 该店在售车型的车辆信息
        // new MainPrice().parseCarPriceFile();
        // new MainPrice().parseCarPriceFile2();
        // 下载车辆信息数据
        new MainPrice().getCarDataFile();
    }

    // 入库城市数据
    public void cityData() {
        try {
            String cityData = T_Config_File.method_读取文件内容(filePath + cityDataName);
            JSONObject jsonRoot = JSONObject.parseObject(cityData);
            JSONArray jsonArray = jsonRoot.getJSONArray("AreaInfoGroups");
            ArrayList<CityData> cityDataArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray proValues = ((JSONObject) jsonArray.get(i)).getJSONArray("Values");
                for (int j = 0; j < proValues.size(); j++) {
                    JSONArray cityArray = ((JSONObject) proValues.get(j)).getJSONArray("Cities");
                    for (int k = 0; k < cityArray.size(); k++) {
                        CityData entity = new CityData();
                        entity.set_C_CityID(((JSONObject) cityArray.get(k)).getString("Id"));
                        entity.set_C_CityName(((JSONObject) cityArray.get(k)).getString("Name"));
                        entity.set_C_CityPinYin(((JSONObject) cityArray.get(k)).getString("Pinyin"));
                        entity.set_C_CityCount(((JSONObject) cityArray.get(k)).getInteger("Count"));
                        entity.set_C_ProvinceID(((JSONObject) proValues.get(j)).getString("Id"));
                        entity.set_C_ProvinceName(((JSONObject) proValues.get(j)).getString("Name"));
                        entity.set_C_ProvincePinYin(((JSONObject) proValues.get(j)).getString("Pinyin"));
                        entity.set_C_ProvincePinYin(((JSONObject) proValues.get(j)).getString("Pinyin"));
                        entity.set_C_ProvinceCount(((JSONObject) proValues.get(j)).getInteger("Count"));
                        entity.set_C_IsFinish(0);
                        entity.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        cityDataArrayList.add(entity);
                    }
                }
            }
            priceDataBase.price_cityData(cityDataArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 下载各省市的经销商列表数据
    public void getDealerFile() {
        ArrayList<Object> dataList = priceDataBase.findDealerCityNotFinish();
        try {
            for (Object o : dataList) {
                for (int i = 1; i < 999; i++) {
                    String mainUrl = "https://dealer.autohome.com.cn/" + ((CityData) o).get_C_CityPinYin() + "/0/0/0/0/" + i + "/1/0/0.html";
                    System.out.println(mainUrl);
                    if (T_Config_File.method_判断文件是否存在(filePath + "经销商信息/" + ((CityData) o).get_C_ProvinceName() + "/" + ((CityData) o).get_C_CityName() + "/" + ((CityData) o).get_C_CityName() + "_" + i + ".txt")) {
                        System.out.println("已存在\t" + filePath + "经销商信息/" + ((CityData) o).get_C_ProvinceName() + "/" + ((CityData) o).get_C_CityName() + "/" + ((CityData) o).get_C_CityName() + "_" + i + ".txt");
                    } else {
                        Document mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "gb2312", mainUrl);
                        Elements mainItems = mainDoc.select(".dealer-list-wrap").select(".list-box").select("li");
                        if (mainItems.size() == 0) {
                            System.out.println("没有数据了");
                            break;
                        } else {
                            try {
                                T_Config_File.method_写文件_根据路径创建文件夹(filePath + "经销商信息/" + ((CityData) o).get_C_ProvinceName() + "/" + ((CityData) o).get_C_CityName() + "/", ((CityData) o).get_C_CityName() + "_" + i + ".txt", mainDoc.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                priceDataBase.updateDealerCityStatus(((CityData) o).get_C_CityID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解析经销商数据
    public void parseDealerFile() {
        try {
            ArrayList<String> provinceList = T_Config_File.method_获取文件夹名称(filePath);
            ArrayList<DealerData> dataList = new ArrayList<>();
            for (String provinceName : provinceList) {
                ArrayList<String> cityList = T_Config_File.method_获取文件夹名称(filePath + provinceName);
                for (String cityName : cityList) {
                    ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + provinceName + "/" + cityName);
                    for (String fileName : fileList) {
                        String dataContent = T_Config_File.method_读取文件内容(filePath + provinceName + "/" + cityName + "/" + fileName);
                        Document mainDoc = Jsoup.parse(dataContent);
                        Elements mainItems = mainDoc.select(".dealer-list-wrap").select(".list-box").select(".list-item");
                        for (int i = 0; i < mainItems.size(); i++) {
                            DealerData dealerData = new DealerData();
                            dealerData.set_C_DealerID(mainItems.get(i).attr("id"));
                            Elements items = mainItems.get(i).select(".tit-row");
                            dealerData.set_C_DealerName(items.select("a").select("span").text());
                            dealerData.set_C_Type(items.select("span").get(1).text());
                            dealerData.set_C_SaleBrandName(mainItems.get(i).select("ul").select("li").get(1).select("span").select("em").text());
                            dealerData.set_C_SaleNum(Integer.parseInt(mainItems.get(i).select("ul").select("li").get(1).select("a").text().replace("共", "").replace("个在售车型", "")));
                            dealerData.set_C_Phone(mainItems.get(i).select("ul").select("li").get(2).select(".tel").text());
                            dealerData.set_C_SaleAddress(mainItems.get(i).select("ul").select("li").get(2).select(".gray.data-business-tip").text() + "->" + mainItems.get(i).select("ul").select("li").get(2).select(".floating").text());
                            dealerData.set_C_Address(mainItems.get(i).select("ul").select("li").get(3).select(".info-addr").text());
                            dealerData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            dealerData.set_C_CityName(fileName.replace(fileName.substring(fileName.indexOf("_")), ""));
                            dataList.add(dealerData);
                        }
                    }
                }
            }
            HashSet<DealerData> set = new HashSet<>(dataList);
            dataList.clear();
            dataList.addAll(set);
            priceDataBase.dealerData(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载车型报价页面_1
    public void getCarPriceFile() {
        try {
            ArrayList<Object> dataList = priceDataBase.findCarPriceNotFinish();
            for (Object o : dataList) {
                String mainUrl = "https://dealer.autohome.com.cn/" + ((DealerData) o).get_C_DealerID() + "/price.html";
                Document mainDoc = null;
                try {
                    mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "gb2312", mainUrl);
                    T_Config_File.method_写文件_根据路径创建文件夹(filePath + "车型报价页面/", ((DealerData) o).get_C_DealerName() + "_" + ((DealerData) o).get_C_DealerID() + ".txt", mainDoc.toString());
                    priceDataBase.updateCarPriceStatus(((DealerData) o).get_C_DealerID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载车型报价页面_2
    public void getCarPriceFile2() {
        ArrayList<Object> dataList = priceDataBase.findNoCarPriceDealer_20240802();
        for (Object o : dataList) {
            String mainUrl = "https://dealer.autohome.com.cn/handler/other/getdata?__action=dealer.getfacseriesinfobydealerid&dealerId=" + ((DealerData) o).get_C_DealerID() + "&show0Price=1";
            Document mainDoc = null;
            try {
                mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "UTF-8", mainUrl);
                T_Config_File.method_写文件_根据路径创建文件夹(filePath + "车型报价页面_2/", ((DealerData) o).get_C_DealerName() + "_" + ((DealerData) o).get_C_DealerID() + ".txt", mainDoc.text());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //解析车型报价页面 获取该店的在售车型Id 拼接下载地址获取 该店在售车型的车辆信息
    public void parseCarPriceFile() {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + "车型报价页面/");
            ArrayList<SaleModData> dataArrayList = new ArrayList<>();
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + "车型报价页面/" + fileName);
                Document mainDoc = Jsoup.parse(content);
                Elements mainItems = mainDoc.select(".tree-dl");
                String dealerId = fileName.split("_")[1].replace(".txt", "");
                for (int i = 0; i < mainItems.size(); i++) {
                    Elements fctItem = mainItems.get(i).select("dt");
                    Elements modItems = mainItems.get(i).select("dd");
                    for (int j = 0; j < modItems.size(); j++) {
                        SaleModData saleModData = new SaleModData();
                        saleModData.set_C_DealerID(dealerId);
                        saleModData.set_C_FactoryID(fctItem.select("a").attr("href").split("/")[2].replace("f_", "").replace(".html", ""));
                        saleModData.set_C_ModelID(modItems.get(j).select("a").attr("href").split("/")[2].replace("b_", "").replace(".html", ""));
                        saleModData.set_C_PriceDataUrl("https://dealer.autohome.com.cn/handler/other/getdata?__action=dealerlq.getdealerspeclist&dealerId=" + saleModData.get_C_DealerID() + "&seriesId=" + saleModData.get_C_ModelID() + "&show0Price=1");
                        saleModData.set_C_IsFinish(0);
                        saleModData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        dataArrayList.add(saleModData);
                    }
                }
            }
            HashSet<SaleModData> set = new HashSet<>(dataArrayList);
            dataArrayList.clear();
            dataArrayList.addAll(set);
            priceDataBase.saleModData(dataArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解析车型报价页面 获取该店的在售车型Id 拼接下载地址获取 该店在售车型的车辆信息_2
    public void parseCarPriceFile2() {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + "车型报价页面_2/");
            ArrayList<SaleModData> dataArrayList = new ArrayList<>();
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + "车型报价页面_2/" + fileName);
                JSONObject jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
                JSONArray jsonArray = jsonRoot.getJSONArray("list");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONArray modArray = ((JSONObject) jsonArray.get(i)).getJSONArray("SeriesList");
                    for (int j = 0; j < modArray.size(); j++) {
                        String dealerId = fileName.split("_")[1].replace(".txt", "");
                        SaleModData saleModData = new SaleModData();
                        saleModData.set_C_DealerID(dealerId);
                        saleModData.set_C_FactoryID(((JSONObject) jsonArray.get(i)).getString("FactoryId"));
                        saleModData.set_C_ModelID(((JSONObject) modArray.get(j)).getString("SeriesId"));
                        saleModData.set_C_PriceDataUrl("https://dealer.autohome.com.cn/handler/other/getdata?__action=dealerlq.getdealerspeclist&dealerId=" + saleModData.get_C_DealerID() + "&seriesId=" + saleModData.get_C_ModelID() + "&show0Price=1");
                        saleModData.set_C_IsFinish(0);
                        saleModData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        dataArrayList.add(saleModData);
                    }
                }

            }
            HashSet<SaleModData> set = new HashSet<>(dataArrayList);
            dataArrayList.clear();
            dataArrayList.addAll(set);
            priceDataBase.saleModData(dataArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载车辆信息数据
    public void getCarDataFile() {
        try {
            ArrayList<Object> dataList = priceDataBase.findNoDealerModel();
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                PriceMoreThread priceMoreThread = new PriceMoreThread(list.get(i), filePath);
                Thread thread = new Thread(priceMoreThread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method_下载车辆信息数据文件(String url, String fileName) {
        try {
            Document mainDoc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            T_Config_File.method_写文件_根据路径创建文件夹(filePath + "车辆价格信息/", fileName, mainDoc.text());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
