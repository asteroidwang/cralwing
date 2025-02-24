package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Factory;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.picture.ModelCategroy;
import com.wangtiantian.entity.picture.PictureHtml;
import com.wangtiantian.entity.picture.PictureInfo;
import com.wangtiantian.mapper.DataBaseConnectPic;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PictureMethod {
    // 1.读取图片路径下载的车型首页并解析获取分类信息
    public void parseModelHomePage(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataList = new ArrayList<>();
            for (String fileName : fileList) {
                String contentFile = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(contentFile);
                Elements mainItems = mainDoc.select("#data-img-list .uibox .uibox-title");
                for (int i = 0; i < mainItems.size(); i++) {
                    if (!mainItems.get(i).text().contains("全景看车")) {
                        int num = Integer.parseInt(mainItems.get(i).select(".uibox-title-font12").text().replace("(", "").replace(")", "").replace("张", "").trim());
                        String picHref = mainItems.get(i).select(".more").attr("href");
                        if (picHref.startsWith("/pic")) {
                            String picHtml = "https://car.autohome.com.cn" + picHref;
                            String code = picHref.replace("/pic/series/", "").replace("/pic/series-t/", "").substring(0, picHref.replace("/pic/series/", "").replace("/pic/series-t/", "").indexOf(".html"));
                            if (num > 60) {
                                for (int j = 0; j < num / 60 + 1; j++) {
                                    int page = j + 1;
                                    String picHtmlOtherPage = picHtml.substring(0, picHtml.indexOf(".html")) + "-p" + page + ".html";
                                    ModelCategroy modelCategroy = new ModelCategroy();
                                    modelCategroy.set_C_Number(page != num / 60 + 1 ? 60 : num - j * 60);
                                    modelCategroy.set_C_ModelCategoryCode(code);
                                    modelCategroy.set_C_PictureCategoryMoreHtml(page == 1 ? picHtml : picHtmlOtherPage);
                                    modelCategroy.set_C_IsFinish(0);
                                    modelCategroy.set_C_Page(page);
                                    modelCategroy.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    dataList.add(modelCategroy);
                                }
                            } else {
                                ModelCategroy modelCategroy = new ModelCategroy();
                                modelCategroy.set_C_Number(num);
                                modelCategroy.set_C_ModelCategoryCode(code);
                                modelCategroy.set_C_PictureCategoryMoreHtml(picHtml);
                                modelCategroy.set_C_IsFinish(0);
                                modelCategroy.set_C_Page(1);
                                modelCategroy.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                dataList.add(modelCategroy);
                            }
                        }
                    }
                }
            }
            ArrayList<Object> resultList = new ArrayList<>();
            Set<String> picCategorySet = new HashSet<>();
            for (Object o : dataList) {
                String picCategory = ((ModelCategroy) o).get_C_PictureCategoryMoreHtml();
                if (!picCategorySet.contains(picCategory)) {
                    picCategorySet.add(picCategory);
                    resultList.add(o);
                }
            }
            new DataBaseConnectPic().insertModelCategoryHtml(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2.下载<更多>页面数据
    public void downloadModelCategory(String filePath) {
        try {
            DataBaseConnectPic dataBaseConnectPic = new DataBaseConnectPic();
            ArrayList<Object> dataList = dataBaseConnectPic.findModelCategoryHtml();
            if (dataList.size() < 32) {
                for (Object bean : dataList) {
                    String html = ((ModelCategroy) bean).get_C_PictureCategoryMoreHtml();
                    String fileNameTmp = html.replace("https://car.autohome.com.cn/pic/", "").replace("https://car.autohome.com.cn/pic/", "");
                    String fileName = fileNameTmp.substring(0, fileNameTmp.indexOf(".html")).replace("/", "_") + ".txt";
                    if (!T_Config_File.method_判断文件是否存在(filePath + fileName)) {
                        if (T_Config_File.method_访问url获取网页源码普通版(html, "GBK", filePath, fileName)) {
                            dataBaseConnectPic.updateModelCategoryHtmlStatus(html);
                        }
                    } else {
                        dataBaseConnectPic.updateModelCategoryHtmlStatus(html);
                    }
                }
            } else {
                List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                        .collect(Collectors.toList());
                CountDownLatch latch = new CountDownLatch(list.size());
                for (int i = 0; i < list.size(); i++) {
                    ThreadModelCategory moreThread = new ThreadModelCategory(list.get(i), filePath, latch);
                    Thread thread = new Thread(() -> {
                        try {
                            moreThread.run();
                        } finally {
                            latch.countDown();
                        }
                    });
                    thread.start();
                }
                latch.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3.解析图片列表页面
    public void parsePicListPage(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataList = new ArrayList<>();
            for (String fileName : fileList) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                Document mainDoc = Jsoup.parse(content);
                Elements mainItems = mainDoc.select(".uibox-con.carpic-list03").select("li");
                if (mainItems.size() > 0) {
                    for (int i = 0; i < mainItems.size(); i++) {
                        String picHtml = mainItems.get(i).select("a").attr("href");
                        if (!picHtml.startsWith("https")) {
                            picHtml = "https://car.autohome.com.cn" + picHtml;
                        }
                        String temp = picHtml.substring(0, picHtml.indexOf(".html")).replace("https://car.autohome.com.cn", "").replace("/Clubphoto/series/", "").replace("https://www.autohome.com.cn/cars/", "").replace("/", "__");
                        String versionId = temp.startsWith("imgs") ? temp.split("-")[2] : temp.split("__")[0];
                        PictureHtml beanPic = new PictureHtml();
                        beanPic.set_C_PictureHtml(picHtml);
                        beanPic.set_C_IsFinish(0);
                        beanPic.set_C_VersionID(versionId);
                        beanPic.set_C_PictureHtmlCode(temp);
                        dataList.add(beanPic);
                    }
                }
            }
            ArrayList<Object> resultList = new ArrayList<>();
            Set<String> picCategorySet = new HashSet<>();
            for (Object o : dataList) {
                String picCategory = ((PictureHtml) o).get_C_PictureHtmlCode();
                if (!picCategorySet.contains(picCategory)) {
                    picCategorySet.add(picCategory);
                    resultList.add(o);
                }
            }
            new DataBaseConnectPic().insertPictureHtml(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4.下载图片页面
    public void downloadPictureHtml(String filePath) {
        try {
            DataBaseConnectPic dataBaseConnectPic = new DataBaseConnectPic();
            int totalCount = dataBaseConnectPic.pictureHtmlCount();
            int a = 0;
            for (int batch = 0; batch < totalCount / 10000; batch++) {
                if (a>50){
                    break;
                }else {
                    int begin = batch * 10000;
                    ArrayList<Object> dataList = dataBaseConnectPic.findPictureHtmlForeach(begin, 10000);
                    List<String> htmlList = new ArrayList<>();
                    if (dataList.size() < 32) {
                        for (Object o : dataList) {
                            String html = ((PictureHtml) o).get_C_PictureHtml();
                            String fileName = ((PictureHtml) o).get_C_PictureHtmlCode() + ".txt";
                            if (!T_Config_File.method_判断文件是否存在(filePath + fileName)) {
                                if (fileName.startsWith("imgs")){
                                    if (T_Config_File.method_访问url获取网页源码普通版(html, "utf-8", filePath, fileName)) {
                                        htmlList.add(html);
                                    }
                                }else {
                                    if (T_Config_File.method_访问url获取网页源码普通版(html, "GBK", filePath, fileName)) {
                                        htmlList.add(html);
                                    }
                                }
                            } else {
                                htmlList.add(html);
                            }
                        }
                        StringBuffer htmls = new StringBuffer();
                        for (int i = 0; i < htmlList.size(); i++) {
                            htmls.append("'").append(htmlList.get(i)).append("',");
                        }
                        dataBaseConnectPic.updatePictureHtmlStatus(htmls.toString().substring(0,htmls.length()-1));
                    } else {
                        List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                                .collect(Collectors.toList());
                        CountDownLatch latch = new CountDownLatch(list.size());
                        for (int i = 0; i < list.size(); i++) {
                            ThreadPictureHtml moreThread = new ThreadPictureHtml(list.get(i), filePath, latch);
                            Thread thread = new Thread(() -> {
                                try {
                                    moreThread.run();
                                } finally {
                                    latch.countDown();
                                }
                            });
                            thread.start();
                        }
                        latch.await();
                    }
                    a++;
                }

            }
            checkPictureHtml(filePath);
            parsePictureHtml(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 检查下载的图片页面是否可用
    public void checkPictureHtml(String filePath) {
        DataBaseConnectPic dataBaseConnectPic = new DataBaseConnectPic();
        ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
        System.out.println(fileList.size());
        ArrayList<String> htmls = new ArrayList<>();
        for (String fileName : fileList) {
            String content = T_Config_File.method_读取文件内容(filePath + fileName);
            Document mainDoc = Jsoup.parse(content);
            String imgUrl = "";
            Elements navItems = null;
            String brandId = "";
            String modelId = "";
            String versionId = "";
            String imgId = "";
            String typeCode = "";
            String highUrl = "";
            int isHigh = 0;
            if (fileName.startsWith("img")) {
                Elements mainItems = mainDoc.select("#big-img-show");
                Elements highItems  = mainDoc.select("#gallery-container").select("a");
                if (highItems.size()>0){
                    highUrl = highItems.attr("href");
                }
                navItems = mainDoc.select("nav").select("li").select(".ant-breadcrumb-link");
                imgUrl = mainItems.attr("src");
//                System.out.println(navItems.size()==0? fileName:"");
            } else {
                Elements mainItems = mainDoc.select("#img");
                imgUrl = "https:" + mainItems.attr("src");
                navItems = mainDoc.select(".breadnav").select("a");
//                System.out.println(navItems.size()==0?fileName:"");
                brandId = navItems.get(2).attr("href").substring(navItems.get(2).attr("href").indexOf("brand-") + 6, navItems.get(2).attr("href").indexOf(".html"));
                modelId = navItems.get(3).attr("href").substring(navItems.get(3).attr("href").indexOf("series/") + 7, navItems.get(3).attr("href").indexOf(".html"));
                String versionIdTemp = navItems.get(4).attr("href").substring(navItems.get(4).attr("href").indexOf("series-s") + 8);
                versionId = versionIdTemp.substring(0, versionIdTemp.indexOf("/"));
                imgId = fileName.split("__")[2].replace(".txt", "");
                typeCode = fileName.split("__")[1];
            }
            if (navItems.size()==0){
                htmls.add(fileName);
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < htmls.size(); i++) {
            stringBuffer.append("'").append(htmls.get(i).replace(".txt","").replace("__","/")).append("',");
            File file = new File(filePath+htmls.get(i));
            if (file.delete()){
                System.out.println("已删除");
            }
        }
        if (htmls.size()>0){
            dataBaseConnectPic.updatePictureHtmlStatusForNot(stringBuffer.toString().substring(0,stringBuffer.toString().length()-1));
        }

    }

    // 5.解析图片页面
    public void parsePictureHtml(String filePath) {
        DataBaseConnectPic dataBaseConnectPic = new DataBaseConnectPic();
        ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
        ArrayList<Object> modelInfoList = dataBaseConnectPic.findModelInfoList();
        ArrayList<Object> dataList = new ArrayList<>();
        for (String fileName : fileList) {
            String content = T_Config_File.method_读取文件内容(filePath + fileName);
            Document mainDoc = Jsoup.parse(content);
            String imgUrl = "";
            Elements navItems = null;
            String brandId = "";
            String modelId = "";
            String versionId = "";
            String imgId = "";
            String typeCode = "";
            String highUrl = "";
            if (fileName.startsWith("img")) {
                Elements mainItems = mainDoc.select("#big-img-show");
                Elements highItems  = mainDoc.select("#gallery-container").select("a");
                if (highItems.size()>0){
                    highUrl = highItems.attr("href");
                }
                navItems = mainDoc.select("nav").select("li").select(".ant-breadcrumb-link");
                imgUrl = mainItems.attr("src");
                brandId = navItems.get(1).select("a").attr("href").substring(navItems.get(1).select("a").attr("href").indexOf("brandid_"), navItems.get(1).select("a").attr("href").indexOf("?")).replace("brandid_", "");
                String modelIdTemp = navItems.get(2).select("a").attr("href").replace("https://www.autohome.com.cn/", "");
                modelId = modelIdTemp.substring(0, modelIdTemp.indexOf("/"));
                versionId = navItems.get(3).select("a").attr("href").substring(navItems.get(3).select("a").attr("href").indexOf("spec/") + 5, navItems.get(3).select("a").attr("href").indexOf("/#"));
                imgId = fileName.split("__")[1].replace(".txt", "");
                typeCode = fileName.split("__")[0].split("-")[3];
            } else {
                Elements mainItems = mainDoc.select("#img");
                imgUrl = "https:" + mainItems.attr("src");
                navItems = mainDoc.select(".breadnav").select("a");
                brandId = navItems.get(2).attr("href").substring(navItems.get(2).attr("href").indexOf("brand-") + 6, navItems.get(2).attr("href").indexOf(".html"));
                modelId = navItems.get(3).attr("href").substring(navItems.get(3).attr("href").indexOf("series/") + 7, navItems.get(3).attr("href").indexOf(".html"));
                String versionIdTemp = navItems.get(4).attr("href").substring(navItems.get(4).attr("href").indexOf("series-s") + 8);
                versionId = versionIdTemp.substring(0, versionIdTemp.indexOf("/"));
                imgId = fileName.split("__")[2].replace(".txt", "");
                typeCode = fileName.split("__")[1];
            }
            String htmlUrl = fileName.startsWith("imgs") ? "https://www.autohome.com.cn/cars/" + fileName.replace(".txt", "").replace("__", "/") + ".html" : "https://car.autohome.com.cn/Clubphoto/series/" + fileName.replace(".txt", "").replace("__", "/") + ".html";
            String modelCode = modelId;
            String brandCode = brandId;
            List<String> factory = modelInfoList.stream().filter(e -> ((Bean_Model) e).get_C_ModelID().equals(modelCode) && ((Bean_Model) e).get_C_BrandID().equals(brandCode)).map(e -> ((Bean_Model) e).get_C_FactoryID()).collect(Collectors.toList());
            String factoryId = factory.get(0);
            PictureInfo pictureInfo = new PictureInfo();
            pictureInfo.set_C_BrandID(brandId);
            pictureInfo.set_C_ModelID(modelId);
            pictureInfo.set_C_FactoryID(factoryId);
            pictureInfo.set_C_VersionID(versionId);
            pictureInfo.set_C_CategoryCode(typeCode);
            pictureInfo.set_C_PictureCode(imgId);
            pictureInfo.set_C_PictureUrl(imgUrl);
            pictureInfo.set_C_PictureHighUrl(highUrl);
            pictureInfo.set_C_PictureHtmlCode(fileName.replace(".txt", ""));
            pictureInfo.set_C_PictureHtmlUrl(htmlUrl);
            pictureInfo.set_C_IsFinish(0);
            pictureInfo.set_C_IsHigh(highUrl.equals("")?0:1);
            dataList.add(pictureInfo);
        }
        ArrayList<Object> resultList = new ArrayList<>();
        Set<String> picCategorySet = new HashSet<>();
        for (Object o : dataList) {
            String picUrl = ((PictureInfo) o).get_C_PictureUrl();
            if (!picCategorySet.contains(picUrl)) {
                picCategorySet.add(picUrl);
                resultList.add(o);
            }
        }
        dataBaseConnectPic.insertPictureInfoList(resultList);
    }
    public void deleteFile(String filePath){
        ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
        System.out.println(fileList.size());
        for (int i = 0; i < fileList.size(); i++) {
            if (i>500000){
                File deFile = new File(filePath+fileList.get(i));
                if (deFile.delete()){
                    System.out.println("1");
                }
            }

        }
    }


}
