package com.wangtiantian.runPrice;

public class MainPrice {
    //    private String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/经销商数据/20240802/";
    public static void main(String[] args) {
        String filePath = "/Users/wangtiantian/MyDisk/汽车之家/经销商数据/20240819/";
        String cityDataName = "cityData.json";
        CarPriceMethod carPriceMethod = new CarPriceMethod();
        // 数据准备->入库城市数据
         carPriceMethod.cityData(filePath+cityDataName);
        // 数据准备-> 下载各省市的经销商列表数据 https://dealer.autohome.com.cn/beijing/0/0/0/0/4/1/0/0.html
        // carPriceMethod.parse_获取经销商分页数据的url();
        // carPriceMethod.downLoad_下载经销商分页数据(filePath+"经销商分页数据/");
        // 解析经销商数据
        // carPriceMethod.parseDealerFile(filePath+"经销商分页数据/");
        // 下载车型报价页面
        // carPriceMethod.getCarPriceFile(filePath+ "车型报价页面_html/");
        // carPriceMethod.getCarPriceFile2( filePath+ "车型报价页面/");
        // 解析车型报价页面 获取该店的在售车型Id 拼接下载地址获取 该店在售车型的车辆信息
        // carPriceMethod.parseCarPriceFile(filePath+ "车型报价页面/");
        // carPriceMethod.parseCarPriceFile2( filePath+ "车型报价页面/");
        // 下载车辆信息数据
         carPriceMethod.getCarDataFile(filePath+"车辆价格信息/");
        // 补充未下载的价格信息页面
        // carPriceMethod.method_补充未下载的车辆价格信息页面(filePath+"车辆价格信息/");
        // 解析数据
        // carPriceMethod.parseCarPriceData(filePath+"车辆价格信息/");
    }


}
