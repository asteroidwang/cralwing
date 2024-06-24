package com.wangtiantian.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.Bean_ModelURL;
import com.wangtiantian.entity.Bean_Version;
import com.wangtiantian.mapper.DataBaseMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DownLoadData {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public void downLoadBrandFactoryModel(String filePath) {
        for (char a = 'A'; a <= 'Z'; a++) {
            String webURL = "https://www.autohome.com.cn/grade/carhtml/" + a + ".html";
            Analysis_AutoHome_Pic.method_Jsoup_通用(webURL, filePath + "品牌/", String.valueOf(a));
        }
    }

    public void downLoadModel(String filePath) {
        try {
            method_下载版本(filePath);
            method_不在图片路径的车型页面(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downLoadParams_Config_Bag(String filePath) {
        try {
            ArrayList<Object> paramsGroups = DataBaseMethod.findDataByDownLoadStatus(0, "params");
            ArrayList<Object> configGroups = DataBaseMethod.findDataByDownLoadStatus(0, "config");
            ArrayList<Object> bagGroups = DataBaseMethod.findDataByDownLoadStatus(0, "bag");
            for (int i = 0; i < paramsGroups.size(); i++) {
                int group = ((Bean_Version) paramsGroups.get(i)).get_C_Group();
                ArrayList<Object> verItems = DataBaseMethod.findVersionIDByGroup(group);
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
                ArrayList<Object> verItems = DataBaseMethod.findVersionIDByGroup(group);
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
                ArrayList<Object> verItems = DataBaseMethod.findVersionIDByGroup(group);
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

    public static void method_下载版本(String filePath) {
        try {
            ArrayList<Object> onLine = DataBaseMethod.findDataByDownLoadStatus(0, "modelURL图片在售");
            ArrayList<Object> noSale = DataBaseMethod.findDataByDownLoadStatus(0, "modelURL图片停售");
            for (int i = 0; i < onLine.size(); i++) {
                String modURL = ((Bean_ModelURL) onLine.get(i)).get_C_ModelURL_图片页面在售();
                String modID = ((Bean_ModelURL) onLine.get(i)).get_C_ModelID();
                method_车型页面(modURL, filePath + "图片路径在售车型页面/", modID, "C_图片页面在售");
            }
            for (int i = 0; i < noSale.size(); i++) {
                String modURL_stop = ((Bean_ModelURL) noSale.get(i)).get_C_ModelURL_图片页面停售();
                String modID = ((Bean_ModelURL) noSale.get(i)).get_C_ModelID();
                method_车型页面(modURL_stop, filePath + "图片路径停售车型页面/", modID + "_t", "C_图片页面停售");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_车型页面(String webURL, String filePath, String fileName, String type) {
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
                DataBaseMethod.updateModelStatus(1, fileName.replace("_t", ""), type);
            }
        } else {
            System.out.println("null");
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }
    }

    public static void method_不在图片路径的车型页面(String filePath) {
        try {
            ArrayList<Object> onLine = DataBaseMethod.findDataByDownLoadStatus(0, "modelURL在售");
            ArrayList<Object> noSale = DataBaseMethod.findDataByDownLoadStatus(0, "modelURL停售");
            for (int i = 0; i < onLine.size(); i++) {
                String modURL = ((Bean_ModelURL) onLine.get(i)).get_C_ModelURL_在售();
                String modID = ((Bean_ModelURL) onLine.get(i)).get_C_ModelID();
//                System.out.println(modURL);
                Document mainDoc = Jsoup.connect(modURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
                if (mainDoc != null) {
                    Elements mainItems = mainDoc.select(".name-param");
                    if (mainItems.size() != 0) {
                        T_Config_File.method_写文件_根据路径创建文件夹(filePath, "在售车型页面/" + modID + ".txt", mainDoc.toString());
                        DataBaseMethod.updateModelStatus(1, modID, "C_在售");
                    }
                } else {
                    System.out.println("null");
                }
            }
            for (int i = 0; i < noSale.size(); i++) {
//                String modID = noSale.get(i);
                String modURL_stop = ((Bean_ModelURL) noSale.get(i)).get_C_ModelURL_停售();
                String modID = ((Bean_ModelURL) noSale.get(i)).get_C_ModelID();
                Document mainDoc_stop = Jsoup.connect(modURL_stop).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
                if (mainDoc_stop != null) {
                    Elements mainItems = mainDoc_stop.select(".modtab1");
                    if (mainItems.size() != 0) {
                        T_Config_File.method_写文件_根据路径创建文件夹(filePath, "停售车型页面/" + modID + "_t.txt", mainDoc_stop.toString());
                        DataBaseMethod.updateModelStatus(1, modID, "C_停售");
                    }
                } else {
                    System.out.println("null");
                }
//                method_车型页面(modURL_stop, filePath + "停售车型页面/", modID + "_t");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                    DataBaseMethod.updateDownLoadStatus(1, String.valueOf(i), "params");
                    System.out.println("params\t成功");
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
                    DataBaseMethod.updateDownLoadStatus(1, String.valueOf(i), "config");
                    System.out.println("config\t成功");
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
                    DataBaseMethod.updateDownLoadStatus(1, String.valueOf(i), "bag");
                    System.out.println("bag\t成功");
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
}
