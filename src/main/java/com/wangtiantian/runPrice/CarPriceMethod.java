package com.wangtiantian.runPrice;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_Price;
import com.wangtiantian.entity.price.*;
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

public class CarPriceMethod {

    private PriceDataBase priceDataBase = new PriceDataBase();

    // 入库城市数据
    public void cityData(String filePath) {
        try {
            String cityData = T_Config_File.method_读取文件内容(filePath);
            JSONObject jsonRoot = JSONObject.parseObject(cityData);
            JSONArray jsonArray = jsonRoot.getJSONArray("AreaInfoGroups");
            ArrayList<Object> cityDataArrayList = new ArrayList<>();
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
            new PriceDataBase().price_cityData(cityDataArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 下载各省市的经销商列表数据
    public void parse_获取经销商分页数据的url() {
        ArrayList<Object> dataList = new PriceDataBase().findDealerCityNotFinish();
        ArrayList<Object> result = new ArrayList<>();
        try {
            for (Object o : dataList) {
                for (int i = 1; i < ((CityData) o).get_C_CityCount() / 15 + 2; i++) {
                    String mainUrl = "https://dealer.autohome.com.cn/" + ((CityData) o).get_C_CityPinYin() + "/0/0/0/0/" + i + "/1/0/0.html";
                    System.out.println(mainUrl);
                    Bean_DealerFenYeUrl bean_dealerFenYeUrl = new Bean_DealerFenYeUrl();
                    bean_dealerFenYeUrl.set_C_DealerFenYeUrl(mainUrl);
                    bean_dealerFenYeUrl.set_C_IsFinish(0);
                    bean_dealerFenYeUrl.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    result.add(bean_dealerFenYeUrl);
//                    Document mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "gb2312", mainUrl);
//                    Elements mainItems = mainDoc.select(".dealer-list-wrap").select(".list-box").select(".list-item");
//                    T_Config_File.method_重复写文件_根据路径创建文件夹(filePath + "经销商信息/", "aaa.txt", ((CityData) o).get_C_CityName() + "\t" + mainItems.size() + "\t" + mainUrl + "\n");
//                    if (mainItems.size() == 0) {
//                        System.out.println("没有数据了");
//                        break;
//                    } else {
//                        try {
//                            T_Config_File.method_写文件_根据路径创建文件夹(filePath + "经销商信息/" + ((CityData) o).get_C_ProvinceName() + "/" + ((CityData) o).get_C_CityName() + "/", ((CityData) o).get_C_CityName() + "_" + i + ".txt", mainDoc.toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
//                new PriceDataBase().updateDealerCityStatus(((CityData) o).get_C_CityID());
            }
            HashSet<Object> set = new HashSet<>(result);
            result.clear();
            result.addAll(set);
            priceDataBase.insert_入库经销商的分页url数据(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downLoad_下载经销商分页数据(String filePath) {
        ArrayList<Object> dataList = priceDataBase.get_所有未下载的经销商分页url数据();
        System.out.println(dataList.size());
        PriceDataBase priceDataBase = new PriceDataBase();
        if (dataList.size() < 36) {
            for (Object bean : dataList) {
                try {
                    String mainUrl = ((Bean_DealerFenYeUrl) bean).get_C_DealerFenYeUrl();
                    String pinyin = mainUrl.split("/")[3];
                    String page = mainUrl.split("/")[8];
                    if (T_Config_File.method_访问url获取网页源码普通版(mainUrl, "GBK", filePath, pinyin + "_" + page + ".txt")) {
                        priceDataBase.update_修改所有经销商分页url表的数据的下载状态(mainUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                DealerFenYeThread moreThread = new DealerFenYeThread(list.get(i), filePath);
                Thread thread = new Thread(moreThread);
                thread.start();
            }
        }
        if (priceDataBase.get_所有未下载的经销商分页url数据().size() > 0) {
            downLoad_下载经销商分页数据(filePath);
        }

    }

    // 解析经销商数据
    public void parseDealerFile(String filePath) {
        try {
            int num = 0;
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataList = new ArrayList<>();
            for (String fileName : fileList) {
                String dataContent = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(dataContent);
                Elements mainItems = mainDoc.select(".dealer-list-wrap").select(".list-box").select(".list-item");
                num += mainItems.size();
                for (int i = 0; i < mainItems.size(); i++) {
                    DealerData dealerData = new DealerData();
                    dealerData.set_C_DealerID(mainItems.get(i).attr("id"));
                    Elements items = mainItems.get(i).select(".tit-row");
                    if (!items.select("a").select("span").text().equals("")) {
                        if(mainItems.get(i).select("ul").select("li").size()>3){
                            dealerData.set_C_DealerName(items.select("a").select("span").text());
                            dealerData.set_C_Type(items.select("span").get(1).text());
                            dealerData.set_C_SaleBrandName(mainItems.get(i).select("ul").select("li").get(1).select("span").select("em").text());
                            dealerData.set_C_SaleNum(Integer.parseInt(mainItems.get(i).select("ul").select("li").get(1).select("a").text().replace("共", "").replace("个在售车型", "")));
                            dealerData.set_C_Phone(mainItems.get(i).select("ul").select("li").get(2) == null ? "-" : mainItems.get(i).select("ul").select("li").get(2).select(".tel").text());
                            dealerData.set_C_SaleAddress(mainItems.get(i).select("ul").select("li").get(2) == null ? "-" : mainItems.get(i).select("ul").select("li").get(2).select(".gray.data-business-tip").text() + "->" + mainItems.get(i).select("ul").select("li").get(2).select(".floating").text());
                            dealerData.set_C_Address(mainItems.get(i).select("ul").select("li").get(3)==null?"-":mainItems.get(i).select("ul").select("li").get(3).select(".info-addr").text());
                            dealerData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            dealerData.set_C_CityName(fileName.replace(fileName.substring(fileName.indexOf("_")), ""));
                            dealerData.set_C_IsFinish(0);
                            dealerData.set_C_FileName(fileName);
                            dataList.add(dealerData);
                        }

                    } else {
                        System.out.println(fileName);
                    }
                }
            }
            System.out.println(dataList.size());
            HashSet<Object> set = new HashSet<>(dataList);
//            dataList.clear();
//            dataList.addAll(set);
            System.out.println(set.size());
            priceDataBase.insert_经销商数据入库(dataList);
            if (dataList.size() != set.size()) {
                priceDataBase.method_修改经销商分页的下载状态为未下载();
//                downLoad_下载经销商分页数据(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            int num = 0;
//            ArrayList<String> provinceList = T_Config_File.method_获取文件夹名称(filePath);
//            ArrayList<Object> dataList = new ArrayList<>();
//            for (String provinceName : provinceList) {
//                ArrayList<String> cityList = T_Config_File.method_获取文件夹名称(filePath + provinceName);
//                for (String cityName : cityList) {
//                    ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + provinceName + "/" + cityName);
//                    for (String fileName : fileList) {
//                        String dataContent = T_Config_File.method_读取文件内容(filePath + provinceName + "/" + cityName + "/" + fileName);
//                        Document mainDoc = Jsoup.parse(dataContent);
//                        Elements mainItems = mainDoc.select(".dealer-list-wrap").select(".list-box").select(".list-item");
//                        num += mainItems.size();
//                        for (int i = 0; i < mainItems.size(); i++) {
//                            DealerData dealerData = new DealerData();
//                            dealerData.set_C_DealerID(mainItems.get(i).attr("id"));
//                            Elements items = mainItems.get(i).select(".tit-row");
//                            dealerData.set_C_DealerName(items.select("a").select("span").text());
//                            dealerData.set_C_Type(items.select("span").get(1).text());
//                            dealerData.set_C_SaleBrandName(mainItems.get(i).select("ul").select("li").get(1).select("span").select("em").text());
//                            dealerData.set_C_SaleNum(Integer.parseInt(mainItems.get(i).select("ul").select("li").get(1).select("a").text().replace("共", "").replace("个在售车型", "")));
//                            dealerData.set_C_Phone(mainItems.get(i).select("ul").select("li").get(2).select(".tel").text());
//                            dealerData.set_C_SaleAddress(mainItems.get(i).select("ul").select("li").get(2).select(".gray.data-business-tip").text() + "->" + mainItems.get(i).select("ul").select("li").get(2).select(".floating").text());
//                            dealerData.set_C_Address(mainItems.get(i).select("ul").select("li").get(3).select(".info-addr").text());
//                            dealerData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                            dealerData.set_C_CityName(fileName.replace(fileName.substring(fileName.indexOf("_")), ""));
//                            dealerData.set_C_IsFinish(0);
//                            dataList.add(dealerData);
//                        }
//                    }
//                }
//            }
//            HashSet<Object> set = new HashSet<>(dataList);
//            dataList.clear();
//            dataList.addAll(set);
//            new PriceDataBase().dealerData(dataList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //下载车型报价页面_1
    public void getCarPriceFile(String filePath) {
        try {
            ArrayList<Object> dataList = new PriceDataBase().get_没有下载的经销商的车型报价页面html();
            if (dataList.size() < 32) {
                for (Object bean : dataList) {
                    try {
                        String dealerId = ((DealerData) bean).get_C_DealerID();
                        String mainUrl = "https://dealer.autohome.com.cn/" + dealerId + "/price.html";
                        if (T_Config_File.method_访问url获取网页源码普通版(mainUrl, "GBK", filePath, dealerId + ".txt")) {
                            priceDataBase.update_修改下载了的经销商报价页面的下载状态(dealerId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                        .collect(Collectors.toList());
                for (int i = 0; i < list.size(); i++) {
                    HtmlDealerModelThread moreThread = new HtmlDealerModelThread(list.get(i), filePath);
                    Thread thread = new Thread(moreThread);
                    thread.start();
                }
            }
//            for (Object o : dataList) {
//                String mainUrl = "https://dealer.autohome.com.cn/" + ((DealerData) o).get_C_DealerID() + "/price.html";
//                Document mainDoc = null;
//                try {
//                    mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "gb2312", mainUrl);
//                    T_Config_File.method_写文件_根据路径创建文件夹(filePath, ((DealerData) o).get_C_DealerName() + "_" + ((DealerData) o).get_C_DealerID() + ".txt", mainDoc.toString());
//                    new PriceDataBase().updateCarPriceStatus(((DealerData) o).get_C_DealerID());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //下载车型报价页面_2
    public void getCarPriceFile2(String filePath) {
        ArrayList<Object> dataList = new PriceDataBase().get_没有下载的经销商的车型报价页面接口();
        System.out.println(dataList.size());
        if (dataList.size() < 32) {
            for (Object bean : dataList) {
                try {
                    String dealerId = ((DealerData) bean).get_C_DealerID();
                    String mainUrl = "https://dealer.autohome.com.cn/handler/other/getdata?__action=dealer.getfacseriesinfobydealerid&dealerId=" + ((DealerData) bean).get_C_DealerID() + "&show0Price=1";
                    if (T_Config_File.method_访问url获取Json普通版(mainUrl, "UTF-8", filePath, dealerId + ".txt")) {
                        priceDataBase.update_修改下载了的经销商报价页面的下载状态(dealerId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                JsonDealerModelThread moreThread = new JsonDealerModelThread(list.get(i), filePath);
                Thread thread = new Thread(moreThread);
                thread.start();
            }
        }
        if (new PriceDataBase().get_没有下载的经销商的车型报价页面接口().size() > 0) {
            getCarPriceFile2(filePath);
        }

//        for (Object o : dataList) {
//            String mainUrl = "https://dealer.autohome.com.cn/handler/other/getdata?__action=dealer.getfacseriesinfobydealerid&dealerId=" + ((DealerData) o).get_C_DealerID() + "&show0Price=1";
//            Document mainDoc = null;
//            try {
//                mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "UTF-8", mainUrl);
//                T_Config_File.method_写文件_根据路径创建文件夹(filePath, ((DealerData) o).get_C_DealerName() + "_" + ((DealerData) o).get_C_DealerID() + ".txt", mainDoc.text());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    //解析车型报价页面 获取该店的在售车型Id 拼接下载地址获取 该店在售车型的车辆信息
    public void parseCarPriceFile(String filePath) {
        try {
//            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataArrayList = new ArrayList<>();
//            for (String fileName : fileList) {
//                String content = T_Config_File.method_读取文件内容(filePath + fileName);
//                Document mainDoc = Jsoup.parse(content);
//                Elements mainItems = mainDoc.select(".tree-dl");
//                String dealerId = fileName.replace(".txt", "");
//                for (int i = 0; i < mainItems.size(); i++) {
//                    Elements fctItem = mainItems.get(i).select("dt");
//                    Elements modItems = mainItems.get(i).select("dd");
//                    for (int j = 0; j < modItems.size(); j++) {
//                        SaleModData saleModData = new SaleModData();
//                        saleModData.set_C_DealerID(dealerId);
//                        saleModData.set_C_FactoryID(fctItem.select("a").attr("href").split("/")[2].replace("f_", "").replace(".html", ""));
//                        saleModData.set_C_ModelID(modItems.get(j).select("a").attr("href").split("/")[2].replace("b_", "").replace(".html", ""));
//                        saleModData.set_C_PriceDataUrl("https://dealer.autohome.com.cn/handler/other/getdata?__action=dealerlq.getdealerspeclist&dealerId=" + saleModData.get_C_DealerID() + "&seriesId=" + saleModData.get_C_ModelID() + "&show0Price=1");
//                        saleModData.set_C_IsFinish(0);
//                        saleModData.set_C_数据来源("html");
//                        saleModData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                        dataArrayList.add(saleModData);
//                    }
//                }
//            }
            dataArrayList.addAll(parseCarPriceFile2(filePath.replace("车型报价页面_html", "车型报价页面")));
            System.out.println(dataArrayList.size());
            HashSet<Object> set = new HashSet<>(dataArrayList);
            System.out.println(set.size());
            dataArrayList.clear();
            dataArrayList.addAll(set);
            System.out.println(dataArrayList.size());
            new PriceDataBase().insert_经销商的车型信息数据入库(dataArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解析车型报价页面 获取该店的在售车型Id 拼接下载地址获取 该店在售车型的车辆信息_2
    public ArrayList<SaleModData> parseCarPriceFile2(String filePath) {
        ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
        ArrayList<SaleModData> dataArrayList = new ArrayList<>();
        for (String fileName : fileList) {
            if (!fileName.equals(".DS_Store")) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                System.out.println(fileName);
                JSONObject jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
//            System.out.println(filePath + fileName + "\t" + jsonRoot);

                JSONArray jsonArray = jsonRoot.getJSONArray("list");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONArray modArray = ((JSONObject) jsonArray.get(i)).getJSONArray("SeriesList");
                    for (int j = 0; j < modArray.size(); j++) {
                        String dealerId = fileName.replace(".txt", "");
                        SaleModData saleModData = new SaleModData();
                        saleModData.set_C_DealerID(dealerId);
                        saleModData.set_C_FactoryID(((JSONObject) jsonArray.get(i)).getString("FactoryId"));
                        saleModData.set_C_ModelID(((JSONObject) modArray.get(j)).getString("SeriesId"));
                        saleModData.set_C_PriceDataUrl("https://dealer.autohome.com.cn/handler/other/getdata?__action=dealerlq.getdealerspeclist&dealerId=" + saleModData.get_C_DealerID() + "&seriesId=" + saleModData.get_C_ModelID() + "&show0Price=1");
                        saleModData.set_C_IsFinish(0);
                        saleModData.set_C_数据来源("接口");
                        saleModData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        dataArrayList.add(saleModData);
                    }
                }
            }
            HashSet<SaleModData> set = new HashSet<>(dataArrayList);
            dataArrayList.clear();
            dataArrayList.addAll(set);
        }
//        new PriceDataBase().saleModData(dataArrayList);
        return dataArrayList;
    }

    //
//    //下载车辆信息数据
    public void getCarDataFile(String filePath) {
        try {
            ArrayList<Object> dataList = priceDataBase.get_未下载的经销商车型数据();
            System.out.println(dataList.size());
            if (dataList.size() < 32) {
                for (Object bean : dataList) {
                    String mainUrl = ((SaleModData) bean).get_C_PriceDataUrl();
                    String fileName = ((SaleModData) bean).get_C_DealerID() + "_" + ((SaleModData) bean).get_C_ModelID() + ".txt";
                    String dealerID = ((SaleModData) bean).get_C_DealerID();
                    String modId = ((SaleModData) bean).get_C_ModelID();
                    T_Config_File.method_访问url获取Json普通版(mainUrl, "UTF-8", filePath, fileName);
//                method_下载车辆信息数据文件(((SaleModData) bean).get_C_PriceDataUrl(), ((SaleModData) bean).get_C_DealerID() + "_" + ((SaleModData) bean).get_C_ModelID() + ".txt", filePath);
                }
            } else {
                List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                        .collect(Collectors.toList());
                for (int i = 0; i < list.size(); i++) {
                    PriceMoreThread priceMoreThread = new PriceMoreThread(list.get(i), filePath);
                    Thread thread = new Thread(priceMoreThread);
                    thread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method_下载车辆信息数据文件(String url, String fileName, String filePath) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mainDoc != null) {
            T_Config_File.method_写文件_根据路径创建文件夹(filePath + "车辆价格信息/", fileName, mainDoc.text());
//            new T_Config_Father(2, 0, 2).updateNoDealerModelStatus(fileName.split("_")[0], fileName.split("_")[1].replace(".txt", ""));
        }
    }

    // 补充未下载的车辆价格信息页面
    public void method_补充未下载的车辆价格信息页面(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataList = new ArrayList<>();
            for (String fileName : fileList) {
                ConfirmCarPriceFile confirmCarPriceFile = new ConfirmCarPriceFile();
                confirmCarPriceFile.set_C_DealerId(fileName.replace(".txt", "").split("_")[0]);
                confirmCarPriceFile.set_C_ModelId(fileName.replace(".txt", "").split("_")[1]);
                confirmCarPriceFile.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                dataList.add(confirmCarPriceFile);
            }
            HashSet<Object> set2 = new HashSet<>(dataList);
            dataList.clear();
            dataList.addAll(set2);
            new PriceDataBase().insert_确认已下载的数据(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseCarPriceData(String filePath) {
        try {
            int num = 0;
            ArrayList<String> fileNameList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataList = new ArrayList<>();
            for (String fileName : fileNameList) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                System.out.println(filePath + fileName);
                JSONArray jsonRoot = null;
                try {
                    jsonRoot = JSONObject.parseObject(content).getJSONArray("result");
                } catch (Exception e) {
                    T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("车辆价格信息/", ""), "解析失败.txt", filePath + fileName + "\n");
                    e.printStackTrace();
                }
                if (jsonRoot != null) {
                    for (int i = 0; i < jsonRoot.size(); i++) {
                        String groupName = ((JSONObject) jsonRoot.get(i)).getString("groupName");
                        JSONArray dataArray = ((JSONObject) jsonRoot.get(i)).getJSONArray("list");
                        num += dataArray.size();
                        for (int j = 0; j < dataArray.size(); j++) {
                            JSONObject jsonObject = ((JSONObject) dataArray.get(j));
                            CarPrice carPrice = new CarPrice();
                            carPrice.set_C_DealerMaxPrice(jsonObject.getString("dealerMaxPrice"));
                            carPrice.set_C_DealerMinPrice(jsonObject.getString("dealerMinPrice"));
                            carPrice.set_C_FctMaxPrice(jsonObject.getString("fctMaxPrice"));
                            carPrice.set_C_FctMinPrice(jsonObject.getString("fctMinPrice"));
                            carPrice.set_C_NewsPrice(jsonObject.getString("newsPrice"));
                            carPrice.set_C_GroupName(groupName);
                            carPrice.set_C_NewsID(jsonObject.getString("newsId"));
                            carPrice.set_C_PriceTime(jsonObject.getString("priceTime"));
                            carPrice.set_C_DealerID(jsonObject.getString("dealerId"));
                            carPrice.set_C_ImageUrl(jsonObject.getString("imageUrl"));
                            carPrice.set_C_PromotionType(jsonObject.getString("promotionType"));
                            carPrice.set_C_SaleState(jsonObject.getString("saleState"));
                            carPrice.set_C_ModelName(jsonObject.getString("seriesName"));
                            carPrice.set_C_ModelID(jsonObject.getString("seriesId"));
                            carPrice.set_C_VersionID(jsonObject.getString("specId"));
                            carPrice.set_C_VersionName(jsonObject.getString("specName"));
                            carPrice.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            dataList.add(carPrice);
                        }
                    }
                }

            }
            System.out.println("总数\t" + num);
            System.out.println(dataList.size());
            HashSet<Object> set = new HashSet<>(dataList);
            dataList.clear();
            dataList.addAll(set);
            System.out.println(dataList.size());
            priceDataBase.insert_车辆的价格信息数据入库(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
