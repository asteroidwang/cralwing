package com.wangtiantian.runKouBei;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.koubei.Bean_KouBei_FenYeUrl;
import com.wangtiantian.entity.koubei.Bean_KouBei_KouBeiShortInfo;
import com.wangtiantian.mapper.KouBei_DataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RunKouBei {
    private int num = 0;

    public static void main(String[] args) throws InterruptedException {
        // 1.下载所有车型的第一页并解析获取总页数 为了获取总页数下载其他分页
        // 2.入库所有分页url数据并下载
        // 3.解析所有已下载的分页url数据并入库 为了获取showId 下载口碑详情页
        // 4.解析口碑数据 口碑图片数据并入库 获取一级评论信息和二级回复信息 追加口碑数据
        // 5.下载一级评论信息和二级回复信息 并解析入库
//        String filePathCommon = "/Users/asteroid/所有文件数据/爬取网页原始数据/口碑评价数据/20241016/";
        String filePathCommon = "D:\\爬取网页源数据\\汽车之家\\口碑评价数据\\20241017\\";
        RunKouBei runKouBei = new RunKouBei();
//        if (runKouBei.downloadFirstPageByModelId(filePathCommon + "口碑分页数据\\")) {
//            runKouBei.parseFirstPageByModelId(filePathCommon + "口碑分页数据\\");
//        }
//        if (runKouBei.downLoadOtherPage(filePathCommon + "口碑分页数据\\")) {
//            runKouBei.parseOtherPage(filePathCommon + "口碑分页数据\\");
//        }
        if (runKouBei.downLoadKouBeiDetailsPage(filePathCommon + "口碑详情页\\")) {
            runKouBei.parseDetailsKouBei(filePathCommon + "口碑详情页\\");
        }

    }

    public Boolean downloadFirstPageByModelId(String filePath) {
        // 1.获取最新的车型数据
        KouBei_DataBase kouBeiDataBase = new KouBei_DataBase();
        ArrayList<String> modelIdList = kouBeiDataBase.getLatestModelIdData();
        if (modelIdList.size() < 36) {
            for (String modelId : modelIdList) {
                String modelKouBeiUrl = "https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=" + modelId + "&pageIndex=1&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0";
                if (T_Config_File.method_访问url获取Json普通版(modelKouBeiUrl, "UTF-8", filePath, modelId + "_1.txt")) {
                    kouBeiDataBase.update_下载状态(modelId, 1);
                }
            }
            return true;
        } else {
            try {
                List<List<String>> list = IntStream.range(0, 6).mapToObj(i -> modelIdList.subList(i * (modelIdList.size() + 5) / 6, Math.min((i + 1) * (modelIdList.size() + 5) / 6, modelIdList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(list.size());
                for (int i = 0; i < list.size(); i++) {
                    FirstPageByModelThread moreThread = new FirstPageByModelThread(list.get(i), filePath, latch);
                    Thread thread = new Thread(moreThread);
                    thread.start();
                }
                latch.await();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public void parseFirstPageByModelId(String filePath) {
        ArrayList<String> fileNameList = T_Config_File.method_获取文件名称(filePath);
        ArrayList<Object> dataList = new ArrayList<>();
        for (String fileName : fileNameList) {
            String modelId = fileName.replace("_1.txt", "");
            String content = T_Config_File.method_读取文件内容(filePath + fileName);
            JSONObject jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
            int pageCount = jsonRoot.getInteger("pagecount");
            for (int i = 1; i < pageCount + 1; i++) {
                int isFinish = 0;
                if (i == 1) {
                    isFinish = 1;
                }
                Bean_KouBei_FenYeUrl kouBeiFenYeUrl = new Bean_KouBei_FenYeUrl();
                kouBeiFenYeUrl.set_C_Page(i);
                kouBeiFenYeUrl.set_C_FenYeUrl("https://koubeiipv6.app.autohome.com.cn/pc/series/list?pm=3&seriesId=" + modelId + "&pageIndex=" + i + "&pageSize=20&yearid=0&ge=0&seriesSummaryKey=0&order=0");
                kouBeiFenYeUrl.set_C_ModelID(modelId);
                kouBeiFenYeUrl.set_C_IsFinish(isFinish);
                kouBeiFenYeUrl.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                kouBeiFenYeUrl.set_C_PageCount(pageCount);
                dataList.add(kouBeiFenYeUrl);
            }
        }
        new KouBei_DataBase().insertFenYeUrl(dataList);
    }

    public Boolean downLoadOtherPage(String filePath) {
        KouBei_DataBase kouBeiDataBase = new KouBei_DataBase();
        ArrayList<Object> dataList = kouBeiDataBase.getFenYeUrl();
        if (dataList.size() > 36) {

            try {
                List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(list.size());
                for (int i = 0; i < list.size(); i++) {
                    OtherPageThread moreThread = new OtherPageThread(list.get(i), filePath, latch);
                    Thread thread = new Thread(moreThread);
                    thread.start();
                }
                latch.await();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            for (Object bean : dataList) {
                String modelId = ((Bean_KouBei_FenYeUrl) bean).get_C_ModelID();
                String modelKouBeiUrl = ((Bean_KouBei_FenYeUrl) bean).get_C_FenYeUrl();
                int page = ((Bean_KouBei_FenYeUrl) bean).get_C_Page();
                if (T_Config_File.method_访问url获取Json普通版(modelKouBeiUrl, "UTF-8", filePath, modelId + "_" + page + ".txt")) {
                    kouBeiDataBase.update_修改已下载的分页数据状态(modelId, page, 1);
                }
            }
            return true;
        }
    }

    public void parseOtherPage(String filePath) {
        ArrayList<String> fileNameList = T_Config_File.method_获取文件名称(filePath);
        ArrayList<Object> dataList = new ArrayList<>();

        for (String fileName : fileNameList) {
            String content = T_Config_File.method_读取文件内容(filePath + fileName);
            JSONObject jsonRoot = null;
            try {
                jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
            } catch (Exception e) {
                KouBei_DataBase kouBeiDataBase = new KouBei_DataBase();
                kouBeiDataBase.update_修改已下载的分页数据状态(fileName.split("_")[0], Integer.parseInt(fileName.split("_")[1].replace(".txt", "")), 0);
            }
//            JSONObject jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
            if (jsonRoot != null) {
                JSONArray jsonArray = jsonRoot.getJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = ((JSONObject) jsonArray.get(i));
                        String showId = jsonObject.getString("showId");
                        String kouBeiId = jsonObject.getString("Koubeiid");
                        String modelId = jsonObject.getString("specid");
                        String buyPlace = jsonObject.getString("buyplace");
                        int kuBeiType = jsonObject.getInteger("koubeiType");
                        String userId = jsonObject.getString("userid");
                        String dealerName = jsonObject.getString("dealerName");
                        String dealerId = jsonObject.getString("dealerId");
                        JSONArray medalsArray = jsonObject.getJSONArray("medals");
                        String medalsName = "";
                        String medalsType = "";
                        for (int j = 0; j < medalsArray.size(); j++) {
                            medalsName = ((JSONObject) medalsArray.get(j)).getString("name");
                            medalsType = ((JSONObject) medalsArray.get(j)).getString("type");
                        }
                        Bean_KouBei_KouBeiShortInfo kouBei_kouBeiShortInfo = new Bean_KouBei_KouBeiShortInfo();
                        kouBei_kouBeiShortInfo.set_C_ShowId(showId);
                        kouBei_kouBeiShortInfo.set_C_KouBeiId(kouBeiId);
                        kouBei_kouBeiShortInfo.set_C_ModelId(modelId);
                        kouBei_kouBeiShortInfo.set_C_BuyPlace(buyPlace);
                        kouBei_kouBeiShortInfo.set_C_DealerName(dealerName == null ? "" : dealerName);
                        kouBei_kouBeiShortInfo.set_C_DealerId(dealerId == null ? "" : dealerId);
                        kouBei_kouBeiShortInfo.set_C_MedalsName(medalsName);
                        kouBei_kouBeiShortInfo.set_C_MedalsType(medalsType);
                        kouBei_kouBeiShortInfo.set_C_IsFinish(0);
                        kouBei_kouBeiShortInfo.set_C_FileName(fileName);
                        kouBei_kouBeiShortInfo.set_C_Content(jsonObject.toString());
                        kouBei_kouBeiShortInfo.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        dataList.add(kouBei_kouBeiShortInfo);
                        if (dataList.size() >= 10000) {
                            new KouBei_DataBase().insertShortKouBeiInfo(dataList);
                            dataList.clear();
                        }
                    }
                }

            }

        }
        new KouBei_DataBase().insertShortKouBeiInfo(dataList);
    }

    public Boolean downLoadKouBeiDetailsPage(String filePath) throws InterruptedException {
        KouBei_DataBase kouBeiDataBase = new KouBei_DataBase();
        int count = kouBeiDataBase.getShortKouBeiDataCount();
        if (count >= 36) {
            for (int kk = 0; kk < count / 10000; kk++) {
                ArrayList<Object> dataList = kouBeiDataBase.getShortKouBeiDataForeach(kk * 10000);
                List<List<Object>> list = IntStream.range(0, 6)
                        .mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(list.size());
                for (int i = 0; i < list.size(); i++) {
                    DetailsKouBeiInfoThread moreThread = new DetailsKouBeiInfoThread(list.get(i), filePath, latch);
                    Thread thread = new Thread(moreThread);
                    thread.start();
                }
                // 等待所有子线程完成
                latch.await();
                kouBeiDataBase.update_修改已下载的口碑详情页数据(method_确认详情页的下载数据(filePath));
                if (kouBeiDataBase.getShortKouBeiData().size() > 0) {
                    num++;
                    if (num < 4) {
                        downLoadKouBeiDetailsPage(filePath);
                    }
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            ArrayList<Object> dataList = kouBeiDataBase.getShortKouBeiData();
            for (Object o : dataList) {
                String showId = ((Bean_KouBei_KouBeiShortInfo) o).get_C_ShowId();
                String kbId = ((Bean_KouBei_KouBeiShortInfo) o).get_C_KouBeiId();
                String mainUrl = "https://k.autohome.com.cn/detail/view_" + showId + ".html#pvareaid=2112108";
                T_Config_File.method_访问url获取网页源码普通版(mainUrl, "UTF-8", filePath, showId + "_" + kbId + ".txt");
            }
            kouBeiDataBase.update_修改已下载的口碑详情页数据(method_确认详情页的下载数据(filePath));
            if (kouBeiDataBase.getShortKouBeiData().size() > 0) {
                num++;
                if (num < 4) {
                    downLoadKouBeiDetailsPage(filePath);
                }

                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public void parseDetailsKouBei(String filePath) {
        List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
        for (String fileName : fileList) {
            String content = T_Config_File.method_读取文件内容(fileName);
            Document mainDoc = Jsoup.parse(content);
            System.out.println(mainDoc);

        }

    }

    public String method_确认详情页的下载数据(String filePath) {
        List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
        fileList = fileList.stream().map(value -> value.replace(".txt", "").split("_")[0].replace(filePath, "")).distinct().collect(Collectors.toList());
        return "'" + fileList.toString().replace(", ", "','").substring(1, fileList.toString().length() - 1) + "'";
    }
}
