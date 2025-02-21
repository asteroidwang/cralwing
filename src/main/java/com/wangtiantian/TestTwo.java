package com.wangtiantian;

import com.wangtiantian.runConfigData.MainConfigData;
import com.wangtiantian.runErShouChe.yiChe.MainYiChe;
import com.wangtiantian.runPicture.PictureMethod;

import java.util.*;

public class TestTwo {


    public static void main(String[] args) {
        String filePath= "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/配置数据/20250210/含版本数据的文件_图片路径/";
        String filePathPic= "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/图片数据/20250210/";
        PictureMethod pictureMethod = new PictureMethod();
        // pictureMethod.parseModelHomePage(filePath);
        // pictureMethod.downloadModelCategory(filePathPic);
        // pictureMethod.parsePicListPage(filePathPic+"车型类型页面/");
        // pictureMethod.downloadPictureHtml(filePathPic+"图片html页面/");
        pictureMethod.checkPictureHtml(filePathPic+"图片html页面/");
        // pictureMethod.parsePictureHtml(filePathPic+"图片html页面/");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 6); // 控制时
//        calendar.set(Calendar.MINUTE,0);       // 控制分
//        calendar.set(Calendar.SECOND, 0);       // 控制秒
//
//        Date time = calendar.getTime();
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new MainConfigData(), time, 1000 * 60 * 60 * 24);// 这里
    }
}

