package com.wangtiantian.modelPrice;

public class MainModelPrice {
    public static void main(String[] args) {
        String filePath="/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/经销商数据/20240806/";
        ModelPriceMethod modelPriceMethod = new ModelPriceMethod();
        // modelPriceMethod.method_根据车型id获取经销商数据(filePath+"车型页面的经销商数据/");
        // modelPriceMethod.method_解析上一步的车型得到的经销商数据(filePath+"车型页面的经销商数据/");
        modelPriceMethod.method_根据经销商id获取下载车辆价格信息文件(filePath);
    }
}
