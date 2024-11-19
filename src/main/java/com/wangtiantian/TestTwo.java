package com.wangtiantian;

import com.wangtiantian.runErShouChe.yiChe.MainYiChe;

import java.util.*;

public class TestTwo {


    public static void main(String[] args) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1); // 控制时
        calendar.set(Calendar.MINUTE,0);       // 控制分
        calendar.set(Calendar.SECOND, 0);       // 控制秒

        Date time = calendar.getTime();         // 得出执行任务的时间,此处为今天的12：00：00

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainYiChe("测试任务"), time, 1000 * 60 * 60 * 24);// 这里
//        Timer timer = new Timer();
//        timer.schedule(new TestNumber("测试任务"), 2000L, 1000L * 60);
//        Timer timer = new Timer();
//      timer.schedule(new MainYiChe("测试任务"),2000L,1000L*60*60*24);
    }
}

