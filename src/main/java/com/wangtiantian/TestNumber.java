package com.wangtiantian;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Brand;
import com.wangtiantian.entity.Bean_Html_Pic;
import com.wangtiantian.entity.koubei.KouBeiData;
import com.wangtiantian.entity.koubei.ReplyKouBei;
import com.wangtiantian.mapper.KouBeiDataBase;
import com.wangtiantian.modelPrice.ModelPriceMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestNumber extends TimerTask {
    private String taskName;

    public  TestNumber(String taskName){
        this.taskName = taskName;
    }
    @Override
    public void run() {
        System.out.println(new Date()+"\t任务"+taskName+"在执行");
    }
}
