package com.wangtiantian.runErShouChe.renRenChe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.ershouche.renrenche.RenRenChe_CarInfo;
import com.wangtiantian.entity.ershouche.renrenche.RenRenChe_CityData;
import com.wangtiantian.entity.ershouche.renrenche.RenRenChe_FenYeUrl;
import com.wangtiantian.mapper.ErShouCheDataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainRenRenChe {
    public static void main(String[] args) {
//        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/二手车数据/renrenche/";
        String filePath = "/Users/wangtiantian/MyDisk/所有文件数据/二手车数据/renrenche/";
        MainRenRenChe renRenChe = new MainRenRenChe();
        // 1
        // renRenChe.method_下载城市数据并入库(filePath);

        // 2
        // renRenChe.method_下载城市分页数据的首页(filePath + "各城市分页的首页数据/");

        // 补充

        // new ErShouCheDataBase().get_文件命名错误数据();
        // 3
        // renRenChe.parse_解析城市分页的首页数据(filePath + "各城市分页的首页数据/");

        // 4
         renRenChe.method_下载城市除首页的其他分页数据(filePath + "各城市分页的首页数据/");


        // final
        // renRenChe.parse_解析城市分页数据(filePath + "各城市分页的首页数据/");

    }

    // 获取城市数据并入库
    public void method_下载城市数据并入库(String filePath) {
        // https://cheapi.renrenche.com/common/all/city?apart=2&_time=1726049281186&callback=jsonp_1726049281186_981833726864275
        String content = T_Config_File.method_读取文件内容(filePath + "cityData.json");
        JSONObject jsonObject = JSON.parseObject(content).getJSONObject("result");
        JSONArray jsonArray = jsonObject.getJSONArray("city");
        ArrayList<Object> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject cityObject = ((JSONObject) jsonArray.get(i));
            String zimu = cityObject.getString("zimu");
            if (!zimu.equals("热门城市")) {
                JSONArray optionsArray = cityObject.getJSONArray("option");
                for (int j = 0; j < optionsArray.size(); j++) {
                    JSONObject optionItems = ((JSONObject) optionsArray.get(j));
                    String quanpin = optionItems.getString("quanpin");
                    String name = optionItems.getString("name");
                    String id = optionItems.getString("id");
                    String listName = optionItems.getString("listName");
                    RenRenChe_CityData renRenCheCityData = new RenRenChe_CityData();
                    renRenCheCityData.set_C_CityId(id);
                    renRenCheCityData.set_C_listName(listName);
                    renRenCheCityData.set_C_IsFinish(0);
                    renRenCheCityData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    renRenCheCityData.set_C_quanpin(quanpin);
                    renRenCheCityData.set_C_name(name);
                    dataList.add(renRenCheCityData);
                }
            }
        }
        new ErShouCheDataBase().rrc_insert_城市数据入库(dataList);
    }

    // 下载城市分页数据的首页
    public void method_下载城市分页数据的首页(String filePath) {
        int failNum = 0;
        ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
        ArrayList<Object> cityDataList = erShouCheDataBase.rrc_get_获取未下载首页分页的城市();
        for (int i = 0; i < cityDataList.size(); i++) {
            String cityPinYin = ((RenRenChe_CityData) cityDataList.get(i)).get_C_listName();
            String cityId = ((RenRenChe_CityData) cityDataList.get(i)).get_C_CityId();
            String mainUrl = "https://www.renrenche.com/" + cityPinYin + "/ershouche/pn1/?reentries=%7B%22reentry_id%22%3A%22a242c2ba-f37d-48db-bd04-116a27baf712%22%7D";
            System.out.println(mainUrl);
            try {
                Thread.sleep(1000 * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (method_访问url获取Json普通版(mainUrl, filePath, cityPinYin + "_1.txt")) {
                erShouCheDataBase.rrc_update_修改已下载的首页数据的下载状态(cityId);
            } else {
                failNum++;
                if (failNum > 3) {
                    try {
                        Thread.sleep(1000 * 60 * 5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    method_下载城市分页数据的首页(filePath);
                }
            }
        }
        if (erShouCheDataBase.rrc_get_获取未下载首页分页的城市().size() > 0) {
            method_下载城市分页数据的首页(filePath);
        }
    }

    // 解析城市分页的首页数据并入库分页url数据
    public void parse_解析城市分页的首页数据(String filePath) {
        ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
        ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
        ArrayList<Object> dataList = new ArrayList<>();
        for (String fileName : fileList) {
            if (!fileName.equals(".DS_Store")) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(content);
                String numCar = mainDoc.select(".tab_car-number").text().replace("全部车源", "").replace("）", "").replace("（", "");
                Elements mainItems = mainDoc.select("#list").select("li");
                int page =1;
                if (mainItems.size()>0){
                    page =Integer.parseInt(numCar.replace("辆", "").equals("")?"50":numCar.replace("辆", "")) / mainItems.size();
                }
                for (int i = 1; i < page + 3; i++) {
                    String cityPinYin = fileName.replace(".txt", "").split("_")[0];
                    String fenYeUrl = "https://www.renrenche.com/" + cityPinYin + "/ershouche/pn" + i + "/?reentries={%22reentry_id%22:%22310385bf-7ac2-43f9-b1f3-9aebfab13543%22}";
                    RenRenChe_FenYeUrl renRenCheFenYeUrl = new RenRenChe_FenYeUrl();
                    renRenCheFenYeUrl.set_C_FenYeUrl(fenYeUrl);
                    renRenCheFenYeUrl.set_C_CityPinYin(cityPinYin);
                    renRenCheFenYeUrl.set_C_CountCar(numCar);
                    renRenCheFenYeUrl.set_C_Page(i);
                    renRenCheFenYeUrl.set_C_PageCount(page);
                    renRenCheFenYeUrl.set_C_IsFinish(0);
                    renRenCheFenYeUrl.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    dataList.add(renRenCheFenYeUrl);
                    if (dataList.size() > 100) {
                        erShouCheDataBase.rrc_insert_入库城市分页Url数据(dataList);
                        dataList.clear();
                    }
                }
            }

        }
        if (dataList.size() > 0) {
            erShouCheDataBase.rrc_insert_入库城市分页Url数据(dataList);
        }

    }

    // 下载城市其他分页数据
    public void method_下载城市除首页的其他分页数据(String filePath) {
        int failNum = 0;
        ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
        ArrayList<Object> fenYeUrlList = erShouCheDataBase.rrc_get_获取未下载分页的url数据();
        for (int i = 0; i < fenYeUrlList.size(); i++) {
            String cityPinYin = ((RenRenChe_FenYeUrl) fenYeUrlList.get(i)).get_C_CityPinYin();
            int page = ((RenRenChe_FenYeUrl) fenYeUrlList.get(i)).get_C_Page();
            String mainUrl = "https://www.renrenche.com/" + cityPinYin + "/ershouche/pn" + page + "/?reentries=%7B\"reentry_id\"%3A\"46282ce3-85a3-4670-8cfa-59b5e24cbd6c\"%7D";
            System.out.println(mainUrl);
            try {
                Thread.sleep(1000 * 60);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (method_访问url获取Json普通版(mainUrl, filePath, cityPinYin + "_" + page + ".txt")) {
                erShouCheDataBase.rrc_update_修改已下载的分页数据的下载状态(mainUrl);
            } else {
                failNum++;
                if (failNum > 3) {
                    try {
                        Thread.sleep(1000 * 60 * 5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    method_下载城市除首页的其他分页数据(filePath);
                }
            }
        }
        if (erShouCheDataBase.rrc_get_获取未下载分页的url数据().size() > 0) {
            method_下载城市除首页的其他分页数据(filePath);
        }
    }

    // final解析城市分页数据获取车辆基本信息
    public void parse_解析城市分页数据(String filePath) {
        ErShouCheDataBase erShouCheDataBase = new ErShouCheDataBase();
        ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
        ArrayList<Object> dataList = new ArrayList<>();
        for (String fileName : fileList) {
            if (!fileName.equals(".DS_Store")) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(content);
                String numCar = mainDoc.select(".tab_car-number").text().replace("全部车源", "").replace("）", "").replace("（", "");
                Elements mainItems = mainDoc.select("#list").select("li");
                int page = Integer.parseInt(numCar.replace("辆", "")) / 50;
                for (int i = 0; i < mainItems.size(); i++) {
                    String infoPostTime = mainItems.get(i).select(".info--post-time").text();
                    String carName = mainItems.get(i).select(".info_title").select(".info_link").text();
                    Elements tagsList = mainItems.get(i).select(".tags.h-clearfix").select("span");
                    StringBuffer tagsListBuffer = new StringBuffer();
                    for (int j = 0; j < tagsList.size(); j++) {
                        tagsListBuffer.append(tagsList.get(j).text() + "###");
                    }
                    String infoParams = mainItems.get(i).select(".info_params").text();
                    String infoPrices = mainItems.get(i).select(".info--price").text();
                    String price = get_转化价格数据(infoPrices.substring(0, infoPrices.length() - 1)) + "万";
                    String shangPaiTime = infoParams.split(" ")[0];
                    String mileNumber = infoParams.split(" ")[1].replace("·", "");
                    RenRenChe_CarInfo renRenChe_carInfo = new RenRenChe_CarInfo();
                    renRenChe_carInfo.set_C_infoPostTime(infoPostTime);
                    renRenChe_carInfo.set_C_carName(carName);
                    renRenChe_carInfo.set_C_tagsList(tagsListBuffer.toString());
                    renRenChe_carInfo.set_C_infoParams(infoParams);
                    renRenChe_carInfo.set_C_price(price);
                    renRenChe_carInfo.set_C_DetailsUrl(mainItems.select("a").attr("href"));
                    renRenChe_carInfo.set_C_上牌时间(shangPaiTime);
                    renRenChe_carInfo.set_C_公里数(mileNumber);
                    renRenChe_carInfo.set_C_FileName(fileName);
                    renRenChe_carInfo.set_C_IsFinish(0);
                    renRenChe_carInfo.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    dataList.add(renRenChe_carInfo);
                    if (dataList.size() > 100) {
                        erShouCheDataBase.rrc_insert_入库车辆的基本信息数据(dataList);
                        dataList.clear();
                    }
                }
            }

        }
        if (dataList.size() > 0) {
            erShouCheDataBase.rrc_insert_入库车辆的基本信息数据(dataList);
        }
    }


    public String get_转化价格数据(String price) {
        return price.replace("折", "1").replace("¥", "2").replace("/", "3").replace("元", "4").replace("-", "5").replace("+", "6").replace("起", "7").replace("万", "8").replace("%", "9").replace("时", "0");
    }

    public static Boolean method_访问url获取Json普通版(String url, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").header("cookie", "58tj_uuid=93a587a4-7eb9-4a8a-8ecb-48ea639e83d4; als=0; wmda_uuid=78780a56aed6776f45c2cd9287391904; wmda_new_uuid=1; xxzlclientid=2a133df4-1a07-4eb6-a0b8-1717568159900; xxzlxxid=pfmxIpgRUKaRrPOhv4+bF3rU1UW6mrZxYwYpArv/JM40RDkiEixFOJFDN4tAean79W/w; wmda_visited_projects=%3B1732038237441%3B1732039838209; new_uv=2; rrc_ip_province=; new_visitor_uuid=0261173037bc82dad0ae812946a2f0e8; id58=CrINgmas4vbAr2hLCpv6Ag==; fzq_h=48940c8c737d5253b8c418a4cccf2439_1726042784137_bfb6ee7203144d9d97bb6891cd878a20_1696328864; sessionid=69a0ee5d-ec40-404b-9c0b-6d069161a908; 58ua=rrcpc; wmda_session_id_1732038237441=1726061632329-782a093a-7e0f-55ce; rrc_rrc_session=e2i859l3q9buaj145c8hq9h111; rrc_rrc_signed=s%2Ce2i859l3q9buaj145c8hq9h111%2Cd38586402f54a9a24921348dd70d383f; rrc_ip_city_twohour=bd; rrc_ip_province=%E6%B2%B3%E5%8C%97; rrc_record_city=bd; rrc_fr=bd_other; rrc_tg=fr%3Dbd_other; rrc_ss=initiative; fzq_js_usdt_infolist_car=6fae5eb20ad4d12d4bda52d69c72d091_1726063524752_2; rrc_common_head_ipcity=bd%7C%E4%BF%9D%E5%AE%9A%7C424; xxzlbbid=pfmbM3wxMDI5M3wxLjEwLjB8MTcyNjA2MzUyODMzMDczNTA0OXxGS2RTTThBZXh5bFcyeVZudmR5R05YV0ZjSXlBVXRWbjhoQXE5aHZEdUFZPXw3OWVhNGNmZmEyYjRmYjQxNWY2Yjg5NjgyNzNlYWNhYl8xNzI2MDYzNTMwMTE4X2FjMjlmOWM4ZTY1ZjRjNWFhM2JkNGQ1MDgxNzUxYWMzXzE2OTYzMjg4NjR8YjY5ODcxMzg1NjA0YTM4MjNhMDRiZmEyZGI0YmE1ZTFfMTcyNjA2MzUyNzgyMF8yNTU=").ignoreContentType(true).get();
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
