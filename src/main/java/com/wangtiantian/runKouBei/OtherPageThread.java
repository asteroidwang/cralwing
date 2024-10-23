package com.wangtiantian.runKouBei;

import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.koubei.Bean_KouBei_FenYeUrl;
import com.wangtiantian.mapper.KouBei_DataBase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class OtherPageThread implements Runnable {
    private List<Object> list;
    private String filePath;
    private CountDownLatch latch;

    public OtherPageThread(List<Object> list, String filePath, CountDownLatch latch) {
        this.list = list;
        this.filePath = filePath;
        this.latch = latch;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            String modelId = ((Bean_KouBei_FenYeUrl) bean).get_C_ModelID();
            String modelKouBeiUrl = ((Bean_KouBei_FenYeUrl) bean).get_C_FenYeUrl();
            int page = ((Bean_KouBei_FenYeUrl) bean).get_C_Page();
            if (T_Config_File.method_访问url获取Json普通版(modelKouBeiUrl, "UTF-8", filePath, modelId + "_" + page + ".txt")) {
                String content = T_Config_File.method_读取文件内容(filePath + modelId + "_" + page + ".txt");
                try {
                    JSONObject jsonRoot = JSONObject.parseObject(content).getJSONObject("result");
                    KouBei_DataBase kouBeiDataBase = new KouBei_DataBase();
                    kouBeiDataBase.update_修改已下载的分页数据状态(modelId, page, 1);
                }catch (Exception e){
                    T_Config_File.delete_删除文件(filePath + modelId + "_" + page + ".txt");
                }


            }
        }
        latch.countDown();
    }
}
