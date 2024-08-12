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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainConfigData {
    private DataBaseMethod dataBaseMethod = new DataBaseMethod();

    public static void main(String[] args) {
        MainConfigData mainConfigData = new MainConfigData();
//        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/配置数据/202408010/";
        String filePath = "/Users/wangtiantian/MyDisk/汽车之家/配置数据/汽车之家_240801/";
        // mainConfigData.method_下载品牌厂商车型数据(filePath+"初始数据/);
        // mainConfigData.parse_品牌厂商车型数据(filePath+"初始数据/);
        // mainConfigData.method_下载含有版本数据的文件(filePath+"含版本数据的文件");
        // mainConfigData.parse_解析含有版本数据的文件(filePath + "含版本数据的文件");
        // mainConfigData.method_下载配置数据(filePath + "params_1/");
        // mainConfigData.method_解析列名(filePath);
        // mainConfigData.method_取列名(filePath);
        mainConfigData.method_解析配置数据(filePath);
    }

    // 1.下载品牌厂商车型数据
    public void method_下载品牌厂商车型数据(String filePath) {
        try {
            for (char a = 'A'; a <= 'Z'; a++) {
                String mainUrl = "https://www.autohome.com.cn/grade/carhtml/" + a + ".html";
                T_Config_File.method_访问url获取网页源码普通版(mainUrl, "UTF-8", filePath, a + ".txt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2.解析品牌厂商车型数据
    public void parse_品牌厂商车型数据(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Bean_Brand> brandData = new ArrayList<>();
            ArrayList<Bean_Factory> fctData = new ArrayList<>();
            ArrayList<Bean_Model> modData = new ArrayList<>();
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
            HashSet<Bean_Brand> brandSet = new HashSet<>(brandData);
            brandData.clear();
            brandData.addAll(brandSet);

            HashSet<Bean_Factory> factorySet = new HashSet<>(fctData);
            fctData.clear();
            fctData.addAll(factorySet);

            HashSet<Bean_Model> modelSet = new HashSet<>(modData);
            modData.clear();
            modData.addAll(modelSet);

            dataBaseMethod.method_入库品牌数据(brandData);
            dataBaseMethod.method_入库厂商数据(fctData);
            dataBaseMethod.method_入库车型数据(modData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3.下载版本所在的页面数据
    public void method_下载含有版本数据的文件(String filePath) {
        try {
            // 1.非图片路径
            method_下载含有版本数据的文件_非图片路径(filePath + "_非图片路径/");
            // 2.图片路径
            method_下载含有版本数据的文件_图片路径(filePath + "_图片路径/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method_下载含有版本数据的文件_非图片路径(String filePath) {
        try {
            // 1.在售
            ArrayList<Object> onSaleList = dataBaseMethod.method_查找车型表中未下载的含版本数据的数据("在售", 0);
            List<List<Object>> saList = IntStream.range(0, 6).mapToObj(i -> onSaleList.subList(i * (onSaleList.size() + 5) / 6, Math.min((i + 1) * (onSaleList.size() + 5) / 6, onSaleList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < saList.size(); i++) {
                ModelMoreThread modelMoreThread = new ModelMoreThread(saList.get(i), filePath, 0);
                Thread thread = new Thread(modelMoreThread);
                thread.start();
            }
            // 2.停售
            ArrayList<Object> noSaleList = dataBaseMethod.method_查找车型表中未下载的含版本数据的数据("停售", 0);
            List<List<Object>> noSalist = IntStream.range(0, 6).mapToObj(i -> noSaleList.subList(i * (noSaleList.size() + 5) / 6, Math.min((i + 1) * (noSaleList.size() + 5) / 6, noSaleList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < noSalist.size(); i++) {
                ModelMoreThread modelMoreThread = new ModelMoreThread(noSalist.get(i), filePath, 1);
                Thread thread = new Thread(modelMoreThread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method_下载含有版本数据的文件_图片路径(String filePath) {
        try {
            // 1.在售
            ArrayList<Object> onSaleList = dataBaseMethod.method_查找车型表中未下载的含版本数据的数据("图片页面在售", 0);
            List<List<Object>> salist = IntStream.range(0, 6).mapToObj(i -> onSaleList.subList(i * (onSaleList.size() + 5) / 6, Math.min((i + 1) * (onSaleList.size() + 5) / 6, onSaleList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < salist.size(); i++) {
                ModelMoreThread modelMoreThread = new ModelMoreThread(salist.get(i), filePath, 2);
                Thread thread = new Thread(modelMoreThread);
                thread.start();
            }

            // 2.停售
            ArrayList<Object> noSaleList = dataBaseMethod.method_查找车型表中未下载的含版本数据的数据("图片页面停售", 0);
            List<List<Object>> noSalist = IntStream.range(0, 6).mapToObj(i -> noSaleList.subList(i * (noSaleList.size() + 5) / 6, Math.min((i + 1) * (noSaleList.size() + 5) / 6, noSaleList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < noSalist.size(); i++) {
                ModelMoreThread modelMoreThread = new ModelMoreThread(noSalist.get(i), filePath, 3);
                Thread thread = new Thread(modelMoreThread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4.解析版本数据并入库
    public void parse_解析含有版本数据的文件(String filePath) {
        try {
            ArrayList<Bean_Version> versionData = new ArrayList<>();
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + "_非图片路径/");
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + "_非图片路径/" + fileName);
                if (fileName.contains("停售")) {
                    versionData.addAll(method_解析非图片路径_停售(content, fileName.replace("_停售.txt", "")));

                } else if (fileName.contains("在售")) {
                    versionData.addAll(method_解析非图片路径_在售(content, fileName.replace("_在售.txt", "")));
                }
            }
            ArrayList<String> fileListPic = T_Config_File.method_获取文件名称(filePath + "_图片路径/");
            for (String fileName : fileListPic) {
                String content = T_Config_File.method_读取文件内容(filePath + "_图片路径/" + fileName);
                versionData.addAll(method_解析图片路径(content, fileName.replace("_图片页面停售.txt", "").replace("_图片页面在售.txt", "")));
            }

            HashSet<Bean_Version> set = new HashSet<>(versionData);
            versionData.clear();
            versionData.addAll(set);
            dataBaseMethod.method_入库版本数据(versionData);

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
                Document verDoc = Jsoup.parse(new URL(verURL).openStream(), "gb2312", verURL);
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
            // dataBaseMethod.insert_批量插入版本ids();
            ArrayList<Object> paramsList = dataBaseMethod.method_根据数据类型获取未下载的数据("params", 0);
            ArrayList<Object> configList = dataBaseMethod.method_根据数据类型获取未下载的数据("config", 0);
            ArrayList<Object> bagList = dataBaseMethod.method_根据数据类型获取未下载的数据("bag", 0);

            if (paramsList.size() <= 6) {
                for (Object o : paramsList) {
                    String ids = ((Bean_VersionIds) o).get_C_Ids();
                    int group = ((Bean_VersionIds) o).get_C_Group();
                    if (T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/v3/Param_ListBySpecIdList.ashx?speclist=" + ids + "&_appid=test&_=1723037336504&_callback=__param1", "gb2312", filePath, group + "_params.txt")) {
                        dataBaseMethod.update_修改车辆配置的数据文件下载状态(group, "params", 1);
                    }
                }
            } else {
                List<List<Object>> paramsData = IntStream.range(0, 6).mapToObj(i -> paramsList.subList(i * (paramsList.size() + 5) / 6, Math.min((i + 1) * (paramsList.size() + 5) / 6, paramsList.size())))
                        .collect(Collectors.toList());
                for (int i = 0; i < paramsData.size(); i++) {
                    ConfigMoreThread params = new ConfigMoreThread(paramsData.get(i), filePath, 0);
                    Thread thread = new Thread(params);
                    thread.start();
                }
            }

            if (configList.size() <= 6) {
                for (Object o : configList) {
                    String ids = ((Bean_VersionIds) o).get_C_Ids();
                    int group = ((Bean_VersionIds) o).get_C_Group();
                    if (T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + ids + "&_=1723037336505&_callback=__config3", "gb2312", filePath.replace("params", "config"), group + "_config.txt")) {
                        dataBaseMethod.update_修改车辆配置的数据文件下载状态(group, "config", 1);
                    }
                }
            } else {
                List<List<Object>> configData = IntStream.range(0, 6).mapToObj(i -> configList.subList(i * (configList.size() + 5) / 6, Math.min((i + 1) * (configList.size() + 5) / 6, configList.size())))
                        .collect(Collectors.toList());
                for (int i = 0; i < configData.size(); i++) {
                    ConfigMoreThread config = new ConfigMoreThread(configData.get(i), filePath.replace("params", "config"), 1);
                    Thread thread = new Thread(config);
                    thread.start();
                }
            }

            if (bagList.size() <= 6) {
                for (Object o : bagList) {
                    String ids = ((Bean_VersionIds) o).get_C_Ids();
                    int group = ((Bean_VersionIds) o).get_C_Group();
                    if (T_Config_File.method_访问url获取Json普通版("https://carif.api.autohome.com.cn/Car/Config_BagBySpecIdListV2.ashx?speclist=" + ids + "&_=1723037336505&_callback=__bag4", "gb2312", filePath.replace("params", "bag"), group + "_bag.txt")) {
                        dataBaseMethod.update_修改车辆配置的数据文件下载状态(group, "bag", 1);
                    }
                }
            } else {
                List<List<Object>> bagData = IntStream.range(0, 6).mapToObj(i -> bagList.subList(i * (bagList.size() + 5) / 6, Math.min((i + 1) * (bagList.size() + 5) / 6, bagList.size())))
                        .collect(Collectors.toList());
                for (int i = 0; i < bagData.size(); i++) {
                    ConfigMoreThread bag = new ConfigMoreThread(bagData.get(i), filePath.replace("params", "bag"), 2);
                    Thread thread = new Thread(bag);
                    thread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解析配置数据获取列名
    public void method_解析列名(String filePath) {
        ArrayList<Object> groupList = dataBaseMethod.method_根据数据类型获取未下载的数据("params", 1);

        for (int i = 1; i < groupList.size() + 1; i++) {
            String paramsContent = T_Config_File.method_读取文件内容(filePath + "params/" + i + "_params.txt");
            String configContent = T_Config_File.method_读取文件内容(filePath + "config/" + i + "_config.txt");
            method_解析params_列名(paramsContent.substring(9, paramsContent.length() - 1), filePath);
            method_解析config_列名(configContent.substring(10, configContent.length() - 1), filePath);
        }
    }

    public static void method_取列名(String filePath) {
        try {
            ArrayList<String> columnNames_params = T_Config_File.method_按行读取文件(filePath + "/params_ColumnName.txt");
            ArrayList<String> columnNames_config = T_Config_File.method_按行读取文件(filePath + "/config_ColumnName.txt");
            LinkedHashSet<String> hashSet_params = new LinkedHashSet<>(columnNames_params);
            LinkedHashSet<String> hashSet_config = new LinkedHashSet<>(columnNames_config);
            ArrayList<String> lost_params = new ArrayList<>(hashSet_params);
            ArrayList<String> lost_config = new ArrayList<>(hashSet_config);
            System.out.println(lost_params.toString().substring(1,lost_params.toString().length()-1).replace(", ","\n"));
            System.out.println("=============");
            System.out.println(lost_config.toString().substring(1,lost_config.toString().length()-1).replace(", ","\n"));
        } catch (Exception e) {

        }
    }

    // 6.解析配置数据
    public void method_解析配置数据(String filePath) {
        ArrayList<Object> params = new ArrayList<>();
        ArrayList<Object> config = new ArrayList<>();
        ArrayList<Object> bag = new ArrayList<>();
//        ArrayList<Object> groupList = dataBaseMethod.method_根据数据类型获取未下载的数据("params", 1);
        for (int i = 1; i < 8152; i++) {
            String paramsContent= T_Config_File.method_读取文件内容(filePath + "params/" + i + "_params.txt");
            String configContent = T_Config_File.method_读取文件内容(filePath + "config/" + i + "_config.txt");
            String bagContent = T_Config_File.method_读取文件内容(filePath + "bag/" + i + "_bag.txt");
//            params.addAll(method_解析params(paramsContent.substring(9,paramsContent.length()-1)));
//            config.addAll(method_解析config(configContent.substring(10,configContent.length()-1)));
//            bag.addAll(method_解析bag(bagContent.substring(7,bagContent.length()-1)));
            params.addAll(method_解析params(paramsContent));
            config.addAll(method_解析config(configContent));
            bag.addAll(method_解析bag(bagContent));
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

        //dataBaseMethod.method_批量插入配置数据(params, "params");
        dataBaseMethod.method_批量插入配置数据(config, "config");
        //dataBaseMethod.method_批量插入配置数据(bag, "bag");
    }


    public static ArrayList<Object> method_解析params(String content) {
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            HashMap<String, String> mapList = T_ZiDuan_Params_Config.params_字段();
            com.alibaba.fastjson.JSONObject jsonRoot = JSON.parseObject(content);
            com.alibaba.fastjson.JSONArray mainContent = jsonRoot.getJSONObject("result").getJSONArray("paramtypeitems");
            ArrayList<Bean_Params> pidList = new ArrayList<>();
            if (mainContent.size() != 0 && mainContent != null) {
                com.alibaba.fastjson.JSONArray pidparamItems = mainContent.getJSONObject(0).getJSONArray("paramitems").getJSONObject(0).getJSONArray("valueitems");
                for (int i = 0; i < pidparamItems.size(); i++) {
                    com.alibaba.fastjson.JSONObject pidObject = pidparamItems.getJSONObject(i);
                    String pid = pidObject.getString("specid");
                    Bean_Params params = new Bean_Params();
                    params.set_C_PID(pid);
                    pidList.add(params);
                }
                for (int i = 0; i < pidList.size(); i++) {
                    for (int ii = 0; ii < mainContent.size(); ii++) {
                        com.alibaba.fastjson.JSONObject mainObject = mainContent.getJSONObject(ii);
                        String typeName = mainObject.getString("name");
                        com.alibaba.fastjson.JSONArray paramItems = mainObject.getJSONArray("paramitems");
                        for (int iii = 0; iii < paramItems.size(); iii++) {
                            com.alibaba.fastjson.JSONObject paramItemsObject = paramItems.getJSONObject(iii);
                            String columnName = paramItemsObject.getString("name");
                            com.alibaba.fastjson.JSONArray paramItemArray = paramItemsObject.getJSONArray("valueitems");
                            for (int iiii = 0; iiii < paramItemArray.size(); iiii++) {
                                com.alibaba.fastjson.JSONObject paramObject = paramItemArray.getJSONObject(iiii);
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
                                    Field field = null;
                                    try {
                                        field = c.getDeclaredField(mapList.get(typeName + "__" + columnName));
                                    } catch (Exception e) {
                                        System.out.println(mapList.get(typeName + "__" + columnName) + "\t" + typeName + "__" + columnName);
                                    }

                                    field.setAccessible(true);
                                    field.set(pidList.get(i), value);
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
            com.alibaba.fastjson.JSONObject jsonRoot = JSON.parseObject(content).getJSONObject("result");
            com.alibaba.fastjson.JSONArray mainJson = jsonRoot.getJSONArray("configtypeitems");
            ArrayList<Bean_Config> configList = new ArrayList<>();
            //得到所需pid
            if (mainJson != null && mainJson.size() != 0) {
                com.alibaba.fastjson.JSONObject pidListObject = mainJson.getJSONObject(0).getJSONArray("configitems").getJSONObject(0);
                com.alibaba.fastjson.JSONArray pidArray = pidListObject.getJSONArray("valueitems");
                for (int i = 0; i < pidArray.size(); i++) {
                    com.alibaba.fastjson.JSONObject pidObject = pidArray.getJSONObject(i);
                    String pid = pidObject.getString("specid");
                    Bean_Config config = new Bean_Config();
                    config.set_C_PID(pid);
                    configList.add(config);
                }
                for (int i = 0; i < configList.size(); i++) {
                    for (int ii = 0; ii < mainJson.size(); ii++) {
                        com.alibaba.fastjson.JSONObject mainObject = mainJson.getJSONObject(ii);
                        String typeName = mainObject.getString("name");
                        com.alibaba.fastjson.JSONArray configItems = mainObject.getJSONArray("configitems");
                        for (int iii = 0; iii < configItems.size(); iii++) {
                            com.alibaba.fastjson.JSONObject configItemObject = configItems.getJSONObject(iii);
                            String columnName = configItemObject.getString("name");
                            com.alibaba.fastjson.JSONArray valueItems = configItemObject.getJSONArray("valueitems");
                            for (int iiii = 0; iiii < valueItems.size(); iiii++) {
                                com.alibaba.fastjson.JSONObject valueItemsObject = valueItems.getJSONObject(iiii);
                                String value = valueItemsObject.getString("value");
                                String PID = valueItemsObject.getString("specid");
                                com.alibaba.fastjson.JSONArray subList = valueItemsObject.getJSONArray("sublist");
                                StringBuffer subValue = new StringBuffer();
                                if (subList.size() != 0) {
                                    for (int iiiii = 0; iiiii < subList.size(); iiiii++) {
                                        com.alibaba.fastjson.JSONObject subObject = subList.getJSONObject(iiiii);
                                        subValue.append(subObject.getString("subname")).append("[sv:" + subObject.getString("subvalue") + "]");
                                        if (subObject.getString("price").equals("0")) {
                                            subValue.append("####");
                                        } else {
                                            subValue.append(subObject.getString("price")).append("####");
                                        }
                                    }
                                }
                                if (subValue.toString().equals("####") || subValue.toString().equals("-")) {
                                    subValue.append("-");
                                }
                                value = value + subValue.toString();
                                if (value.equals("")) {
                                    value = "-";
                                }
                                if (configList.get(i).get_C_PID().equals(PID)) {
                                    Class c = configList.get(i).getClass();
                                    Field field = c.getDeclaredField(mapList.get(typeName + "__" + columnName));
                                    field.setAccessible(true);
                                    field.set(configList.get(i), value);
                                }
                            }
                        }
                    }
                    dataList.add(configList.get(i));
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
            com.alibaba.fastjson.JSONObject jsonRoot = JSON.parseObject(content);
            com.alibaba.fastjson.JSONArray mainJson = jsonRoot.getJSONObject("result").getJSONArray("bagtypeitems");
            for (int i = 0; i < mainJson.size(); i++) {
                com.alibaba.fastjson.JSONObject mainObject = mainJson.getJSONObject(i);
                com.alibaba.fastjson.JSONArray bagItems = mainObject.getJSONArray("bagitems");
                for (int ii = 0; ii < bagItems.size(); ii++) {
                    com.alibaba.fastjson.JSONObject bagItem = bagItems.getJSONObject(ii);
                    String specid = bagItem.getString("specid");
                    com.alibaba.fastjson.JSONArray valueItems = bagItem.getJSONArray("valueitems");
                    if (valueItems.size() != 0) {
                        for (int iii = 0; iii < valueItems.size(); iii++) {
                            com.alibaba.fastjson.JSONObject valueItem = valueItems.getJSONObject(iii);
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

    public static void method_解析params_列名(String content, String filePath) {
        try {
            JSONObject jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
            JSONArray mainContent = jsonRoot.getJSONArray("paramtypeitems");
            ArrayList<Bean_Params> pidList = new ArrayList<>();
            if (mainContent.size() != 0 && mainContent != null) {
                JSONArray pidparamItems = mainContent.getJSONObject(0).getJSONArray("paramitems").getJSONObject(0).getJSONArray("valueitems");
                for (int i = 0; i < pidparamItems.size(); i++) {
                    com.alibaba.fastjson.JSONObject pidObject = pidparamItems.getJSONObject(i);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_解析config_列名(String content, String filePath) {
        try {
            JSONObject jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
            JSONArray mainJson = jsonRoot.getJSONArray("configtypeitems");
            ArrayList<Bean_Config> configList = new ArrayList<>();
            T_Config_AutoHome autoHome = new T_Config_AutoHome(0, 1, 2);
            //得到所需pid
//        System.out.println(mainJson);
            if (mainJson != null && mainJson.size() != 0) {
                com.alibaba.fastjson.JSONObject pidListObject = mainJson.getJSONObject(0).getJSONArray("configitems").getJSONObject(0);
                JSONArray pidArray = pidListObject.getJSONArray("valueitems");
                for (int i = 0; i < pidArray.size(); i++) {
                    com.alibaba.fastjson.JSONObject pidObject = pidArray.getJSONObject(i);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
