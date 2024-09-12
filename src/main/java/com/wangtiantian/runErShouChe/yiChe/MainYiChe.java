package com.wangtiantian.runErShouChe.yiChe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.ershouche.yiChe.YiChe_CarInfo;
import com.wangtiantian.entity.ershouche.yiChe.YiChe_CityData;
import com.wangtiantian.entity.ershouche.yiChe.YiChe_FenYeUrl;
import com.wangtiantian.mapper.ErShouCheDataBase;
import com.wangtiantian.runErShouChe.che168.Che168AllFenYeThread;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainYiChe {
    public static void main(String[] args) {
//        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/二手车数据/yiche/20240911/";
        String filePath = "D:/爬取网页源数据/yiche/20240911/";
        MainYiChe mainYiChe = new MainYiChe();
        // 1
        // mainYiChe.method_下载城市数据并入库(filePath);

        // 2
        // mainYiChe.method_下载所有城市的首页数据(filePath + "各个城市分页数据/");

        // 3
        // mainYiChe.parse_解析所有城市的首页数据(filePath + "各个城市分页数据/");

        // 4
        // mainYiChe.method_下载其余分页url数据(filePath + "各个城市分页数据/");

        // 5
        // mainYiChe.parse_解析所有车辆基本信息(filePath + "各个城市分页数据/");

        // 6
        mainYiChe.method_获取易车二手车的车辆详情页面(filePath+"车辆详情页页面/");

    }

    // 1.下载城市数据并入库
    public void method_下载城市数据并入库(String filePath) {
        String cityUrl = "https://mapi.yiche.com/web_app/api/v1/city/get_area_list";
        ArrayList<Object> dataList = new ArrayList<>();
        if (T_Config_File.method_访问url获取Json普通版(cityUrl, "UTF-8", filePath, "cityData.txt")) {
            // 解析数据
            String cityContent = T_Config_File.method_读取文件内容(filePath + "cityData.txt");
            JSONObject jsonObject = JSONObject.parseObject(cityContent).getJSONObject("data");
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject cityObject = jsonArray.getJSONObject(i);
                String parentId = cityObject.getString("ParentId");
                String cityId = cityObject.getString("CityId");
                String cityLevel = cityObject.getString("CityLevel");
                String parentRegionId = cityObject.getString("ParentRegionId");
                String engName = cityObject.getString("EngName");
                String cityName = cityObject.getString("CityName");
                String regionId = cityObject.getString("RegionId");
                String initial = cityObject.getString("Initial");
                YiChe_CityData yiCheCityData = new YiChe_CityData();
                yiCheCityData.set_C_CityId(cityId);
                yiCheCityData.set_C_CityLevel(cityLevel);
                yiCheCityData.set_C_CityName(cityName);
                yiCheCityData.set_C_Initial(initial);
                yiCheCityData.set_C_EngName(engName);
                yiCheCityData.set_C_ParentId(parentId);
                yiCheCityData.set_C_ParentRegionId(parentRegionId);
                yiCheCityData.set_C_RegionId(regionId);
                yiCheCityData.set_C_IsFinish(0);
                yiCheCityData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                dataList.add(yiCheCityData);
            }
            ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
            erShouCheDataBase.insert_入库易车的城市数据(dataList);
        }


    }

    // 2.下载所有城市的首页数据
    public void method_下载所有城市的首页数据(String filePath) {
        try {
            ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
            ArrayList<Object> cityDataList = erShouCheDataBase.yiche_get_获取未下载首页分页的城市();
            for (Object cityItem : cityDataList) {
                String cityPinYin = ((YiChe_CityData) cityItem).get_C_EngName();
                System.out.println(cityPinYin);
                String cityId = ((YiChe_CityData) cityItem).get_C_CityId();
                String mainUrl = "https://proconsumer.taocheche.com/c-yiche-consumer/car/get_ucar_list?page=1";
                String cityName = ((YiChe_CityData) cityItem).get_C_CityName();
                String regionId = ((YiChe_CityData) cityItem).get_C_RegionId();
                String encodedString = URLEncoder.encode(cityName, "UTF-8");
                String cookie = "auto_id=181f5816f5e830e88e006c677b6a2349; uuid=f216f73b-9ab6-4dcc-979b-7f60eea3740a; _utrace=f216f73b-9ab6-4dcc-979b-7f60eea3740a; _clck=1fag7v9%7C2%7Cfp3%7C0%7C1617; ipCity={%22cityId%22:910%2C%22cityName%22:%22%E4%BF%9D%E5%AE%9A%22%2C%22citySpell%22:%22baoding%22%2C%22cityCode%22:%22130600%22}; city=%7B%22cityName%22%3A%22" + encodedString + "%22%2C%22cityId%22%3A" + cityId + "%2C%22cityCode%22%3A" + regionId + "%2C%22citySpell%22%3A%22" + cityPinYin + "%22%7D; _clsk=s0qf0m%7C1726024145609%7C7%7C1%7Cz.clarity.ms%2Fcollect";
                method_访问url获取Json普通版(mainUrl, cityId, cookie, filePath, cityPinYin + "_1.txt");
                new ErShouCheDataBase().yiche_update_修改已下载的首页数据的下载状态(cityId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 3.解析所有城市的首页数据
    public void parse_解析所有城市的首页数据(String filePath) {
        try {
            List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
            ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
            ArrayList<Object> dataList = new ArrayList<>();
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(fileName);
                ArrayList<Object> cityDataList = erShouCheDataBase.yiche_get_根据城市拼音获取其他数据(fileName.replace(filePath, "").replace("_1.txt", ""));
                String engName = ((YiChe_CityData) cityDataList.get(0)).get_C_EngName();
                String cityName = ((YiChe_CityData) cityDataList.get(0)).get_C_CityName();
                String cityCode = ((YiChe_CityData) cityDataList.get(0)).get_C_CityId();
                String regionId = ((YiChe_CityData) cityDataList.get(0)).get_C_RegionId();
                JSONObject jsonObject = JSONObject.parseObject(content);
                JSONObject dataJson = jsonObject.getJSONObject("data").getJSONObject("carinfo");
                int totalPage = dataJson.getInteger("totalPage");
                int count = dataJson.getInteger("count");
                for (int i = 1; i < totalPage + 1; i++) {
                    YiChe_FenYeUrl bean = new YiChe_FenYeUrl();
                    bean.set_C_FenYeUrl("https://proconsumer.taocheche.com/c-yiche-consumer/car/get_ucar_list?page=" + i);
                    bean.set_C_CityPinYin(fileName.replace(filePath, "").replace("_1.txt", ""));
                    bean.set_C_PageCount(totalPage);
                    bean.set_C_Page(i);
                    bean.set_C_CountCar(String.valueOf(count));
                    bean.set_C_IsFinish(0);
                    bean.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    bean.set_C_CityId(cityCode);
                    bean.set_C_CityName(URLEncoder.encode(cityName, "UTF-8"));
                    bean.set_C_EngName(engName);
                    bean.set_C_RegionId(regionId);
                    dataList.add(bean);
                }
            }
            erShouCheDataBase.yiche_insert_城市分页数据url入库(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 4.下载其余分页url数据
    public void method_下载其余分页url数据(String filePath) {
        try {
            ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
            ArrayList<Object> cityDataList = erShouCheDataBase.yiche_get_获取未下载的城市分页url();
            System.out.println(cityDataList.size());
            if (cityDataList.size() > 36) {
                List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> cityDataList.subList(i * (cityDataList.size() + 5) / 6, Math.min((i + 1) * (cityDataList.size() + 5) / 6, cityDataList.size())))
                        .collect(Collectors.toList());
                for (int i = 0; i < cityDataList.size(); i++) {
                    YiCheFenYeThread moreThread = new YiCheFenYeThread(list.get(i), filePath);
                    Thread thread = new Thread(moreThread);
                    thread.start();
                }
            } else {
                for (Object cityItem : cityDataList) {
                    String cityPinYin = ((YiChe_FenYeUrl) cityItem).get_C_EngName();
                    String cityId = ((YiChe_FenYeUrl) cityItem).get_C_CityId();
                    String mainUrl = ((YiChe_FenYeUrl) cityItem).get_C_FenYeUrl();
                    String cityName = ((YiChe_FenYeUrl) cityItem).get_C_CityName();
                    String regionId = ((YiChe_FenYeUrl) cityItem).get_C_RegionId();
                    int page = ((YiChe_FenYeUrl) cityItem).get_C_Page();
                    String cookie = "auto_id=181f5816f5e830e88e006c677b6a2349; uuid=f216f73b-9ab6-4dcc-979b-7f60eea3740a; _utrace=f216f73b-9ab6-4dcc-979b-7f60eea3740a; _clck=1fag7v9%7C2%7Cfp3%7C0%7C1617; ipCity={%22cityId%22:910%2C%22cityName%22:%22%E4%BF%9D%E5%AE%9A%22%2C%22citySpell%22:%22baoding%22%2C%22cityCode%22:%22130600%22}; city=%7B%22cityName%22%3A%22" + cityName + "%22%2C%22cityId%22%3A" + cityId + "%2C%22cityCode%22%3A" + regionId + "%2C%22citySpell%22%3A%22" + cityPinYin + "%22%7D; _clsk=s0qf0m%7C1726024145609%7C7%7C1%7Cz.clarity.ms%2Fcollect";
                    if (MainYiChe.method_访问url(mainUrl, cityId, cookie, filePath, cityPinYin + "_" + page + ".txt")) {
                        new ErShouCheDataBase().yiche_update_修改已下载的分页数据下载状态(mainUrl);
                    }
                }
            }
            if (erShouCheDataBase.yiche_get_获取未下载的城市分页url().size() > 0) {
                method_下载其余分页url数据(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // final 解析所有车辆基本信息
    public void parse_解析所有车辆基本信息(String filePath) {
        List<String> fileList = T_Config_File.method_流式获取文件名称(filePath);
        ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
        ArrayList<Object> dataList = new ArrayList<>();
        for (String fileName : fileList) {
            if (!fileName.equals("/Users/asteroid/所有文件数据/爬取网页原始数据/二手车数据/yiche/20240911/各个城市分页数据/.DS_Store")) {
                String content = T_Config_File.method_读取文件内容(fileName);
                JSONObject jsonObject = JSONObject.parseObject(content);
                JSONObject dataJson = jsonObject.getJSONObject("data").getJSONObject("carinfo");
                int totalPage = dataJson.getInteger("totalPage");
                int count = dataJson.getInteger("count");
                JSONArray jsonArray = dataJson.getJSONArray("carList");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject itemObject = ((JSONObject) jsonArray.get(i));
                    JSONArray labelsArray = itemObject.getJSONArray("labels");
                    String tradeNumber = itemObject.getString("tradeNumber");
                    String carName = itemObject.getString("carName");
                    String platformAuth = itemObject.getString("platformAuth");
                    String vendorId = itemObject.getString("vendorId");
                    String cityId = itemObject.getString("cityId");
                    String source = itemObject.getString("source");
                    String licenseDate = itemObject.getString("licenseDate");
                    String carYear = itemObject.getString("carYear");
                    String unifiedDistributionNumber = itemObject.getString("unifiedDistributionNumber");
                    String referPrice = itemObject.getString("referPrice");
                    String videoCount = itemObject.getString("videoCount");
                    String serialName = itemObject.getString("serialName");
                    String extUCarId = itemObject.getString("extUCarId");
                    String usedId = itemObject.getString("usedId");
                    String cityLocalName = itemObject.getString("cityLocalName");
                    String carType = itemObject.getString("carType");
                    String cityName = itemObject.getString("cityName");
                    String serialId = itemObject.getString("serialId");
                    String displayPrice = itemObject.getString("displayPrice");
                    String monthlyPayment = itemObject.getString("monthlyPayment");
                    String downPayment = itemObject.getString("downPayment");
                    String ypUsedId = itemObject.getString("ypUsedId");
                    String isFixedPrice = itemObject.getString("isFixedPrice");
                    String uCarId = itemObject.getString("uCarId");
                    String cityStandardId = itemObject.getString("cityStandardId");
                    String unifiedNumber = itemObject.getString("unifiedNumber");
                    String isParallelImportCar = itemObject.getString("isParallelImportCar");
                    String relationIndex = itemObject.getString("relationIndex");
                    String vendorName = itemObject.getString("vendorName");
                    String keyAccountLabel = itemObject.getString("keyAccountLabel") == null ? "-" : itemObject.getString("keyAccountLabel");
                    String carId = itemObject.getString("carId");
                    String coverUrl = itemObject.getString("coverUrl");
                    String drivingMileage = itemObject.getString("drivingMileage");
                    String recommendType = itemObject.getString("recommendType");
                    String unifiedVendorNumber = itemObject.getString("unifiedVendorNumber");
                    String relationPage = itemObject.getString("relationPage");
                    String bgcolor = "";
                    String labelText = "";
                    String fontText = "";
                    String type = "";
                    String colorType = "";
                    if (null != labelsArray && labelsArray.size() > 0) {
                        for (int j = 0; j < labelsArray.size(); j++) {
                            JSONObject labelsObject = ((JSONObject) labelsArray.get(j));
                            bgcolor = labelsObject.getString("bgcolor");
                            labelText = labelsObject.getString("labelText");
                            fontText = labelsObject.getString("fontText");
                            type = labelsObject.getString("type");
                            colorType = labelsObject.getString("colorType") == null ? "-" : labelsObject.getString("colorType");
                        }
                    }
                    YiChe_CarInfo carInfo = new YiChe_CarInfo();
                    carInfo.set_C_tradeNumber(tradeNumber);
                    carInfo.set_C_carName(carName);
                    carInfo.set_C_platformAuth(platformAuth);
                    carInfo.set_C_vendorId(vendorId);
                    carInfo.set_C_cityId(cityId);
                    carInfo.set_C_source(source);
                    carInfo.set_C_licenseDate(licenseDate);
                    carInfo.set_C_carYear(carYear);
                    carInfo.set_C_unifiedDistributionNumber(unifiedDistributionNumber);
                    carInfo.set_C_referPrice(referPrice);
                    carInfo.set_C_videoCount(videoCount);
                    carInfo.set_C_serialName(serialName);
                    carInfo.set_C_extUCarId(extUCarId);
                    carInfo.set_C_usedId(usedId);
                    carInfo.set_C_cityLocalName(cityLocalName);
                    carInfo.set_C_carType(carType);
                    carInfo.set_C_cityName(cityName);
                    carInfo.set_C_serialId(serialId);
                    carInfo.set_C_displayPrice(displayPrice);
                    carInfo.set_C_monthlyPayment(monthlyPayment);
                    carInfo.set_C_downPayment(downPayment);
                    carInfo.set_C_ypUsedId(ypUsedId);
                    carInfo.set_C_isFixedPrice(isFixedPrice);
                    carInfo.set_C_uCarId(uCarId);
                    carInfo.set_C_cityStandardId(cityStandardId);
                    carInfo.set_C_unifiedNumber(unifiedNumber);
                    carInfo.set_C_isParallelImportCar(isParallelImportCar);
                    carInfo.set_C_relationIndex(relationIndex);
                    carInfo.set_C_vendorName(vendorName);
                    carInfo.set_C_keyAccountLabel(keyAccountLabel);
                    carInfo.set_C_carId(carId);
                    carInfo.set_C_coverUrl(coverUrl);
                    carInfo.set_C_drivingMileage(drivingMileage);
                    carInfo.set_C_recommendType(recommendType);
                    carInfo.set_C_unifiedVendorNumber(unifiedVendorNumber);
                    carInfo.set_C_relationPage(relationPage);
                    carInfo.set_C_bgcolor(bgcolor);
                    carInfo.set_C_labelText(labelText);
                    carInfo.set_C_fontText(fontText);
                    carInfo.set_C_type(type);
                    carInfo.set_C_colorType(colorType);
                    carInfo.set_C_FileName(fileName.replace(filePath, ""));
                    carInfo.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    carInfo.set_C_IsFinish(0);
                    dataList.add(carInfo);
                    if (dataList.size() > 100000) {
                        erShouCheDataBase.yiche_insert_车辆基本信息数据入库(dataList);
                        dataList.clear();
                    }
                }
            }
        }
        if (dataList.size() > 0) {
            erShouCheDataBase.yiche_insert_车辆基本信息数据入库(dataList);
        }
    }

    // 获取易车二手车的车辆详情页面
    public void method_获取易车二手车的车辆详情页面(String filePath){
        ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
        ArrayList<Object> carInfoList = erShouCheDataBase.yiche_get_车辆基本信息数据();
        if (carInfoList.size()>36){
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> carInfoList.subList(i * (carInfoList.size() + 5) / 6, Math.min((i + 1) * (carInfoList.size() + 5) / 6, carInfoList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                YiCheDetailsThread moreThread = new YiCheDetailsThread(list.get(i), filePath);
                Thread thread = new Thread(moreThread);
                thread.start();
            }
        }else {
            for (int i = 0; i < carInfoList.size(); i++) {
                String carId = ((YiChe_CarInfo)carInfoList.get(i)).get_C_uCarId();
                String cityPinYin =((YiChe_CarInfo)carInfoList.get(i)).get_C_FileName().split("_")[0];
                String cityName = ((YiChe_CarInfo)carInfoList.get(i)).get_C_cityName();
                String mainUrl ="https://yiche.taocheche.com/detail/"+carId+"?city="+cityPinYin;
                if(T_Config_File.method_访问url获取网页源码普通版(mainUrl,"UTF-8",filePath,carId+"_"+cityPinYin+".txt")){
                    erShouCheDataBase.yiche_update_修改已下载详情页面的车辆状态(carId,cityName);
                }
            }
        }

        if (erShouCheDataBase.yiche_get_车辆基本信息数据().size()>0){
            method_获取易车二手车的车辆详情页面(filePath);
        }
    }

    // 带请求头的下载城市分页首页
    public static Boolean method_访问url获取Json普通版(String url, String cityId, String cookie, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").header("cityid", cityId).header("cookie", cookie).ignoreContentType(true).get();
        } catch (Exception e) {
            return false;
        }
        if (mainDoc != null) {
            T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName, mainDoc.text());
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            return true;
        } else {
            return false;
        }
    }

    public static Boolean method_访问url(String url, String cityId, String cookie, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").header("cityid", cityId).header("cookie", cookie).ignoreContentType(true).get();
        } catch (Exception e) {
            return false;
        }
        if (mainDoc != null) {
            JSONObject jsonObject = JSON.parseObject(mainDoc.text());
            JSONObject dataJson = jsonObject.getJSONObject("data").getJSONObject("carinfo");
            if (dataJson == null && !fileName.split("_")[1].replace(".txt", "").equals("1")) {
                System.out.println(fileName + "\t" + mainDoc.text());
                return false;
            } else {
                T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName, mainDoc.text());
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                return true;
            }
        } else {
            return false;
        }
    }


}
