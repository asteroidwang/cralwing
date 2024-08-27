package com.wangtiantian;

import com.wangtiantian.dao.T_Config_File;

import java.util.ArrayList;

public class testFileName {
    public static void main(String[] args) {
        ArrayList<String> fileList = T_Config_File.method_获取文件名称("/Users/wangtiantian/MyDisk/test/");
        for(String fileName :fileList){
            System.out.println(fileName.replace(".txt",""));
        }
    }
}
