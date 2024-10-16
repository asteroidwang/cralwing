package com.wangtiantian;

import com.wangtiantian.runErShouChe.yiChe.MainYiChe;

import java.util.*;

public class TestTwo {


    public static void main(String[] args) {
      Timer timer = new Timer();
      timer.schedule(new MainYiChe("测试任务"),2000L,1000L*60*60*24);
    }
}

