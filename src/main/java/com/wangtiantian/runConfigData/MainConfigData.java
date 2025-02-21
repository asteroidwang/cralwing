package com.wangtiantian.runConfigData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_ZiDuan_Params_Config;
import com.wangtiantian.entity.*;
import com.wangtiantian.entity.configData.Bean_VersionIds;
import com.wangtiantian.mapper.DataBaseMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainConfigData extends TimerTask {
    @Override
    public void run() {
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        YearMonth currentYearMonth = YearMonth.now();
        int totalDay = currentYearMonth.lengthOfMonth();
        if (day == 10 || day == 20 || day == 30 || (totalDay < 30 && totalDay == day)) {
            MainConfigData mainConfigData = new MainConfigData();
            String currentTime = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String filePath = "/Users/wangtiantian/MyDisk/汽车之家/配置数据/" + currentTime + "/";
            if (T_Config_File.method_判断文件是否存在(filePath+"config_ColumnName.txt")){
                System.out.println("今日任务已完成");
            }else {
                // 创建表
                System.out.println(new Date() + "\t数据爬取中");
                mainConfigData.method_创建所有爬取汽车之家配置数据需要的表(currentTime);
                mainConfigData.method_下载品牌厂商车型数据(filePath + "初始数据/");
                mainConfigData.parse_品牌厂商车型数据(filePath + "初始数据/");
                if (mainConfigData.method_下载含有版本数据的文件(filePath + "含版本数据的文件")) {
                    mainConfigData.parse_解析含有版本数据的文件(filePath + "含版本数据的文件");
                }
                mainConfigData.method_下载配置数据(filePath + "params/");
                mainConfigData.method_解析列名(filePath);
                mainConfigData.method_创建所有爬取汽车之家配置数据表(currentTime, filePath);
                mainConfigData.method_解析配置数据(filePath);
                System.out.println(new Date() + "\t数据完成");
            }

        } else {
            System.out.println("现在是" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "还没到时间哦～");
        }

    }
    // 创建爬取汽车之家配置数据所需要的表
    public void method_创建所有爬取汽车之家配置数据需要的表(String currentTime) {
        // 获取当前时间
        new DataBaseMethod().method_创建爬取汽车之家配置数据所需要的表(currentTime);
    }

    public void method_创建所有爬取汽车之家配置数据表(String currentTime, String filePath) {
        // 获取当前时间
        String columns = method_取列名(filePath);
        String paramSql = columns.split(",__")[0];
        String configSql = columns.split(",__")[1];
        new DataBaseMethod().method_创建爬取汽车之家配置数据表(currentTime, paramSql, configSql);
    }

    // 1.下载品牌厂商车型数据
    public void method_下载品牌厂商车型数据(String filePath) {
        try {
            System.out.println("download ing");
            for (char a = 'A'; a <= 'Z'; a++) {
                String mainUrl = "https://www.autohome.com.cn/grade/carhtml/" + a + ".html";
                T_Config_File.method_访问url获取网页源码普通版(mainUrl, "UTF-8", filePath, a + ".txt");
            }
            System.out.println("download ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2.解析品牌厂商车型数据
    public void parse_品牌厂商车型数据(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> brandData = new ArrayList<>();
            ArrayList<Object> fctData = new ArrayList<>();
            ArrayList<Object> modData = new ArrayList<>();
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(content);
                Elements brandItems = mainDoc.select("dl");
                for (int i = 0; i < brandItems.size(); i++) {
                    String brandID = brandItems.get(i).attr("id");
                    Elements brandItem = brandItems.get(i).select("dt");
                    Elements fctItems = brandItems.get(i).select(".h3-tit");
                    int fctnumber = fctItems.select("a").size();
                    String brandName = brandItem.select("div").select("a").text();
                    String brandURL = "https:" + brandItem.select("div").select("a").attr("href");
                    String brandLogo = "https:" + brandItem.select("img").attr("src");
                    Bean_Brand brand = new Bean_Brand();
                    brand.set_C_BrandLogo(brandLogo);
                    brand.set_C_BrandID(brandID);
                    brand.set_C_BrandName(brandName);
                    brand.set_C_BrandURL(brandURL);
                    brand.set_C_FactoryNumber(fctnumber);
                    brand.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    brandData.add(brand);
                    for (int ii = 0; ii < fctItems.size(); ii++) {
                        Elements modItems = brandItems.get(i).select("ul").get(ii).select("li");
                        String fctName = fctItems.get(ii).select("a").text();
                        String fctURL = "https:" + fctItems.get(ii).select("a").attr("href");
                        String fctID_temp = fctURL.substring(fctURL.indexOf("-"));
                        String fctID_String = fctID_temp.substring(fctID_temp.indexOf("."));
                        String fctID = fctID_temp.replace(fctID_String, "");
                        String temp = fctID.substring(1);
                        String temp1 = temp.substring(temp.indexOf("-")).replace("-", "");
                        int number = 0;
                        for (int iii = 0; iii < modItems.size(); iii++) {
                            if (!modItems.get(iii).toString().equals("<li class=\"dashline\"></li>")) {
                                number++;
                                String modTemp = modItems.get(iii).toString();
                                String status_mod_temp = modItems.get(iii).select(".red").toString();
                                String modStatus = "在售";
                                if (status_mod_temp.equals("")) {
                                    modStatus = "停售";
                                }
                                String modID = modItems.get(iii).attr("id").replace("s", "");
                                String modName = modItems.get(iii).select("h4").select("a").text();
                                String modURL = "https:" + modItems.get(iii).select("h4").select("a").attr("href");
                                Bean_Model model = new Bean_Model();
                                model.set_C_BrandID(brandID);
                                model.set_C_FactoryID(temp1);
                                model.set_C_ModelName(modName);
                                model.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                model.set_C_ModelID(modID);
                                model.set_C_ModelURL(modURL);
                                model.set_C_停售(0);
                                model.set_C_即将销售(0);
                                model.set_C_图片页面停售(0);
                                model.set_C_图片页面在售(0);
                                model.set_C_在售(0);
                                modData.add(model);
                            }
                        }
                        Bean_Factory factory = new Bean_Factory();
                        factory.set_modNumber(number);
                        factory.set_C_FactoryName(fctName);
                        factory.set_C_FactoryURL(fctURL);
                        factory.set_C_BrandID(brandID);
                        factory.set_C_FactoryID(temp1);
                        factory.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        fctData.add(factory);
                    }
                }
            }
            HashSet<Object> brandSet = new HashSet<>(brandData);
            brandData.clear();
            brandData.addAll(brandSet);

            HashSet<Object> factorySet = new HashSet<>(fctData);
            fctData.clear();
            fctData.addAll(factorySet);

            HashSet<Object> modelSet = new HashSet<>(modData);
            modData.clear();
            modData.addAll(modelSet);

            new DataBaseMethod().method_入库品牌数据(brandData);
            new DataBaseMethod().method_入库厂商数据(fctData);
            new DataBaseMethod().method_入库车型数据(modData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3.下载版本所在的页面数据
    public Boolean method_下载含有版本数据的文件(String filePath) {
        // 1.非图片路径
        System.out.println("download ing");
        method_下载版本源文件_在售数据_新(filePath + "_非图片路径/");
        method_下载含有版本数据的文件_非图片路径_NOSALE(filePath + "_非图片路径/");
        method_下载版本源文件_即将销售数据(filePath + "_非图片路径/");
        method_下载含有版本数据的文件_图片路径_OnSale(filePath + "_图片路径/");
        method_下载含有版本数据的文件_图片路径_NoSale(filePath + "_图片路径/");
        System.out.println("download ok");
        return true;
    }

    public void method_下载版本源文件_在售数据_新(String filePath) {
        try {
            // https://car-web-api.autohome.com.cn/car/series/getspeclistresponse?seriesid=18&tagid=2&tagname=%E5%8D%B3%E5%B0%86%E9%94%80%E5%94%AE
            ArrayList<Object> onSaleList = new DataBaseMethod().method_查找车型表中未下载的含版本数据的数据("在售", 0);
            if (onSaleList.size() < 32) {
                for (Object bean : onSaleList) {
                    String modId = ((Bean_Model) bean).get_C_ModelID();
                    String url = "https://car-web-api.autohome.com.cn/car/series/getspeclistresponse?seriesid=" + modId + "&tagid=1&tagname=%E5%9C%A8%E5%94%AE";
                    if (T_Config_File.method_访问url获取Json普通版(url, "UTF-8", filePath, modId + "_在售.txt")) {
                        new DataBaseMethod().method_修改车型表中下载的id状态(modId, "在售", 0);
                    }
                }
            } else {
                List<List<Object>> saList = IntStream.range(0, 6).mapToObj(i -> onSaleList.subList(i * (onSaleList.size() + 5) / 6, Math.min((i + 1) * (onSaleList.size() + 5) / 6, onSaleList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(saList.size());
                for (int i = 0; i < saList.size(); i++) {
                    ModelMoreThread modelMoreThread = new ModelMoreThread(saList.get(i), filePath, 0);
                    Thread thread = new Thread(() -> {
                        try {
                            modelMoreThread.run();
                        } finally {
                            latch.countDown();
                        }
                    });
                    thread.start();
                }
                latch.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void method_下载版本源文件_即将销售数据(String filePath) {
        try {
            // 2.停售
            ArrayList<Object> noSaleList = new DataBaseMethod().method_查找车型表中未下载的含版本数据的数据("即将销售", 0);
            if (noSaleList.size() < 32) {
                for (Object bean : noSaleList) {
                    String modId = ((Bean_Model) bean).get_C_ModelID();
                    String url = "https://car-web-api.autohome.com.cn/car/series/getspeclistresponse?seriesid=" + modId + "&tagid=2&tagname=%E5%8D%B3%E5%B0%86%E9%94%80%E5%94%AE";

                    if (T_Config_File.method_访问url获取Json普通版(url, "UTF-8", filePath, modId + "_即将销售.txt")) {
                        new DataBaseMethod().method_修改车型表中下载的id状态(modId, "即将销售", 4);
                    }
                }
            } else {
                List<List<Object>> noSalist = IntStream.range(0, 6).mapToObj(i -> noSaleList.subList(i * (noSaleList.size() + 5) / 6, Math.min((i + 1) * (noSaleList.size() + 5) / 6, noSaleList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(noSalist.size());
                for (int i = 0; i < noSalist.size(); i++) {
                    ModelMoreThread modelMoreThread = new ModelMoreThread(noSalist.get(i), filePath, 4);
                    Thread thread = new Thread(() -> {
                        try {
                            modelMoreThread.run();
                        } finally {
                            latch.countDown();
                        }

                    });
                    thread.start();
                }
                latch.await();
            }
        } catch (Exception e) {
        }
    }

    public void method_下载含有版本数据的文件_非图片路径_ONSALE(String filePath) {
        try {
            // 1.在售
            ArrayList<Object> onSaleList = new DataBaseMethod().method_查找车型表中未下载的含版本数据的数据("在售", 0);
            if (onSaleList.size() < 32) {
                for (Object bean : onSaleList) {
                    String modId = ((Bean_Model) bean).get_C_ModelID();
                    String url = "";

                    url = "https://www.autohome.com.cn/" + modId + "/#pvareaid=3454427";
                    if (T_Config_File.method_访问url获取网页源码普通版(url, "gb2312", filePath, modId + "_在售.txt")) {
                        new DataBaseMethod().method_修改车型表中下载的id状态(modId, "在售", 1);
                    }
                }
            } else {
                List<List<Object>> saList = IntStream.range(0, 6).mapToObj(i -> onSaleList.subList(i * (onSaleList.size() + 5) / 6, Math.min((i + 1) * (onSaleList.size() + 5) / 6, onSaleList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(saList.size());
                for (int i = 0; i < saList.size(); i++) {
                    ModelMoreThread modelMoreThread = new ModelMoreThread(saList.get(i), filePath, 0);
                    Thread thread = new Thread(() -> {
                        try {
                            modelMoreThread.run();
                        } finally {
                            latch.countDown();
                        }
                    });
                    thread.start();
                }
                latch.await();
            }
        } catch (Exception e) {
        }
    }

    public void method_下载含有版本数据的文件_非图片路径_NOSALE(String filePath) {
        try {
            // 2.停售
            ArrayList<Object> noSaleList = new DataBaseMethod().method_查找车型表中未下载的含版本数据的数据("停售", 0);
            if (noSaleList.size() < 32) {
                for (Object bean : noSaleList) {
                    String modId = ((Bean_Model) bean).get_C_ModelID();
                    String url = "";
                    url = "https://www.autohome.com.cn/" + modId + "/sale.html#pvareaid=3311673";
                    if (T_Config_File.method_访问url获取网页源码普通版(url, "gb2312", filePath, modId + "_停售.txt")) {
                        new DataBaseMethod().method_修改车型表中下载的id状态(modId, "停售", 1);
                    }
                }
            } else {
                List<List<Object>> noSalist = IntStream.range(0, 6).mapToObj(i -> noSaleList.subList(i * (noSaleList.size() + 5) / 6, Math.min((i + 1) * (noSaleList.size() + 5) / 6, noSaleList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(noSalist.size());
                for (int i = 0; i < noSalist.size(); i++) {
                    ModelMoreThread modelMoreThread = new ModelMoreThread(noSalist.get(i), filePath, 1);
                    Thread thread = new Thread(() -> {
                        try {
                            modelMoreThread.run();
                        } finally {
                            latch.countDown();
                        }

                    });
                    thread.start();
                }
                latch.await();
            }
        } catch (Exception e) {
        }
    }

    public void method_下载含有版本数据的文件_图片路径_OnSale(String filePath) {
        try {
            // 1.在售
            ArrayList<Object> onSaleList = new DataBaseMethod().method_查找车型表中未下载的含版本数据的数据("图片页面在售", 0);
            if (onSaleList.size() < 32) {
                for (Object bean : onSaleList) {
                    String modId = ((Bean_Model) bean).get_C_ModelID();
                    String url = "";

                    url = "https://car.autohome.com.cn/pic/series/" + modId + ".html#pvareaid=3454438";
                    if (T_Config_File.method_访问url获取网页源码普通版(url, "gb2312", filePath, modId + "_图片页面在售.txt")) {
                        new DataBaseMethod().method_修改车型表中下载的id状态(modId, "图片页面在售", 2);
                    }
                }
            } else {
                List<List<Object>> salist = IntStream.range(0, 6).mapToObj(i -> onSaleList.subList(i * (onSaleList.size() + 5) / 6, Math.min((i + 1) * (onSaleList.size() + 5) / 6, onSaleList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(salist.size());
                for (int i = 0; i < salist.size(); i++) {
                    ModelMoreThread modelMoreThread = new ModelMoreThread(salist.get(i), filePath, 2);
                    Thread thread = new Thread(() -> {
                        try {
                            modelMoreThread.run();
                        } finally {
                            latch.countDown();
                        }
                    });
                    thread.start();
                }
                latch.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method_下载含有版本数据的文件_图片路径_NoSale(String filePath) {
        try {
            // 2.停售
            ArrayList<Object> noSaleList = new DataBaseMethod().method_查找车型表中未下载的含版本数据的数据("图片页面停售", 0);
            if (noSaleList.size() < 32) {
                for (Object bean : noSaleList) {
                    String modId = ((Bean_Model) bean).get_C_ModelID();
                    String url = "";

                    url = "https://car.autohome.com.cn/pic/series-t/" + modId + ".html";
                    if (T_Config_File.method_访问url获取网页源码普通版(url, "gb2312", filePath, modId + "_图片页面停售.txt")) {
                        new DataBaseMethod().method_修改车型表中下载的id状态(modId, "图片页面停售", 3);
                    }
                }
            } else {
                List<List<Object>> noSalist = IntStream.range(0, 6).mapToObj(i -> noSaleList.subList(i * (noSaleList.size() + 5) / 6, Math.min((i + 1) * (noSaleList.size() + 5) / 6, noSaleList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(noSalist.size());
                for (int i = 0; i < noSalist.size(); i++) {
                    ModelMoreThread modelMoreThread = new ModelMoreThread(noSalist.get(i), filePath, 3);
                    Thread thread = new Thread(() -> {
                        try {
                            modelMoreThread.run();
                        } finally {
                            latch.countDown();
                        }

                    });
                    thread.start();
                }
                latch.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4.解析版本数据并入库
    public void parse_解析含有版本数据的文件(String filePath) {
        try {
            ArrayList<Object> versionData = new ArrayList<>();
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + "_非图片路径/");
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + "_非图片路径/" + fileName);
                if (fileName.contains("停售")) {
                    versionData.addAll(method_解析非图片路径_停售(content, fileName.replace("_停售.txt", "")));
                } else if (fileName.contains("在售")) {
                    versionData.addAll(method_解析非图片路径_在售_New(content, fileName.replace("_在售.txt", ""), "在售"));
                } else {
                    versionData.addAll(method_解析非图片路径_在售_New(content, fileName.replace("_即将销售.txt", ""), "即将销售"));
                }
            }
            ArrayList<String> fileListPic = T_Config_File.method_获取文件名称(filePath + "_图片路径/");
            for (String fileName : fileListPic) {
                String content = T_Config_File.method_读取文件内容(filePath + "_图片路径/" + fileName);
                versionData.addAll(method_解析图片路径(content, fileName.replace("_图片页面停售.txt", "").replace("_图片页面在售.txt", "")));
            }

            HashSet<Object> set = new HashSet<>(versionData);
            versionData.clear();
            versionData.addAll(set);
            new DataBaseMethod().method_入库版本数据(versionData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Bean_Version> method_解析非图片路径_停售(String content, String modID) {
        ArrayList<Bean_Version> result = new ArrayList<>();
        try {
            String versionID = "";
            String versionName = "";
            String versionURL = "";
            String updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Document mainDoc = Jsoup.parse(content);
            Elements stopmainItem = mainDoc.select(".modtab1").select(".name").select("a");
            for (int ii = 0; ii < stopmainItem.size(); ii++) {
                versionName = stopmainItem.get(ii).text();
                versionURL = "https:" + stopmainItem.get(ii).attr("href");
                versionID = versionURL.replace("https://www.autohome.com.cn/spec/", "").replace("/", "");
                Bean_Version version = new Bean_Version();
                version.set_C_VersionID(versionID);
                version.set_C_VersionName(versionName);
                version.set_C_VersionURL(versionURL);
                version.set_C_VersionStatus("停售");
                version.set_C_ModelID(modID);
                version.set_C_UpdateTime(updateTime);
                version.set_bag(0);
                version.set_config(0);
                version.set_params(0);
                result.add(version);
            }
            Elements stopmainItem2 = mainDoc.select(".modtab2").select(".name").select("a");
            for (int ii = 0; ii < stopmainItem2.size(); ii++) {
                versionName = stopmainItem2.get(ii).text();
                versionURL = "https:" + stopmainItem2.get(ii).attr("href");
                versionID = versionURL.replace("https://www.autohome.com.cn/spec/", "").replace("/", "");
                Bean_Version version = new Bean_Version();
                version.set_C_VersionID(versionID);
                version.set_C_VersionName(versionName);
                version.set_C_VersionURL(versionURL);
                version.set_C_VersionStatus("停售");
                version.set_C_ModelID(modID);
                version.set_C_UpdateTime(updateTime);
                version.set_bag(0);
                version.set_config(0);
                version.set_params(0);
                result.add(version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Bean_Version> method_解析非图片路径_在售_New(String content, String modID, String status) {
        ArrayList<Bean_Version> result = new ArrayList<>();
        try {
            JSONObject jsonObject = JSON.parseObject(content).getJSONObject("result");
            if (jsonObject != null) {
                JSONArray specList = jsonObject.getJSONObject("specinfo").getJSONArray("speclist");
                for (int i = 0; i < specList.size(); i++) {
                    JSONArray specListItems = ((JSONObject) specList.get(i)).getJSONArray("yearspeclist");
                    for (int j = 0; j < specListItems.size(); j++) {
                        JSONArray jsonArray = ((JSONObject) specListItems.get(j)).getJSONArray("speclist");
                        for (int k = 0; k < jsonArray.size(); k++) {
                            JSONObject carInfo = ((JSONObject) jsonArray.get(k));
                            String versionId = carInfo.getString("id");
                            String versionName = carInfo.getString("name");
                            String versionUlr = "https://www.autohome.com.cn/spec/" + versionId + "#pvareaid=3454492";
                            Bean_Version version = new Bean_Version();
                            version.set_C_VersionID(versionId);
                            version.set_C_VersionName(versionName);
                            version.set_C_VersionURL(versionUlr);
                            version.set_C_VersionStatus(status);
                            version.set_C_ModelID(modID);
                            version.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            version.set_bag(0);
                            version.set_config(0);
                            version.set_params(0);
                            result.add(version);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Bean_Version> method_解析非图片路径_在售(String content, String modID) {
        ArrayList<Bean_Version> result = new ArrayList<>();
        try {
            Document mainDoc = Jsoup.parse(content);
            Elements willSaleItems = mainDoc.select("#specWrap-1").select(".name-param");
            Elements onSaleItems = mainDoc.select("#specWrap-2").select(".name-param");
            String versionStatus = "在售";
            for (int i = 0; i < willSaleItems.size(); i++) {
                versionStatus = "即将销售";
                String tempWillId = mainDoc.select("#navTop").select("li").get(2).select("a").attr("href");
                String verIDTemp_left = tempWillId.substring(tempWillId.indexOf("series/"));
                String verIDTempRight = tempWillId.substring(tempWillId.indexOf(".html"));
                String verIDTemp = tempWillId.substring(tempWillId.indexOf("series/"));
                String verIDLeft = verIDTemp.substring(verIDTemp.indexOf("#"));
                String verID = verIDTemp_left.replace(verIDTempRight, "");
                String verName = willSaleItems.get(i).select(".name").text();
                String verURL = "https://www.autohome.com.cn" + willSaleItems.get(i).select(".name").attr("href");
                Document verDoc = Jsoup.parse(new URL(verURL).openStream(), "GBK", verURL);
                Elements verEl = verDoc.select(".athm-sub-nav__channel.athm-js-sticky").select("li");
                String verIDNumber_L = verEl.get(0).select("a").attr("href").replace("/spec/", "");
                String verIDNumber_R = verIDNumber_L.substring(verIDNumber_L.indexOf("/"));
                String verIDNumber = verIDNumber_L.replace(verIDNumber_R, "");
                Bean_Version version = new Bean_Version();
                version.set_C_VersionID(verIDNumber);
                version.set_C_VersionName(verName);
                version.set_C_VersionURL(verURL);
                version.set_C_VersionStatus(versionStatus);
                version.set_C_ModelID(modID);
                version.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                version.set_bag(0);
                version.set_config(0);
                version.set_params(0);
                result.add(version);
            }
            for (int i = 0; i < onSaleItems.size(); i++) {
                String verID = onSaleItems.get(i).select("p").attr("id").replace("spec_", "");
                String verName = onSaleItems.get(i).select(".name").text();
                String verURL = "https://www.autohome.com.cn" + onSaleItems.get(i).select(".name").attr("href");
                Bean_Version version = new Bean_Version();
                version.set_C_VersionID(verID);
                version.set_C_VersionName(verName);
                version.set_C_VersionURL(verURL);
                version.set_C_VersionStatus(versionStatus);
                version.set_C_ModelID(modID);
                version.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                version.set_bag(0);
                version.set_config(0);
                version.set_params(0);
                result.add(version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Bean_Version> method_解析图片路径(String content, String modID) {
        ArrayList<Bean_Version> result = new ArrayList<>();
        try {
            Document mainDoc = Jsoup.parse(content);
            Elements mainItems = mainDoc.select(".search-pic-cardl").select("dd");
            for (int ii = 0; ii < mainItems.size(); ii++) {
                String year = mainDoc.select(".search-pic-cardl").select("dt").get(ii).text();
                for (int iii = 0; iii < mainItems.get(ii).select("li").size(); iii++) {
                    String verName = mainItems.get(ii).select("li").get(iii).select("a").text().replace(mainItems.get(ii).select("li").get(iii).select("span").text(), "");
                    String hrefURL = mainItems.get(ii).select("li").get(iii).select("a").attr("href").replace("/pic/series-s", "").replace(".html#pvareaid=2042220", "");
                    String verID_temp = hrefURL.substring(hrefURL.indexOf("/"));
                    String verID = hrefURL.replace(verID_temp, "");
                    String verURL = "https://www.autohome.com.cn/spec/" + verID;
                    String verStatusTemp = mainItems.get(ii).select("li").get(iii).select("a").select("i").toString();
                    String verStatus = "在售";
                    if (verStatusTemp.indexOf("class=\"icon icon-stopsale\">") > -1) {
                        verStatus = "停售";
                    } else if (verStatusTemp.indexOf("class=\"icon icon-wseason\"") > -1) {
                        verStatus = "未上市";
                    }
                    Bean_Version beanVersion = new Bean_Version();
                    beanVersion.set_C_VersionName(year + " " + verName);
                    beanVersion.set_C_VersionID(verID);
                    beanVersion.set_C_VersionStatus(verStatus);
                    beanVersion.set_C_VersionURL(verURL);
                    beanVersion.set_bag(0);
                    beanVersion.set_config(0);
                    beanVersion.set_params(0);
                    beanVersion.set_C_ModelID(modID);
                    beanVersion.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    result.add(beanVersion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 5.版本ids入库 下载配置数据
    public void method_下载配置数据(String filePath) {
        try {
            System.out.println("download ing");
            if (!T_Config_File.method_判断文件是否存在(filePath)) {
                new DataBaseMethod().insert_批量插入版本ids();
            }
            ArrayList<Object> paramsList = new DataBaseMethod().method_根据数据类型获取未下载的数据("params", 0);
            ArrayList<Object> configList = new DataBaseMethod().method_根据数据类型获取未下载的数据("config", 0);
            ArrayList<Object> bagList = new DataBaseMethod().method_根据数据类型获取未下载的数据("bag", 0);

            if (paramsList.size() <= 36) {
                for (Object o : paramsList) {
                    String ids = ((Bean_VersionIds) o).get_C_Ids();
                    int group = ((Bean_VersionIds) o).get_C_Group();
                    if (T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/v3/Param_ListBySpecIdList.ashx?speclist=" + ids + "&_appid=test&_=1723037336504&_callback=__param1", "gb2312", filePath, group + "_params.txt")) {
                        new DataBaseMethod().update_修改车辆配置的数据文件下载状态(group, "params", 1);
                    }
                }
            } else {
                List<List<Object>> paramsData = IntStream.range(0, 6).mapToObj(i -> paramsList.subList(i * (paramsList.size() + 5) / 6, Math.min((i + 1) * (paramsList.size() + 5) / 6, paramsList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latchParams = new CountDownLatch(paramsData.size());
                for (int i = 0; i < paramsData.size(); i++) {
                    ConfigMoreThread params = new ConfigMoreThread(paramsData.get(i), filePath, 0, latchParams);
                    Thread thread = new Thread(params);
                    thread.start();
                }
                latchParams.await();
            }
            if (configList.size() <= 32) {
                for (Object o : configList) {
                    String ids = ((Bean_VersionIds) o).get_C_Ids();
                    int group = ((Bean_VersionIds) o).get_C_Group();
                    if (T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + ids + "&_=1723037336505&_callback=__config3", "gb2312", filePath.replace("params", "config"), group + "_config.txt")) {
                        new DataBaseMethod().update_修改车辆配置的数据文件下载状态(group, "config", 1);
                    }
                }
            } else {
                List<List<Object>> configData = IntStream.range(0, 6).mapToObj(i -> configList.subList(i * (configList.size() + 5) / 6, Math.min((i + 1) * (configList.size() + 5) / 6, configList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latchConfig = new CountDownLatch(configData.size());
                for (int i = 0; i < configData.size(); i++) {
                    ConfigMoreThread config = new ConfigMoreThread(configData.get(i), filePath.replace("params", "config"), 1, latchConfig);
                    Thread thread = new Thread(config);
                    thread.start();
                }
                latchConfig.await();
            }
            if (bagList.size() <= 36) {
                for (Object o : bagList) {
                    String ids = ((Bean_VersionIds) o).get_C_Ids();
                    int group = ((Bean_VersionIds) o).get_C_Group();
                    if (T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/Config_BagBySpecIdListV2.ashx?speclist=" + ids + "&_=1723037336505&_callback=__bag4", "gb2312", filePath.replace("params", "bag"), group + "_bag.txt")) {
                        new DataBaseMethod().update_修改车辆配置的数据文件下载状态(group, "bag", 1);
                    }
                }
            } else {
                List<List<Object>> bagData = IntStream.range(0, 6).mapToObj(i -> bagList.subList(i * (bagList.size() + 5) / 6, Math.min((i + 1) * (bagList.size() + 5) / 6, bagList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latchBag = new CountDownLatch(bagData.size());
                for (int i = 0; i < bagData.size(); i++) {
                    ConfigMoreThread bag = new ConfigMoreThread(bagData.get(i), filePath.replace("params", "bag"), 2, latchBag);
                    Thread thread = new Thread(bag);
                    thread.start();
                }
                latchBag.await();
            }
            while (new DataBaseMethod().method_根据数据类型获取未下载的数据("params", 0).size() > 0 || new DataBaseMethod().method_根据数据类型获取未下载的数据("config", 0).size() > 0 || new DataBaseMethod().method_根据数据类型获取未下载的数据("bag", 0).size() > 0) {
                method_下载配置数据(filePath);
            }
            System.out.println("download ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解析配置数据获取列名
    public void method_解析列名(String filePath) {
//        ArrayList<Object> groupList = new DataBaseMethod().method_根据数据类型获取未下载的数据("params", 1);
        int groupCount = new DataBaseMethod().get_版本表中组数();
        System.out.println(groupCount);
        for (int i = 1; i < groupCount; i++) {
            String paramsContent = T_Config_File.method_读取文件内容(filePath + "params/" + i + "_params.txt");
            String configContent = T_Config_File.method_读取文件内容(filePath + "config/" + i + "_config.txt");
            method_解析params_列名(paramsContent.substring(9, paramsContent.length() - 1), filePath, i);
            method_解析config_列名(configContent.substring(10, configContent.length() - 1), filePath, i);
        }
    }

    public static String method_取列名(String filePath) {
        String sqlContent = "";
        try {
            ArrayList<String> columnNames_params = T_Config_File.method_按行读取文件(filePath + "/params_ColumnName.txt");
            ArrayList<String> columnNames_config = T_Config_File.method_按行读取文件(filePath + "/config_ColumnName.txt");
            LinkedHashSet<String> hashSet_params = new LinkedHashSet<>(columnNames_params);
            LinkedHashSet<String> hashSet_config = new LinkedHashSet<>(columnNames_config);
            ArrayList<String> lost_params = new ArrayList<>(hashSet_params);
            ArrayList<String> lost_config = new ArrayList<>(hashSet_config);
            ArrayList<String> paramsList = new ArrayList<>();
            ArrayList<String> paramsMapList = new ArrayList<>();
            ArrayList<String> configList = new ArrayList<>();
            ArrayList<String> configMapList = new ArrayList<>();

            for (String paramsString : lost_params) {
                String params_原来 = paramsString;
                String params_替换 = paramsString.replace("/", "_").replace("-", "_").replace("[", "_").replace("]", "_").replace(" ", "_").replace(".", "_").replace("（", "_").replace("）", "_").replace("*", "_").replace("·", "_").replace("(", "_").replace(")", "").replace("%", "_").replace("°", "_").replace("・", "_");
                for (int i = 0; i < 6; i++) {
                    if (params_替换.endsWith("_")) {
                        params_替换 = params_替换.substring(0, params_替换.length() - 1);
                    }
                }
                paramsList.add("C_" + params_替换);
                paramsMapList.add("paramsMapList.put(\"" + params_原来 + "\", \"C_" + params_替换 + "\");");
            }
            for (String configString : lost_config) {
                String config_原来 = configString;
                String config_替换 = configString.replace("/", "_").replace("-", "_").replace("[", "_").replace("]", "_").replace(" ", "_").replace(".", "_").replace("（", "_").replace("）", "_").replace("*", "_").replace("·", "_").replace("(", "_").replace(")", "_").replace("%", "_").replace("°", "_").replace("・", "_");
                for (int i = 0; i < 5; i++) {
                    if (config_替换.endsWith("_")) {
                        config_替换 = config_替换.substring(0, config_替换.length() - 1);
                    }
                }
                configList.add("C_" + config_替换);
                configMapList.add("configMapList.put(\"" + config_原来 + "\", \"" + "C_" + config_替换 + "\");");
            }
            LinkedHashSet<String> paramsAll = new LinkedHashSet<>(paramsList);
            ArrayList<String> paramsFinal = new ArrayList<>(paramsAll);
            LinkedHashSet<String> configAll = new LinkedHashSet<>(configList);
            ArrayList<String> configFinal = new ArrayList<>(configAll);

            LinkedHashSet<String> paramsMapListAll = new LinkedHashSet<>(paramsMapList);
            ArrayList<String> paramsMapListFinal = new ArrayList<>(paramsMapListAll);
            LinkedHashSet<String> configMapListAll = new LinkedHashSet<>(configMapList);
            ArrayList<String> configMapListFinal = new ArrayList<>(configMapListAll);

            method_修改Bean_ParamsAndConfig(paramsFinal, "Bean_Params");
            method_修改Bean_ParamsAndConfig(configFinal, "Bean_Config");
            method_修改ZiDuan_Prams_Config(paramsMapListFinal, configMapListFinal);
            sqlContent = method_Param_Config_SQl(paramsFinal, configFinal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlContent;
    }

    public static String method_Param_Config_SQl(ArrayList<String> paramsMapList, ArrayList<String> configMapList) {
        StringBuffer stringBufferConfig = new StringBuffer();
        for (String param : paramsMapList) {
            stringBufferConfig.append(param).append(" nvarchar(1000),");
        }

        stringBufferConfig.append("__");
        for (String config : configMapList) {
            stringBufferConfig.append(config).append(" nvarchar(1000),");
        }
        stringBufferConfig.append("__");
        return stringBufferConfig.toString();
    }

    // 修改Bean_Params
    public static void method_修改Bean_ParamsAndConfig(ArrayList<String> configList, String fileName) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String config : configList) {
            String test = "    private String  " + config + "; public void set_" + config + "(String " + config + "){this." + config + "=" + config + ".replace(\"\\n\",\"\").replace(\"\\r\",\"\").replace(\"\\t\",\"\").trim();}public String get_" + config + "(){return " + config + ";}";
            stringBuffer.append(test).append("\n");
        }
//          String beanConfig = T_Config_File.method_读取文件内容("src/main/java/com/wangtiantian/entity/" + fileName + ".java");
//        String tempString = beanConfig.substring(beanConfig.indexOf("return C_PID;") + "return C_PID;".length() + 1, beanConfig.indexOf("    private String  C_UpdateTime;"));
//        beanConfig = beanConfig.replace(tempString, stringBuffer.toString());
        StringBuffer beanConfig = new StringBuffer();
        beanConfig.append("package com.wangtiantian.entity;\n" +
                "\n" +
                "import java.text.SimpleDateFormat;\n" +
                "import java.util.Date;\n" +
                "\n" +
                "public class "+fileName+" {\n" +
                "    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}\n" +
                "    private String  C_PID;public void set_C_PID(String C_PID){this.C_PID=C_PID;}public String  get_C_PID(){return C_PID;}\n");
        beanConfig.append(stringBuffer.toString());
        beanConfig.append("    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(new Date());}\n" +
                "}");
//        System.out.println(beanConfig);
        T_Config_File.method_写文件("src/main/java/com/wangtiantian/entity/" + fileName + ".java", beanConfig.toString());
    }

    public static void method_修改ZiDuan_Prams_Config(ArrayList<String> paramsMapList, ArrayList<String> configMapList) {
        StringBuffer stringBufferConfig = new StringBuffer();
        stringBufferConfig.append("package com.wangtiantian.dao;\n" +
                "\n" +
                "import java.text.SimpleDateFormat;\n" +
                "import java.util.Date;\n" +
                "import java.util.HashMap;\n" +
                "\n" +
                "public class T_ZiDuan_Params_Config {\n" +
                "    public static HashMap<String, String> params_字段() {\n" +
                "        HashMap<String, String> paramsMapList = new HashMap<>();");
        for (String param : paramsMapList) {
            stringBufferConfig.append("        ").append(param).append("\n");
        }
        stringBufferConfig.append("        paramsMapList.put(\"C_UpdateTime\",new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(new Date()));\n" +
                "        return paramsMapList;\n" +
                "    }\n" +
                "\n" +
                "    public static HashMap<String, String> config_字段() {\n" +
                "        HashMap<String, String> configMapList = new HashMap<>();");
        for (String config : configMapList) {
            stringBufferConfig.append("        ").append(config).append("\n");
        }
        stringBufferConfig.append("        configMapList.put(\"C_UpdateTime\",new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(new Date()));\n" +
                "        return configMapList;\n" +
                "    }\n" +
                "}");

//        String beanConfig = T_Config_File.method_读取文件内容("src/main/java/com/wangtiantian/entity/" + fileName + ".java");
//        String tempString = beanConfig.substring(beanConfig.indexOf("return C_PID;") + "return C_PID;".length() + 1, beanConfig.indexOf("    private String  C_UpdateTime;"));
//        beanConfig = beanConfig.replace(tempString, stringBuffer.toString());
        T_Config_File.method_写文件("src/main/java/com/wangtiantian/dao/T_ZiDuan_Params_Config.java", stringBufferConfig.toString());
    }

    // 6.解析配置数据
    public void method_解析配置数据(String filePath) {
        ArrayList<Object> params = new ArrayList<>();
        ArrayList<Object> config = new ArrayList<>();
        ArrayList<Object> bag = new ArrayList<>();
        int groupCount = new DataBaseMethod().get_版本表中组数();
        for (int i = 1; i < groupCount + 1; i++) {
            String paramsContent = T_Config_File.method_读取文件内容(filePath + "params/" + i + "_params.txt");
            String configContent = T_Config_File.method_读取文件内容(filePath + "config/" + i + "_config.txt");
            String bagContent = T_Config_File.method_读取文件内容(filePath + "bag/" + i + "_bag.txt");
            params.addAll(method_解析params(paramsContent.substring(9, paramsContent.length() - 1)));
            config.addAll(method_解析config(configContent.substring(10, configContent.length() - 1)));
            bag.addAll(method_解析bag(bagContent.substring(7, bagContent.length() - 1)));
        }
        HashSet<Object> setParams = new HashSet<>(params);
        params.clear();
        params.addAll(setParams);

        HashSet<Object> setConfig = new HashSet<>(config);
        config.clear();
        config.addAll(setConfig);

        HashSet<Object> setBag = new HashSet<>(bag);
        bag.clear();
        bag.addAll(setBag);

        new DataBaseMethod().method_批量插入配置数据(params, "params");
        new DataBaseMethod().method_批量插入配置数据(config, "config");
        new DataBaseMethod().method_批量插入配置数据(bag, "bag");
    }


    public static ArrayList<Object> method_解析params(String content) {
//        System.out.println(content);
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            HashMap<String, String> mapList = T_ZiDuan_Params_Config.params_字段();
            JSONObject jsonRoot = null;
            try {
                jsonRoot = JSON.parseObject(content);
            } catch (Exception e) {
                System.out.println(content);
            }
//            JSONObject jsonRoot = JSON.parseObject(content);
            JSONArray mainContent = jsonRoot.getJSONObject("result").getJSONArray("paramtypeitems");
            ArrayList<Bean_Params> pidList = new ArrayList<>();
            if (mainContent.size() != 0 && mainContent != null) {
                JSONArray pidparamItems = mainContent.getJSONObject(0).getJSONArray("paramitems").getJSONObject(0).getJSONArray("valueitems");
                for (int i = 0; i < pidparamItems.size(); i++) {
                    JSONObject pidObject = pidparamItems.getJSONObject(i);
                    String pid = pidObject.getString("specid");
                    Bean_Params params = new Bean_Params();
                    params.set_C_PID(pid);
                    pidList.add(params);
                }
                for (int i = 0; i < pidList.size(); i++) {
                    for (int ii = 0; ii < mainContent.size(); ii++) {
                        JSONObject mainObject = mainContent.getJSONObject(ii);
                        String typeName = mainObject.getString("name");
                        JSONArray paramItems = mainObject.getJSONArray("paramitems");
                        for (int iii = 0; iii < paramItems.size(); iii++) {
                            JSONObject paramItemsObject = paramItems.getJSONObject(iii);
                            String columnName = paramItemsObject.getString("name");
                            JSONArray paramItemArray = paramItemsObject.getJSONArray("valueitems");
                            for (int iiii = 0; iiii < paramItemArray.size(); iiii++) {
                                JSONObject paramObject = paramItemArray.getJSONObject(iiii);
                                String PID = paramObject.getString("specid");
                                String value = paramObject.getString("value");
                                String optionType = paramObject.getString("optiontype") == null ? "" : paramObject.getString("optiontype").equals("1") ? "●" : paramObject.getString("optiontype").equals("2") ? "○" : "";
                                JSONArray subList = paramObject.getJSONArray("sublist");
                                StringBuffer subValue = new StringBuffer();
//                                System.out.println(value+"\t"+subList.size());
                                if (subList.size() > 0) {
                                    for (int j = 0; j < subList.size(); j++) {
                                        JSONObject subObject = subList.getJSONObject(j);
                                        String svType = subObject.getString("optiontype");
                                        String subvalueItem = subObject.getString("subvalue");

                                        if (!subObject.getString("subname").equals("")) {
                                            subValue.append(subObject.getString("subname"));
                                        }
                                        if (svType.equals("1")) {
                                            svType = "●";
                                        } else if (svType.equals("2")) {
                                            svType = "○";
                                        }
//                                        System.out.println(subvalueItem);
                                        subValue.append(svType + subvalueItem + "####");
//                                        if (subObject.getString("price").equals("0")) {
//                                            subValue.append("####");
//                                        } else {
//                                            subValue.append(subvalueItem+"[" + subObject.getString("price") + "元]####");
//                                        }
                                    }
                                }
                                value = optionType + value + subValue.toString();

                                if (value.contains("####")) {
                                    if (value.substring(value.length() - 4).equals("####")) {
                                        value = value.substring(0, value.length() - 4);
                                    }
                                }
                                if (value.equals("")) {
                                    value = "-";
                                }
                                if (pidList.get(i).get_C_PID().equals(PID)) {
                                    Class c = pidList.get(i).getClass();
                                    Field field = c.getDeclaredField(mapList.get(typeName + "__" + columnName));
                                    field.setAccessible(true);
                                    field.set(pidList.get(i), value.replace("####", "").replace("&nbsp;", ""));
                                }
                            }
                        }
                    }
                    dataList.add(pidList.get(i));
//                    paramsDao.method_更新Params数据(pidList.get(i), pidList.get(i).get_C_PID());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static ArrayList<Object> method_解析config(String content) {
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            HashMap<String, String> mapList = T_ZiDuan_Params_Config.config_字段();
            JSONObject jsonRoot = JSON.parseObject(content).getJSONObject("result");
            JSONArray mainJson = jsonRoot.getJSONArray("configtypeitems");
            ArrayList<Bean_Config> configList = new ArrayList<>();
            //得到所需pid
            if (mainJson != null && mainJson.size() != 0) {
                JSONObject pidListObject = mainJson.getJSONObject(0).getJSONArray("configitems").getJSONObject(0);
                JSONArray pidArray = pidListObject.getJSONArray("valueitems");
                for (int i = 0; i < pidArray.size(); i++) {
                    JSONObject pidObject = pidArray.getJSONObject(i);
                    String pid = pidObject.getString("specid");
                    Bean_Config config = new Bean_Config();
                    config.set_C_PID(pid);
                    configList.add(config);
                }
                for (int i = 0; i < configList.size(); i++) {
                    for (int ii = 0; ii < mainJson.size(); ii++) {
                        JSONObject mainObject = mainJson.getJSONObject(ii);
                        String typeName = mainObject.getString("name");
                        JSONArray configItems = mainObject.getJSONArray("configitems");
                        for (int iii = 0; iii < configItems.size(); iii++) {
                            JSONObject configItemObject = configItems.getJSONObject(iii);
                            String columnName = configItemObject.getString("name");
                            JSONArray valueItems = configItemObject.getJSONArray("valueitems");
                            for (int iiii = 0; iiii < valueItems.size(); iiii++) {
                                JSONObject valueItemsObject = valueItems.getJSONObject(iiii);
                                String value = valueItemsObject.getString("value");
                                String PID = valueItemsObject.getString("specid");
                                JSONArray subList = valueItemsObject.getJSONArray("sublist");
                                JSONArray priceList = valueItemsObject.getJSONArray("price");
                                StringBuffer subValue = new StringBuffer();
                                if (subList.size() > 0) {
                                    for (int j = 0; j < subList.size(); j++) {
                                        JSONObject subObject = subList.getJSONObject(j);
                                        String svType = subObject.getString("subvalue");
                                        if (svType.equals("1")) {
                                            svType = "●";
                                        } else if (svType.equals("2")) {
                                            svType = "○";
                                        }
                                        subValue.append(svType + subObject.getString("subname"));
                                        if (subObject.getString("price").equals("0")) {
                                            subValue.append("####");
                                        } else {
                                            subValue.append("[" + subObject.getString("price") + "元]####");
                                        }
                                    }
                                }
                                if (priceList.size() > 0) {
                                    for (int j = 0; j < priceList.size(); j++) {
                                        JSONObject priceObject = priceList.getJSONObject(j);
                                        String price_sub_name = priceObject.getString("subname");
                                        String price_price = priceObject.getString("price");
                                        if (price_sub_name.equals("")) {
                                            subValue.append("[" + price_price + "元]####");
                                        } else {
                                            subValue.append("[" + price_sub_name + "->" + price_price + "元]####");
                                        }
                                    }
                                }

                                if (subValue.toString().equals("####") || subValue.toString().equals("-")) {
                                    subValue.append("-");
                                }
                                value = value + subValue.toString();
                                if (value.contains("####")) {
                                    if (value.substring(value.length() - 4).equals("####")) {
                                        value = value.substring(0, value.length() - 4);
                                    }
                                }
                                if (value.equals("")) {
                                    value = "-";
                                }
                                if (configList.get(i).get_C_PID().equals(PID)) {
                                    Class c = configList.get(i).getClass();
                                    Field field = c.getDeclaredField(mapList.get(typeName + "__" + columnName));
                                    field.setAccessible(true);
                                    field.set(configList.get(i), value.replace("####", "").replace("&nbsp;", ""));
                                }
                            }
                        }
                    }
                    dataList.add(configList.get(i));
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static ArrayList<Object> method_解析bag(String content) {
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            JSONObject jsonRoot = JSON.parseObject(content);
            JSONArray mainJson = jsonRoot.getJSONObject("result").getJSONArray("bagtypeitems");
            for (int i = 0; i < mainJson.size(); i++) {
                JSONObject mainObject = mainJson.getJSONObject(i);
                JSONArray bagItems = mainObject.getJSONArray("bagitems");
                for (int ii = 0; ii < bagItems.size(); ii++) {
                    JSONObject bagItem = bagItems.getJSONObject(ii);
                    String specid = bagItem.getString("specid");
                    JSONArray valueItems = bagItem.getJSONArray("valueitems");
                    if (valueItems.size() != 0) {
                        for (int iii = 0; iii < valueItems.size(); iii++) {
                            JSONObject valueItem = valueItems.getJSONObject(iii);
                            Bean_Bag bag = new Bean_Bag();
                            bag.set_C_PID(specid);
                            bag.set_C_PriceDesc(valueItem.getString("pricedesc"));
                            bag.set_C_Price(valueItem.getString("price"));
                            bag.set_C_BagID(valueItem.getString("bagid"));
                            bag.set_C_Name(valueItem.getString("name"));
                            bag.set_C_Description(valueItem.getString("description"));
//                            T_Config_AutoHome bagDao = new T_Config_AutoHome(0, 1, 3);
//                            bagDao.method_更新bag数据(bag, specid, valueItem.getString("bagid"));
                            dataList.add(bag);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static void method_解析params_列名(String content, String filePath, int name) {
        try {
            JSONObject jsonRoot = null;
            try {
                jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
            } catch (Exception e) {
                System.out.println(name);
            }
            if (null != jsonRoot) {


                JSONArray mainContent = jsonRoot.getJSONArray("paramtypeitems");
                ArrayList<Bean_Params> pidList = new ArrayList<>();
                if (mainContent.size() != 0 && mainContent != null) {
                    JSONArray pidparamItems = mainContent.getJSONObject(0).getJSONArray("paramitems").getJSONObject(0).getJSONArray("valueitems");
                    for (int i = 0; i < pidparamItems.size(); i++) {
                        JSONObject pidObject = pidparamItems.getJSONObject(i);
                        String pid = pidObject.getString("specid");
                        Bean_Params params = new Bean_Params();
                        params.set_C_PID(pid);
                        pidList.add(params);
                    }
                    for (int i = 0; i < pidList.size(); i++) {
                        for (int ii = 0; ii < mainContent.size(); ii++) {
                            JSONObject mainObject = mainContent.getJSONObject(ii);
                            String typeName = mainObject.getString("name");
                            JSONArray paramItems = mainObject.getJSONArray("paramitems");
                            for (int iii = 0; iii < paramItems.size(); iii++) {
                                JSONObject paramItemsObject = paramItems.getJSONObject(iii);
                                String columnName = paramItemsObject.getString("name");
                                JSONArray paramItemArray = paramItemsObject.getJSONArray("valueitems");
                                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "列名"), "params_ColumnName.txt", typeName + "__" + columnName + "\n");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_解析config_列名(String content, String filePath, int name) {
        try {
            JSONObject jsonRoot = null;
            try {
                jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
            } catch (Exception e) {
                System.out.println(name);
            }
            if (null != jsonRoot) {
                JSONArray mainJson = jsonRoot.getJSONArray("configtypeitems");
                ArrayList<Bean_Config> configList = new ArrayList<>();
                T_Config_AutoHome autoHome = new T_Config_AutoHome(0, 1, 2);
                //得到所需pid
//        System.out.println(mainJson);
                if (mainJson != null && mainJson.size() != 0) {
                    JSONObject pidListObject = mainJson.getJSONObject(0).getJSONArray("configitems").getJSONObject(0);
                    JSONArray pidArray = pidListObject.getJSONArray("valueitems");
                    for (int i = 0; i < pidArray.size(); i++) {
                        JSONObject pidObject = pidArray.getJSONObject(i);
                        String pid = pidObject.getString("specid");
                        Bean_Config config = new Bean_Config();
                        config.set_C_PID(pid);
                        configList.add(config);
                    }
                    for (int i = 0; i < configList.size(); i++) {
                        for (int ii = 0; ii < mainJson.size(); ii++) {
                            JSONObject mainObject = mainJson.getJSONObject(ii);
                            String typeName = mainObject.getString("name");
                            JSONArray configItems = mainObject.getJSONArray("configitems");
                            for (int iii = 0; iii < configItems.size(); iii++) {
                                JSONObject configItemObject = configItems.getJSONObject(iii);
                                String columnName = configItemObject.getString("name");
                                JSONArray valueItems = configItemObject.getJSONArray("valueitems");
                                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "列名"), "config_ColumnName.txt", typeName + "__" + columnName + "\n");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
