package com.wangtiantian.runPicture;

import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.configData.CarBaseInfo;
import com.wangtiantian.entity.picture.PictureHtml;
import com.wangtiantian.entity.picture.PictureInfo;
import com.wangtiantian.mapper.DataBaseConnectPic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ThreadPictureInfo implements Runnable {
    private List<Object> list;
    private String filePath;
    private CountDownLatch latch;
    private ArrayList<Object> carInfoList;

    public ThreadPictureInfo(List<Object> list, String filePath, CountDownLatch latch, ArrayList<Object> carInfoList) {
        this.list = list;
        this.filePath = filePath;
        this.latch = latch;
        this.carInfoList = carInfoList;
    }

    @Override
    public void run() {
        Map<String, String> mapCategory = new HashMap<>();
        mapCategory.put("10", "中控");
        mapCategory.put("51", "改装");
        mapCategory.put("3", "座椅");
        mapCategory.put("12", "细节");
        mapCategory.put("14", "特点");
        mapCategory.put("55", "车展");
        mapCategory.put("1", "外观");
        mapCategory.put("13", "评测");
        mapCategory.put("200", "网友实拍");
        mapCategory.put("15", "活动");
        mapCategory.put("53", "官图");
        mapCategory.put("54", "硬致");
        Thread currentThread = Thread.currentThread();
        DataBaseConnectPic dataBaseConnectPic = new DataBaseConnectPic();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        List<String> htmlList = new ArrayList<>();
        for (Object o : list) {
            String brandId = ((PictureInfo) o).get_C_BrandID();
            String factoryId = ((PictureInfo) o).get_C_FactoryID();
            String modelId = ((PictureInfo) o).get_C_ModelID();
            String versionId = ((PictureInfo) o).get_C_VersionID();
            String typeCode = ((PictureInfo) o).get_C_CategoryCode();
            String imgCode = ((PictureInfo) o).get_C_PictureCode();
            List<CarBaseInfo> carInfo = carInfoList.stream().filter(e -> ((CarBaseInfo) e).get_C_BrandID().equals(brandId)
                    && ((CarBaseInfo) e).get_C_FactoryID().equals(factoryId)
                    && ((CarBaseInfo) e).get_C_ModelID().equals(modelId)
                    && ((CarBaseInfo) e).get_C_VersionID().equals(versionId)
            ).map(e -> ((CarBaseInfo) e)).collect(Collectors.toList());
            String brandName = carInfo.get(0).get_C_BrandName();
            String factoryName = carInfo.get(0).get_C_ModelName();
            String modelName = carInfo.get(0).get_C_FactoryName();
            String versionName = carInfo.get(0).get_C_VersionName();
            String typeName = mapCategory.get(typeCode);
            String folderPath = brandName + "/" + factoryName + "/" + modelName + "/" + versionName + "/" + typeName + "/";
            String fileName = imgCode + ".jpg";
            String imgUrl = ((PictureInfo) o).get_C_IsHigh() == 1 ? ((PictureInfo) o).get_C_PictureHighUrl() : ((PictureInfo) o).get_C_PictureUrl();
            if (!T_Config_File.method_判断文件是否存在(filePath + folderPath + fileName)) {
                if (T_Config_File.downloadImage(imgUrl, filePath + folderPath, fileName)) {
                    htmlList.add(imgCode);
                }
            } else {
                htmlList.add(imgCode);
            }
        }
        StringBuffer htmls = new StringBuffer();
        for (int i = 0; i < htmlList.size(); i++) {
            htmls.append("'").append(htmlList.get(i)).append("',");
        }
        dataBaseConnectPic.updateImgStatus(htmls.toString().substring(0, htmls.toString().length() - 1));
        latch.countDown();
    }
}