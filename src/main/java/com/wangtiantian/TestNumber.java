package com.wangtiantian;

import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Brand;
import com.wangtiantian.entity.Bean_Html_Pic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TestNumber {
    private static T_Config_AutoHome picHtmlDao = new T_Config_AutoHome(0, 5, 1);

    public static void main(String[] args) {
        String filePath = "/Volumes/PS2000W/汽车之家/图片html页面/";
        ArrayList<String> list = new ArrayList<>();
        //method_获取文件数量();
        //method_下载已入库品牌图片(filePath);
    }

    public static void runFlow(String id, ArrayList<String> list) {
    }

    public static void method_获取文件数量() {
        String filePath = "/Volumes/PS2000W/汽车之家/图片html页面/";
        ArrayList<String> brandList = T_Config_File.method_获取文件夹名称(filePath);
        int fctNumber = 0;
        int modNumber = 0;
        int verNumber = 0;
        for (int i = 0; i < brandList.size(); i++) {
            ArrayList<String> fctList = T_Config_File.method_获取文件夹名称(filePath + brandList.get(i));
            fctNumber += fctList.size();
            for (int ii = 0; ii < fctList.size(); ii++) {
                ArrayList<String> modList = T_Config_File.method_获取文件夹名称(filePath + brandList.get(i) + "/" + fctList.get(ii));
                modNumber += modList.size();
                for (int iii = 0; iii < modList.size(); iii++) {
                    ArrayList<String> verList = T_Config_File.method_获取文件夹名称(filePath + brandList.get(i) + "/" + fctList.get(ii) + "/" + modList.get(iii));
                    verNumber += verList.size();
                }
            }
        }
    }

    public static void method_下载已入库品牌图片(String filePath) {
        try {
            ArrayList<Object> itemsList = picHtmlDao.method_根据下载状态查找数据(0);
            int number = 0;
            for (int i = 0; i < itemsList.size(); i++) {
                String brandID = ((Bean_Html_Pic) itemsList.get(i)).get_C_BrandID();
                String fctId = ((Bean_Html_Pic) itemsList.get(i)).get_C_FactoryID();
                String modId = ((Bean_Html_Pic) itemsList.get(i)).get_C_ModelID();
                String verID = ((Bean_Html_Pic) itemsList.get(i)).get_C_VersionID();
                String type = ((Bean_Html_Pic) itemsList.get(i)).get_C_Type();
                String picID = ((Bean_Html_Pic) itemsList.get(i)).get_C_ImgID();
                Boolean isExists = T_Config_File.method_判断文件是否存在(filePath + brandID + "/" + fctId + "/" + modId + "/" + verID + "/" + type + "/" + picID + ".txt");
                if (!isExists) {
                    number++;
//                    https://car.autohome.com.cn/photo/62267/1/8746431.html
                    method_Jsoup_通用("https://car.autohome.com.cn/photo/" + verID + "/" + type + "/" + picID + ".html", filePath + brandID + "/" + fctId + "/" + modId + "/" + verID + "/" + type + "/", picID
                    );
                }
            }
            System.out.println(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_Jsoup_通用(String webURL, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect(webURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
        } catch (Exception e) {
//            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        if (mainDoc != null) {
            System.out.println(dateFormat.format(new Date()));
            T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName + ".txt", mainDoc.toString());
        } else {
//            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }
    }

}
