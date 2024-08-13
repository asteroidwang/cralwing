package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.picture.PictureVersion;
import com.wangtiantian.entity.picture.Picture_Url;
import com.wangtiantian.mapper.PictureDataBase;
import com.wangtiantian.runPrice.PriceMoreThread;
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
        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/图片数据/";
        MainPicture mainPicture = new MainPicture();
        // mainPicture.downLoad_下载所有版本的第一页全图文件(filePath+"版本图片分页数据/");
        // mainPicture.parse_解析所有版本的第一页图片文件获取分页总页数并将分页url入库(filePath + "版本图片分页数据/");
        mainPicture.downLoad_所有分页();
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
                if (pageCount > 1) {
                    for (int i = 1; i < pageCount + 1; i++) {
                        String mainUrl = "https://car.autohome.com.cn/photolist/spec/" + fileName.replace("_1.txt", "") + "/p" + i + "/#pvareaid=3454554";
                        Picture_Url picture_url = new Picture_Url();
                        picture_url.set_C_FenYeUrl(mainUrl);
                        picture_url.set_C_VersionId(fileName.replace("_1.txt", ""));
                        picture_url.set_C_IsFinish(0);
                        picture_url.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        dataList.add(picture_url);
                    }
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
    public void downLoad_所有分页(){
        System.out.println(111);
    }}
