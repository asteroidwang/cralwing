package com.wangtiantian.runKouBei;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.koubei.KouBeiData;
import com.wangtiantian.entity.koubei.ReplyKouBei;

import java.util.List;

public class SecondReplyDataThread implements Runnable {
    private List<Object> list;
    private String filePath;

    public SecondReplyDataThread(List<Object> list, String filePath) {
        this.list = list;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            String kbId = ((ReplyKouBei) bean).get_C_KouBeiID();
            String freplyId  =((ReplyKouBei)bean).get_C_nextString();
            String mainUrl = "https://koubeiipv6.app.autohome.com.cn/autov9.13.0/news/replytoplevelsublist.ashx?_appid=koubei&koubeiid="+kbId+"&freplyid="+freplyId+"&next=0&pagesize=999999&pm=1&appversion=1&orderBy=0";
            if (T_Config_File.method_访问url获取Json普通版(mainUrl,"UTF-8",filePath,kbId+"_二级级评论_0.txt")){
                T_Config_File.method_重复写文件_根据路径创建文件夹(filePath.replace("二级评论/",""),"已下载的二级评论.txt",mainUrl+"\n");
            }

        }
    }
}