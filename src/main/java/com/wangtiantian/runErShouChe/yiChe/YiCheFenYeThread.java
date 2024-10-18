package com.wangtiantian.runErShouChe.yiChe;

import com.wangtiantian.entity.ershouche.che168.Che168_CityData;
import com.wangtiantian.entity.ershouche.yiChe.YiChe_FenYeUrl;
import com.wangtiantian.mapper.ErShouCheDataBase;
import com.wangtiantian.runErShouChe.che168.MainChe168;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class YiCheFenYeThread implements Runnable {

    private List<Object> list;
    private String filePath;
    private CountDownLatch latch;

    public YiCheFenYeThread(List<Object> list, String filePath, CountDownLatch latch) {
        this.list = list;
        this.filePath = filePath;
        this.latch = latch;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        System.out.println("Thread " + currentThread.getName() + " (ID: " + currentThread.getId() + ") is processing group.");
        for (Object cityItem : list) {
            String cityPinYin = ((YiChe_FenYeUrl) cityItem).get_C_EngName();
            String cityId = ((YiChe_FenYeUrl) cityItem).get_C_CityId();
            String mainUrl = ((YiChe_FenYeUrl) cityItem).get_C_FenYeUrl();
            String cityName = ((YiChe_FenYeUrl) cityItem).get_C_CityName();
            String regionId = ((YiChe_FenYeUrl) cityItem).get_C_RegionId();
            int page = ((YiChe_FenYeUrl) cityItem).get_C_Page();
            String cookie = "auto_id=181f5816f5e830e88e006c677b6a2349; uuid=f216f73b-9ab6-4dcc-979b-7f60eea3740a; _utrace=f216f73b-9ab6-4dcc-979b-7f60eea3740a; _clck=1fag7v9%7C2%7Cfp3%7C0%7C1617; ipCity={%22cityId%22:910%2C%22cityName%22:%22%E4%BF%9D%E5%AE%9A%22%2C%22citySpell%22:%22baoding%22%2C%22cityCode%22:%22130600%22}; city=%7B%22cityName%22%3A%22" + cityName + "%22%2C%22cityId%22%3A" + cityId + "%2C%22cityCode%22%3A" + regionId + "%2C%22citySpell%22%3A%22" + cityPinYin + "%22%7D; _clsk=s0qf0m%7C1726024145609%7C7%7C1%7Cz.clarity.ms%2Fcollect";
            if (MainYiChe.method_访问url(mainUrl, cityId, cookie, filePath, cityPinYin + "_" + page + ".txt")) {
                new ErShouCheDataBase().yiche_update_修改已下载的分页数据下载状态(mainUrl);
            }

        }
        latch.countDown();
    }
}