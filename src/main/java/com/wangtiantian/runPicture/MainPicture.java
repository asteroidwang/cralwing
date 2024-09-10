package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.picture.PictureHtmlFileData;
import com.wangtiantian.entity.picture.PictureHtmlUrl;
import com.wangtiantian.entity.picture.PictureUrl;
import com.wangtiantian.entity.picture.Picture_FenYeUrl;
import com.wangtiantian.mapper.PictureDataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainPicture {
    private PictureDataBase pictureDataBase = new PictureDataBase();

    public static void main(String[] args) {
//        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/图片数据/";
        String filePath = "D:/爬取网页源数据/汽车之家/图片数据/";
//        String filePath1 = "G:/汽车之家/图片的具体页面/"；
        String filePath1 = "H:/汽车之家/图片的具体页面/";
        MainPicture mainPicture = new MainPicture();
        // mainPicture.downLoad_下载所有版本的第一页全图文件(filePath+"版本图片分页数据/");
        // mainPicture.parse_解析所有版本的第一页图片文件获取分页总页数并将分页url入库(filePath + "版本图片分页数据/");
        // mainPicture.downLoad_所有分页(filePath + "版本图片分页数据/");
        // mainPicture.parse_解析分页数据入库图片具体页面的url并下载未下载的漏网之鱼(filePath + "版本图片分页数据/");
//         mainPicture.downLoad_下载图片的具体页面获取下载高清图的Url地址(filePath + "图片的具体页面/");
         mainPicture.method_修改已下载的图片具体页面的下载状态(filePath + "图片的具体页面/");
        // mainPicture.method_修改已下载的图片具体页面的下载状态(filePath1);
//        mainPicture.parse_解析下载图片的具体页面获取下载图片的url或者高清图的url(filePath + "图片的具体页面/");
        // mainPicture.downLoad_下载图片(filePath + "图片/");
    }

    // 1.下载所有版本的第一页
    public void downLoad_下载所有版本的第一页全图文件(String filePath) {
        try {
            ArrayList<Object> dataList = pictureDataBase.get_未下载的版本的第一页全图文件();
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                VersionFirstPicMoreThread versionFirstPicMoreThread = new VersionFirstPicMoreThread(list.get(i), filePath);
                Thread thread = new Thread(versionFirstPicMoreThread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2.解析所有版本的第一页图片数据并入库
    public void parse_解析所有版本的第一页图片文件获取分页总页数并将分页url入库(String filePath) {
        try {
            ArrayList<Object> dataList = new ArrayList<>();
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(content);
                Elements pageItems = mainDoc.select(".page").select("a");
                int pageCount = 0;
                if (pageItems.size() == 0) {
                    pageCount = 1;
                } else {
                    pageCount = Integer.parseInt(pageItems.get(pageItems.size() - 1).attr("href").split("/")[4].replace("p", ""));
                }
                for (int i = 1; i < pageCount + 1; i++) {
                    String mainUrl = "https://car.autohome.com.cn/photolist/spec/" + fileName.replace("_1.txt", "") + "/p" + i + "/#pvareaid=3454554";
                    Picture_FenYeUrl picture_url = new Picture_FenYeUrl();
                    picture_url.set_C_FenYeUrl(mainUrl);
                    picture_url.set_C_VersionId(fileName.replace("_1.txt", ""));
                    picture_url.set_C_IsFinish(0);
                    picture_url.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    dataList.add(picture_url);
                }
            }
            HashSet<Object> set = new HashSet<>(dataList);
            dataList.clear();
            dataList.addAll(set);
            pictureDataBase.insertConfirmCarPriceFileModel(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3.下载所有分页数据
    public void downLoad_所有分页(String filePath) {
        try {
            // 查找所有未下载的分页url
            ArrayList<Object> dataList = pictureDataBase.get_所有未下载的分页url();
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                FenYeMoreThread moreThread = new FenYeMoreThread(list.get(i), filePath);
                Thread thread = new Thread(moreThread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4.解析分页数据入库图片具体页面的url并下载未下载的漏网之鱼
    public void parse_解析分页数据入库图片具体页面的url并下载未下载的漏网之鱼(String filePath) {
        ArrayList<Object> dataList = pictureDataBase.get_图片分页表中的所有数据();
        ArrayList<Object> result = new ArrayList<>();
        int count = 0;
        for (Object o : dataList) {
            String mainUrl = ((Picture_FenYeUrl) o).get_C_FenYeUrl();
            int page = Integer.parseInt(mainUrl.split("/")[6].replace("p", ""));
            String content = T_Config_File.method_读取文件内容(filePath + ((Picture_FenYeUrl) o).get_C_VersionId() + "_" + page + ".txt");
            Document mainDoc = Jsoup.parse(content);
            Elements mainItems = mainDoc.select("#imgList").select("li");
            count += mainItems.size();
            result.addAll(parse_解析分页数据入库图片具体页面的url(content, filePath + ((Picture_FenYeUrl) o).get_C_VersionId() + "_" + page + ".txt"));
        }
        System.out.println(count);
        System.out.println(result.size());
//        HashSet<Object> set = new HashSet<>(result);
//        result.clear();
//        result.addAll(set);
        System.out.println(result.size());
        pictureDataBase.insert_图片具体页面的文件下载相关数据(result);

//        ArrayList<Object> dataList2 = new ArrayList<>();
//        for (Object o : dataList) {
//            String mainUrl = ((Picture_Url) o).get_C_FenYeUrl();
//            int page = Integer.parseInt(mainUrl.split("/")[6].replace("p", ""));
//            if (!T_Config_File.method_判断文件是否存在(filePath+((Picture_Url) o).get_C_VersionId() + "_"+page+".txt")){
//                System.out.println(mainUrl+"\t"+filePath+((Picture_Url) o).get_C_VersionId() + "_"+page+".txt");
//                dataList2.add(o);
//
//            }
//        }
//        List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList2.subList(i * (dataList2.size() + 5) / 6, Math.min((i + 1) * (dataList2.size() + 5) / 6, dataList2.size())))
//                .collect(Collectors.toList());
//        for (int i = 0; i < list.size(); i++) {
//            FenYeMoreThread moreThread = new FenYeMoreThread(list.get(i), filePath);
//            Thread thread = new Thread(moreThread);
//            thread.start();
//        }
    }

    public ArrayList<Object> parse_解析分页数据入库图片具体页面的url(String content, String fileName) {
        ArrayList<Object> dataList = new ArrayList<>();
        Document mainDoc = Jsoup.parse(content);
        Elements mainItems = mainDoc.select("#imgList").select("li");
        Elements elementsItems = mainDoc.select(".mini_left").select("a");
        String brandId = elementsItems.get(2).attr("href").replace("/pic/brand-", "").replace(".html", "");
        String modId = elementsItems.get(3).attr("href").replace("/pic/series-t/", "").replace(".html", "").replace("/pic/series/", "");
        for (int i = 0; i < mainItems.size(); i++) {
            String imgId = mainItems.get(i).attr("id").replace("li", "");
            String tempUrl = mainItems.get(i).select("a").get(0).attr("href");
            String imgUrl = "https://car.autohome.com.cn" + tempUrl;
            String verId = tempUrl.split("/")[2];
            String imgType = tempUrl.split("/")[3];
            PictureHtmlUrl pictureHtmlUrl = new PictureHtmlUrl();
            pictureHtmlUrl.set_C_BrandId(brandId);
            pictureHtmlUrl.set_C_PictureHtmlUrl(imgUrl);
            pictureHtmlUrl.set_C_FileName(fileName);
            pictureHtmlUrl.set_C_ModelId(modId);
            pictureHtmlUrl.set_C_ImgId(imgId);
            pictureHtmlUrl.set_C_ImgType(imgType);
            pictureHtmlUrl.set_C_VersionId(verId);
            pictureHtmlUrl.set_C_IsFinish(0);
            pictureHtmlUrl.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            dataList.add(pictureHtmlUrl);
        }
        return dataList;
    }

    // 5.下载图片具体页面的数据文件
    public void downLoad_下载图片的具体页面获取下载高清图的Url地址(String filePath) {
        int countData = pictureDataBase.get_下载图片具体页面中还需下载的数量();
        System.out.println(countData);
        for (int k = 0; k < countData / 10000 + 1; k++) {
            ArrayList<Object> dataList = pictureDataBase.get_分页查询所有未下载的图片具体页面的url(k * 10000);
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int ii = 0; ii < list.size(); ii++) {
                PictureHtmlUrlThread moreThread = new PictureHtmlUrlThread(list.get(ii), filePath);
                Thread thread = new Thread(moreThread);
                thread.start();
            }
        }

    }

    // 6.修改已下载的图片具体页面的下载状态
    public void method_修改已下载的图片具体页面的下载状态(String filePath) {
        try {
//            ArrayList<String> dataList = T_Config_File.method_获取文件名称(filePath);
            List<String> dataList = T_Config_File.method_流式获取文件名称(filePath);
            System.out.println(dataList.size());
            ArrayList<Object> result = new ArrayList<>();
            for (String fileNeme : dataList) {
                fileNeme = fileNeme.replace(".txt", "");
                String url = "https://car.autohome.com.cn/photo/" + fileNeme.split("_")[2] + "/" + fileNeme.split("_")[4] + "/" + fileNeme.split("_")[3] + ".html";
                PictureHtmlFileData pictureHtmlFileData = new PictureHtmlFileData();
                pictureHtmlFileData.set_C_PictureHtmlUrl(url);
                pictureHtmlFileData.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                result.add(pictureHtmlFileData);
            }
            pictureDataBase.insert_图片具体页面已下载的文件数据(result);
//            if( pictureDataBase.get_下载图片具体页面中还需下载的数量()>0){
//                downLoad_下载图片的具体页面获取下载高清图的Url地址(filePath);
//                method_修改已下载的图片具体页面的下载状态(filePath);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 7.解析下载图片的具体页面获取下载图片的url或者高清图的url
    public void parse_解析下载图片的具体页面获取下载图片的url或者高清图的url(String filePath) {
        ArrayList<Object> result = new ArrayList<>();
        try {
//            int countData =pictureDataBase.get_已经下载的图片具体页面的数据数量();
//            for (int k = 0; k < countData / 10000 + 1; k++) {
//                ArrayList<Object> dataList = pictureDataBase.get_已经下载的图片具体页面的数据(k * 10000);
////            ArrayList<Object> dataList = pictureDataBase.get_已经下载的图片具体页面的数据();
//            System.out.println(dataList.size());
//            for (Object o : dataList) {
//                String versionId = ((PictureHtmlUrl) o).get_C_VersionId();
//                String modId = ((PictureHtmlUrl) o).get_C_ModelId();
//                String brandId = ((PictureHtmlUrl) o).get_C_BrandId();
//                String imgId = ((PictureHtmlUrl) o).get_C_ImgId();
//                String imgType = ((PictureHtmlUrl) o).get_C_ImgType();
//                String fileName = brandId + "_" + modId + "_" + versionId + "_" + imgId + "_" + imgType + ".txt";
//                String content = T_Config_File.method_读取文件内容(filePath + fileName);
//                Document mainDoc = Jsoup.parse(content);
//                Elements mainItems = mainDoc.select("#img");
//                String imgUrl = "https:" + mainItems.attr("src");
//                String bigImg = mainDoc.select("#btnBigphoto").attr("href").equals("") ? "无高清图" : "https:" + mainDoc.select("#btnBigphoto").attr("href");
//                PictureUrl pictureUrl = new PictureUrl();
//                pictureUrl.set_C_GaoQingImgUrl(bigImg);
//                pictureUrl.set_C_ImgUrl(imgUrl);
//                pictureUrl.set_C_BrandId(brandId);
//                pictureUrl.set_C_ModelId(modId);
//                pictureUrl.set_C_VersionId(versionId);
//                pictureUrl.set_C_ImgId(imgId);
//                pictureUrl.set_C_FactoryId("");
//                pictureUrl.set_C_ImgType(imgType);
//                pictureUrl.set_C_IsFinish(0);
//                pictureUrl.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                result.add(pictureUrl);
//                if (result.size() > 10000) {
//                    HashSet<Object> set = new HashSet<>(result);
//                    result.clear();
//                    result.addAll(set);
//                    pictureDataBase.insert_下载图片的url数据入库(result);
//                    result.clear();
//                }
//            }


            ArrayList<String> fileList =T_Config_File.method_获取文件名称(filePath);
            for(String fileName:fileList){
                String content = T_Config_File.method_读取文件内容(filePath+fileName);
                Document mainDoc= Jsoup.parse(content);
                String versionId = fileName.split("_")[2];
                String modId =fileName.split("_")[1];
                String brandId =fileName.split("_")[0];
                String imgId = fileName.split("_")[3];
                String imgType=fileName.split("_")[4].replace(".txt","");
                Elements mainItems = mainDoc.select("#img");
                String imgUrl ="https:"+ mainItems.attr("src");
                String bigImg = mainDoc.select("#btnBigphoto").attr("href").equals("")?"无高清图":"https:"+mainDoc.select("#btnBigphoto").attr("href");
                PictureUrl pictureUrl = new PictureUrl();
                pictureUrl.set_C_GaoQingImgUrl(bigImg);
                pictureUrl.set_C_ImgUrl(imgUrl);
                pictureUrl.set_C_BrandId(brandId);
                pictureUrl.set_C_ModelId(modId);
                pictureUrl.set_C_VersionId(versionId);
                pictureUrl.set_C_ImgId(imgId);
                pictureUrl.set_C_FactoryId("");
                pictureUrl.set_C_ImgType(imgType);
                pictureUrl.set_C_IsFinish(0);
                pictureUrl.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                result.add(pictureUrl);
            }
            HashSet<Object> set = new HashSet<>(result);
            result.clear();
            result.addAll(set);
            pictureDataBase.insert_下载图片的url数据入库(result);
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    // 8.下载图片
    public void downLoad_下载图片(String filePath) {
        try {
            ArrayList<Object> dataList = pictureDataBase.get_未下载的图片的url();
//            for (Object bean : dataList) {
//                try {
//                    String mainUrl = ((PictureUrl) bean).get_C_GaoQingImgUrl().equals("无高清图") ? ((PictureUrl) bean).get_C_ImgUrl() : ((PictureUrl) bean).get_C_GaoQingImgUrl();
//                    String tempFilePath = ((PictureUrl) bean).get_C_BrandId() + "/" + ((PictureUrl) bean).get_C_ModelId() + "/" + ((PictureUrl) bean).get_C_VersionId() + "/" + ((PictureUrl) bean).get_C_ImgType() + "/";
//                    String fileName = ((PictureUrl) bean).get_C_ImgId();
//                    if (T_Config_File.downloadImage(mainUrl, filePath + tempFilePath, fileName+".jpg")) {
//                        T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("图片/", ""), "已下载的图片Url.txt", mainUrl + "\n");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int ii = 0; ii < list.size(); ii++) {
                PictureUrlThread moreThread = new PictureUrlThread(list.get(ii), filePath);
                Thread thread = new Thread(moreThread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}