package com.wangtiantian.controller;

import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_PicURL;
import com.wangtiantian.entity.Bean_VersionPic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Analysis_AutoHome_Pic {
    public static void method_下载图片页面html(String filePath) {
        T_Config_AutoHome autoHome = new T_Config_AutoHome(0, 0, 10);
        ArrayList<Object> result = autoHome.method_查找未下载图片的版本();
        for (int i = 0; i < result.size(); i++) {
            String verID = ((Bean_VersionPic) result.get(i)).get_C_VersionID();
            String modID = ((Bean_VersionPic) result.get(i)).get_C_ModelID();
            String fctID = ((Bean_VersionPic) result.get(i)).get_C_FactoryID();
            String brandID = ((Bean_VersionPic) result.get(i)).get_C_BrandID();
            int number = 1;
            String webURL = "https://car.autohome.com.cn/photolist/spec/" + verID + "/p1/#pvareaid=3454554";
            Document fileDoc = null;
            try {
                fileDoc = Jsoup.connect(webURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
            } catch (Exception e) {
                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "首次访问出错.txt", webURL + "\n");
            }
            if (fileDoc.select(".page-item-next") != null) {
                if (fileDoc.select(".page-item-next").size() != 0) {
                    String fileItems = fileDoc.select(".page-item-next").get(1).attr("href");
                    String tempNumber = fileItems.toString().substring(fileItems.toString().indexOf(verID)).replace(verID + "/p", "");
                    number = Integer.parseInt(tempNumber.replace("/", ""));
                }
            } else {
                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "空指针.txt", webURL + "\n");
            }
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "总数.txt", brandID + "\t" + fctID + "\t" + modID + "\t" + verID + "\t" + number + "\n");
            for (int ii = 1; ii < number + 1; ii++) {
                String picURL = "https://car.autohome.com.cn/photolist/spec/" + verID + "/p" + ii + "/#pvareaid=3454554";
                method_Jsoup(picURL, filePath + brandID + "/" + fctID + "/" + modID + "/" + verID + "/", verID + "_" + ii, verID + "_" + number, verID);
            }
        }
    }

    public static void method_确认html页面数量并解析(String filePath) {
        try {
            T_Config_AutoHome autoHome = new T_Config_AutoHome(0,0,10);
            ArrayList<String> brandList = T_Config_File.method_按行读取文件("/Users/asteroid/所有文件数据/爬取网页原始数据/brandID.txt");
//            ArrayList<String> brandList = T_Config_File.method_获取文件夹名称(filePath);
//            ArrayList<Object> brandList = autoHome.method_查找未下载图片的品牌ID();
            for (int i = 0; i < brandList.size(); i++) {
//                String brandID = ((Bean_VersionPic)brandList.get(i)).get_C_BrandID();
                String brandID = brandList.get(i);
                ArrayList<String> fctList = T_Config_File.method_获取文件夹名称(filePath + brandID);
                for (int ii = 0; ii < fctList.size(); ii++) {
                    //二级目录 厂商ID
                    String fctID = fctList.get(ii);
                    ArrayList<String> modList = T_Config_File.method_获取文件夹名称(filePath + brandID + "/" + fctID);
                    for (int iii = 0; iii < modList.size(); iii++) {
                        //三级目录 车型ID
                        String modID = modList.get(iii);
                        ArrayList<String> verList = T_Config_File.method_获取文件夹名称(filePath + brandID + "/" + fctID + "/" + modList.get(iii));
                        for (int k = 0; k < verList.size(); k++) {
                            //四级目录 版本ID
                            String verID = verList.get(k);
                            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + brandID + "/" + fctID + "/" + modList.get(iii) + "/" + verList.get(k));
                            for (int kk = 0; kk < fileList.size(); kk++) {
                                method_解析图片所在页面下载图片(filePath + brandID + "/" + fctID + "/" + modList.get(iii) + "/" + verList.get(k) + "/" + fileList.get(kk), brandID, fctID, modID, verID);
                            }
                        }
                    }
                }
            }
//            for (int i = 0; i < brandList.size(); i++) {
//                //一级目录 品牌ID
//                String brandID = brandList.get(i);
//                ArrayList<String> fctList = T_Config_File.method_获取文件夹名称(filePath + brandID);
//                for (int ii = 0; ii < fctList.size(); ii++) {
//                    //二级目录 厂商ID
//                    String fctID = fctList.get(ii);
//                    ArrayList<String> modList = T_Config_File.method_获取文件夹名称(filePath + brandID + "/" + fctID);
//                    for (int iii = 0; iii < modList.size(); iii++) {
//                        //三级目录 车型ID
//                        String modID = modList.get(iii);
//                        ArrayList<String> verList = T_Config_File.method_获取文件夹名称(filePath + brandID + "/" + fctID + "/" + modList.get(iii));
//                        for (int k = 0; k < verList.size(); k++) {
//                            //四级目录 版本ID
//                            String verID = verList.get(k);
//                            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath + brandID + "/" + fctID + "/" + modList.get(iii) + "/" + verList.get(k));
//                            for (int kk = 0; kk < fileList.size(); kk++) {
////                                System.out.println(filePath+brandID+"/"+fctID+"/"+modList.get(iii)+"/"+verList.get(k)+"/"+fileList.get(kk));
//                                method_解析图片所在页面下载图片(filePath + brandID + "/" + fctID + "/" + modList.get(iii) + "/" + verList.get(k) + "/" + fileList.get(kk), brandID, fctID, modID, verID);
//                            }
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_解析图片所在页面下载图片(String filePath, String brandID, String fctID, String modID, String verID) {
        try {
            String content = T_Config_File.method_读取文件内容(filePath);
            Document mainDoc = Jsoup.parse(content);
            Elements mainItems = mainDoc.select("#imgList");
            for (int i = 0; i < mainItems.size(); i++) {
                for (int ii = 0; ii < mainItems.get(i).select("li").size(); ii++) {
                    String imgID = mainItems.get(i).select("li").get(ii).attr("id").replace("li", "");
                    String imgURL = mainItems.get(i).select("li").get(ii).select("a").select("img").attr("src");
                    if (imgURL.indexOf("https://x.autoimg.cn/car/images/grey-bg.gif") > -1) {
                        imgURL = mainItems.get(i).select("li").get(ii).select("a").select("img").attr("src2");
                    }
                    String photoURL = mainItems.get(i).select("li").get(ii).select("a").attr("href");
                    String[] url = photoURL.split("/");
                    String imgType = url[3];
//                    T_Config_File.method_重复写文件_根据路径创建文件夹("/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/汽车之家_20240218/图片/","图片类型.txt",imgType+"\n");
                    T_Config_AutoHome picURLDao = new T_Config_AutoHome(0, 0, 11);
                    Bean_PicURL picURL = new Bean_PicURL();
                    picURL.set_C_PicID(imgID);
                    picURL.set_C_PicURL("https:"+imgURL);
                    picURL.set_C_是否下载(0);
                    picURL.set_C_BrandID(brandID);
                    picURL.set_C_FactoryID(fctID);
                    picURL.set_C_ModelID(modID);
                    picURL.set_C_PID(verID);
                    picURL.set_C_PicType(imgType);
                    picURL.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
                    picURLDao.method_更新图片URL数据(picURL,imgID,verID,imgType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method_Jsoup(String webURL, String filePath, String fileName, String total, String versionID) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect(webURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
        } catch (Exception e) {
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        if (mainDoc != null) {
            System.out.println(dateFormat.format(new Date()));
//            System.out.println(total+"\t"+versionID+"\t"+fileName);
            T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName + ".txt", mainDoc.toString());
            if (fileName.equals(total)) {
                T_Config_AutoHome autoHome = new T_Config_AutoHome(0, 0, 10);
                autoHome.method_修改下载图片的状态(versionID);
            }
        } else {
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }

    }

    public static void method_Jsoup_通用(String webURL, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.connect(webURL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36").ignoreContentType(true).get();
        } catch (Exception e) {
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        if (mainDoc != null) {
            System.out.println(filePath+fileName+".txt\t"+dateFormat.format(new Date()));
            T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName + ".txt", mainDoc.toString());
//            T_Config_AutoHome autoHome = new T_Config_AutoHome(0, 0, 9);
//            String temp = fileName.substring(fileName.indexOf("_"));
//            autoHome.method_修改车型页面下载状态(fileName.replace(temp, ""));
        } else {
            System.out.println("null");
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "出错.txt", webURL + "\n");
        }
    }
}
