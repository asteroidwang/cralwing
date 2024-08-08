package com.wangtiantian.runKouBei;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.CommonMoreThread;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.koubei.KouBeiData;
import com.wangtiantian.entity.koubei.KouBeiInfo;
import com.wangtiantian.entity.koubei.ModelKouBei;
import com.wangtiantian.entity.price.ModelDealerData;
import com.wangtiantian.mapper.KouBeiDataBase;
import com.wangtiantian.runPrice.PriceMoreThread;
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
                    if (dataArrayList.size()>1000){
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
            ArrayList<String> dataList =T_Config_File.method_按行读取文件("C:/Users/Administrator/Downloads/补充的下载数据ShowId.txt");
            for (String showId : dataList) {
                try {
                    String content = T_Config_File.method_读取文件内容(filePath + "口碑具体页面数据/" + showId + ".txt");
                    dataArrayList.add(parseKouBeiData(content, showId));
                    if (dataArrayList.size()>1000){
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
                    String columnName = itemInfo.get(i).select(".name").text().replace(" ","").trim();
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
                    String tempC1 = c1.replace(v1, "").replace(" ","").trim();
                    if (tempC1.equals("驾驶感受")) {
                        jiaShiGanShou = v1+"->"+ itemSpaceInfo.get(i).select("p").text();
                    } else if (tempC1.equals("操控")) {
                        caoKong = v1+"->"+ itemSpaceInfo.get(i).select("p").text();
                    } else if (tempC1.equals("舒适性")) {
                        shuShiXing = v1+"->"+ itemSpaceInfo.get(i).select("p").text();
                    } else if (tempC1.equals("内饰")) {
                        neiShi = v1+"->"+ itemSpaceInfo.get(i).select("p").text();
                    } else if (tempC1.equals("油耗")) {
                        youHao = v1+"->"+ itemSpaceInfo.get(i).select("p").text();
                    } else if (tempC1.equals("性价比")) {
                        xingJiaBi = v1+"->"+ itemSpaceInfo.get(i).select("p").text();
                    } else if (tempC1.equals("空间")) {
                        kongJian = v1+"->"+ itemSpaceInfo.get(i).select("p").text();
                    } else if (tempC1.equals("外观")) {
                        waiGuan = v1+"->"+ itemSpaceInfo.get(i).select("p").text();
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
}
