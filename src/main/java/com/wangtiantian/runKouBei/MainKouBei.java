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
    private KouBeiDataBase kouBeiDataBase = new KouBeiDataBase();
    private String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/口碑评价数据/20240804/";

    public static void main(String[] args) {
        // 1.https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=3170&pageIndex=1&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0
        // new MainKouBei().getModelKouBeiFirstFileUrl();

        //
        // new MainKouBei().getModelKouBeiFileDataAndUrl();
        new MainKouBei().method_获取总页数并拼接url入库();
    }

    // 获取每个车型的第一页口碑
    public void getModelKouBeiFirstFileUrl() {
        try {
            ArrayList<Object> modList = kouBeiDataBase.getModIDList();
            ArrayList<ModelKouBei> dataList = new ArrayList<>();
            for (Object o : modList) {
                String mainUrl = "https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=" + ((Bean_Model) o).get_C_ModelID() + "&pageIndex=" + 1 + "&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0";
                ModelKouBei modelKouBei = new ModelKouBei();
                modelKouBei.set_C_ModelID(((Bean_Model) o).get_C_ModelID());
                modelKouBei.set_C_Page(1);
                modelKouBei.set_C_ModelKouBeiUrl(mainUrl);
                modelKouBei.set_C_IsFinish(0);
                modelKouBei.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                dataList.add(modelKouBei);

//                for (int i = 1; i < 99999; i++) {
//                    String mainUrl = "https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=" + ((Bean_Model) o).get_C_ModelID() + "&pageIndex=" + i + "&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0";
//                    Document mainDoc = null;
//                    try {
//                        mainDoc = Jsoup.parse(new URL(mainUrl).openStream(), "UTF-8", mainUrl);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (mainDoc != null) {
//                        JSONObject mainJson = JSONObject.parse(mainDoc.text()).getJSONObject("result");
//                        JSONArray jsonArray = mainJson.getJSONArray("list");
//                        if (jsonArray.size() == 0) {
//                            break;
//                        } else {
////                        T_C onfig_File.me
//                            ModelKouBei modelKouBei = new ModelKouBei();
//                            modelKouBei.set_C_ModelID(((Bean_Model) o).get_C_ModelID());
//                            modelKouBei.set_C_Page(i);
//                            modelKouBei.set_C_ModelKouBeiUrl(mainUrl);
//                            modelKouBei.set_C_IsFinish(0);
//                            modelKouBei.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                            dataList.add(modelKouBei);
//                        }
//                    }
//                }
            }
            HashSet<ModelKouBei> set = new HashSet<>(dataList);
            dataList.clear();
            dataList.addAll(set);
            kouBeiDataBase.modelUrlList(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 下载各个车型的第一页口碑页面
    public void getModelKouBeiFileDataAndUrl() {
        try {
            ArrayList<Object> modUrlList = kouBeiDataBase.getModFirstKouBei();
            for (Object o : modUrlList) {
                String modFirstUrl = ((ModelKouBei) o).get_C_ModelKouBeiUrl();
                Document mainDoc = null;
                try {
                    mainDoc = Jsoup.parse(new URL(modFirstUrl).openStream(), "UTF-8", modFirstUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mainDoc != null) {
                    T_Config_File.method_写文件_根据路径创建文件夹(filePath + ((ModelKouBei) o).get_C_ModelID() + "/", ((ModelKouBei) o).get_C_ModelID() + "_1.txt", mainDoc.text());
                    kouBeiDataBase.update下载状态(modFirstUrl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取总页数，并拼接相应url入库
    public void method_获取总页数并拼接url入库() {
        try {
            ArrayList<String> folderList = T_Config_File.method_获取文件夹名称(filePath);
            ArrayList<ModelKouBei> dataList = new ArrayList<>();
            for (String folderName : folderList) {
                String content = T_Config_File.method_读取文件内容(filePath + folderName + "/" + folderName + "_1.txt");
                int pageCount = JSONObject.parse(content).getJSONObject("result").getInteger("pagecount");
                for (int i = 2; i < pageCount + 1; i++) {
                    String mainUrl = "https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=" + folderName + "&pageIndex=" + i + "&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0";
                    ModelKouBei modelKouBei = new ModelKouBei();
                    modelKouBei.set_C_ModelID(folderName);
                    modelKouBei.set_C_Page(1);
                    modelKouBei.set_C_ModelKouBeiUrl(mainUrl);
                    modelKouBei.set_C_IsFinish(0);
                    modelKouBei.set_C_CountPage(pageCount);
                    modelKouBei.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    dataList.add(modelKouBei);
                }
            }
            HashSet<ModelKouBei> set = new HashSet<>(dataList);
            dataList.clear();
            dataList.addAll(set);
            kouBeiDataBase.modelUrlList(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
