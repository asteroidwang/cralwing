package com.wangtiantian.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_ZiDuan_Params_Config;
import com.wangtiantian.entity.*;
import com.wangtiantian.mapper.DataBaseMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Analysis_AutoHome {
    private static int chooseDataBase = 5;

    private static T_Config_AutoHome brandDao = new T_Config_AutoHome(0, chooseDataBase, 0);
    private static T_Config_AutoHome fctDao = new T_Config_AutoHome(0, chooseDataBase, 1);
    private static T_Config_AutoHome modDao = new T_Config_AutoHome(0, chooseDataBase, 2);
    private static T_Config_AutoHome verDao = new T_Config_AutoHome(0, chooseDataBase, 3);
    private static T_Config_AutoHome paramsDao = new T_Config_AutoHome(0, chooseDataBase, 4);
    private static T_Config_AutoHome configDao = new T_Config_AutoHome(0, chooseDataBase, 5);
    private static T_Config_AutoHome bagDao = new T_Config_AutoHome(0, chooseDataBase, 6);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void method_下载品牌_厂商_车型数据(String filePath) {
        try {
            for (char a = 'A'; a <= 'Z'; a++) {
                String mainURL = "https://www.autohome.com.cn/grade/carhtml/" + a + ".html";
                Analysis_AutoHome_Pic.method_Jsoup_通用(mainURL, filePath + "品牌/", String.valueOf(a));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_解析品牌厂商车型(String filePath) {
        try {
            for (char a = 'A'; a <= 'Z'; a++) {
                String content = T_Config_File.method_读取文件内容(filePath + "品牌/" + a + ".txt");
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
                    brand.set_C_UpdateTime(dateFormat.format(new Date()));
                    brandDao.method_更新品牌数据(brand, brandID);
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
                                model.set_C_UpdateTime(dateFormat.format(new Date()));
                                model.set_C_ModelID(modID);
                                model.set_C_ModelURL(modURL);
                                modDao.method_更新车系数据(model, modID, brandID, temp1);
                            }
                        }
                        String updateTime = dateFormat.format(new Date());
                        Bean_Factory factory = new Bean_Factory();
                        factory.set_modNumber(number);
                        factory.set_C_FactoryName(fctName);
                        factory.set_C_FactoryURL(fctURL);
                        factory.set_C_BrandID(brandID);
                        factory.set_C_FactoryID(temp1);
                        factory.set_C_UpdateTime(updateTime);
                        fctDao.method_更新厂商数据(factory, temp1, brandID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_下载版本(String filePath) {
        try {
            ArrayList<Object> result = modDao.method_查找未下载的车型ID(0);
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + "图片路径在售车型页面/");
            ArrayList<String> fileList_stop = T_Config_File.method_获取文件名称(filePath + "图片路径停售车型页面/");
            ArrayList<String> onLine = new ArrayList<>();
            ArrayList<String> noSale = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                String modID = ((Bean_Model) result.get(i)).get_C_ModelID();
                if (!fileList.contains(modID + ".txt")) {
                    onLine.add(modID);
                }
                if (!fileList_stop.contains(modID + "_t.txt")) {
                    noSale.add(modID);
                }
            }
            for (int i = 0; i < onLine.size(); i++) {
                String modID = onLine.get(i);
                if (!T_Config_File.method_判断文件是否存在(filePath + "图片路径在售车型页面/" + modID + ".txt")) {
                    String modURL = "https://car.autohome.com.cn/pic/series/" + modID + ".html";
                    method_车型页面(modURL, filePath + "图片路径在售车型页面/", modID);
                }

            }
            for (int i = 0; i < noSale.size(); i++) {
                String modID = ((Bean_Model) result.get(i)).get_C_ModelID();
                if (!T_Config_File.method_判断文件是否存在(filePath + "图片路径停售车型页面/" + modID + "_t.txt")) {
                    String modURL_stop = "https://car.autohome.com.cn/pic/series-t/" + modID + ".html";
                    method_车型页面(modURL_stop, filePath + "图片路径停售车型页面/", modID + "_t");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_车型页面(String webURL, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect(webURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
        } catch (Exception e) {
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }
        if (mainDoc != null) {
            Elements mainItems = mainDoc.select(".search-pic-cardl");
            if (mainItems.size() != 0) {
                System.out.println(filePath + fileName + ".txt\t" + dateFormat.format(new Date()));
                T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName + ".txt", mainDoc.toString());
            }
        } else {
            System.out.println("null");
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }
    }

    public static void method_不在图片路径的车型页面(String filePath) {
        try {
            ArrayList<Object> result = modDao.method_查找未下载的车型ID(0);
            ArrayList<String> onLine = new ArrayList<>();
            ArrayList<String> noSale = new ArrayList<>();
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + "在售车型页面/");
            ArrayList<String> fileList_stop = T_Config_File.method_获取文件名称(filePath + "停售车型页面/");
            for (int i = 0; i < result.size(); i++) {
                String modID = ((Bean_Model) result.get(i)).get_C_ModelID();
                if (!fileList.contains(modID + ".txt")) {
                    onLine.add(modID);
                }
                if (!fileList_stop.contains(modID + "_t.txt")) {
                    noSale.add(modID);
                }
            }
            for (int i = 0; i < onLine.size(); i++) {
                String modID = onLine.get(i);
                if (!T_Config_File.method_判断文件是否存在(filePath + "在售车型页面/" + modID + ".txt")) {
                    String modURL = "https://www.autohome.com.cn/" + modID + "/#pvareaid=2042208";
                    Document mainDoc = Jsoup.connect(modURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
                    if (mainDoc != null) {
                        Elements mainItems = mainDoc.select(".name-param");
                        System.out.println(mainItems.size());
                        if (mainItems.size() != 0) {
                            T_Config_File.method_写文件_根据路径创建文件夹(filePath, "在售车型页面/" + modID + ".txt", mainDoc.toString());
                        }
                    } else {
                        System.out.println("null");
                    }
                }
            }
            for (int i = 0; i < noSale.size(); i++) {
                String modID = noSale.get(i);
                if (!T_Config_File.method_判断文件是否存在(filePath + "停售车型页面/" + modID + "_t.txt")) {
                    String modURL_stop = "https://www.autohome.com.cn/" + modID + "/sale.html#pvareaid=3311673";
                    Document mainDoc_stop = Jsoup.connect(modURL_stop).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
                    if (mainDoc_stop != null) {
                        Elements mainItems = mainDoc_stop.select(".modtab1");
                        System.out.println(mainItems.size());
                        if (mainItems.size() != 0) {
                            T_Config_File.method_写文件_根据路径创建文件夹(filePath, "停售车型页面/" + modID + "_t.txt", mainDoc_stop.toString());
                        }
                    } else {
                        System.out.println("null");
                    }
                }
//                method_车型页面(modURL_stop, filePath + "停售车型页面/", modID + "_t");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解析图片页面的版本信息数据
    public static void method_解析版本(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + "车型版本/");
            for (int i = 0; i < fileList.size(); i++) {
                String content = T_Config_File.method_读取文件内容(filePath + "车型版本/" + fileList.get(i));
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
                        String modID = fileList.get(i).replace(".txt", "").replace("_t", "");
                        Bean_Version beanVersion = new Bean_Version();
                        beanVersion.set_C_VersionName(year + " " + verName);
                        beanVersion.set_C_VersionID(verID);
                        beanVersion.set_C_VersionStatus(verStatus);
                        beanVersion.set_C_VersionURL(verURL);
//                        beanVersion.set_C_是否下载(0);
                        beanVersion.set_bag(0);
                        beanVersion.set_config(0);
                        beanVersion.set_params(0);
                        beanVersion.set_C_ModelID(modID);
                        beanVersion.set_C_UpdateTime(dateFormat.format(new Date()));
                        T_Config_AutoHome verDao = new T_Config_AutoHome(0, 0, 3);
                        verDao.method_更新版本数据(beanVersion, verID, modID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //版本正常页面数据
    public static void method_版本(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            T_Config_AutoHome modDao = new T_Config_AutoHome(0, 0, 9);
            ArrayList<Object> modItems = modDao.method_查找();
            for (int i = 0; i < fileList.size(); i++) {
                String fileType = fileList.get(i).replace(".txt", "");
                String modID = fileList.get(i).replace(".txt", "").replace("_t", "");
                if (fileType.indexOf("_t") > -1) {
                    String content = T_Config_File.method_读取文件内容(filePath + fileList.get(i));
                    method_停售车型(content, modID);
                } else {
                    String content = T_Config_File.method_读取文件内容(filePath + modID + ".txt");
                    String noContent = T_Config_File.method_读取文件内容(filePath + modID + "_t.txt");
                    method_在售车型(content, modID);
                    method_停售车型(noContent, modID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //1
    public static ArrayList<Bean_Version> method_停售车型(String content, String modID) {
        ArrayList<Bean_Version> versionList = new ArrayList<>();
        try {
//            T_Config_AutoHome versionDao = new T_Config_AutoHome(0, 1, 0);
//            T_Config_AutoHome versionDao = new T_Config_AutoHome(0, 0, 3);
            String versionID = "";
            String versionName = "";
            String versionStatus = "";
            String versionURL = "";
            String updateTime = dateFormat.format(new Date());
            Document mainDoc = Jsoup.parse(content);
            Elements stopmainItem = mainDoc.select(".modtab1").select(".name").select("a");
            for (int ii = 0; ii < stopmainItem.size(); ii++) {
                versionStatus = "停售";
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
//                version.set_C_是否下载(0);
                version.set_bag(0);
                version.set_config(0);
                version.set_params(0);
                versionList.add(version);
//                verDao.method_更新版本数据(version, versionID, modID);
            }
            Elements stopmainItem2 = mainDoc.select(".modtab2").select(".name").select("a");
//            T_Config_File.method_重复写文件_根据路径创建文件夹("/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/数据文件_汽车之家_2024_1_10/", "版本数量统计.txt", modID+"\t"+stopmainItem.size() + "\t" + stopmainItem2.size() + "\n");
            for (int ii = 0; ii < stopmainItem2.size(); ii++) {
                versionStatus = "停售";
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
//                version.set_C_是否下载(0);
                version.set_bag(0);
                version.set_config(0);
                version.set_params(0);
//                verDao.method_更新版本数据(version, versionID, modID);
                versionList.add(version);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionList;
    }

    //2
    public static ArrayList<Bean_Version> method_在售车型(String content, String modID) {
        ArrayList<Bean_Version> versionList = new ArrayList<>();
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
                String updateTime = dateFormat.format(new Date());
                version.set_C_UpdateTime(updateTime);
//                version.set_C_是否下载(0);
                version.set_bag(0);
                version.set_config(0);
                version.set_params(0);
                versionList.add(version);
//                verDao.method_更新版本数据(version, verIDNumber, modID);
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
                String updateTime = dateFormat.format(new Date());
                version.set_C_UpdateTime(updateTime);
//                version.set_C_是否下载(0);
                version.set_bag(0);
                version.set_config(0);
                version.set_params(0);
                versionList.add(version);
//                verDao.method_更新版本数据(version, verID, modID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionList;
    }

    //3
    public static ArrayList<Bean_Version> method_图片路径车型页面(String content, String modID) {
        ArrayList<Bean_Version> versionList = new ArrayList<>();
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
//                        beanVersion.set_C_是否下载(0);
                    beanVersion.set_bag(0);
                    beanVersion.set_config(0);
                    beanVersion.set_params(0);
                    beanVersion.set_C_ModelID(modID);
                    beanVersion.set_C_UpdateTime(dateFormat.format(new Date()));
                    versionList.add(beanVersion);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionList;
    }

    //解析车型页面入库版本
    public static ArrayList<Bean_Version> method_解析车型入库版本(String filePath) {
        ArrayList<Bean_Version> versionDataList = new ArrayList<>();
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件夹名称(filePath);
            for (int i = 0; i < fileList.size(); i++) {
                String folderName = fileList.get(i);
                ArrayList<String> files = T_Config_File.method_获取文件名称(filePath + folderName);
                if (folderName.equals("在售车型页面")) {
                    for (int j = 0; j < files.size(); j++) {
                        String content = T_Config_File.method_读取文件内容(filePath + folderName + "/" + files.get(j));
                        ArrayList<Bean_Version> versionArrayList = method_在售车型(content, files.get(j).replace(".txt", ""));
                        versionDataList.addAll(versionArrayList);
                    }
                } else if (folderName.equals("停售车型页面")) {
                    for (int j = 0; j < files.size(); j++) {
                        String content = T_Config_File.method_读取文件内容(filePath + folderName + "/" + files.get(j));
                        ArrayList<Bean_Version> versionArrayList = method_停售车型(content, files.get(j).replace("_t.txt", ""));
                        versionDataList.addAll(versionArrayList);
                    }
                } else {
                    for (int j = 0; j < files.size(); j++) {
                        String content = T_Config_File.method_读取文件内容(filePath + folderName + "/" + files.get(j));
                        ArrayList<Bean_Version> versionArrayList = method_图片路径车型页面(content, files.get(j).replace("_t.txt", "").replace(".txt", ""));
                        versionDataList.addAll(versionArrayList);
                    }
                }
            }
            HashSet<Bean_Version> set = new HashSet<>(versionDataList);
            versionDataList.clear();
            versionDataList.addAll(set);
            method_test(versionDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionDataList;
    }

    public static ArrayList<Bean_Version> method_test(ArrayList<Bean_Version> list) {
        for (int i = 0; i < list.size(); i++) {
            verDao.method_新增(list.get(i));
        }
        return list;
    }

    //在售车型
    public void method_在售车型页面(String filePath) {
        try {
//            String content
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //调用下载配置信息所在页面的方法
    public static void method_下载配置信息(String filePath) {
        try {
            ArrayList<Object> paramsGroups = verDao.method_查找未下载的params(0);
            ArrayList<Object> configGroups = verDao.method_查找未下载的config(0);
            ArrayList<Object> bagGroups = verDao.method_查找未下载的bag(0);
            for (int i = 0; i < paramsGroups.size(); i++) {
                int group = ((Bean_Version) paramsGroups.get(i)).get_C_Group();
                ArrayList<Object> verItems = verDao.method_查找未下载的分组版本ID(group);
                String versions = "";
                for (int ii = 0; ii < verItems.size(); ii++) {
                    String verID = ((Bean_Version) verItems.get(ii)).get_C_VersionID();
                    Boolean resultVer = verID.matches("[0-9]{1,}");
                    if (!resultVer) {
                        T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", ""), "版本ID.txt", verID + "\n");
                    } else {
                        versions += verID + ",";
                    }
                }
                if (!versions.equals("")) {
                    versions = versions.substring(0, versions.length() - 1);
                    String paramsAPI = "https://carif.api.autohome.com.cn/Car/v3/Param_ListBySpecIdList.ashx?speclist=" + versions + "&_appid=test&_=1708497025742&_callback=__param1";
                    method_params(paramsAPI, filePath, group);
                }
            }
            for (int i = 0; i < configGroups.size(); i++) {
                int group = ((Bean_Version) configGroups.get(i)).get_C_Group();
                ArrayList<Object> verItems = verDao.method_查找未下载的分组版本ID(group);
                String versions = "";
                for (int ii = 0; ii < verItems.size(); ii++) {
                    String verID = ((Bean_Version) verItems.get(ii)).get_C_VersionID();
                    Boolean resultVer = verID.matches("[0-9]{1,}");
                    if (!resultVer) {
                        T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", ""), "版本ID.txt", verID + "\n");
                    } else {
                        versions += verID + ",";
                    }
                }
                if (!versions.equals("")) {
                    versions = versions.substring(0, versions.length() - 1);
                    String configAPI = "https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + versions + "&_=1704953414626&_callback=__config3";
                    method_config(configAPI, filePath, group);

                }

            }
            for (int i = 0; i < bagGroups.size(); i++) {
                int group = ((Bean_Version) bagGroups.get(i)).get_C_Group();
                ArrayList<Object> verItems = verDao.method_查找未下载的分组版本ID(group);
                String versions = "";
                for (int ii = 0; ii < verItems.size(); ii++) {
                    String verID = ((Bean_Version) verItems.get(ii)).get_C_VersionID();
                    Boolean resultVer = verID.matches("[0-9]{1,}");
                    if (!resultVer) {
                        T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", ""), "版本ID.txt", verID + "\n");
                    } else {
                        versions += verID + ",";
                    }

                }
                if (!versions.equals("")) {
                    versions = versions.substring(0, versions.length() - 1);
                    String bagAPI = "https://carif.api.autohome.com.cn/Car/Config_BagBySpecIdListV2.ashx?speclist=" + versions + "&_=1704953414627&_callback=__bag4";
                    method_bag(bagAPI, filePath, group);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_下载配置信息_补充(String filePath) {
        try {
            int a = 1;
            String versions = "41372,52801,54269,44340,1009788,56368,56792,54505,62774,53699";
            String paramsAPI = "https://carif.api.autohome.com.cn/Car/v3/Param_ListBySpecIdList.ashx?speclist=" + versions + "&_appid=test&_=1708497025742&_callback=__param1";
            String configAPI = "https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + versions + "&_=1704953414626&_callback=__config3";
            String bagAPI = "https://carif.api.autohome.com.cn/Car/Config_BagBySpecIdListV2.ashx?speclist=" + versions + "&_=1704953414627&_callback=__bag4";
//            method_params(paramsAPI, filePath, 6781);
            method_config(configAPI, filePath, 7661);
//            method_bag(bagAPI, filePath, 7344 + a);
//                    verDao.method_修改下载状态(1, group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //基本配置数据
    public static void method_params(String url, String filePath, int i) {
        try {
            Document mainDoc = null;
            try {
                mainDoc = Jsoup.parse(new URL(url).openStream(), "GBK", url);
            } catch (Exception e) {
                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "出错"), "下载params_new.txt", url + "\n");
            }

            if (mainDoc != null) {
                String content = mainDoc.text();
                content = content.substring(9, content.length() - 1);
                JSONObject jsonRoot = JSON.parseObject(content);
                if (jsonRoot.getString("message").equals("成功")) {
                    T_Config_File.method_写文件_根据路径创建文件夹(filePath, i + "_params.txt", content);
                    verDao.method_修改下载状态("params", 1, i);
                    System.out.println("成功");
                } else {
                    T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "出错"), "下载params_new.txt", url + "\n");
                    System.out.println("false");
                }
            } else {
                System.out.println("没有数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //参数配置数据
    public static void method_config(String url, String filePath, int i) {
        try {
            Document mainDoc = null;
            try {
                mainDoc = Jsoup.parse(new URL(url).openStream(), "GBK", url);
            } catch (Exception e) {
                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "出错"), "下载config.txt", url + "\n");
            }
            if (mainDoc != null) {
                String content = mainDoc.text();
                content = content.substring(10, content.length() - 1);
                JSONObject jsonRoot = JSON.parseObject(content);
                if (jsonRoot.getString("message").equals("成功")) {
                    T_Config_File.method_写文件_根据路径创建文件夹(filePath.replace("params", "config"), i + "_config.txt", content);
                    verDao.method_修改下载状态("config", 1, i);
                    System.out.println("成功");
                } else {
                    T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "出错"), "下载config.txt", url + "\n");
                    System.out.println("false");
                }
            } else {
//                verDao.method_修改下载状态(3, i);
                System.out.println("没有数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //选装包数据
    public static void method_bag(String url, String filePath, int i) {
        try {
            Document mainDoc = null;
            try {
                mainDoc = Jsoup.parse(new URL(url).openStream(), "GBK", url);
            } catch (Exception e) {
                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "出错"), "下载params_new.txt", url + "\n");
            }
            if (mainDoc != null) {
                String content = mainDoc.text();
                content = content.substring(7, content.length() - 1);
                JSONObject jsonRoot = JSON.parseObject(content);
                if (jsonRoot.getString("message").equals("成功")) {
                    T_Config_File.method_写文件_根据路径创建文件夹(filePath.replace("params", "bag"), i + "_bag.txt", content);
                    verDao.method_修改下载状态("bag", 1, i);
                    System.out.println("成功");
                } else {
                    T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "出错"), "下载bag.txt", url + "\n");
                    System.out.println("false");
                }
            } else {
//                verDao.method_修改下载状态(4, i);
                System.out.println("没有数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_解析params_config_bag(String filePath) {
        try {
            ArrayList<Object> groupList = verDao.method_查找所有版本分组();
            ArrayList<Object> paramsDataList = new ArrayList<>();
            ArrayList<Object> configDataList = new ArrayList<>();
            ArrayList<Object> bagDataList = new ArrayList<>();
            for (int i = 0; i < groupList.size(); i++) {
                int group = ((Bean_Version) groupList.get(i)).get_C_Group();
                String content_params = T_Config_File.method_读取文件内容(filePath + group + "_params" + ".txt");
                String content_config = T_Config_File.method_读取文件内容(filePath.replace("params", "config/" + group + "_config.txt"));
                String content_bag = T_Config_File.method_读取文件内容(filePath.replace("params", "bag/" + group + "_bag.txt"));
                ArrayList<Object> paramsList = method_解析params(content_params, filePath);
                paramsDataList.addAll(paramsList);
                ArrayList<Object> configList = method_解析config(content_config, filePath);
                configDataList.addAll(configList);
                ArrayList<Object> bagList = method_解析bag(content_bag);
                bagDataList.addAll(bagList);
            }
            HashSet<Object> setParams = new HashSet<>(paramsDataList);
            paramsDataList.clear();
            paramsDataList.addAll(setParams);

            HashSet<Object> setConfig = new HashSet<>(configDataList);
            configDataList.clear();
            configDataList.addAll(setConfig);

            HashSet<Object> setBag = new HashSet<>(bagDataList);
            bagDataList.clear();
            bagDataList.addAll(setBag);

            DataBaseMethod.dataBase_i_d_u(paramsDataList, "params");
//            DataBaseMethod.dataBase_i_d_u(configDataList, "config");
//            DataBaseMethod.dataBase_i_d_u(bagDataList, "bag");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_解析params_config_bag_One(String filePath) {
        try {
            int i = 8061;
            String content_params = T_Config_File.method_读取文件内容(filePath + i + "_params" + ".txt");
            String content_config = T_Config_File.method_读取文件内容(filePath.replace("params", "config/" + i + "_config.txt"));
            String content_bag = T_Config_File.method_读取文件内容(filePath.replace("params", "bag/" + i + "_bag.txt"));
            method_解析params(content_params, filePath);
            method_解析config(content_config, filePath);
            method_解析bag(content_bag);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Object> method_解析params(String content, String filePath) {
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            HashMap<String, String> mapList = T_ZiDuan_Params_Config.params_字段();
            JSONObject jsonRoot = JSON.parseObject(content);
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
                                if (value.equals("")) {
                                    for (int j = 0; j < paramObject.getJSONArray("sublist").size(); j++) {
                                        String optionType = paramObject.getJSONArray("sublist").getJSONObject(j).getString("optiontype");
                                        String value_sub = paramObject.getJSONArray("sublist").getJSONObject(j).getString("subvalue");
                                        String sunName = paramObject.getJSONArray("sublist").getJSONObject(j).getString("subname");
                                        value += value_sub + "[optiontype:" + optionType + "]" + sunName + "####";
                                    }
                                }
                                if (pidList.get(i).get_C_PID().equals(PID)) {
                                    Class c = pidList.get(i).getClass();
                                    Field field = c.getDeclaredField(mapList.get(typeName + "__" + columnName));
                                    field.setAccessible(true);
                                    field.set(pidList.get(i), value);
                                }
                            }
                        }
                    }
//                    dataList.add(pidList.get(i));
                    paramsDao.method_更新Params数据(pidList.get(i), pidList.get(i).get_C_PID());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static ArrayList<Object> method_解析config(String content, String filePath) {
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
                                String subValue = "";
                                if (subList.size() != 0) {
                                    for (int iiiii = 0; iiiii < subList.size(); iiiii++) {
                                        JSONObject subObject = subList.getJSONObject(iiiii);
                                        subValue += subObject.getString("subname") + "[sv:" + subObject.getString("subvalue") + "]" + "####";
                                    }
                                }
                                if (subValue.equals("####") || subValue.equals("-")) {
                                    subValue = "";
                                }
                                value = value + subValue;
                                if (configList.get(i).get_C_PID().equals(PID)) {
                                    Class c = configList.get(i).getClass();
                                    Field field = c.getDeclaredField(mapList.get(typeName + "__" + columnName));
                                    field.setAccessible(true);
                                    field.set(configList.get(i), value);
                                }
                            }
                        }
                    }
//                    dataList.add(configList.get(i));
                    configDao.method_更新config数据(configList.get(i), configList.get(i).get_C_PID());
                }
            }
        } catch (Exception e) {
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
                            bagDao.method_更新bag数据(bag, specid, valueItem.getString("bagid"));
//                            dataList.add(bag);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static void method_取列名(String filePath) {
        try {
//            ArrayList<String> columnNames = T_Config_File.method_按行读取文件(filePath+"/config_ColumnName.txt");
//            ArrayList<String> columnNames = T_Config_File.method_按行读取文件(filePath + "/config_ColumnName.txt");
            ArrayList<String> columnNames_params = T_Config_File.method_按行读取文件(filePath + "/params_ColumnName.txt");
            ArrayList<String> columnNames_config = T_Config_File.method_按行读取文件(filePath + "/config_ColumnName.txt");
            LinkedHashSet<String> hashSet_params = new LinkedHashSet<>(columnNames_params);
            LinkedHashSet<String> hashSet_config = new LinkedHashSet<>(columnNames_config);
            ArrayList<String> lost_params = new ArrayList<>(hashSet_params);
            ArrayList<String> lost_config = new ArrayList<>(hashSet_config);
            System.out.println(lost_params);
            System.out.println(lost_config);
        } catch (Exception e) {

        }
    }

    public static void method_下载口碑评分(String filePath) {
        try {
            System.out.println(filePath);
            T_Config_AutoHome versionAutoHome = new T_Config_AutoHome(0, 0, 3);
            T_Config_AutoHome modelAutoHome = new T_Config_AutoHome(0, 0, 9);
            ArrayList<Object> modelList = modelAutoHome.method_根据品牌ID查找车型("33");
            for (int i = 0; i < modelList.size(); i++) {
                String modID = ((Bean_Model) modelList.get(i)).get_C_ModelID();
                ArrayList<Object> versionList = versionAutoHome.method_根据车型ID查找版本(modID);
                for (int j = 0; j < versionList.size(); j++) {
                    String verID = ((Bean_Version) versionList.get(j)).get_C_VersionID();
                    String kbURL = "https://k.autohome.com.cn/" + verID + "/index_1.html?#listcontainer" + verID;
                    System.out.println(kbURL);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void method_解析params_config_bag_列名(String filePath) {
//        try {
//            ArrayList<Object> groupList = verDao.method_查找所有版本分组();
//            for (int i = 1; i < groupList.size(); i++) {
//                int group = ((Bean_Version) groupList.get(i)).get_C_Group();
//                String content_params = T_Config_File.method_读取文件内容(filePath + group + "_params" + ".txt");
//                String content_config = T_Config_File.method_读取文件内容(filePath.replace("params", "config/" + group + "_config.txt"));
//                method_解析params_列名(content_params, filePath);
//                method_解析config_列名(content_config, filePath);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void method_解析params_列名(String content, String filePath) {
//        try {
//            T_Config_AutoHome autoHome = new T_Config_AutoHome(0, 1, 1);
//            JSONObject jsonRoot = JSON.parseObject(content);
//            JSONArray mainContent = jsonRoot.getJSONObject("result").getJSONArray("paramtypeitems");
//            ArrayList<Bean_Params> pidList = new ArrayList<>();
//            if (mainContent.size() != 0 && mainContent != null) {
//                JSONArray pidparamItems = mainContent.getJSONObject(0).getJSONArray("paramitems").getJSONObject(0).getJSONArray("valueitems");
//                for (int i = 0; i < pidparamItems.size(); i++) {
//                    JSONObject pidObject = pidparamItems.getJSONObject(i);
//                    String pid = pidObject.getString("specid");
//                    Bean_Params params = new Bean_Params();
//                    params.set_C_PID(pid);
//                    pidList.add(params);
//                }
//                for (int i = 0; i < pidList.size(); i++) {
//                    for (int ii = 0; ii < mainContent.size(); ii++) {
//                        JSONObject mainObject = mainContent.getJSONObject(ii);
//                        String typeName = mainObject.getString("name");
//                        JSONArray paramItems = mainObject.getJSONArray("paramitems");
//                        for (int iii = 0; iii < paramItems.size(); iii++) {
//                            JSONObject paramItemsObject = paramItems.getJSONObject(iii);
//                            String columnName = paramItemsObject.getString("name");
//                            JSONArray paramItemArray = paramItemsObject.getJSONArray("valueitems");
//                            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "列名"), "params_ColumnName.txt", typeName + "__" + columnName + "\n");
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void method_解析config_列名(String content, String filePath) {
//        try {
//            JSONObject jsonRoot = JSON.parseObject(content).getJSONObject("result");
//            JSONArray mainJson = jsonRoot.getJSONArray("configtypeitems");
//            ArrayList<Bean_Config> configList = new ArrayList<>();
//            T_Config_AutoHome autoHome = new T_Config_AutoHome(0, 1, 2);
//            //得到所需pid
////        System.out.println(mainJson);
//            if (mainJson != null && mainJson.size() != 0) {
//                JSONObject pidListObject = mainJson.getJSONObject(0).getJSONArray("configitems").getJSONObject(0);
//                JSONArray pidArray = pidListObject.getJSONArray("valueitems");
//                for (int i = 0; i < pidArray.size(); i++) {
//                    JSONObject pidObject = pidArray.getJSONObject(i);
//                    String pid = pidObject.getString("specid");
//                    Bean_Config config = new Bean_Config();
//                    config.set_C_PID(pid);
//                    configList.add(config);
//                }
//                for (int i = 0; i < configList.size(); i++) {
//                    for (int ii = 0; ii < mainJson.size(); ii++) {
//                        JSONObject mainObject = mainJson.getJSONObject(ii);
//                        String typeName = mainObject.getString("name");
//                        JSONArray configItems = mainObject.getJSONArray("configitems");
//                        for (int iii = 0; iii < configItems.size(); iii++) {
//                            JSONObject configItemObject = configItems.getJSONObject(iii);
//                            String columnName = configItemObject.getString("name");
//                            JSONArray valueItems = configItemObject.getJSONArray("valueitems");
//                            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("params", "列名"), "config_ColumnName.txt", typeName + "__" + columnName + "\n");
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}