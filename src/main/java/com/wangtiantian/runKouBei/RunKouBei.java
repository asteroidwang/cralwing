package com.wangtiantian.runKouBei;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.koubei.*;
import com.wangtiantian.mapper.KouBei_DataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
        // 4.解析口碑数据 追加口碑数据  口碑图片数据并入库 获取一级评论信息和二级回复信息
        // 5.下载一级评论信息和二级回复信息 并解析入库
//        String filePathCommon = "/Users/asteroid/所有文件数据/爬取网页原始数据/口碑评价数据/";
        String filePathCommon = "D:\\爬取网页源数据\\汽车之家\\口碑评价数据\\20241017\\";
        RunKouBei runKouBei = new RunKouBei();
//        if (runKouBei.downloadFirstPageByModelId(filePathCommon + "口碑分页数据\\")) {
//            runKouBei.parseFirstPageByModelId(filePathCommon + "口碑分页数据\\");
//        }
//        if (runKouBei.downLoadOtherPage(filePathCommon + "口碑分页数据\\")) {
//            runKouBei.parseOtherPage(filePathCommon + "口碑分页数据\\");
//        }

//        if (runKouBei.downLoadKouBeiDetailsPage(filePathCommon + "口碑详情页\\")) {
//            runKouBei.parseDetailsKouBei(filePathCommon + "口碑详情页\\");
        runKouBei.parseDetailsKouBei(filePathCommon + "口碑详情页\\");
//        }



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
//        kouBeiDataBase.update_修改已下载的口碑详情页数据(method_确认详情页的下载数据(filePath));
        int count = kouBeiDataBase.getShortKouBeiDataCount();
        if (count != 0) {
            if (count >= 36) {
                for (int kk = 0; kk < count / 10000; kk++) {
                    ArrayList<Object> dataList = kouBeiDataBase.getShortKouBeiDataForeach(kk * 10000);
                    List<List<Object>> list = IntStream.range(0, 6)
                            .mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                            .collect(Collectors.toList());
                    CountDownLatch latch = new CountDownLatch(list.size());
                    for (int i = 0; i < list.size(); i++) {
                        DetailsKouBeiInfoThread moreThread = new DetailsKouBeiInfoThread(list.get(i), filePath);
                        Thread thread = new Thread(() -> {
                            try {
                                moreThread.run();
                            } finally {
                                latch.countDown();
                            }

                        });
                        thread.start();
                    }
                    // 等待所有子线程完成
                    latch.await();
                    method_确认详情页的下载数据(filePath);
                }
                if (kouBeiDataBase.getShortKouBeiDataCount() > 0) {
                    num++;
                    if (num < 4) {
                        downLoadKouBeiDetailsPage(filePath);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                ArrayList<Object> dataList = kouBeiDataBase.getShortKouBeiDataForeach(0);
                for (Object o : dataList) {
                    String showId = ((Bean_KouBei_KouBeiShortInfo) o).get_C_ShowId();
                    String kbId = ((Bean_KouBei_KouBeiShortInfo) o).get_C_KouBeiId();
                    String mainUrl = "https://k.autohome.com.cn/detail/view_" + showId + ".html#pvareaid=2112108";
                    T_Config_File.method_访问url获取网页源码普通版(mainUrl, "UTF-8", filePath, showId + "_" + kbId + ".txt");
                }
                method_确认详情页的下载数据(filePath);
                if (kouBeiDataBase.getShortKouBeiDataCount() > 0) {
                    num++;
//                if (num < 4) {
                    downLoadKouBeiDetailsPage(filePath);
//                }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
        return true;

    }

    public void parseDetailsKouBei(String filePath) {
        List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
        ArrayList<Object> dataListKouBeiInfo = new ArrayList<>();
        ArrayList<Object> dataListKouBeiImg = new ArrayList<>();
        ArrayList<Object> zhuiPing = new ArrayList<>();
        ArrayList<Object> pictureList = new ArrayList<>();
        for (String fileName : fileList) {
            String mainContent = T_Config_File.method_读取文件内容(fileName);
            String fileNameShort = fileName.replace(filePath, "").replace(".txt", "");
            if (!fileNameShort.equals(".DS_Store")) {
                String showId = fileNameShort.split("_")[0];
                String kbId = fileNameShort.split("_")[1];
                KouBeiData kouBeiData = new KouBeiData();
                try {
                    if (mainContent.contains("您访问的口碑存在异常被隐藏")) {
                        kouBeiData.set_C_ShowID(showId);
                        kouBeiData.set_C_DealerID("");
                        kouBeiData.set_C_DealerName("");
                        kouBeiData.set_C_KouBeiContent("您访问的口碑存在异常被隐藏");
                        kouBeiData.set_C_UserName("");
                        kouBeiData.set_C_UserID("");
                        kouBeiData.set_C_VersionID("");
                        kouBeiData.set_C_VersionName("");
                        kouBeiData.set_C_ModelID("");
                        kouBeiData.set_C_UpTime("");
                        kouBeiData.set_C_KoubeiID("");
                        kouBeiData.set_C_Title("");
                        kouBeiData.set_C_XingShiLiCheng("");
                        kouBeiData.set_C_BaiGongLiYouHao("");
                        kouBeiData.set_C_LuoCheGouMaiJia("");
                        kouBeiData.set_C_GouMaiShiJian("");
                        kouBeiData.set_C_GouMaiDiDian("");
                        kouBeiData.set_C_ZuiManYi("");
                        kouBeiData.set_C_ZuiBuManyi("");
                        kouBeiData.set_C_JiaShiGanShou("");
                        kouBeiData.set_C_CaoKong("");
                        kouBeiData.set_C_ShuShiXing("");
                        kouBeiData.set_C_NeiShi("");
                        kouBeiData.set_C_YouHao("");
                        kouBeiData.set_C_XingJiaBi("");
                        kouBeiData.set_C_KongJian("");
                        kouBeiData.set_C_WaiGuan("");
                        kouBeiData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    } else {
                        Document mainDoc = Jsoup.parse(mainContent);
                        Elements mainItems = mainDoc.select(".con-left.fl");
                        // 图片数据
                        Elements mainItemsImg = mainDoc.select(".lazyload");
                        if (mainItemsImg.size() > 0) {
                            for (int j = 0; j < mainItemsImg.size(); j++) {
                                KouBeiPicture kouBeiPicture = new KouBeiPicture();
                                kouBeiPicture.set_C_PictureUrl(mainItemsImg.get(j).attr("data-src").contains("https") ? mainItemsImg.get(j).attr("data-src") : "https:" + mainItemsImg.get(j).attr("data-src"));
                                kouBeiPicture.set_C_IsFinish(0);
                                kouBeiPicture.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                kouBeiPicture.set_C_ShowID(showId);
                                kouBeiPicture.set_C_KouBeiID(kbId);
                                kouBeiPicture.set_C_NumCount(mainItemsImg.size());
                                kouBeiPicture.set_C_Position(String.valueOf(j));
                                dataListKouBeiImg.add(kouBeiPicture);
                            }
                        }

                        // 质量评价
                        Elements mainItemsQulity = mainDoc.select(".qulity-nop");
                        if (mainItemsQulity != null && !mainItemsQulity.toString().equals("")) {
                            KouBeiDataPlus kouBeiDataPlus = new KouBeiDataPlus();
                            String time = mainItems.select("span").text();
                            String C_购车后追评时间间隔 = time.split(" \\| ")[0];
                            String C_追评时间 = time.split(" \\| ")[1];
                            String C_追评的全部内容 = mainItems.text();
                            StringBuffer C_追评图片 = new StringBuffer();
                            // 追评图片数据
                            Elements imgItems = mainItems.select(".multi-imgList").select("li");
                            for (int i = 0; i < imgItems.size(); i++) {
                                String img = imgItems.get(i).select("img").attr("data-src");
                                KouBeiPicture kouBeiPicture = new KouBeiPicture();
                                kouBeiPicture.set_C_ShowID(fileName.replace(".txt",""));
                                kouBeiPicture.set_C_PictureUrl(img);
                                kouBeiPicture.set_C_IsFinish(0);
                                kouBeiPicture.set_C_NumCount(imgItems.size());
                                kouBeiPicture.set_C_Position("质量评价_"+String.valueOf(i));
                                kouBeiPicture.set_C_KouBeiID("");
                                kouBeiPicture.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                pictureList.add(kouBeiPicture);
                            }
                            kouBeiDataPlus.set_C_ShowID(fileName.replace(".txt",""));
                            kouBeiDataPlus.set_C_KouBeiID("");
                            kouBeiDataPlus.set_C_Content_追加(C_追评的全部内容);
                            kouBeiDataPlus.set_C_IsFinish(0);
                            kouBeiDataPlus.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            kouBeiDataPlus.set_C_购车后追评时间间隔(C_购车后追评时间间隔);
                            kouBeiDataPlus.set_C_追评时间(C_追评时间);
                            kouBeiDataPlus.set_C_Html(mainItems.toString());
                            zhuiPing.add(kouBeiDataPlus);
                        }
                        // 购车后的追评
                        Elements mainItemsPlus = mainDoc.select(".kb-conplus");
                        if (mainItemsPlus != null && !mainItemsPlus.toString().equals("")) {
                            for (int i = 0; i < mainItemsPlus.size(); i++) {
                                KouBeiDataPlus kouBeiDataPlus = new KouBeiDataPlus();
                                String time = mainItemsPlus.get(i).select("span").text();
                                String C_购车后追评时间间隔 = time.split(" \\| ")[0];
                                String C_追评时间 = time.split(" \\| ")[1];
                                String C_追评的全部内容 = mainItemsPlus.get(i).text();
                                StringBuffer C_追评图片 = new StringBuffer();
                                Elements imgItems = mainItemsPlus.select(".multi-imgList").select("li");
                                for (int j = 0; j < imgItems.size(); j++) {
                                    String img = imgItems.get(j).select("img").attr("data-src");
                                    KouBeiPicture kouBeiPicture = new KouBeiPicture();
                                    kouBeiPicture.set_C_ShowID(fileName.replace(".txt",""));
                                    kouBeiPicture.set_C_PictureUrl(img);
                                    kouBeiPicture.set_C_IsFinish(0);
                                    kouBeiPicture.set_C_NumCount(imgItems.size());
                                    kouBeiPicture.set_C_Position("第"+i+"次追评-"+j);
                                    kouBeiPicture.set_C_KouBeiID("");
                                    kouBeiPicture.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    pictureList.add(kouBeiPicture);
                                }
                                kouBeiDataPlus.set_C_ShowID(fileName.replace(".txt",""));
                                kouBeiDataPlus.set_C_KouBeiID("");
                                kouBeiDataPlus.set_C_Content_追加(C_追评的全部内容);
                                kouBeiDataPlus.set_C_IsFinish(0);
                                kouBeiDataPlus.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                kouBeiDataPlus.set_C_购车后追评时间间隔(C_购车后追评时间间隔);
                                kouBeiDataPlus.set_C_追评时间(C_追评时间);
                                kouBeiDataPlus.set_C_Html(mainItemsPlus.get(i).toString());
                                zhuiPing.add(kouBeiDataPlus);
                            }
                        }

                        String title = mainItems.select(".title").text();
                        String dealerId = mainItems.select(".grey.car-dealer").select("a").toString().equals("") ? "" : mainItems.select(".grey.car-dealer").select("a").attr("href").split("/")[3].split("#")[0];
                        String dealerName = mainItems.select(".grey.car-dealer").select("a").text();
                        String kouBeiContent = mainItems.toString();
                        String userName = mainItems.select(".msg.fl").select("p").select("a").text();
                        String userId = mainItems.select(".msg.fl").select("p").select("a").attr("href").split("/")[3];
                        String versionName = mainItems.select(".car-msg").select(".main-blue").text();
                        String versionId = mainItems.select(".car-msg").select(".main-blue").select("a").get(1).attr("href").split("/")[4];
                        String modelId = mainItems.select(".car-msg").select(".main-blue").select("a").get(0).attr("href").split("/")[3];
                        String upTime = mainItems.select(".timeline-con").select("span").get(0).text();
                        String tempString1 = mainItems.select("script").toString().substring(mainItems.select("script").toString().indexOf("koubeiid"));
                        String tempString2 = tempString1.substring(tempString1.indexOf(";"));
                        String kouBeiId = tempString1.replace(tempString2, "").replace("koubeiid = ", "");
                        Elements itemInfo = mainItems.select(".kb-con").select("li");
                        String xingShiLiCheng = "";
                        String baiGongLiYouHao = "";
                        String luoCheGouMaiJia = "";
                        String gouMaiShiJian = "";
                        String gouMaiDiDian = "";
                        for (int i = 0; i < itemInfo.size(); i++) {
                            String columnName = itemInfo.get(i).select(".name").text().replace(" ", "").trim();
                            String value = itemInfo.get(i).select(".key").text();
                            if (columnName.equals("行驶里程")) {
                                xingShiLiCheng = value;
                            } else if (columnName.equals("百公里油耗")) {
                                baiGongLiYouHao = value;
                            } else if (columnName.equals("裸车购买价")) {
                                luoCheGouMaiJia = value;
                            } else if (columnName.equals("购买时间")) {
                                gouMaiShiJian = value;
                            } else if (columnName.equals("购买地点")) {
                                gouMaiDiDian = value;
                            }
                        }
                        String zuiManYi = mainItems.select(".satisfied.kb-item").text();
                        String zuiBuManYi = mainItems.select(".unsatis.kb-item").text();
                        String jiaShiGanShou = "";
                        String caoKong = "";
                        String shuShiXing = "";
                        String neiShi = "";
                        String youHao = "";
                        String xingJiaBi = "";
                        String kongJian = "";
                        String waiGuan = "";
                        Elements itemSpaceInfo = mainItems.select(".space.kb-item");
                        for (int i = 0; i < itemSpaceInfo.size(); i++) {
                            String c1 = itemSpaceInfo.get(i).select("h1").text();
                            String v1 = itemSpaceInfo.get(i).select(".star-num").text();
                            String tempC1 = c1.replace(v1, "").replace(" ", "").trim();
                            if (tempC1.equals("驾驶感受")) {
                                jiaShiGanShou = v1 + "->" + itemSpaceInfo.get(i).select("p").text();
                            } else if (tempC1.equals("操控")) {
                                caoKong = v1 + "->" + itemSpaceInfo.get(i).select("p").text();
                            } else if (tempC1.equals("舒适性")) {
                                shuShiXing = v1 + "->" + itemSpaceInfo.get(i).select("p").text();
                            } else if (tempC1.equals("内饰")) {
                                neiShi = v1 + "->" + itemSpaceInfo.get(i).select("p").text();
                            } else if (tempC1.equals("油耗")) {
                                youHao = v1 + "->" + itemSpaceInfo.get(i).select("p").text();
                            } else if (tempC1.equals("性价比")) {
                                xingJiaBi = v1 + "->" + itemSpaceInfo.get(i).select("p").text();
                            } else if (tempC1.equals("空间")) {
                                kongJian = v1 + "->" + itemSpaceInfo.get(i).select("p").text();
                            } else if (tempC1.equals("外观")) {
                                waiGuan = v1 + "->" + itemSpaceInfo.get(i).select("p").text();
                            }
                        }
                        kouBeiData.set_C_ShowID(showId);
                        kouBeiData.set_C_DealerID(dealerId);
                        kouBeiData.set_C_DealerName(dealerName);
                        kouBeiData.set_C_KouBeiContent(kouBeiContent);
                        kouBeiData.set_C_UserName(userName);
                        kouBeiData.set_C_UserID(userId);
                        kouBeiData.set_C_VersionID(versionId);
                        kouBeiData.set_C_VersionName(versionName);
                        kouBeiData.set_C_ModelID(modelId);
                        kouBeiData.set_C_UpTime(upTime);
                        kouBeiData.set_C_KoubeiID(kouBeiId);
                        kouBeiData.set_C_Title(title);
                        kouBeiData.set_C_XingShiLiCheng(xingShiLiCheng);
                        kouBeiData.set_C_BaiGongLiYouHao(baiGongLiYouHao);
                        kouBeiData.set_C_LuoCheGouMaiJia(luoCheGouMaiJia);
                        kouBeiData.set_C_GouMaiShiJian(gouMaiShiJian);
                        kouBeiData.set_C_GouMaiDiDian(gouMaiDiDian);
                        kouBeiData.set_C_ZuiManYi(zuiManYi);
                        kouBeiData.set_C_ZuiBuManyi(zuiBuManYi);
                        kouBeiData.set_C_JiaShiGanShou(jiaShiGanShou);
                        kouBeiData.set_C_CaoKong(caoKong);
                        kouBeiData.set_C_ShuShiXing(shuShiXing);
                        kouBeiData.set_C_NeiShi(neiShi);
                        kouBeiData.set_C_YouHao(youHao);
                        kouBeiData.set_C_XingJiaBi(xingJiaBi);
                        kouBeiData.set_C_KongJian(kongJian);
                        kouBeiData.set_C_WaiGuan(waiGuan);
                        kouBeiData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        dataListKouBeiInfo.add(kouBeiData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        new KouBei_DataBase().insertKouBeiInfo(dataListKouBeiInfo);
        new KouBei_DataBase().insertKouBeiImgUrl(dataListKouBeiImg);
    }

    public void method_确认详情页的下载数据(String filePath) {
        KouBei_DataBase kouBeiDataBase = new KouBei_DataBase();
        List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
        fileList = fileList.stream().map(value -> value.replace(".txt", "").split("_")[0].replace(filePath, "")).distinct().collect(Collectors.toList());
        int batchSize = 100;
        for (int i = 0; i < fileList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, fileList.size());
            List<String> batchList = fileList.subList(i, end);
            kouBeiDataBase.update_修改已下载的口碑详情页数据("'" + batchList.toString().replace(", ", "','").replace("[", "").replace("]", "") + "'");
        }
    }
}
