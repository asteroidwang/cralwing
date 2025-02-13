package com.wangtiantian.runErShouChe.che168;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.ershouche.BeanCarInfoErShou;
import com.wangtiantian.entity.ershouche.che168.Bean_CarHtml;
import com.wangtiantian.entity.ershouche.che168.Che168_CarInfo;
import com.wangtiantian.entity.ershouche.che168.Che168_CityData;
import com.wangtiantian.entity.ershouche.che168.Che168_FenYeUrl;
import com.wangtiantian.mapper.ErShouCheDataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainChe168 {
    public static void main(String[] args) {
//        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/二手车数据/che168/20240820/";
        String filePath = "/Users/wangtiantian/MyDisk/汽车之家/二手车数据/che168/20241010/";
        MainChe168 mainChe168 = new MainChe168();
        // mainChe168.get_城市数据(filePath);
        // mainChe168.method_先下载每个城市的第一页二手车数据获取总页数(filePath+"各城市二手车分页/");
        // mainChe168.parse_解析第一页的数据获取总页数(filePath + "各城市二手车分页/");
        // mainChe168.downLoad_下载分页数据(filePath + "各城市二手车分页/");
        // mainChe168.parse_解析分页数据获取二手车数据(filePath + "各城市二手车分页/");
        // mainChe168.method_下载车辆详情页(filePath + "车辆详情页/");

        // mainChe168.parse_解析车辆详情页(filePath + "车辆详情页/");
    }

    public void method_修改已下载的车辆价格数据状态(String filePath) {
        List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
        for(String fileNamepath:fileList){
            System.out.println(fileNamepath);
        }
    }

    // 获取城市数据
    public void get_城市数据(String filePath) {
        if (T_Config_File.method_访问url获取Json普通版("https://dealer.autohome.com.cn/DealerList/GetAreasAjax?provinceId=0&cityId=&brandid=0&manufactoryid=0&seriesid=0&isSales=0", "GBK", filePath, "cityData.json")) {
            String content = T_Config_File.method_读取文件内容(filePath + "cityData.json");
            JSONObject jsonRoot = JSONObject.parseObject(content);
            JSONArray jsonArray = jsonRoot.getJSONArray("AreaInfoGroups");
            ArrayList<Object> cityDataArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray proValues = ((JSONObject) jsonArray.get(i)).getJSONArray("Values");
                for (int j = 0; j < proValues.size(); j++) {
                    JSONArray cityArray = ((JSONObject) proValues.get(j)).getJSONArray("Cities");
                    for (int k = 0; k < cityArray.size(); k++) {
                        Che168_CityData entity = new Che168_CityData();
                        entity.set_C_CityID(((JSONObject) cityArray.get(k)).getString("Id"));
                        entity.set_C_CityName(((JSONObject) cityArray.get(k)).getString("Name"));
                        entity.set_C_CityPinYin(((JSONObject) cityArray.get(k)).getString("Pinyin"));
                        entity.set_C_CityCount(((JSONObject) cityArray.get(k)).getInteger("Count"));
                        entity.set_C_ProvinceID(((JSONObject) proValues.get(j)).getString("Id"));
                        entity.set_C_ProvinceName(((JSONObject) proValues.get(j)).getString("Name"));
                        entity.set_C_ProvincePinYin(((JSONObject) proValues.get(j)).getString("Pinyin"));
                        entity.set_C_ProvincePinYin(((JSONObject) proValues.get(j)).getString("Pinyin"));
                        entity.set_C_ProvinceCount(((JSONObject) proValues.get(j)).getInteger("Count"));
                        entity.set_C_IsFinish(0);
                        entity.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        cityDataArrayList.add(entity);
                    }
                }
            }
            new ErShouCheDataBase().insert_cityData(cityDataArrayList);
        }
    }

    // 1.根据地区获取二手车数据
    // 先下载每个城市的第一页
    public void method_先下载每个城市的第一页二手车数据获取总页数(String filePath) {
        try {
            ArrayList<Object> dataList = new ErShouCheDataBase().get_CityData();
            if (dataList.size() < 32) {
                for (Object o : dataList) {
                    String pinyin = ((Che168_CityData) o).get_C_CityPinYin();
                    String mainUrl = "https://www.che168.com/nlist/" + pinyin + "/list/?pvareaid=100533";
                    if (MainChe168.method_访问二手车url获取网页源码(mainUrl, "GBK", filePath, pinyin + "_1.txt")) {
                        new ErShouCheDataBase().update_修改已下载的城市状态(pinyin);
                    }
                }
            } else {
                List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                        .collect(Collectors.toList());
                for (int i = 0; i < list.size(); i++) {
                    Che168FenYeThread moreThread = new Che168FenYeThread(list.get(i), filePath);
                    Thread thread = new Thread(moreThread);
                    thread.start();
                }
            }
            if (new ErShouCheDataBase().get_CityData().size() > 0) {
                method_先下载每个城市的第一页二手车数据获取总页数(filePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2.解析第一页的数据获取总页数
    public void parse_解析第一页的数据获取总页数(String filePath) {
        try {
            ArrayList<Object> dataList = new ErShouCheDataBase().get_CityData_已经下载();
            ArrayList<Object> result = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                String pinyin = ((Che168_CityData) dataList.get(i)).get_C_CityPinYin();
                String content = T_Config_File.method_读取文件内容(filePath + pinyin + "_1.txt");
                Document mainDoc = Jsoup.parse(content);
                Elements countItems = mainDoc.select(".list-menu").select(".tab-nav").select("li");
//                String countNum = countItems.select(".current").select("a").text().replace("全部车源(", "").replace(")", "");
                String countNum = "0";
                if (!countItems.select(".current").select("a").text().replace("全部车源(", "").replace(")", "").equals("")) {
                    countNum = countItems.select(".current").select("a").text().replace("全部车源(", "").replace(")", "");
                }
                if (countNum.equals("全部车源")) {
                    countNum = "0";
                }

                for (int ii = 1; ii < Integer.parseInt(countNum) / 56 + 2; ii++) {
                    int isFinish = 0;
                    if (ii == 1) {
                        isFinish = 1;
                    }
                    String mainUrl = "https://www.che168.com/" + pinyin + "/a0_0msdgscncgpi1ltocsp" + ii + "exx0/?pvareaid=102179#currengpostion";
                    Che168_FenYeUrl fenYeUrl = new Che168_FenYeUrl();
                    fenYeUrl.set_C_FenYeUrl(mainUrl);
                    fenYeUrl.set_C_IsFinish(isFinish);
                    fenYeUrl.set_C_Page(ii);
                    fenYeUrl.set_C_PageCount(Integer.parseInt(countNum) / 56 + 1);
                    fenYeUrl.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    fenYeUrl.set_C_CityPinYin(pinyin);
                    fenYeUrl.set_C_CountCar(countNum);
                    result.add(fenYeUrl);
                }
            }
            new ErShouCheDataBase().insert_新增二手车数据分页(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 3.下载分页数据
    public void downLoad_下载分页数据(String filePath) {
        ArrayList<Object> dataList = new ErShouCheDataBase().get_获取未下载的分页url();
        System.out.println(dataList.size());
        if (dataList.size() < 32) {
            for (Object o : dataList) {
                String mainUrl = ((Che168_FenYeUrl) o).get_C_FenYeUrl();
                System.out.println(mainUrl);
                String cityPinYin = mainUrl.split("/")[3];
                String fileName = cityPinYin + "_" + ((Che168_FenYeUrl) o).get_C_Page() + ".txt";
                if (method_访问二手车url获取网页源码(mainUrl, "GBK", filePath, fileName)) {
                    new ErShouCheDataBase().update_修改已下载的分页数据下载状态(mainUrl);
                }
            }
        } else {
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                Che168AllFenYeThread moreThread = new Che168AllFenYeThread(list.get(i), filePath);
                Thread thread = new Thread(moreThread);
                thread.start();
            }
        }
        System.out.println(new ErShouCheDataBase().get_获取未下载的分页url().size());
        if (new ErShouCheDataBase().get_获取未下载的分页url().size() > 0) {
            downLoad_下载分页数据(filePath);
        }
    }

    // 4.解析分页数据获取二手车数据
    public void parse_解析分页数据获取二手车数据(String filePath) {
        ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
        ArrayList<Object> dataList = new ArrayList<>();
        for (String fileName : fileList) {
            if (!fileName.equals(".DS_Store")) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(content);
                Elements countItems = mainDoc.select(".list-menu").select(".tab-nav").select("li");
                String countNum = countItems.select(".current").select("a").text().replace("全部车源(", "").replace(")", "");
                if (countNum.equals("全部车源")) {
                    countNum = "0";
                }
                Elements mainItems = mainDoc.select(".viewlist_ul").select("li");
                for (int i = 0; i < mainItems.size(); i++) {
                    String imgUrl = "https:" + mainItems.get(i).select("img").attr("src");
                    String carName = mainItems.get(i).select(".card-name").text();
                    String[] infoItems = mainItems.get(i).select(".cards-unit").text().split("／");
                    String huiYuan = "";
                    String mile = "";
                    String time = "";
                    String location = "";
                    if (infoItems.length == 4) {
                        mile = infoItems[0];
                        time = infoItems[1];
                        location = infoItems[2];
                        huiYuan = infoItems[3];
                    } else if (infoItems.length == 3) {
                        mile = infoItems[0];
                        time = infoItems[1];
                        location = infoItems[2];
                    } else if (infoItems.length == 2) {
                        mile = infoItems[0];
                        time = infoItems[1];
                    } else if (infoItems.length == 1) {
                        mile = infoItems[0];
                    }
                    String guohu = mainItems.get(i).select(".tags").select(".tags-light").text();
                    String htmlUrl = carName.equals("") ? "" : "https://www.che168.com" + mainItems.get(i).select(".carinfo").attr("href");
//                    String carId =
                    String price = mainItems.get(i).select(".cards-price-box").select(".pirce").text();
                    String priceYuan = mainItems.get(i).select(".cards-price-box").select("s").text();
                    String carId = mainItems.get(i).select(".carinfo").attr("href").equals("") ? "" : mainItems.get(i).select(".carinfo").attr("href").substring(0, mainItems.get(i).select(".carinfo").attr("href").indexOf("."));
                    Bean_CarHtml html = new Bean_CarHtml();
                    html.set_C_CarHtml(htmlUrl);
                    html.set_C_IsFinish(0);
                    html.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    dataList.add(html);
//                    Che168_CarInfo carInfoErShou = new Che168_CarInfo();
//                    carInfoErShou.set_C_CarId(htmlUrl);
//                    carInfoErShou.set_C_CarResource("che168");
//                    carInfoErShou.set_C_Price(price);
//                    carInfoErShou.set_C_CarId(carId);
//                    carInfoErShou.set_C_ImgUrl(imgUrl);
//                    carInfoErShou.set_C_CityName(location);
//                    carInfoErShou.set_C_MileNumber(mile);
//                    carInfoErShou.set_C_PublishTime("");
//                    carInfoErShou.set_C_RegistrationTime(time);
                }
            }
        }
        new ErShouCheDataBase().insert_新增che168车辆详情页url数据(dataList);
    }

    public static Boolean method_访问二手车url获取网页源码(String url, String encode, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.parse(new URL(url).openStream(), encode, url);
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

    public void method_下载车辆详情页(String filePath) {
        ArrayList<Object> dataList = new ErShouCheDataBase().get_获取che168车辆详情页url数据();
        if (dataList.size() < 36) {
            for (Object o : dataList) {
                String htmlUrl = ((Bean_CarHtml) o).get_C_CarHtml();
                if (!htmlUrl.equals("-")) {
                    String fileName = htmlUrl.substring(htmlUrl.indexOf("dealer") + 7, htmlUrl.indexOf(".html")).replace("/", "_");
                    method_访问url获取网页源码普通版(htmlUrl, "GBK", filePath, fileName + ".txt");
                }
            }
        } else {
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                Che168DetailsThread moreThread = new Che168DetailsThread(list.get(i), filePath);
                Thread thread = new Thread(moreThread);
                thread.start();
            }
        }
        if (new ErShouCheDataBase().get_获取che168车辆详情页url数据().size() > 0) {
            method_下载车辆详情页(filePath);
        }
    }

    public void parse_解析车辆详情页(String filePath) {
        try {
            List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
            for (String fileNamePath : fileList) {
                String fileName = fileNamePath.replace(filePath, "");
                System.out.println(fileName);
                String content = T_Config_File.method_读取文件内容(fileNamePath);
                Document mainDoc = Jsoup.parse(content);
                Elements mainItems = mainDoc.select(".basic-item-ul").select("li");
                System.out.println(mainItems.size());
                System.out.println(mainItems.get(0).text());
//                for (int i = 0; i < mainItems.size(); i++) {
//                    System.out.println(mainItems.get(i));
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Boolean method_访问url获取网页源码普通版(String url, String encode, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.parse(new URL(url).openStream(), encode, url);
        } catch (Exception e) {
            return false;
        }
        Elements mainItems = mainDoc.select(".car-brand-name");
        if (mainItems.size() > 0) {
            T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName, mainDoc.toString());
            return true;
        } else {
            return false;
        }

    }
}
