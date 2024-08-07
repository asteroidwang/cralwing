package com.wangtiantian.dao;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class T_Config_File {
    //读取文件内容
    public static String method_读取文件内容(String filePath) {
        String text = "";
        try {
            File file = new File(filePath);
            if (!file.exists()){
                T_Config_File.method_重复写文件_根据路径创建文件夹(" E:/汽车之家/口碑评价数据/20240804/","不存在.txt",filePath+"\n");
            }
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
    public static void method_写文件_根据路径创建文件夹(String filePath,String fileName,String content) {
        try {
            File file = new File(filePath);
            if (!file.exists()){
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(filePath+fileName);
            Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush();
            writer.close();
            fos.close();
            System.out.println("下载一次\t"+filePath+fileName);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    //重复写文件
    public static void method_重复写文件_根据路径创建文件夹(String filePath,String fileName,String content) {
        try {
            File file  = new File(filePath);
            if (!file.exists()){
                file.mkdirs();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath+fileName, true), 331074);//165537
            bufferedOutputStream.write(content.getBytes(StandardCharsets.UTF_8));   //StandardCharsets.UTF_8
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取文件夹下文件名称
    public static ArrayList<String> method_获取文件名称(String filePath){
        ArrayList<String> fileNames = new ArrayList<>();
        File file = new File(filePath);
        File[] files = file.listFiles(File::isFile);
        for (int i = 0; i < files.length; i++) {
            fileNames.add(files[i].getName());
        }
        return fileNames;
    }
    public static ArrayList<String> method_获取文件夹名称(String filePath){
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
}
