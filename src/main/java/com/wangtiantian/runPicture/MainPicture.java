package com.wangtiantian.runPicture;

public class MainPicture {
    public static void main(String[] args) {
        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/配置数据/20250210/含版本数据的文件_图片路径/";
        // String filePathPic = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/图片数据/20250210/";
        String filePathPic = "/Users/wangtiantian/MyDisk/汽车之家/图片数据/20250210/";
        PictureMethod pictureMethod = new PictureMethod();
        // pictureMethod.parseModelHomePage(filePath);
        // pictureMethod.downloadModelCategory(filePathPic+"车型类型页面/");
        // pictureMethod.parsePicListPage(filePathPic + "车型类型页面/");
        pictureMethod.downloadPictureHtml(filePathPic + "图片html页面_第四部分/");
        // pictureMethod.downloadPicture(filePathPic + "图片/");
    }
}
