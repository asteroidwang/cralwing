package com.wangtiantian.runErShouChe.che300;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.ershouche.che300.Che300_CarInfo;
import com.wangtiantian.entity.ershouche.che300.Che300_CityData;
import com.wangtiantian.mapper.ErShouCheDataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.List;

public class MainChe300 {
    public static void main(String[] args) {
//        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/二手车数据/che300/20240912/";
        String filePath = "D:/ZEDATA_2024/二手车数据/che300/20240912/";
        MainChe300 mainChe300 = new MainChe300();
        // mainChe300.method_下载城市数据并解析入库(filePath);

        // mainChe300.method_下载各个城市的首页数据(filePath + "各个城市分页数据/");
        mainChe300.parse_各个城市的分页数据(filePath + "各个城市分页数据/");
    }

    // 1.城市数据
    public void method_下载城市数据并解析入库(String filePath) {
        try {
            ArrayList<Object> dataList = new ArrayList<>();
            T_Config_File.method_访问url获取Json普通版("https://www.che300.com/api/city/city_selector", "GBK", filePath, "cityData.txt");
            String content = T_Config_File.method_读取文件内容(filePath + "cityData.txt");
            JSONObject jsonObject = JSON.parseObject(content).getJSONObject("data");
            JSONArray cityArrayInitial = jsonObject.getJSONArray("initial");
            JSONObject provinceObject = jsonObject.getJSONObject("provinces");
            for (int i = 0; i < cityArrayInitial.size(); i++) {
                JSONArray provinceArray = provinceObject.getJSONArray(cityArrayInitial.get(i).toString());
                for (int j = 0; j < provinceArray.size(); j++) {
                    String prov_admin_code = ((JSONObject) provinceArray.get(j)).getString("admin_code");
                    String prov_id = ((JSONObject) provinceArray.get(j)).getString("prov_id");
                    String prov_name = ((JSONObject) provinceArray.get(j)).getString("prov_name");
                    String pro_initial = ((JSONObject) provinceArray.get(j)).getString("initial");
                    JSONArray cityArray = ((JSONObject) provinceArray.get(j)).getJSONArray("city_list");
                    for (int k = 0; k < cityArray.size(); k++) {
                        Che300_CityData che300CityData = new Che300_CityData();
                        String lot = ((JSONObject) cityArray.get(k)).getString("lot");
                        String admin_code = ((JSONObject) cityArray.get(k)).getString("admin_code");
                        String city_name = ((JSONObject) cityArray.get(k)).getString("city_name");
                        String all_plate_prefix = ((JSONObject) cityArray.get(k)).getString("all_plate_prefix");
                        String city_initial = ((JSONObject) cityArray.get(k)).getString("initial");
                        String city_code = ((JSONObject) cityArray.get(k)).getString("city_code");
                        String order_show = ((JSONObject) cityArray.get(k)).getString("order_show");
                        String plate_prefix = ((JSONObject) cityArray.get(k)).getString("plate_prefix");
                        String lat = ((JSONObject) cityArray.get(k)).getString("lat");
                        String city_id = ((JSONObject) cityArray.get(k)).getString("city_id");
                        che300CityData.set_C_prov_admin_code(prov_admin_code);
                        che300CityData.set_C_prov_id(prov_id);
                        che300CityData.set_C_prov_name(prov_name);
                        che300CityData.set_C_pro_initial(pro_initial);
                        che300CityData.set_C_lot(lot);
                        che300CityData.set_C_admin_code(admin_code);
                        che300CityData.set_C_city_name(city_name);
                        che300CityData.set_C_all_plate_prefix(all_plate_prefix);
                        che300CityData.set_C_city_initial(city_initial);
                        che300CityData.set_C_city_code(city_code);
                        che300CityData.set_C_order_show(order_show);
                        che300CityData.set_C_plate_prefix(plate_prefix);
                        che300CityData.set_C_lat(lat);
                        che300CityData.set_C_city_id(city_id);
                        che300CityData.set_C_IsFinish(0);
                        che300CityData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        dataList.add(che300CityData);
                    }
                }
            }
            new ErShouCheDataBase().che300_insert_入库城市数据(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2.下载各个城市的首页数据
    public void method_下载各个城市的首页数据(String filePath) {
        ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
        ArrayList<Object> cityDataList = erShouCheDataBase.che300_get_获取未下载首页的数据();
        StringBuffer cityInfoBuffer = new StringBuffer();
        for (int i = 0; i < cityDataList.size(); i++) {
            String cityId = ((Che300_CityData) cityDataList.get(i)).get_C_city_id();
            String cityName = ((Che300_CityData) cityDataList.get(i)).get_C_city_name();
            for (int j = 1; j < 99999; j++) {
                String mainUrl = "https://m.che300.com/all_list.htm?city=" + cityName + "&page=" + j;
                String cookie = "Hm_lvt_f33b83d5b301d5a0c3e722bd9d89acc8=1726128345; HMACCOUNT=C55176529CCEEDD5; Hm_lvt_12b6a0c74b9c210899f69b3429653ed6=1726152956; city=" + cityId + "; Hm_lpvt_12b6a0c74b9c210899f69b3429653ed6=1726152988; zg_did=%7B%22did%22%3A%20%2219010bff80e1802-0eca044cf9cbf4-1a525637-1d03d0-19010bff80f2940%22%7D; zg_db630a48aa614ee784df54cc5d0cdabb=%7B%22sid%22%3A%201726152957991%2C%22updated%22%3A%201726152990588%2C%22info%22%3A%201726152957993%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22m.che300.com%22%2C%22zs%22%3A%200%2C%22sc%22%3A%200%2C%22firstScreen%22%3A%201726152957991%7D; Hm_lpvt_f33b83d5b301d5a0c3e722bd9d89acc8=1726153230; _che300=OnSBTfpPC%2Bv10hLv8jS9xUAaBfyoVs28uCveqLGYYti3SrDfOwBgX0rrxudl0%2BkgeQUPgiPQ1WO7oreBshkWab%2BlVjsFeT34d1bqVgJctq1BSoCIkMwvvop05vcs55EEF8FJ%2F1GVEO90LeHQjJztiG7%2B5gi9wy9mSTTOP%2FPYcTWpy5RPbwEXt98xDg%2FEO%2BCmT6BHZtEo29WvjWk4YoAah7dVE0KOBI6msR4ewUnvh9%2BvAtgeOm68Ait%2Bkcn4IT%2FAjcfbeHzYy%2F0%2FfwP%2FPZkTgqS0lh%2BwPyJrAI9%2FKC78ZuQqjMgtKTZ9%2FX0yQC6Ql%2Ff7k3d2iqx2oI2p14L1a7tGEqS%2BmUnE0mw5h%2FFEpyLSBXRpi8UGYg6931jNBbjkC94DuqlvHz8ba2u6cDYUz55mtN3OKp3y8v0dKqCNZJe6gAuq8G3fTJz%2FfPtr9YMstLcOetyh%2FpldCqAn2SJNFDZiv2PYGQ56Mc1Fv4%2F7D9bXxKbsWsxVlb7Km2pkmgyajbsPyI%2B%2BJc9jIixzr8G0hMJCM1dpi0yvkyREs2aaydBHbjceZdiV6rAxD3meYklF7vcCaXRURvpJfl4hJBeuP9F%2FjQ%3D%3D0f8fd5374f329d8dfd7bcff53ddf7d852ab4d5ae";
//            String cookie ="_dx_captcha_cid=77949496; _dx_uzZo5y=580edada146b50a08f0e77ab96123285eb4d94a6890b6bf2dc6a9748061586a37ade8706; zg_did=%7B%22did%22%3A%20%2219010bff80e1802-0eca044cf9cbf4-1a525637-1d03d0-19010bff80f2940%22%7D; zg_db630a48aa614ee784df54cc5d0cdabb=%7B%22sid%22%3A%201718267934736%2C%22updated%22%3A%201718267934736%2C%22info%22%3A%201718267934737%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22%22%7D; _dx_FMrPY6=66e152171QBUo999FpOs55DlsoPIheouTOaCrf21; _dx_app_f780acccd6e391d352f2076600d5aa16=66e152171QBUo999FpOs55DlsoPIheouTOaCrf21; Hm_lvt_f33b83d5b301d5a0c3e722bd9d89acc8=1726128345; HMACCOUNT=C55176529CCEEDD5; _dx_captcha_vid=DDCCF6F5E50BE9E01949898BF8765D1F4F0C65F911BBAF18593CCC1D3187D68C58922E7F2D4865BFB3658723D6358CEB0C560DC7D44D40FA4DB2FFCBC929FBDB510D11F9BE3525F330CEBD026AD20ACC; Hm_lpvt_f33b83d5b301d5a0c3e722bd9d89acc8=1726152077; historyCitys=%5B%7B%22city_id%22%3A1%2C%22city_name%22%3A%22%E5%8C%97%E4%BA%AC%22%7D%2C%7B%22city_id%22%3A99%2C%22city_name%22%3A%22%E6%B7%AE%E5%8D%97%22%7D%2C%7B%22city_id%22%3A13%2C%22city_name%22%3A%22%E5%90%88%E8%82%A5%22%7D%5D; city=1; _che300=URRndnyD3uchVDEQpr7HsiKjNBVfcorTSzaQcMWS2%2FsobRsats9nYEoQONc2YhJMjPRNyHCMmKy9NWU3Fb3ogzMaT7gDEs093ix6Ns3IiMbZanFiXMsbsb7lPe5Ulcd1vQVokdXwQwmt2V44B%2BjqSzOg0MrJRd0%2FQjpyBPfSd5n90%2FYVfrVXlN%2BS3k9hpHKbQb9PLWcUPYwbiGXD8JMDuyshdryU%2FK5Sg5xuGV8Dv%2FIAYoJslFF1AQTskusx2ur3Kub5kmWs%2BQr7Il0sypAD31LUZIInqq44nBkgGrhgvzAXKtrMJLkodhMUTwDpRZ64oVRClBBbxwiAjzwlO5mLb3N0R00VpAKQ3otnDJ%2FfyJKp%2BunMhcCKZJNUsJt8r2EQkqMJ8Gzg%2BEq6bJF5DMzTzHqFYLU9qwK%2FCPqb1HOq%2FK6xaobufbFRLGSs7mXqmjWpaGl%2F%2BlPiCHjjmqG%2BPA0eT9DX9scV35qXDM%2B7L8QpwbkL0rSLc8E3Km34oa1mLAFHUQR0TNWkPWF03jcaKF57AGJwrahar7mpyR6TVOVXo9OcMDmOT538M4JMIUT%2BctGSwMPppLbp2JlQFbviqeXzdw%3D%3D88f1a0d10873fe4ac50458d2fcfedfcaa3ec3d33; spidercooskieXX12=1726152082; spidercodeCI12X3=77ddbfb3de0edf32b07c17a80173a6a5\n";
                Document mainDoc = null;
                try {
                    mainDoc = Jsoup.connect(mainUrl).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").header("cookie", cookie).ignoreContentType(true).get();
                    Random random = new Random();
                    int intR = 1+random.nextInt(5);
                    Thread.sleep(intR*500);
                } catch (Exception e) {
                    System.out.println(cityName);
                }
                if (mainDoc != null && mainDoc.toString().contains("款")) {
                    T_Config_File.method_写文件_根据路径创建文件夹(filePath, cityName + "_" + cityId + "_" + j + ".txt", mainDoc.toString());
                } else {
                    erShouCheDataBase.che300_update_修改已下载的分页数据的下载状态(cityId);
                    break;
                }
            }


        }
    }

    // 3.解析各个城市的分页数据
    public void parse_各个城市的分页数据(String filePath) {
        List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
        ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
        ArrayList<Object> dataList = new ArrayList<>();
        for (String fileNamePath : fileList) {
            String fileName = fileNamePath.replace(filePath, "");
            if (!fileName.equals(".DS_Store")) {
                String cityName = fileName.split("_")[0];
                String cityId = fileName.split("_")[1];
                String pageCount = fileName.split("_")[2].replace(".txt", "");
                String content = T_Config_File.method_读取文件内容(fileNamePath);
                Document mainDoc = Jsoup.parse(content);
                Elements mainItems = null;
                if (pageCount.equals("1")) {
                    mainItems = mainDoc.select(".mainCon").select("a");
                } else {
                    mainItems = mainDoc.select("a");
                }
                for (int i = 0; i < mainItems.size(); i++) {
                    String detailsUrl = mainItems.get(i).select(".carItem").attr("href");
                    String carImg = mainItems.get(i).select(".list_img").attr("src");
                    String carName = mainItems.get(i).select(".carItemMsgTitle").select("span").text();
                    String carItemMsgMile = mainItems.get(i).select(".carItemMsgMile").select("span").text();
                    String[] temp = carItemMsgMile.split(" \\| ");
                    String shangPaiTime = temp[0];
                    String mileNum = temp[1];
                    String carResource = temp[2];
                    String carPrice = mainItems.get(i).select(".price-vpr").select(".eval-price").text();
                    String priceItemString = mainItems.get(i).select(".price-vpr").text().substring(mainItems.get(i).select(".price-vpr").text().indexOf("万") + 1);
                    String gujia = priceItemString.substring(0, priceItemString.indexOf("万") < -1 ? priceItemString.indexOf("性价比") : priceItemString.indexOf("万") + 1);
                    String xingjiabi = "";
                    if (priceItemString.contains("性价比")) {
                        xingjiabi = priceItemString.substring(priceItemString.indexOf("性价比"));
                    }
                    Che300_CarInfo che300_carInfo = new Che300_CarInfo();
                    che300_carInfo.set_C_Page(Integer.parseInt(pageCount));
                    che300_carInfo.set_C_CarPrice(carPrice);
                    che300_carInfo.set_C_CityName(cityName);
                    che300_carInfo.set_C_上牌时间(shangPaiTime);
                    che300_carInfo.set_C_公里数(mileNum);
                    che300_carInfo.set_C_CarResource(carResource);
                    che300_carInfo.set_C_CityId(cityId);
                    che300_carInfo.set_C_CarPrice(carPrice);
                    che300_carInfo.set_C_VersionName(carName);
                    che300_carInfo.set_C_DetailsUrl(detailsUrl);
                    che300_carInfo.set_C_CarImg(carImg);
                    che300_carInfo.set_C_PublishTime("");
                    che300_carInfo.set_C_估价(gujia);
                    che300_carInfo.set_C_性价比(xingjiabi);
                    che300_carInfo.set_C_IsFinish(0);
                    che300_carInfo.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    dataList.add(che300_carInfo);
                    if (dataList.size() > 100) {
                        erShouCheDataBase.che300_insert_入库车辆基本信息数据(dataList);
                        dataList.clear();
                    }
                }
            }

        }
        if (dataList.size() > 0) {
            erShouCheDataBase.che300_insert_入库车辆基本信息数据(dataList);
        }
    }

    public void method_解析各城市的首页数据(Document mainDoc) {
        System.out.println(mainDoc.select(".mainCon").select("a"));
    }

    public static Boolean method_访问url获取网页源码(String url, String filePath, String fileName, String cookie) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").header("cookie", cookie).ignoreContentType(true).get();
        } catch (Exception e) {
            return false;
        }
        if (mainDoc != null && mainDoc.toString().contains("款")) {
            T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName, mainDoc.toString());
            return true;
        } else {
            return false;
        }
    }
}
