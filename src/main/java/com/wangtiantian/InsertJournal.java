package com.wangtiantian;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Magazine;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class InsertJournal {
    private String filePath = "/Users/wangtiantian/MyDisk/所有文件数据/期刊杂志/Advances_20240206/acc/文章内容/";
    private String pptName = "Advances in Nano Research期刊信息标注.pptx";

    public static void main(String[] args) {
        InsertJournal insertJournal = new InsertJournal();
        insertJournal.getDataList(insertJournal.filePath);
    }

    // 1.获取入库数据
    public ArrayList<Object> getDataList(String filePath) {
        ArrayList<Object> result = new ArrayList<>();
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(content);
                Elements mainItem = mainDoc.select("table").select("tr");
//                for (int i = 0; i < mainItem.size(); i++) {
//                    Elements elements = mainItem.get(i).select("td");
                // 年 卷 期 doi
                String year_col_qi = mainItem.get(0).select("td").text().replace("Buy article PDF Instant access to the full article PDF for the next 48 hrs US$ 35 Buy article PDF", "");
                String volumeString = year_col_qi.substring(year_col_qi.indexOf("Volume"));
                String numberString = volumeString.substring(volumeString.indexOf("Number"));
                String volume = volumeString.replace(numberString, "").replace("Volume", "").replace(",", "").trim();
                String yearString = numberString.substring(numberString.indexOf(","));
                String number = numberString.replace(yearString, "").replace("Number", "").trim();
                String pageString = yearString.substring(yearString.indexOf("pages"));
                String yearTemp = yearString.replace(pageString, "").replace(",", "").trim();
                String year = yearTemp.replace(yearTemp.substring(0, yearTemp.length() - 5), "").trim();
                String doiString = pageString.substring(pageString.indexOf("DOI"));
                String doi = doiString.replace("DOI", "").replace(":", "").trim();
                // 题目
                String title = mainItem.get(1).select("td").text().replace("Full Text PDF", "").replace("open access download PDF", "").trim();
                // 作者
                String authors = mainItem.get(2).select("td").text().replace(" and ", ",");
                // 摘要
                String abstractString = mainItem.get(4).select("td").text();
                // 关键词
                String keywords = mainItem.get(6).select("td").text().replace(";", ",");
                // 作者-作者单位
                String authorsDw = mainItem.get(8).select("td").toString().replace("&amp;", "&").replace("<td></td>", "").replace(":<br>", ":").replace(",<br>",",").replace(";"," ").replace("<td>", "").replace("</td>", "").replace("<br>", "#####");
                String[] authorsDwList = authorsDw.split("#####");
                System.out.println(year+"\t"+volume+"\t"+number+"\t"+title);
                // 非正常情况
                // 1.地址没有作者
                // 2。地址youzuozhe
                for (int i = 0; i < authorsDwList.length; i++) {
                    String[] authors_work_space = authorsDwList[i].split(":");
                    String author_name ="";
                    String work_space ="";
                    if (authors_work_space.length < 2) {
                        if (authors.split(",").length>1){
                            author_name="";
                        }else {
                            author_name = authors;
                        }
                        work_space = authors_work_space[0];
                        System.out.println("数据1：\t作者\t"+author_name.replace("\n","")+"\t单位\t"+work_space.replace("\n",""));
                    }else {
                        author_name = authors_work_space[0];
                        work_space= authors_work_space[1];
                        System.out.println("数据2：\t作者\t"+author_name.replace("\n","")+"\t单位\t"+work_space.replace("\n",""));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
