package com.wangtiantian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.controller.AnalysisData;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Params;
import com.wangtiantian.entity.Bean_Version;
import com.wangtiantian.mapper.DataBaseMethod;
import com.wangtiantian.runConfigData.MainConfigData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class CheckData {
    public static void main(String[] args) {
        parse_解析含有版本数据的文件("/Users/wangtiantian/MyDisk/汽车之家/配置数据/20250210/含版本数据的文件");
    }
    public static void parse_解析含有版本数据的文件(String filePath) {
        try {
            ArrayList<Object> versionData = new ArrayList<>();
            ArrayList<Object> versionData1 = new ArrayList<>();
            ArrayList<Object> versionData2 = new ArrayList<>();
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + "_非图片路径/");
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + "_非图片路径/" + fileName);
                if (fileName.contains("停售")) {
                    versionData1.addAll(method_解析非图片路径_停售(content, fileName.replace("_停售.txt", "")));
                    versionData.addAll(method_解析非图片路径_停售(content, fileName.replace("_停售.txt", "")));
                } else if (fileName.contains("在售")) {
                    versionData1.addAll(method_解析非图片路径_在售_New(content, fileName.replace("_在售.txt", ""), "在售"));
                    versionData.addAll(method_解析非图片路径_在售_New(content, fileName.replace("_在售.txt", ""), "在售"));
                } else {
                    versionData1.addAll(method_解析非图片路径_在售_New(content, fileName.replace("_即将销售.txt", ""), "即将销售"));
                    versionData.addAll(method_解析非图片路径_在售_New(content, fileName.replace("_即将销售.txt", ""), "即将销售"));
                }
            }
            ArrayList<String> fileListPic = T_Config_File.method_获取文件名称(filePath + "_图片路径/");
            for (String fileName : fileListPic) {
                String content = T_Config_File.method_读取文件内容(filePath + "_图片路径/" + fileName);
                versionData2.addAll(method_解析图片路径(content, fileName.replace("_图片页面停售.txt", "").replace("_图片页面在售.txt", "")));
                versionData.addAll(method_解析图片路径(content, fileName.replace("_图片页面停售.txt", "").replace("_图片页面在售.txt", "")));
            }


            HashSet<Object> set1 = new HashSet<>(versionData1);
            versionData1.clear();
            versionData1.addAll(set1);
            HashSet<Object> set2 = new HashSet<>(versionData2);
            versionData2.clear();
            versionData2.addAll(set2);

            HashSet<Object> set = new HashSet<>(versionData);
            versionData.clear();
            versionData.addAll(set);
            System.out.println(versionData1.size()+"\t"+versionData2.size()+"\t"+ versionData.size());
//            new DataBaseMethod().method_入库版本数据(versionData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<Bean_Version> method_解析非图片路径_停售(String content, String modID) {
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

    public static ArrayList<Bean_Version> method_解析非图片路径_在售_New(String content, String modID, String status) {
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

    public static ArrayList<Bean_Version> method_解析非图片路径_在售(String content, String modID) {
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

    public static ArrayList<Bean_Version> method_解析图片路径(String content, String modID) {
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
}
