package com.wangtiantian.runKouBei;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.CommonMoreThread;
import com.wangtiantian.dao.T_Config_File;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import java.util.stream.IntStream;

public class KouBeiMethod {
    private KouBeiDataBase kouBeiDataBase = new KouBeiDataBase();
    //    private String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/口碑评价数据/20240804/";
    private String filePath = "E:/汽车之家/口碑评价数据/20240804/";
//    private String filePath = "/Users/asteroid/所有文件数据/一级评论";

    // 获取每个车型的第一页口碑
    public void getModelKouBeiFirstFileUrl() {
        try {
            ArrayList<Object> modList = kouBeiDataBase.getModIDList();
            ArrayList<Object> dataList = new ArrayList<>();
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
            HashSet<Object> set = new HashSet<>(dataList);
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
            ArrayList<Object> dataList = new ArrayList<>();
            for (String folderName : folderList) {
                String content = T_Config_File.method_读取文件内容(filePath + folderName + "/" + folderName + "_1.txt");
                int pageCount = ((JSONObject) JSONObject.parse(content)).getJSONObject("result").getInteger("pagecount");
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
            HashSet<Object> set = new HashSet<>(dataList);
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
            ArrayList<Object> dataList = new ArrayList<>();
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
            HashSet<Object> set = new HashSet<>(dataList);
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
            ArrayList<Object> dataArrayList = new ArrayList<>();
            ArrayList<Object> dataList = kouBeiDataBase.findAllKouBeiShowId();
            for (Object o : dataList) {
                String showId = ((KouBeiInfo) o).get_C_ShowID();
                try {
                    String content = T_Config_File.method_读取文件内容(filePath + "口碑具体页面数据/" + showId + ".txt");
                    dataArrayList.add(parseKouBeiData(content, showId));
                    if (dataArrayList.size() > 1000) {
                        HashSet<Object> set = new HashSet<>(dataArrayList);
                        dataArrayList.clear();
                        dataArrayList.addAll(set);
                        kouBeiDataBase.insetForeachKouBeiData(dataArrayList);
                        dataArrayList.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            HashSet<Object> set = new HashSet<>(dataArrayList);
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
            ArrayList<Object> dataArrayList = new ArrayList<>();
            ArrayList<String> dataList = T_Config_File.method_按行读取文件("C:/Users/Administrator/Downloads/补充的下载数据ShowId.txt");
            for (String showId : dataList) {
                try {
                    String content = T_Config_File.method_读取文件内容(filePath + "口碑具体页面数据/" + showId + ".txt");
                    dataArrayList.add(parseKouBeiData(content, showId));
                    if (dataArrayList.size() > 1000) {
                        HashSet<Object> set = new HashSet<>(dataArrayList);
                        dataArrayList.clear();
                        dataArrayList.addAll(set);
                        kouBeiDataBase.insetForeachKouBeiData(dataArrayList);
                        dataArrayList.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            HashSet<Object> set = new HashSet<>(dataArrayList);
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
            ArrayList<Object> result = new ArrayList<>();
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
        ArrayList<Object> replyData = new ArrayList<>();
        ArrayList<String> dataList = T_Config_File.method_获取文件名称(filePath + "一级评论数据/");
        for (String o : dataList) {
            String kbId = o.replace("_一级评论_0.txt", "");
            replyData.addAll(parse_解析一级评论数据(T_Config_File.method_读取文件内容(filePath + "一级评论数据/" + kbId + "_一级评论_0.txt"), filePath, kbId + "_一级评论_0.txt"));
            if (replyData.size() > 10000) {
                HashSet<Object> set = new HashSet<>(replyData);
                replyData.clear();
                replyData.addAll(set);
                kouBeiDataBase.insetForeachKouBeiReplyData(replyData);
                replyData.clear();
            }
        }
        HashSet<Object> set = new HashSet<>(replyData);
        replyData.clear();
        replyData.addAll(set);

        kouBeiDataBase.insetForeachKouBeiReplyData(replyData);
    }

    public ArrayList<Object> parse_解析一级评论数据(String content, String filePath, String fileName) {
        ArrayList<Object> dataList = new ArrayList<>();
        content = content.replace("$", "钱的符号").replace("\\\"", "不该有的英文引号").replace("\\", "两个斜杠");
        Pattern pattern = Pattern.compile("\"rcontent\":\"(.*?)\",\"rmemberId\"");
        Matcher matcher = pattern.matcher(content);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String firstPart = matcher.group(1); // 第一个引号与第二个引号之间的内容
            matcher.appendReplacement(result, "\"rcontent\":\"" + firstPart.replace("\n", "").replace("\r", "").replace("\n\r", "").replace("'", "''").replace("\"", "不该有的英文引号") + "\",\"rmemberId\"");
        }
        matcher.appendTail(result);
        content = result.toString();
//        System.out.println(filePath + fileName);
//        System.out.println(content);
        JSONObject jsonRoot1 = null;
        try {
            jsonRoot1 = JSONObject.parseObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonRoot1 != null) {
            if (jsonRoot1.getString("message").equals("")) {
            } else {
                JSONObject jsonRoot = jsonRoot1.getJSONObject("result");
                if (jsonRoot != null) {
                    JSONArray jsonArray = jsonRoot.getJSONArray("list");
                    System.out.println(jsonArray);
                    JSONArray jsonArray1 = jsonRoot.getJSONArray("wonderfullist");
                    String nextString = jsonRoot.getString("next");
                    if (jsonArray1.size() != 0) {
                        for (int i = 0; i < jsonArray1.size(); i++) {
                            JSONObject wonderObject = ((JSONObject) jsonArray1.get(i));
                            ReplyKouBei replyKouBei = new ReplyKouBei();
                            replyKouBei.set_C_nextString(nextString);
                            replyKouBei.set_C_hasmore(jsonRoot.getString("hasmore"));
                            replyKouBei.set_C_freplyCount_TieZi(jsonRoot.getString("freplyCount"));
                            replyKouBei.set_C_KouBeiID(fileName.replace("_一级评论_0.txt", "").equals("") ? "-" : fileName.replace("_一级评论_0.txt", ""));
                            replyKouBei.set_C_rfloor(wonderObject.getString("rfloor") == null ? "-" : wonderObject.getString("rfloor"));
                            replyKouBei.set_C_iscarowner(wonderObject.getString("iscarowner") == null ? "-" : wonderObject.getString("iscarowner"));
                            replyKouBei.set_C_rmemberId(wonderObject.getString("rmemberId") == null ? "-" : wonderObject.getString("rmemberId"));
                            replyKouBei.set_C_rcontentLength(wonderObject.getString("rcontentLength") == null ? "-" : wonderObject.getString("rcontentLength"));
                            replyKouBei.set_C_createType(wonderObject.getString("createType") == null ? "-" : wonderObject.getString("createType"));
                            replyKouBei.set_C_freplyId(wonderObject.getString("freplyId") == null ? "-" : wonderObject.getString("freplyId"));
                            replyKouBei.set_C_rup(wonderObject.getString("rup") == null ? "-" : wonderObject.getString("rup"));
                            replyKouBei.set_C_chatIndex(wonderObject.getString("chatIndex") == null ? "-" : wonderObject.getString("chatIndex"));
                            replyKouBei.set_C_rmemberSex(wonderObject.getString("rmemberSex") == null ? "-" : wonderObject.getString("rmemberSex"));
                            replyKouBei.set_C_robjId(wonderObject.getString("robjId") == null ? "-" : wonderObject.getString("robjId"));
                            replyKouBei.set_C_rmemberName(wonderObject.getString("rmemberName") == null ? "-" : wonderObject.getString("rmemberName"));
                            replyKouBei.set_C_rreplyDate(wonderObject.getString("rreplyDate") == null ? "-" : wonderObject.getString("rreplyDate"));
                            replyKouBei.set_C_carname(wonderObject.getString("carname") == null ? "-" : wonderObject.getString("carname"));
                            replyKouBei.set_C_replyId(wonderObject.getString("replyId") == null ? "-" : wonderObject.getString("replyId"));
                            replyKouBei.set_C_forbidReply(wonderObject.getString("forbidReply") == null ? "-" : wonderObject.getString("forbidReply"));
                            replyKouBei.set_C_ruserHeaderImage(wonderObject.getString("ruserHeaderImage") == null ? "-" : wonderObject.getString("ruserHeaderImage"));
                            replyKouBei.set_C_rtargetMemberId(wonderObject.getString("rtargetMemberId") == null ? "-" : wonderObject.getString("rtargetMemberId"));
                            replyKouBei.set_C_carownerlevels(wonderObject.getString("carownerlevels") == null ? "-" : wonderObject.getString("carownerlevels"));
                            replyKouBei.set_C_chatcount(wonderObject.getString("chatcount") == null ? "-" : wonderObject.getString("chatcount"));
                            replyKouBei.set_C_replydate(wonderObject.getString("replydate") == null ? "-" : wonderObject.getString("replydate"));
                            replyKouBei.set_C_rtargetReplyId(wonderObject.getString("rtargetReplyId") == null ? "-" : wonderObject.getString("rtargetReplyId"));
                            replyKouBei.set_C_location(wonderObject.getString("location") == null ? "-" : wonderObject.getString("location"));
                            replyKouBei.set_C_rcontent(wonderObject.getString("rcontent") == null ? "-" : wonderObject.getString("rcontent"));
                            replyKouBei.set_C_freplyCount_First(wonderObject.getString("freplyCount") == null ? "-" : wonderObject.getString("freplyCount"));
                            dataList.add(replyKouBei);
                        }
                    }

                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject wonderObject = ((JSONObject) jsonArray.get(i));
                        ReplyKouBei replyKouBei = new ReplyKouBei();
                        replyKouBei.set_C_freplyCount_TieZi(jsonRoot.getString("freplyCount"));
                        replyKouBei.set_C_KouBeiID(fileName.replace("_一级评论_0.txt", "").equals("") ? "-" : fileName.replace("_一级评论_0.txt", ""));
                        replyKouBei.set_C_rfloor(wonderObject.getString("rfloor") == null ? "-" : wonderObject.getString("rfloor"));
                        replyKouBei.set_C_iscarowner(wonderObject.getString("iscarowner") == null ? "-" : wonderObject.getString("iscarowner"));
                        replyKouBei.set_C_rmemberId(wonderObject.getString("rmemberId") == null ? "-" : wonderObject.getString("rmemberId"));
                        replyKouBei.set_C_rcontentLength(wonderObject.getString("rcontentLength") == null ? "-" : wonderObject.getString("rcontentLength"));
                        replyKouBei.set_C_createType(wonderObject.getString("createType") == null ? "-" : wonderObject.getString("createType"));
                        replyKouBei.set_C_freplyId(wonderObject.getString("freplyId") == null ? "-" : wonderObject.getString("freplyId"));
                        replyKouBei.set_C_rup(wonderObject.getString("rup") == null ? "-" : wonderObject.getString("rup"));
                        replyKouBei.set_C_chatIndex(wonderObject.getString("chatIndex") == null ? "-" : wonderObject.getString("chatIndex"));
                        replyKouBei.set_C_rmemberSex(wonderObject.getString("rmemberSex") == null ? "-" : wonderObject.getString("rmemberSex"));
                        replyKouBei.set_C_robjId(wonderObject.getString("robjId") == null ? "-" : wonderObject.getString("robjId"));
                        replyKouBei.set_C_rmemberName(wonderObject.getString("rmemberName") == null ? "-" : wonderObject.getString("rmemberName"));
                        replyKouBei.set_C_rreplyDate(wonderObject.getString("rreplyDate") == null ? "-" : wonderObject.getString("rreplyDate"));
                        replyKouBei.set_C_carname(wonderObject.getString("carname") == null ? "-" : wonderObject.getString("carname"));
                        replyKouBei.set_C_replyId(wonderObject.getString("replyId") == null ? "-" : wonderObject.getString("replyId"));
                        replyKouBei.set_C_forbidReply(wonderObject.getString("forbidReply") == null ? "-" : wonderObject.getString("forbidReply"));
                        replyKouBei.set_C_ruserHeaderImage(wonderObject.getString("ruserHeaderImage") == null ? "-" : wonderObject.getString("ruserHeaderImage"));
                        replyKouBei.set_C_rtargetMemberId(wonderObject.getString("rtargetMemberId") == null ? "-" : wonderObject.getString("rtargetMemberId"));
                        replyKouBei.set_C_carownerlevels(wonderObject.getString("carownerlevels") == null ? "-" : wonderObject.getString("carownerlevels"));
                        replyKouBei.set_C_chatcount(wonderObject.getString("chatcount") == null ? "-" : wonderObject.getString("chatcount"));
                        replyKouBei.set_C_replydate(wonderObject.getString("replydate") == null ? "-" : wonderObject.getString("replydate"));
                        replyKouBei.set_C_rtargetReplyId(wonderObject.getString("rtargetReplyId") == null ? "-" : wonderObject.getString("rtargetReplyId"));
                        replyKouBei.set_C_location(wonderObject.getString("location") == null ? "-" : wonderObject.getString("location"));
                        replyKouBei.set_C_rcontent(wonderObject.getString("rcontent") == null ? "-" : wonderObject.getString("rcontent"));
                        replyKouBei.set_C_freplyCount_First(wonderObject.getString("freplyCount") == null ? "-" : wonderObject.getString("freplyCount"));
                        dataList.add(replyKouBei);
//            JSONArray subQuoteList = wonderObject.getJSONArray("subQuoteList");
//            for (int j = 0; j < subQuoteList.size(); j++) {
//                JSONObject object = ((JSONObject) subQuoteList.get(j));
//                ReplyKouBei replyKouBei1 = new ReplyKouBei();
//                replyKouBei1.set_C_KouBeiID(fileName.replace("_一级评论_0.txt", "").equals("") ? "-" : fileName.replace("_一级评论_0.txt", ""));
//                replyKouBei1.set_C_rfloor(object.getString("rfloor") == null ? "-" : object.getString("rfloor"));
//                replyKouBei1.set_C_iscarowner(object.getString("iscarowner") == null ? "-" : object.getString("iscarowner"));
//                replyKouBei1.set_C_rmemberId(object.getString("rmemberId") == null ? "-" : object.getString("rmemberId"));
//                replyKouBei1.set_C_rcontentLength(object.getString("rcontentLength") == null ? "-" : object.getString("rcontentLength"));
//                replyKouBei1.set_C_createType(object.getString("createType") == null ? "-" : object.getString("createType"));
//                replyKouBei1.set_C_freplyId(object.getString("freplyId") == null ? "-" : object.getString("freplyId"));
//                replyKouBei1.set_C_rup(object.getString("rup") == null ? "-" : object.getString("rup"));
//                replyKouBei1.set_C_chatIndex(object.getString("chatIndex") == null ? "-" : object.getString("chatIndex"));
//                replyKouBei1.set_C_rmemberSex(object.getString("rmemberSex") == null ? "-" : object.getString("rmemberSex"));
//                replyKouBei1.set_C_robjId(object.getString("robjId") == null ? "-" : object.getString("robjId"));
//                replyKouBei1.set_C_rmemberName(object.getString("rmemberName") == null ? "-" : object.getString("rmemberName"));
//                replyKouBei1.set_C_rreplyDate(object.getString("rreplyDate") == null ? "-" : object.getString("rreplyDate"));
//                replyKouBei1.set_C_carname(object.getString("carname") == null ? "-" : object.getString("carname"));
//                replyKouBei1.set_C_replyId(object.getString("replyId") == null ? "-" : object.getString("replyId"));
//                replyKouBei1.set_C_forbidReply(object.getString("forbidReply") == null ? "-" : object.getString("forbidReply"));
//                replyKouBei1.set_C_ruserHeaderImage(object.getString("ruserHeaderImage") == null ? "-" : object.getString("ruserHeaderImage"));
//                replyKouBei1.set_C_rtargetMemberId(object.getString("rtargetMemberId") == null ? "-" : object.getString("rtargetMemberId"));
//                replyKouBei1.set_C_carownerlevels(object.getString("carownerlevels") == null ? "-" : object.getString("carownerlevels"));
//                replyKouBei1.set_C_chatcount(object.getString("chatcount") == null ? "-" : object.getString("chatcount"));
//                replyKouBei1.set_C_replydate(object.getString("replydate") == null ? "-" : object.getString("replydate"));
//                replyKouBei1.set_C_rtargetReplyId(object.getString("rtargetReplyId") == null ? "-" : object.getString("rtargetReplyId"));
//                replyKouBei1.set_C_location(object.getString("location") == null ? "-" : object.getString("location"));
//                replyKouBei1.set_C_rcontent(object.getString("rcontent") == null ? "-" : object.getString("rcontent"));
//                replyKouBei1.set_C_freplyCount(object.getString("freplyCount") == null ? "-" : object.getString("freplyCount"));
//                dataList.add(replyKouBei1);
//            }
                    }
                }
            }
        }
        System.out.println("解析完一次");
        System.out.println(dataList.size());
        return dataList;
    }

    public ArrayList<Object> parse_解析一级评论数据_原来(String content, String filePath, String fileName) {
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            JSONObject jsonRoot = null;
            try {
                jsonRoot = JSON.parseObject(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonRoot != null) {
                JSONArray jsonArray = jsonRoot.getJSONObject("result").getJSONArray("list");
                System.out.println(jsonArray);
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
                        user_id = badge.getString("user_id") == null ? "-" : badge.getString("user_id");
                        achievement_id = badge.getString("achievement_id") == null ? "-" : badge.getString("achievement_id");
                        badge_name = badge.getString("badge_name") == null ? "-" : badge.getString("badge_name");
                        badge_icon = badge.getString("badge_icon") == null ? "-" : badge.getString("badge_icon");
                    }
//
                    firstReply.set_C_replydate(replydate);
                    firstReply.set_C_chatcount(chatcount);
                    firstReply.set_C_iscarowner(iscarowner);
                    firstReply.set_C_carownerlevels(carownerlevels);
                    firstReply.set_C_carname(carname);
                    firstReply.set_C_location(location);
                    firstReply.set_C_forbidReply(forbidReply);
                    firstReply.set_C_freplyCount_TieZi(freplyCount);
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
                    firstReply.set_C_badge_badge_icon(badge_icon);
                    firstReply.set_C_badge_user_id(user_id);
                    firstReply.set_C_badge_achievement_id(achievement_id);
                    firstReply.set_C_badge_badge_name(badge_name);
                    firstReply.set_C_KouBeiID(robjId);
                    firstReply.set_C_IsFinish(0);
                    firstReply.set_C_freplyCount_First(freplyCount);
                    firstReply.set_C_nextString(nextString);
                    firstReply.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    dataList.add(firstReply);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void downLoad_下载回复一级评论的数据文件(String filePath) {
        try {
            int getCount = kouBeiDataBase.get_二级回复文件应有数量();
            System.out.println(getCount);
            for (int kk = 0; kk < getCount / 1000; kk++) {
                ArrayList<Object> dataList = kouBeiDataBase.get_查询未下载的最后层级评论(kk * 10000);
                List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                        .collect(Collectors.toList());
                for (int i = 0; i < list.size(); i++) {
                    SecondReplyDataThread moreThread = new SecondReplyDataThread(list.get(i), filePath);
                    Thread thread = new Thread(moreThread);
                    thread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirm_确认已下载的二级评论数据(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataList = new ArrayList<>();
            for (String fileName : fileList) {
                ConfirmReplySecond replySecond = new ConfirmReplySecond();
                String tempString = fileName.replace("_二级级评论_0.txt", "");
                String kbId = tempString.split("_")[0];
                String freplyId = tempString.split("_")[1];
                String mainUrl = "https://koubeiipv6.app.autohome.com.cn/autov9.13.0/news/replytoplevelsublist.ashx?_appid=koubei&koubeiid=" + kbId + "&freplyid=" + freplyId + "&next=0&pagesize=999999&pm=1&appversion=1&orderBy=0";
                replySecond.set_C_ReplySecondUrl(mainUrl);
                dataList.add(replySecond);
            }
            kouBeiDataBase.insert_确认已下载的二级评论数据(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void method_解析二级评论数据(String filePath) {
        try {
            ArrayList<Object> replyData = new ArrayList<>();
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            for (String fileName : fileList) {
                if (!fileName.equals(".DS_Store")) {

                    String content = T_Config_File.method_读取文件内容(filePath + fileName);
                    String kbId = fileName.split("_")[0];

                    replyData.addAll(parse_解析二级评论数据(content, filePath, fileName));
                    if (replyData.size() > 10000) {
                        HashSet<Object> set = new HashSet<>(replyData);
                        replyData.clear();
                        replyData.addAll(set);
                        System.out.println("入库");
                        kouBeiDataBase.insetForeachKouBeiReplyDat2(replyData);
                        replyData.clear();
                    }
                }
            }
            HashSet<Object> set = new HashSet<>(replyData);
            replyData.clear();
            replyData.addAll(set);
            kouBeiDataBase.insetForeachKouBeiReplyDat2(replyData);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Object> parse_解析二级评论数据(String content, String filePath, String fileName) {
        ArrayList<Object> dataList = new ArrayList<>();
        try {
            content = content.replace("$", "钱的符号").replace("\\\"", "不该有的英文引号").replace("\\", "两个斜杠");
            Pattern pattern = Pattern.compile("\"rcontent\":\"(.*?)\",\"rmemberId\"");
            Matcher matcher = pattern.matcher(content);
            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                String firstPart = matcher.group(1); // 第一个引号与第二个引号之间的内容
                matcher.appendReplacement(result, "\"rcontent\":\"" + firstPart.replace("\n", "").replace("\r", "").replace("\n\r", "").replace("'", "''").replace("\"", "不该有的英文引号") + "\",\"rmemberId\"");
            }
            matcher.appendTail(result);
            content = result.toString();
            JSONObject jsonRoot = JSONObject.parseObject(content);
            JSONObject jsonResult = jsonRoot.getJSONObject("result");
            String nextString = jsonResult.getString("next");
            String ruserHeaderImage = jsonResult.getString("ruserHeaderImage");
            String iscarowenr = jsonResult.getString("iscarowner");
            String rmemberId = jsonResult.getString("rmemberId");
            String carownerlevels = jsonResult.getString("carownerlevels");
            JSONArray jsonArray = jsonResult.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject wonderObject = ((JSONObject) jsonArray.get(i));
                ReplyKouBei replyKouBei = new ReplyKouBei();
                replyKouBei.set_C_freplyCount_TieZi(jsonRoot.getString("freplyCount"));
                replyKouBei.set_C_KouBeiID(fileName.split("_")[0]);
                replyKouBei.set_C_ruserHeaderImage(ruserHeaderImage);
                replyKouBei.set_C_iscarowner(iscarowenr);
                replyKouBei.set_C_rmemberId(rmemberId);
                replyKouBei.set_C_iscarowner(iscarowenr);
                replyKouBei.set_C_carownerlevels(carownerlevels);
                replyKouBei.set_C_rfloor(wonderObject.getString("rfloor") == null ? "-" : wonderObject.getString("rfloor"));
                replyKouBei.set_C_iscarowner(wonderObject.getString("iscarowner") == null ? "-" : wonderObject.getString("iscarowner"));
                replyKouBei.set_C_rmemberId(wonderObject.getString("rmemberId") == null ? "-" : wonderObject.getString("rmemberId"));
                replyKouBei.set_C_rcontentLength(wonderObject.getString("rcontentLength") == null ? "-" : wonderObject.getString("rcontentLength"));
                replyKouBei.set_C_createType(wonderObject.getString("createType") == null ? "-" : wonderObject.getString("createType"));
                replyKouBei.set_C_freplyId(wonderObject.getString("freplyId") == null ? "-" : wonderObject.getString("freplyId"));
                replyKouBei.set_C_rup(wonderObject.getString("rup") == null ? "-" : wonderObject.getString("rup"));
                replyKouBei.set_C_chatIndex(wonderObject.getString("chatIndex") == null ? "-" : wonderObject.getString("chatIndex"));
                replyKouBei.set_C_rmemberSex(wonderObject.getString("rmemberSex") == null ? "-" : wonderObject.getString("rmemberSex"));
                replyKouBei.set_C_robjId(wonderObject.getString("robjId") == null ? "-" : wonderObject.getString("robjId"));
                replyKouBei.set_C_rmemberName(wonderObject.getString("rmemberName") == null ? "-" : wonderObject.getString("rmemberName"));
                replyKouBei.set_C_rreplyDate(wonderObject.getString("rreplyDate") == null ? "-" : wonderObject.getString("rreplyDate"));
                replyKouBei.set_C_carname(wonderObject.getString("carname") == null ? "-" : wonderObject.getString("carname"));
                replyKouBei.set_C_replyId(wonderObject.getString("replyId") == null ? "-" : wonderObject.getString("replyId"));
                replyKouBei.set_C_forbidReply(wonderObject.getString("forbidReply") == null ? "-" : wonderObject.getString("forbidReply"));
                replyKouBei.set_C_ruserHeaderImage(wonderObject.getString("ruserHeaderImage") == null ? "-" : wonderObject.getString("ruserHeaderImage"));
                replyKouBei.set_C_rtargetMemberId(wonderObject.getString("rtargetMemberId") == null ? "-" : wonderObject.getString("rtargetMemberId"));
                replyKouBei.set_C_carownerlevels(wonderObject.getString("carownerlevels") == null ? "-" : wonderObject.getString("carownerlevels"));
                replyKouBei.set_C_chatcount(wonderObject.getString("chatcount") == null ? "-" : wonderObject.getString("chatcount"));
                replyKouBei.set_C_replydate(wonderObject.getString("replydate") == null ? "-" : wonderObject.getString("replydate"));
                replyKouBei.set_C_rtargetReplyId(wonderObject.getString("rtargetReplyId") == null ? "-" : wonderObject.getString("rtargetReplyId"));
                replyKouBei.set_C_location(wonderObject.getString("location") == null ? "-" : wonderObject.getString("location"));
                replyKouBei.set_C_rcontent(wonderObject.getString("rcontent") == null ? "-" : wonderObject.getString("rcontent"));
                replyKouBei.set_C_freplyCount_First(wonderObject.getString("freplyCount") == null ? "-" : wonderObject.getString("freplyCount"));
                replyKouBei.set_C_nextString(nextString);
                dataList.add(replyKouBei);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void method_获取口碑数据中的图片Url入库() {
        try {
            ArrayList<Object> dataList = new ArrayList<>();
            int numCount = kouBeiDataBase.getCount();
            for (int kk = 0; kk < numCount / 10000; kk++) {
                ArrayList<Object> dataResultList = kouBeiDataBase.find_查找所有口碑帖子数据下载图片(kk * 10000);
                for (int i = 0; i < dataResultList.size(); i++) {
                    String mainContent = ((KouBeiData) dataResultList.get(i)).get_C_KouBeiContent();
                    String kouBeiId = ((KouBeiData) dataResultList.get(i)).get_C_KoubeiID();
                    String showId = ((KouBeiData) dataResultList.get(i)).get_C_ShowID();
                    Document mainDoc = Jsoup.parse(mainContent);
                    Elements mainItems = mainDoc.select(".lazyload");
                    if (mainItems.size() > 0) {
                        for (int j = 0; j < mainItems.size(); j++) {
                            KouBeiPicture kouBeiPicture = new KouBeiPicture();
                            kouBeiPicture.set_C_PictureUrl(mainItems.get(j).attr("data-src"));
                            kouBeiPicture.set_C_IsFinish(0);
                            kouBeiPicture.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            kouBeiPicture.set_C_ShowID(showId);
                            kouBeiPicture.set_C_KouBeiID(kouBeiId);
                            kouBeiPicture.set_C_NumCount(mainItems.size());
                            kouBeiPicture.set_C_Position(String.valueOf(j));
                            dataList.add(kouBeiPicture);
                            if (dataList.size() > 10000) {
                                kouBeiDataBase.insert_需要下载的口碑帖子里的图片(dataList);
                                dataList.clear();
                            }
                        }
                    }
                }
                if (dataList.size() > 0) {
                    kouBeiDataBase.insert_需要下载的口碑帖子里的图片(dataList);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downLoad_下载口碑中的图片(String filePath) {
        try {
            int numCount = kouBeiDataBase.get_口碑图片总数();
            System.out.println(numCount);
            int kk=0;
//                for (int kk = 0; kk < numCount / 10000; kk++) {
                    ArrayList<Object> dataResultList = kouBeiDataBase.get_获取口碑图片的url数据(kk * 10000);
                    System.out.println(dataResultList.size());
                    if (dataResultList.size()>36){
                        List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataResultList.subList(i * (dataResultList.size() + 5) / 6, Math.min((i + 1) * (dataResultList.size() + 5) / 6, dataResultList.size())))
                                .collect(Collectors.toList());
                        for (int i = 0; i < list.size(); i++) {
                            KouBeiPictureMoreThread moreThread = new KouBeiPictureMoreThread(list.get(i), filePath);
                            Thread thread = new Thread(moreThread);
                            thread.start();
                        }
                    }else {
                        for (int i = 0; i < dataResultList.size(); i++) {
                            String showId = ((KouBeiPicture)dataResultList.get(i)).get_C_ShowID();
                            String kbId = ((KouBeiPicture)dataResultList.get(i)).get_C_KouBeiID();
                            String position = ((KouBeiPicture)dataResultList.get(i)).get_C_Position();
                            String mainUrl = ((KouBeiPicture)dataResultList.get(i)).get_C_PictureUrl();
                            T_Config_File.downloadImage(mainUrl, filePath + showId + "/", showId + "_" + kbId + "_" + position);
                        }
                    }

//                }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update_修改已下载的口碑图片的口碑状态(String filePath){
        try {
            ArrayList<String> showIdList = T_Config_File.method_获取文件夹名称(filePath);
            ArrayList<Object> dataList =new ArrayList<>();
            for(String showId:showIdList){
                System.out.println(showId);
                ConfirmKouBeiPicture confirmKouBeiPicture = new ConfirmKouBeiPicture();
                confirmKouBeiPicture.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                confirmKouBeiPicture.set_C_ShowID(showId);
                dataList.add(confirmKouBeiPicture);
            }
            new KouBeiDataBase().insert_已下载的口碑帖子里的图片数据(dataList);
//            downLoad_下载口碑中的图片(filePath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
