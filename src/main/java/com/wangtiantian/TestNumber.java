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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestNumber {
    public static void main(String[] args) {
        String filePath = "/Users/asteroid/所有文件数据/爬取网页原始数据/汽车之家/经销商数据/20240827/";
        ModelPriceMethod modelPriceMethod = new ModelPriceMethod();
        modelPriceMethod.method_解析所有经销商分页数据(filePath + "车型页面的经销商数据分页/");
    }

}
