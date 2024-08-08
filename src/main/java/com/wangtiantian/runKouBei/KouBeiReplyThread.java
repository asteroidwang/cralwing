package com.wangtiantian.runKouBei;

import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.dao.T_Config_KouBei;
import com.wangtiantian.entity.koubei.KouBeiData;
import com.wangtiantian.mapper.KouBeiDataBase;

import java.util.List;

public class KouBeiReplyThread implements Runnable {
    private List<Object> list;
    private String filePath;

    public KouBeiReplyThread(List<Object> list, String filePath) {
        this.list = list;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            String kbId = ((KouBeiData) bean).get_C_KoubeiID();
            String showID = ((KouBeiData) bean).get_C_ShowID();
            String mainUrl = "https://koubeiipv6.app.autohome.com.cn/autov9.13.0/news/replytoplevellist.ashx?pm=1&koubeiId="+kbId+"&next=0&pagesize=9999999";
            T_Config_File.method_访问url获取Json普通版(mainUrl,"UTF-8",filePath,kbId+"_一级评论_0.txt");
//            T_Config_KouBei replyDataDao = new T_Config_KouBei(2, 1, 2);
//            replyDataDao.update修改一级评论的下载状态(showID);

        }
    }
}
