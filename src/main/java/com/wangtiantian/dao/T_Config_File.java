package com.wangtiantian.dao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class T_Config_File {
    //读取文件内容
    public static String method_读取文件内容(String filePath) {
        String text = "";
        try {
            File file = new File(filePath);
//            if (!file.exists()) {
//                T_Config_File.method_重复写文件_根据路径创建文件夹(" D:/汽车之家/口碑评价数据/20240804/", "不存在.txt", filePath + "\n");
//            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            br.close();
            text = sb.toString();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return text;
    }

    //写文件
    public static void method_写文件_根据路径创建文件夹(String filePath, String fileName, String content) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(filePath + fileName);
            Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
            writer.close();
            fos.close();
            System.out.println("下载一次\t" + filePath + fileName+"\t"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    //重复写文件
    public static void method_重复写文件_根据路径创建文件夹(String filePath, String fileName, String content) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath + fileName, true), 331074);//165537
            bufferedOutputStream.write(content.getBytes(StandardCharsets.UTF_8));   //StandardCharsets.UTF_8
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取文件夹下文件名称
    public static ArrayList<String> method_获取文件名称(String filePath) {
        ArrayList<String> fileNames = new ArrayList<>();
        File file = new File(filePath);
        File[] files = file.listFiles(File::isFile);
        for (int i = 0; i < files.length; i++) {
            fileNames.add(files[i].getName());
        }
        return fileNames;
    }

    public static List<String> method_流式获取文件名称(String filePath) {
        List<String> fileNames = new ArrayList<>();
        try {
            Stream<Path> paths = Files.walk(Paths.get(filePath));
          fileNames =  paths.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
        }

        return fileNames;
    }

    public static ArrayList<String> method_获取文件夹名称(String filePath) {
        ArrayList<String> flodersNames = new ArrayList<>();
        File file = new File(filePath);
        File[] folders = file.listFiles(File::isDirectory);
        for (int i = 0; i < folders.length; i++) {
            flodersNames.add(folders[i].getName());
        }
        return flodersNames;
    }

    public static ArrayList<String> method_按行读取文件(String filePath) {
        ArrayList<String> data = new ArrayList<>();
        try {
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            String everyLine = "";
            while ((line = br.readLine()) != null) {
                everyLine = line;
                data.add(everyLine);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Boolean method_判断文件是否存在(String file) {
        return new File(file).exists();
    }

    public static Boolean method_访问url获取网页源码普通版(String url, String encode, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.parse(new URL(url).openStream(), encode, url);
        } catch (Exception e) {
            return false;
        }
        if (mainDoc != null) {
            T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName, mainDoc.toString());
            return true;
        } else {
            return false;
        }

    }

    public static Boolean method_访问url获取Json普通版(String url, String encode, String filePath, String fileName) {
        Document mainDoc = null;
        try {
            mainDoc = Jsoup.parse(new URL(url).openStream(), encode, url);
        } catch (Exception e) {
            return false;
        }
        if (mainDoc != null) {
            T_Config_File.method_写文件_根据路径创建文件夹(filePath, fileName, mainDoc.text());
            return true;
        } else {
            return false;
        }
    }

    public static boolean downloadImage(String imageUrl, String filePath, String fileName) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(filePath + fileName);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            System.out.println("成功下载一次\t"+filePath+ fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void delete_删除文件(String filePath){
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()){
                System.out.println("已删除");
            }else {
                System.out.println("删除失败");
            }
        }
    }
    public static List<String> method_流式获取文件名称(String filePath) {
        List<String> fileNames = new ArrayList<>();
        try {
            Stream<Path> paths = Files.walk(Paths.get(filePath));
            fileNames = paths.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileNames;
    }
}
