package com.wangtiantian.runKouBei;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.koubei.KouBeiPicture;
import com.wangtiantian.entity.koubei.ReplyKouBei;

import java.util.List;

public class KouBeiPictureMoreThread implements Runnable {
    private List<Object> list;
    private String filePath;

    public KouBeiPictureMoreThread(List<Object> list, String filePath) {
        this.list = list;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object bean : list) {
            String showId = ((KouBeiPicture) bean).get_C_ShowID();
            String kbId = ((KouBeiPicture) bean).get_C_KouBeiID();
            String position = ((KouBeiPicture) bean).get_C_Position();
            String mainUrl = ((KouBeiPicture) bean).get_C_PictureUrl();
            T_Config_File.downloadImage(mainUrl, filePath + showId + "/", showId + "_" + kbId + "_" + position+".jpg");
        }
    }
}