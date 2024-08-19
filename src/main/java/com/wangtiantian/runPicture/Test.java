package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Test {
    public static void main(String[] args) {
        String content = T_Config_File.method_读取文件内容("/Users/asteroid/所有文件数据/图片具体页面.txt");
        String content2 = T_Config_File.method_读取文件内容("/Users/asteroid/所有文件数据/图片具体页面2.txt");
        Document mainDoc = Jsoup.parse(content);
        Elements mainItems =mainDoc.select("#img");
        Elements gaoQinItems =mainDoc.select("#btnBigphoto");
        Document mainDoc2 = Jsoup.parse(content2);
        Elements mainItems2 =mainDoc2.select("#img");
        Elements gaoQinItems2 =mainDoc2.select("#btnBigphoto");
        String versionId="";
        if (gaoQinItems2.size()==0){

        }
    }
}
