package com.wangtiantian.runKouBei;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.koubei.*;
import com.wangtiantian.mapper.KouBeiDataBase;
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

public class KouBeiMethod {
    private KouBeiDataBase kouBeiDataBase = new KouBeiDataBase();
    //    private String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/口碑评价数据/20240804/";
    private String filePath = "E:/汽车之家/口碑评价数据/20240804/";

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

    // 获取上一步入库的未下载的url下载口碑页面数据
    public void parseKouBeiToGetShoeId() {
        try {
            ArrayList<String> folderList = T_Config_File.method_获取文件夹名称(filePath);
            ArrayList<KouBeiInfo> dataList = new ArrayList<>();
            int num = 0;
            for (String folderName : folderList) {
                ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + folderName + "/");
                num += fileList.size();
                for (String fileName : fileList) {
                    String content = T_Config_File.method_读取文件内容(filePath + folderName + "/" + fileName);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = JSONObject.parseObject(content).getJSONObject("result");
                    } catch (Exception e) {
                        System.out.println(filePath + folderName + "/" + fileName);
                    }
                    if (jsonObject != null) {
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        if (jsonArray != null && jsonArray.size() != 0) {
                            jsonArray.forEach(item -> {
                                JSONObject jsonObject1 = (JSONObject) item;
                                KouBeiInfo kouBeiInfo = new KouBeiInfo();
                                kouBeiInfo.set_C_KouBeiUrl("https://k.autohome.com.cn/detail/view_" + jsonObject1.getString("showId") + ".html#pvareaid=2112108");
                                kouBeiInfo.set_C_DealerID(jsonObject1.getString("dealerId"));
                                kouBeiInfo.set_C_ShowID(jsonObject1.getString("showId"));
                                kouBeiInfo.set_C_ModelID(folderName);
                                kouBeiInfo.set_C_VersionID(jsonObject1.getString("specid"));
                                kouBeiInfo.set_C_IsFinish(0);
                                kouBeiInfo.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                dataList.add(kouBeiInfo);
                            });
                        }
                    }

                }
            }
            HashSet<KouBeiInfo> set = new HashSet<>(dataList);
            dataList.clear();
            dataList.addAll(set);
            kouBeiDataBase.insetForeachKouBeiInfo(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取已下载的所有口碑具体页面
    public void getKouBeiDesc() {
        try {
            ArrayList<KouBeiData> dataArrayList = new ArrayList<>();
            ArrayList<Object> dataList = kouBeiDataBase.findAllKouBeiShowId();
            for (Object o : dataList) {
                String showId = ((KouBeiInfo) o).get_C_ShowID();
                try {
                    String content = T_Config_File.method_读取文件内容(filePath + "口碑具体页面数据/" + showId + ".txt");
                    dataArrayList.add(parseKouBeiData(content, showId));
                    if (dataArrayList.size() > 1000) {
                        HashSet<KouBeiData> set = new HashSet<>(dataArrayList);
                        dataArrayList.clear();
                        dataArrayList.addAll(set);
                        kouBeiDataBase.insetForeachKouBeiData(dataArrayList);
                        dataArrayList.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            HashSet<KouBeiData> set = new HashSet<>(dataArrayList);
            dataArrayList.clear();
            dataArrayList.addAll(set);
            kouBeiDataBase.insetForeachKouBeiData(dataArrayList);
//            dataArrayList.add(parseKouBeiData(T_Config_File.method_读取文件内容("/Users/asteroid/Downloads/0177232yn764r30csg00000000.txt"), "0177232yn764r30csg00000000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 补充缺失的下载的数据
    public void getKouBeiDescQueShi() {
        try {
            ArrayList<KouBeiData> dataArrayList = new ArrayList<>();
            ArrayList<String> dataList = T_Config_File.method_按行读取文件("C:/Users/Administrator/Downloads/补充的下载数据ShowId.txt");
            for (String showId : dataList) {
                try {
                    String content = T_Config_File.method_读取文件内容(filePath + "口碑具体页面数据/" + showId + ".txt");
                    dataArrayList.add(parseKouBeiData(content, showId));
                    if (dataArrayList.size() > 1000) {
                        HashSet<KouBeiData> set = new HashSet<>(dataArrayList);
                        dataArrayList.clear();
                        dataArrayList.addAll(set);
                        kouBeiDataBase.insetForeachKouBeiData(dataArrayList);
                        dataArrayList.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            HashSet<KouBeiData> set = new HashSet<>(dataArrayList);
            dataArrayList.clear();
            dataArrayList.addAll(set);
            kouBeiDataBase.insetForeachKouBeiData(dataArrayList);
//            dataArrayList.add(parseKouBeiData(T_Config_File.method_读取文件内容("/Users/asteroid/Downloads/0177232yn764r30csg00000000.txt"), "0177232yn764r30csg00000000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public KouBeiData parseKouBeiData(String mainContent, String showId) {
        KouBeiData kouBeiData = new KouBeiData();
        ArrayList<String> info = new ArrayList<>();
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
                kouBeiData.set_C_ShowID(showId);
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
//                System.out.println(mainItems);
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
                    System.out.println(itemSpaceInfo.get(i));
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
                kouBeiData.set_C_ShowID(showId);
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return kouBeiData;
    }

    // 获取所有口碑的一级回复数据
    public void getReplyFile() {
        try {
            int getCount = kouBeiDataBase.getCount();
            for (int kk = 0; kk < getCount / 1000; kk++) {
                ArrayList<Object> dataList = kouBeiDataBase.getReplyKouBei(kk * 1000);
                if (dataList.size() <= 6) {
                    for (Object bean : dataList) {
                        String kbId = ((KouBeiData) bean).get_C_KoubeiID();
                        String mainUrl = "https://koubeiipv6.app.autohome.com.cn/autov9.13.0/news/replytoplevellist.ashx?pm=1&koubeiId=" + kbId + "&next=0&pagesize=9999999";
                        T_Config_File.method_访问url获取Json普通版(mainUrl, "UTF-8", filePath, kbId + "_一级评论_0.txt");
                        kouBeiDataBase.update_修改一级评论的文件下载状态(kbId);
                    }
                } else {
                    List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                            .collect(Collectors.toList());
                    for (int i = 0; i < list.size(); i++) {
                        KouBeiReplyThread commonMoreThread = new KouBeiReplyThread(list.get(i), filePath + "评论数据/");
                        Thread thread = new Thread(commonMoreThread);
                        thread.start();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 修改口碑的一级回复数据的下载状态
    public void update_修改口碑的一级回复数据的下载状态() {
        try {
            ArrayList<String> dataList = T_Config_File.method_获取文件名称(filePath + "评论数据/");
            ArrayList<KouBeiTest> result = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                KouBeiTest kouBeiTest = new KouBeiTest();
                kouBeiTest.set_C_KoubeiID(dataList.get(i).replace("_一级评论_0.txt", ""));
                result.add(kouBeiTest);
            }
            kouBeiDataBase.insetForeachKouBeiTest(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解析口碑的一级回复数据
    public void method_一级评论数据() {
//        ArrayList<String> dataList = T_Config_File.method_获取文件名称(filePath + "一级评论数据/");
        ArrayList<ReplyKouBei> replyData = new ArrayList<>();
//        int getCount = kouBeiDataBase.getCount();
        int getCount = 1320272;
        for (int kk = 0; kk < getCount / 1000; kk++) {
            ArrayList<Object> dataList = kouBeiDataBase.getReplyKouBei(kk * 1000);
            for (Object o : dataList) {
                String fileName = ((KouBeiData) o).get_C_KoubeiID();
                replyData.addAll(parse_解析一级评论数据(T_Config_File.method_读取文件内容(filePath + "一级评论数据/" + fileName+"_一级评论_0.txt"), filePath, fileName));
                System.out.println(replyData.size());
                if (replyData.size() > 1000) {
                    HashSet<ReplyKouBei> set = new HashSet<>(replyData);
                    replyData.clear();
                    replyData.addAll(set);
                    kouBeiDataBase.insetForeachKouBeiReplyData(replyData);
                    replyData.clear();
                }
                HashSet<ReplyKouBei> set2 = new HashSet<>(replyData);
                replyData.clear();
                replyData.addAll(set2);
                kouBeiDataBase.update_修改一级评论的文件下载状态(fileName);
            }
        }
        if (replyData.size() > 0) {
            kouBeiDataBase.insetForeachKouBeiReplyData(replyData);
        }
    }

    public ArrayList<ReplyKouBei> parse_解析一级评论数据(String content, String filePath, String fileName) {
        System.out.println("解析一个文件");
        ArrayList<ReplyKouBei> dataList = new ArrayList<>();
        try {
            JSONObject jsonRoot = null;
            try {
                jsonRoot = JSONObject.parse(content);
            } catch (Exception e) {
                System.out.println(filePath);
                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("一级评论数据/",""), "一级评论解析失败的.txt", filePath + "一级评论数据/" + fileName + "\n");
            }
            if (jsonRoot != null) {
                JSONArray jsonArray = jsonRoot.getJSONObject("result").getJSONArray("list");
                String nextString = jsonRoot.getJSONObject("result").getString("next");
                for (int i = 0; i < jsonArray.size(); i++) {
                    ReplyKouBei firstReply = new ReplyKouBei();
                    String replydate = ((JSONObject) jsonArray.get(i)).getString("replydate") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("replydate");
                    String chatcount = ((JSONObject) jsonArray.get(i)).getString("chatcount") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("chatcount");
                    String iscarowner = ((JSONObject) jsonArray.get(i)).getString("iscarowner") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("iscarowner");
                    String carownerlevels = ((JSONObject) jsonArray.get(i)).getString("carownerlevels") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("carownerlevels");
                    String carname = ((JSONObject) jsonArray.get(i)).getString("carname") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("carname");
                    String location = ((JSONObject) jsonArray.get(i)).getString("location") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("location");
                    String forbidReply = ((JSONObject) jsonArray.get(i)).getString("forbidReply") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("forbidReply");
                    String freplyCount = ((JSONObject) jsonArray.get(i)).getString("freplyCount") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("freplyCount");
                    String rmemberSex = ((JSONObject) jsonArray.get(i)).getString("rmemberSex") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rmemberSex");
                    String robjId = ((JSONObject) jsonArray.get(i)).getString("robjId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("robjId");
                    String rreplyDate = ((JSONObject) jsonArray.get(i)).getString("rreplyDate") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rreplyDate");
                    String rup = ((JSONObject) jsonArray.get(i)).getString("rup") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rup");
                    JSONArray subQuoteList = ((JSONObject) jsonArray.get(i)).getJSONArray("subQuoteList");
                    String freplyId = ((JSONObject) jsonArray.get(i)).getString("freplyId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("freplyId");
                    String rtargetReplyId = ((JSONObject) jsonArray.get(i)).getString("rtargetReplyId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rtargetReplyId");
                    String rtargetMemberId = ((JSONObject) jsonArray.get(i)).getString("rtargetMemberId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rtargetMemberId");
                    String rfloor = ((JSONObject) jsonArray.get(i)).getString("rfloor") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rfloor");
                    String rcontentLength = ((JSONObject) jsonArray.get(i)).getString("rcontentLength") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rcontentLength");
                    String createType = ((JSONObject) jsonArray.get(i)).getString("createType") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("createType");
                    String chatIndex = ((JSONObject) jsonArray.get(i)).getString("chatIndex") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("chatIndex");
                    String ruserHeaderImage = ((JSONObject) jsonArray.get(i)).getString("ruserHeaderImage") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("ruserHeaderImage");
                    String rcontent = ((JSONObject) jsonArray.get(i)).getString("rcontent") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rcontent");
                    String rmemberId = ((JSONObject) jsonArray.get(i)).getString("rmemberId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rmemberId");
                    String rmemberName = ((JSONObject) jsonArray.get(i)).getString("rmemberName") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rmemberName");
                    String replyId = ((JSONObject) jsonArray.get(i)).getString("replyId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("replyId");
                    JSONObject badge = ((JSONObject) jsonArray.get(i)).getJSONObject("badge");
                    String user_id = "";
                    String achievement_id = "";
                    String badge_name = "";
                    String badge_icon = "";
                    if (badge != null) {
                        user_id = badge.getString("user_id");
                        achievement_id = badge.getString("achievement_id");
                        badge_name = badge.getString("badge_name");
                        badge_icon = badge.getString("badge_icon");
                    }
                    if (subQuoteList.size() != 0) {
                        for (int j = 0; j < subQuoteList.size(); j++) {
                            ReplyKouBei secondReply = new ReplyKouBei();
                            String replydateSub = ((JSONObject) subQuoteList.get(j)).getString("replydate") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("replydate");
                            String chatcountSub = ((JSONObject) subQuoteList.get(j)).getString("chatcount") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("chatcount");
                            String iscarownerSub = ((JSONObject) subQuoteList.get(j)).getString("iscarowner") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("iscarowner");
                            String carownerlevelsSub = ((JSONObject) subQuoteList.get(j)).getString("carownerlevels") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("carownerlevels");
                            String carnameSub = ((JSONObject) subQuoteList.get(j)).getString("carname") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("carname");
                            String forbidReplySub = ((JSONObject) subQuoteList.get(j)).getString("forbidReply") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("forbidReply");
                            String rmemberSexSub = ((JSONObject) subQuoteList.get(j)).getString("rmemberSex") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rmemberSex");
                            String robjIdSub = ((JSONObject) subQuoteList.get(j)).getString("robjId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("robjId");
                            String rreplyDateSub = ((JSONObject) subQuoteList.get(j)).getString("rreplyDate") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rreplyDate");
                            String rupSub = ((JSONObject) subQuoteList.get(j)).getString("rup") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rup");
                            String replyIdSub = ((JSONObject) subQuoteList.get(j)).getString("replyId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("replyId");
                            String freplyIdSub = ((JSONObject) subQuoteList.get(j)).getString("freplyId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("freplyId");
                            String rtargetReplyIdSub = ((JSONObject) subQuoteList.get(j)).getString("rtargetReplyId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rtargetReplyId");
                            String rtargetMemberIdSub = ((JSONObject) subQuoteList.get(j)).getString("rtargetMemberId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rtargetMemberId");
                            String rfloorSub = ((JSONObject) subQuoteList.get(j)).getString("rfloor") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rfloor");
                            String rcontentLengthSub = ((JSONObject) subQuoteList.get(j)).getString("rcontentLength") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rcontentLength");
                            String createTypeSub = ((JSONObject) subQuoteList.get(j)).getString("createType") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("createType");
                            String chatIndexSub = ((JSONObject) subQuoteList.get(j)).getString("chatIndex") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("chatIndex");
                            String freplyCountSub = ((JSONObject) subQuoteList.get(j)).getString("freplyCount") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("freplyCount");
                            String rcontentSub = ((JSONObject) subQuoteList.get(j)).getString("rcontent") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rcontent");
                            String rmemberIdSub = ((JSONObject) subQuoteList.get(j)).getString("rmemberId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rmemberId");
                            String rmemberNameSub = ((JSONObject) subQuoteList.get(j)).getString("rmemberName") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rmemberName");
                            String ruserHeaderImageSub = ((JSONObject) subQuoteList.get(j)).getString("ruserHeaderImage") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("ruserHeaderImage");
                            secondReply.set_C_replydateSub(replydateSub);
                            secondReply.set_C_chatcountSub(chatcountSub);
                            secondReply.set_C_iscarownerSub(iscarownerSub);
                            secondReply.set_C_carownerlevelsSub(carownerlevelsSub);
                            secondReply.set_C_carnameSub(carnameSub);
                            secondReply.set_C_forbidReplySub(forbidReplySub);
                            secondReply.set_C_rmemberSexSub(rmemberSexSub);
                            secondReply.set_C_robjIdSub(robjIdSub);
                            secondReply.set_C_rreplyDateSub(rreplyDateSub);
                            secondReply.set_C_rupSub(rupSub);
                            secondReply.set_C_replyIdSub(replyIdSub);
                            secondReply.set_C_freplyIdSub(freplyIdSub);
                            secondReply.set_C_rtargetReplyIdSub(rtargetReplyIdSub);
                            secondReply.set_C_rtargetMemberIdSub(rtargetMemberIdSub);
                            secondReply.set_C_rfloorSub(rfloorSub);
                            secondReply.set_C_rcontentLengthSub(rcontentLengthSub);
                            secondReply.set_C_createTypeSub(createTypeSub);
                            secondReply.set_C_chatIndexSub(chatIndexSub);
                            secondReply.set_C_freplyCountSub(freplyCountSub);
                            secondReply.set_C_rcontentSub(rcontentSub);
                            secondReply.set_C_rmemberIdSub(rmemberIdSub);
                            secondReply.set_C_rmemberNameSub(rmemberNameSub);
                            secondReply.set_C_ruserHeaderImageSub(ruserHeaderImageSub);
                            secondReply.set_C_badge_icon(badge_icon);
                            secondReply.set_C_badge_user_id(user_id);
                            secondReply.set_C_badge_achievement_id(achievement_id);
                            secondReply.set_C_badge_name(badge_name);
                            secondReply.set_C_KouBeiID(robjId);
                            secondReply.set_C_ReplyContent(rcontent);
                            secondReply.set_C_ReplyUserID(rmemberId);
                            secondReply.set_C_ReplyUserName(rmemberName);
                            secondReply.set_C_TargetUserID(rtargetMemberId);
                            secondReply.set_C_nextString(nextString);
                            secondReply.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            dataList.add(secondReply);
                        }
                    }

                    firstReply.set_C_replydate(replydate);
                    firstReply.set_C_chatcount(chatcount);
                    firstReply.set_C_iscarowner(iscarowner);
                    firstReply.set_C_carownerlevels(carownerlevels);
                    firstReply.set_C_carname(carname);
                    firstReply.set_C_location(location);
                    firstReply.set_C_forbidReply(forbidReply);
                    firstReply.set_C_freplyCount(freplyCount);
                    firstReply.set_C_rmemberSex(rmemberSex);
                    firstReply.set_C_robjId(robjId);
                    firstReply.set_C_rreplyDate(rreplyDate);
                    firstReply.set_C_rup(rup);
                    firstReply.set_C_freplyId(freplyId);
                    firstReply.set_C_rtargetReplyId(rtargetReplyId);
                    firstReply.set_C_rtargetMemberId(rtargetMemberId);
                    firstReply.set_C_rfloor(rfloor);
                    firstReply.set_C_rcontentLength(rcontentLength);
                    firstReply.set_C_createType(createType);
                    firstReply.set_C_chatIndex(chatIndex);
                    firstReply.set_C_ruserHeaderImage(ruserHeaderImage);
                    firstReply.set_C_rcontent(rcontent);
                    firstReply.set_C_rmemberId(rmemberId);
                    firstReply.set_C_rmemberName(rmemberName);
                    firstReply.set_C_replyId(replyId);
                    firstReply.set_C_badge_icon(badge_icon);
                    firstReply.set_C_badge_user_id(user_id);
                    firstReply.set_C_badge_achievement_id(achievement_id);
                    firstReply.set_C_badge_name(badge_name);
                    firstReply.set_C_KouBeiID(robjId);
                    firstReply.set_C_ReplyContent(rcontent);
                    firstReply.set_C_ReplyUserID(rmemberId);
                    firstReply.set_C_ReplyUserName(rmemberName);
                    firstReply.set_C_TargetUserID("");
                    firstReply.set_C_IsFinish(0);
                    firstReply.set_C_nextString(nextString);
                    firstReply.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    dataList.add(firstReply);
                }
            }
//            kouBeiDataBase.insetForeachKouBeiReplyData(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
