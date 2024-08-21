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
        KouBeiDataBase kouBeiDataBase = new KouBeiDataBase();
        String filePath = "E:/汽车之家/口碑评价数据/20240804/";
        ArrayList<Object> replyData = new ArrayList<>();
//        ArrayList<String> dataList = T_Config_File.method_获取文件名称(filePath + "一级评论数据/");
        int getCount = kouBeiDataBase.getCount();
        for (int kk = 0; kk < getCount / 1000; kk++) {
            ArrayList<Object> dataList = kouBeiDataBase.getNotParseFirstPingLunKouBeiId(kk * 10000);
//        + "_一级评论_0.txt"
            for (Object o : dataList) {
                String kbId = ((KouBeiData) o).get_C_KoubeiID();
                replyData.addAll(parse_解析一级评论数据(T_Config_File.method_读取文件内容(filePath + "一级评论数据/" + kbId + "_一级评论_0.txt"), filePath, kbId + "_一级评论_0.txt"));
            }
        }
        System.out.println(replyData.size());
    }

    public static ArrayList<Object> parse_解析一级评论数据(String content, String filePath, String fileName) {
        ArrayList<Object> dataList = new ArrayList<>();
        content = content.replace("$", "钱的符号").replace("\\\"", "不该有的英文引号").replace("\\", "两个斜杠");
        Pattern pattern = Pattern.compile("\"rcontent\":\"(.*?)\",\"rmemberId\"");
        Matcher matcher = pattern.matcher(content);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String firstPart = matcher.group(1); // 第一个引号与第二个引号之间的内容
            matcher.appendReplacement(result, "\"rcontent\":\"" + firstPart.replace("\n", "").replace("\r", "").replace("\n\r", "").replace("'", "''").replace("\"", "不该有的英文引号") + "\",\"rmemberId\"");
        }
        matcher.appendTail(result);
        content = result.toString();
        JSONObject jsonRoot = JSON.parseObject(content).getJSONObject("result");
        JSONArray jsonArray = jsonRoot.getJSONArray("list");
        JSONArray jsonArray1 = jsonRoot.getJSONArray("wonderfullist");
        String nextString = jsonRoot.getString("next");
        if (jsonArray1.size() != 0) {
            for (int i = 0; i < jsonArray1.size(); i++) {
                JSONObject wonderObject = ((JSONObject) jsonArray1.get(i));
                ReplyKouBei replyKouBei = new ReplyKouBei();
                replyKouBei.set_C_nextString(nextString);
                replyKouBei.set_C_hasmore(jsonRoot.getString("hasmore"));
                replyKouBei.set_C_freplyCount_TieZi(jsonRoot.getString("freplyCount"));
                replyKouBei.set_C_KouBeiID(fileName.replace("_一级评论_0.txt", "").equals("") ? "-" : fileName.replace("_一级评论_0.txt", ""));
                replyKouBei.set_C_rfloor(wonderObject.getString("rfloor") == null ? "-" : wonderObject.getString("rfloor"));
                replyKouBei.set_C_iscarowner(wonderObject.getString("iscarowner") == null ? "-" : wonderObject.getString("iscarowner"));
                replyKouBei.set_C_rmemberId(wonderObject.getString("rmemberId") == null ? "-" : wonderObject.getString("rmemberId"));
                replyKouBei.set_C_rcontentLength(wonderObject.getString("rcontentLength") == null ? "-" : wonderObject.getString("rcontentLength"));
                replyKouBei.set_C_createType(wonderObject.getString("createType") == null ? "-" : wonderObject.getString("createType"));
                replyKouBei.set_C_freplyId(wonderObject.getString("freplyId") == null ? "-" : wonderObject.getString("freplyId"));
                replyKouBei.set_C_rup(wonderObject.getString("rup") == null ? "-" : wonderObject.getString("rup"));
                replyKouBei.set_C_chatIndex(wonderObject.getString("chatIndex") == null ? "-" : wonderObject.getString("chatIndex"));
                replyKouBei.set_C_rmemberSex(wonderObject.getString("rmemberSex") == null ? "-" : wonderObject.getString("rmemberSex"));
                replyKouBei.set_C_robjId(wonderObject.getString("robjId") == null ? "-" : wonderObject.getString("robjId"));
                replyKouBei.set_C_rmemberName(wonderObject.getString("rmemberName") == null ? "-" : wonderObject.getString("rmemberName"));
                replyKouBei.set_C_rreplyDate(wonderObject.getString("rreplyDate") == null ? "-" : wonderObject.getString("rreplyDate"));
                replyKouBei.set_C_carname(wonderObject.getString("carname") == null ? "-" : wonderObject.getString("carname"));
                replyKouBei.set_C_replyId(wonderObject.getString("replyId") == null ? "-" : wonderObject.getString("replyId"));
                replyKouBei.set_C_forbidReply(wonderObject.getString("forbidReply") == null ? "-" : wonderObject.getString("forbidReply"));
                replyKouBei.set_C_ruserHeaderImage(wonderObject.getString("ruserHeaderImage") == null ? "-" : wonderObject.getString("ruserHeaderImage"));
                replyKouBei.set_C_rtargetMemberId(wonderObject.getString("rtargetMemberId") == null ? "-" : wonderObject.getString("rtargetMemberId"));
                replyKouBei.set_C_carownerlevels(wonderObject.getString("carownerlevels") == null ? "-" : wonderObject.getString("carownerlevels"));
                replyKouBei.set_C_chatcount(wonderObject.getString("chatcount") == null ? "-" : wonderObject.getString("chatcount"));
                replyKouBei.set_C_replydate(wonderObject.getString("replydate") == null ? "-" : wonderObject.getString("replydate"));
                replyKouBei.set_C_rtargetReplyId(wonderObject.getString("rtargetReplyId") == null ? "-" : wonderObject.getString("rtargetReplyId"));
                replyKouBei.set_C_location(wonderObject.getString("location") == null ? "-" : wonderObject.getString("location"));
                replyKouBei.set_C_rcontent(wonderObject.getString("rcontent") == null ? "-" : wonderObject.getString("rcontent"));
                replyKouBei.set_C_freplyCount_First(wonderObject.getString("freplyCount") == null ? "-" : wonderObject.getString("freplyCount"));
                dataList.add(replyKouBei);
            }
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject wonderObject = ((JSONObject) jsonArray.get(i));
            ReplyKouBei replyKouBei = new ReplyKouBei();
            replyKouBei.set_C_freplyCount_TieZi(jsonRoot.getString("freplyCount"));
            replyKouBei.set_C_KouBeiID(fileName.replace("_一级评论_0.txt", "").equals("") ? "-" : fileName.replace("_一级评论_0.txt", ""));
            replyKouBei.set_C_rfloor(wonderObject.getString("rfloor") == null ? "-" : wonderObject.getString("rfloor"));
            replyKouBei.set_C_iscarowner(wonderObject.getString("iscarowner") == null ? "-" : wonderObject.getString("iscarowner"));
            replyKouBei.set_C_rmemberId(wonderObject.getString("rmemberId") == null ? "-" : wonderObject.getString("rmemberId"));
            replyKouBei.set_C_rcontentLength(wonderObject.getString("rcontentLength") == null ? "-" : wonderObject.getString("rcontentLength"));
            replyKouBei.set_C_createType(wonderObject.getString("createType") == null ? "-" : wonderObject.getString("createType"));
            replyKouBei.set_C_freplyId(wonderObject.getString("freplyId") == null ? "-" : wonderObject.getString("freplyId"));
            replyKouBei.set_C_rup(wonderObject.getString("rup") == null ? "-" : wonderObject.getString("rup"));
            replyKouBei.set_C_chatIndex(wonderObject.getString("chatIndex") == null ? "-" : wonderObject.getString("chatIndex"));
            replyKouBei.set_C_rmemberSex(wonderObject.getString("rmemberSex") == null ? "-" : wonderObject.getString("rmemberSex"));
            replyKouBei.set_C_robjId(wonderObject.getString("robjId") == null ? "-" : wonderObject.getString("robjId"));
            replyKouBei.set_C_rmemberName(wonderObject.getString("rmemberName") == null ? "-" : wonderObject.getString("rmemberName"));
            replyKouBei.set_C_rreplyDate(wonderObject.getString("rreplyDate") == null ? "-" : wonderObject.getString("rreplyDate"));
            replyKouBei.set_C_carname(wonderObject.getString("carname") == null ? "-" : wonderObject.getString("carname"));
            replyKouBei.set_C_replyId(wonderObject.getString("replyId") == null ? "-" : wonderObject.getString("replyId"));
            replyKouBei.set_C_forbidReply(wonderObject.getString("forbidReply") == null ? "-" : wonderObject.getString("forbidReply"));
            replyKouBei.set_C_ruserHeaderImage(wonderObject.getString("ruserHeaderImage") == null ? "-" : wonderObject.getString("ruserHeaderImage"));
            replyKouBei.set_C_rtargetMemberId(wonderObject.getString("rtargetMemberId") == null ? "-" : wonderObject.getString("rtargetMemberId"));
            replyKouBei.set_C_carownerlevels(wonderObject.getString("carownerlevels") == null ? "-" : wonderObject.getString("carownerlevels"));
            replyKouBei.set_C_chatcount(wonderObject.getString("chatcount") == null ? "-" : wonderObject.getString("chatcount"));
            replyKouBei.set_C_replydate(wonderObject.getString("replydate") == null ? "-" : wonderObject.getString("replydate"));
            replyKouBei.set_C_rtargetReplyId(wonderObject.getString("rtargetReplyId") == null ? "-" : wonderObject.getString("rtargetReplyId"));
            replyKouBei.set_C_location(wonderObject.getString("location") == null ? "-" : wonderObject.getString("location"));
            replyKouBei.set_C_rcontent(wonderObject.getString("rcontent") == null ? "-" : wonderObject.getString("rcontent"));
            replyKouBei.set_C_freplyCount_First(wonderObject.getString("freplyCount") == null ? "-" : wonderObject.getString("freplyCount"));
            dataList.add(replyKouBei);
        }
        return dataList;
    }

}
