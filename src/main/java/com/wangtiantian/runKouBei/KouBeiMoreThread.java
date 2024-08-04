package com.wangtiantian.runKouBei;

import com.wangtiantian.CommonMoreThread;
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
//            ArrayList<Object> modUrlList = kouBeiDataBase.getModFirstKouBei();
//            for (Object o : modUrlList) {
//                String modFirstUrl = ((ModelKouBei) o).get_C_ModelKouBeiUrl();
//                Document mainDoc = null;
//                try {
//                    mainDoc = Jsoup.parse(new URL(modFirstUrl).openStream(), "UTF-8", modFirstUrl);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (mainDoc != null) {
//                    T_Config_File.method_写文件_根据路径创建文件夹(filePath + ((ModelKouBei) o).get_C_ModelID() + "/", ((ModelKouBei) o).get_C_ModelID() + "_"+((ModelKouBei) o).get_C_Page()+".txt", mainDoc.text());
//                    kouBeiDataBase.update下载状态(modFirstUrl);
//                }
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
