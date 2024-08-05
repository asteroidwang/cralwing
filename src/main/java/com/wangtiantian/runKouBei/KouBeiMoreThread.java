package com.wangtiantian.runKouBei;

import com.wangtiantian.CommonMoreThread;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_KouBei;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KouBeiMoreThread {
    private String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/口碑评价数据/20240804/";
    public void method_获取上一步入库的未下载的url下载口碑页面数据(){
        try {
            T_Config_KouBei kouBeiDataBase = new T_Config_KouBei(2, 1, 0);
            ArrayList<Object> dataList = kouBeiDataBase.getUrlPageCount不为0();
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                CommonMoreThread commonMoreThread = new CommonMoreThread(list.get(i), filePath);
                Thread thread = new Thread(commonMoreThread);
                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void method_获取上一步入库的未下载的url下载具体口碑页面数据(){
        try {
            T_Config_KouBei kouBeiDataBase = new T_Config_KouBei(2, 1, 1);
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath+"口碑具体页面数据/");
            StringBuffer url =new StringBuffer();
            for(String showId : fileList){
                 url.append("'https://k.autohome.com.cn/detail/view_"+showId.replace(".txt","")+".html#pvareaid=2112108',");
            }
            kouBeiDataBase.method修改具体口碑页面的下载状态(url.toString().substring(0,url.length()-1));
            ArrayList<Object> dataList = kouBeiDataBase.getUrl未下载();
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dataList.subList(i * (dataList.size() + 5) / 6, Math.min((i + 1) * (dataList.size() + 5) / 6, dataList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
//                CommonMoreThread commonMoreThread = new CommonMoreThread(list.get(i), filePath+"口碑具体页面数据/");
//                Thread thread = new Thread(commonMoreThread);
//                thread.start();
                KouBeiMoreThreadRun kouBeiMoreThreadRun = new KouBeiMoreThreadRun(list.get(i),filePath+"口碑具体页面数据/");
                Thread thread = new Thread(kouBeiMoreThreadRun);
                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
