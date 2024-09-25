package com.wangtiantian;

import java.util.*;

public class TestTwo {


    public static void main(String[] args) {
      Timer timer = new Timer();
      timer.schedule(new TestNumber("测试任务"),2000L,1000L*60);
    }
}

