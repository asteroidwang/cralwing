package com.wangtiantian;

import com.wangtiantian.runConfigData.MainConfigData;
import com.wangtiantian.runErShouChe.yiChe.MainYiChe;

import java.util.*;

public class TestTwo {


    public static void main(String[] args) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6); // 控制时
        calendar.set(Calendar.MINUTE,0);       // 控制分
        calendar.set(Calendar.SECOND, 0);       // 控制秒

        Date time = calendar.getTime();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MainConfigData(), time, 1000 * 60 * 60 * 24);// 这里
    }
}

