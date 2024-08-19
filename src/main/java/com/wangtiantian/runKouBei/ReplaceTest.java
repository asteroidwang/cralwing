package com.wangtiantian.runKouBei;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.koubei.ReplyKouBei;
import com.wangtiantian.mapper.KouBeiDataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceTest {
    public static void main(String[] args) {
        String filePath = "/Users/asteroid/所有文件数据/一级评论/";
        ArrayList<String> fileNames = T_Config_File.method_获取文件名称(filePath);
        ArrayList<Object> res = new ArrayList<>();
        for (String fileName : fileNames) {
            if (!fileName.equals(".DS_Store")) {
                String path1 = filePath + fileName;
                String jsonString = T_Config_File.method_读取文件内容(path1);
                // 清除所有特殊符号
                jsonString = removeAllSpecialSymbols(jsonString);
                // 对于非法双引号进行判断
                jsonString = removeIllegalQuotes(jsonString);
                JSONObject jsonObject = JSON.parseObject(jsonString);
               res.addAll(new KouBeiMethod().parse_解析一级评论数据(jsonString, filePath, fileName));

            }

        }
        System.out.println(res.size());
        new KouBeiDataBase().insetForeachKouBeiReplyData(res);
    }


    /**
     * @author Mr-Glacier
     * @apiNote 移除所有特殊字符
     */
    private static String removeAllSpecialSymbols(String jsonString) {
        // 使用正则表达式来匹配并移除所有特殊符号
        // 正则表达式匹配需要保留的字符以外的所有字符
        String regex = "[^\"{}\\[\\],:a-zA-Z0-9\u4e00-\u9fa5]";
        // 使用空字符串替换所有匹配到的特殊字符
        return jsonString.replaceAll(regex, "").replace("\"\"", "\"")
                .replace("\":\",", "\":\"\",")
                .replace(":\":", ":")
                .replace("\",\",", "\",")
                .replace(",\",\"", ",");
    }

    /**
     * @author Mr-Glacier
     * @apiNote 移除所有非法 "
     */
    public static String removeIllegalQuotes(String str) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '"') {
                // 检查 双引号前面是否是中文字符 || 数字 ||字母
                char prevChar = str.charAt(i - 1);
                char nextChar = str.charAt(i + 1);
                // prevChar >= '\u4e00' && prevChar <= '\u9fff' 中文字符, 数字,字母
                boolean isPrevChineseOrPunctuation = (prevChar >= '\u4e00' && prevChar <= '\u9fa5') || Character.isDigit(prevChar) || Character.isLetter(prevChar)
                        || nextChar == '\u0022';
                if (prevChar == '\u002c' || prevChar == '\u003a') {
                    char prevprevChar = str.charAt(i - 2);
                    if ((prevprevChar >= '\u4e00' && prevprevChar <= '\u9fa5')) {
                        isPrevChineseOrPunctuation = true;
                    }
                }
                boolean isNextChineseOrPunctuation = (nextChar >= '\u4e00' && nextChar <= '\u9fa5') || Character.isDigit(nextChar) || Character.isLetter(nextChar);
                if (nextChar == '\u002c') {
                    char nextnextChar = str.charAt(i + 2);
                    if ((nextnextChar >= '\u4e00' && nextnextChar <= '\u9fa5')) {
                        isNextChineseOrPunctuation = true;
                    }
                }
                if (isPrevChineseOrPunctuation && isNextChineseOrPunctuation) {
                    positions.add(i);
                }
            }
            // 对字符串中存在的非法双引号进行去除
        }
        for (int i = positions.size() - 1; i >= 0; i--) {
            int position = positions.get(i);
            str = str.substring(0, position) + str.substring(position + 1);
        }
        return str;
    }
//    public static void main(String[] args) {
//        ArrayList<Object> dataList = new ArrayList<>();
//        ArrayList<String> fileList = T_Config_File.method_获取文件名称("/Users/asteroid/所有文件数据/一级评论/");
//        for (String fileName : fileList) {
//            if (fileName.equals(".DS_Store")) {
//                continue;
//            }
//            String content = T_Config_File.method_读取文件内容("/Users/asteroid/所有文件数据/一级评论/" + fileName);
//            Pattern pattern = Pattern.compile("\"rcontent\":(.*?)\"rmemberId\"");
//            Matcher matcher = pattern.matcher(content);
//            StringBuffer result = new StringBuffer();
//            while (matcher.find()) {
//                String firstPart = matcher.group(1); // 第一个引号与第二个引号之间的内容
//                if (firstPart.matches(".*[\\u4e00-\\u9fa5].*")||firstPart.matches(".*[\\\\p{Punct}].*")||firstPart.matches(".*[\\\\p{IsPunctuation}].*")) {
//                    // 如果包含中文字符
//                    matcher.appendReplacement(result, "\"rcontent\":\"" + firstPart.replace("\\","回车").replace("'","''").replace("\"","英文双引号") + "\",\"rmemberId\"");
//                } else {
//                    // 保持原样
//                    matcher.appendReplacement(result, matcher.group());
//                }
//            }
//            matcher.appendTail(result);
//            System.out.println(content);
//            System.out.println(result);
//            JSONObject jsonRoot = JSON.parseObject(result.toString()).getJSONObject("result");
//            JSONArray jsonArray = jsonRoot.getJSONArray("list");
//            String nextString = jsonRoot.getString("next");
//            for (int i = 0; i < jsonArray.size(); i++) {
//                ReplyKouBei firstReply = new ReplyKouBei();
//                String replydate = ((JSONObject) jsonArray.get(i)).getString("replydate") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("replydate");
//                String chatcount = ((JSONObject) jsonArray.get(i)).getString("chatcount") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("chatcount");
//                String iscarowner = ((JSONObject) jsonArray.get(i)).getString("iscarowner") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("iscarowner");
//                String carownerlevels = ((JSONObject) jsonArray.get(i)).getString("carownerlevels") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("carownerlevels");
//                String carname = ((JSONObject) jsonArray.get(i)).getString("carname") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("carname");
//                String location = ((JSONObject) jsonArray.get(i)).getString("location") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("location");
//                String forbidReply = ((JSONObject) jsonArray.get(i)).getString("forbidReply") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("forbidReply");
//                String freplyCount = ((JSONObject) jsonArray.get(i)).getString("freplyCount") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("freplyCount");
//                String rmemberSex = ((JSONObject) jsonArray.get(i)).getString("rmemberSex") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rmemberSex");
//                String robjId = ((JSONObject) jsonArray.get(i)).getString("robjId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("robjId");
//                String rreplyDate = ((JSONObject) jsonArray.get(i)).getString("rreplyDate") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rreplyDate");
//                String rup = ((JSONObject) jsonArray.get(i)).getString("rup") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rup");
//                JSONArray subQuoteList = ((JSONObject) jsonArray.get(i)).getJSONArray("subQuoteList");
//                String freplyId = ((JSONObject) jsonArray.get(i)).getString("freplyId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("freplyId");
//                String rtargetReplyId = ((JSONObject) jsonArray.get(i)).getString("rtargetReplyId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rtargetReplyId");
//                String rtargetMemberId = ((JSONObject) jsonArray.get(i)).getString("rtargetMemberId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rtargetMemberId");
//                String rfloor = ((JSONObject) jsonArray.get(i)).getString("rfloor") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rfloor");
//                String rcontentLength = ((JSONObject) jsonArray.get(i)).getString("rcontentLength") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rcontentLength");
//                String createType = ((JSONObject) jsonArray.get(i)).getString("createType") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("createType");
//                String chatIndex = ((JSONObject) jsonArray.get(i)).getString("chatIndex") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("chatIndex");
//                String ruserHeaderImage = ((JSONObject) jsonArray.get(i)).getString("ruserHeaderImage") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("ruserHeaderImage");
//                String rcontent = ((JSONObject) jsonArray.get(i)).getString("rcontent") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rcontent");
//                String rmemberId = ((JSONObject) jsonArray.get(i)).getString("rmemberId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rmemberId");
//                String rmemberName = ((JSONObject) jsonArray.get(i)).getString("rmemberName") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("rmemberName");
//                String replyId = ((JSONObject) jsonArray.get(i)).getString("replyId") == null ? "-" : ((JSONObject) jsonArray.get(i)).getString("replyId");
//                JSONObject badge = ((JSONObject) jsonArray.get(i)).getJSONObject("badge");
//                String user_id = "";
//                String achievement_id = "";
//                String badge_name = "";
//                String badge_icon = "";
//                if (badge != null) {
//                    user_id = badge.getString("user_id");
//                    achievement_id = badge.getString("achievement_id");
//                    badge_name = badge.getString("badge_name");
//                    badge_icon = badge.getString("badge_icon");
//                }
//                if (subQuoteList.size() != 0) {
//                    for (int j = 0; j < subQuoteList.size(); j++) {
//                        ReplyKouBei secondReply = new ReplyKouBei();
//                        String replydateSub = ((JSONObject) subQuoteList.get(j)).getString("replydate") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("replydate");
//                        String chatcountSub = ((JSONObject) subQuoteList.get(j)).getString("chatcount") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("chatcount");
//                        String iscarownerSub = ((JSONObject) subQuoteList.get(j)).getString("iscarowner") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("iscarowner");
//                        String carownerlevelsSub = ((JSONObject) subQuoteList.get(j)).getString("carownerlevels") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("carownerlevels");
//                        String carnameSub = ((JSONObject) subQuoteList.get(j)).getString("carname") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("carname");
//                        String forbidReplySub = ((JSONObject) subQuoteList.get(j)).getString("forbidReply") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("forbidReply");
//                        String rmemberSexSub = ((JSONObject) subQuoteList.get(j)).getString("rmemberSex") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rmemberSex");
//                        String robjIdSub = ((JSONObject) subQuoteList.get(j)).getString("robjId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("robjId");
//                        String rreplyDateSub = ((JSONObject) subQuoteList.get(j)).getString("rreplyDate") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rreplyDate");
//                        String rupSub = ((JSONObject) subQuoteList.get(j)).getString("rup") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rup");
//                        String replyIdSub = ((JSONObject) subQuoteList.get(j)).getString("replyId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("replyId");
//                        String freplyIdSub = ((JSONObject) subQuoteList.get(j)).getString("freplyId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("freplyId");
//                        String rtargetReplyIdSub = ((JSONObject) subQuoteList.get(j)).getString("rtargetReplyId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rtargetReplyId");
//                        String rtargetMemberIdSub = ((JSONObject) subQuoteList.get(j)).getString("rtargetMemberId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rtargetMemberId");
//                        String rfloorSub = ((JSONObject) subQuoteList.get(j)).getString("rfloor") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rfloor");
//                        String rcontentLengthSub = ((JSONObject) subQuoteList.get(j)).getString("rcontentLength") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rcontentLength");
//                        String createTypeSub = ((JSONObject) subQuoteList.get(j)).getString("createType") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("createType");
//                        String chatIndexSub = ((JSONObject) subQuoteList.get(j)).getString("chatIndex") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("chatIndex");
//                        String freplyCountSub = ((JSONObject) subQuoteList.get(j)).getString("freplyCount") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("freplyCount");
//                        String rcontentSub = ((JSONObject) subQuoteList.get(j)).getString("rcontent") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rcontent");
//                        String rmemberIdSub = ((JSONObject) subQuoteList.get(j)).getString("rmemberId") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rmemberId");
//                        String rmemberNameSub = ((JSONObject) subQuoteList.get(j)).getString("rmemberName") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("rmemberName");
//                        String ruserHeaderImageSub = ((JSONObject) subQuoteList.get(j)).getString("ruserHeaderImage") == null ? "-" : ((JSONObject) subQuoteList.get(j)).getString("ruserHeaderImage");
//                        secondReply.set_C_replydateSub(replydateSub);
//                        secondReply.set_C_chatcountSub(chatcountSub);
//                        secondReply.set_C_iscarownerSub(iscarownerSub);
//                        secondReply.set_C_carownerlevelsSub(carownerlevelsSub);
//                        secondReply.set_C_carnameSub(carnameSub);
//                        secondReply.set_C_forbidReplySub(forbidReplySub);
//                        secondReply.set_C_rmemberSexSub(rmemberSexSub);
//                        secondReply.set_C_robjIdSub(robjIdSub);
//                        secondReply.set_C_rreplyDateSub(rreplyDateSub);
//                        secondReply.set_C_rupSub(rupSub);
//                        secondReply.set_C_replyIdSub(replyIdSub);
//                        secondReply.set_C_freplyIdSub(freplyIdSub);
//                        secondReply.set_C_rtargetReplyIdSub(rtargetReplyIdSub);
//                        secondReply.set_C_rtargetMemberIdSub(rtargetMemberIdSub);
//                        secondReply.set_C_rfloorSub(rfloorSub);
//                        secondReply.set_C_rcontentLengthSub(rcontentLengthSub);
//                        secondReply.set_C_createTypeSub(createTypeSub);
//                        secondReply.set_C_chatIndexSub(chatIndexSub);
//                        secondReply.set_C_freplyCountSub(freplyCountSub);
//                        secondReply.set_C_rcontentSub(rcontentSub);
//                        secondReply.set_C_rmemberIdSub(rmemberIdSub);
//                        secondReply.set_C_rmemberNameSub(rmemberNameSub);
//                        secondReply.set_C_ruserHeaderImageSub(ruserHeaderImageSub);
//                        secondReply.set_C_badge_icon(badge_icon);
//                        secondReply.set_C_badge_user_id(user_id);
//                        secondReply.set_C_badge_achievement_id(achievement_id);
//                        secondReply.set_C_badge_name(badge_name);
//                        secondReply.set_C_KouBeiID(robjId);
//                        secondReply.set_C_ReplyContent(rcontent);
//                        secondReply.set_C_ReplyUserID(rmemberId);
//                        secondReply.set_C_ReplyUserName(rmemberName);
//                        secondReply.set_C_TargetUserID(rtargetMemberId);
//                        secondReply.set_C_nextString(nextString);
//                        secondReply.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                        dataList.add(secondReply);
//                    }
//                }
//
//                firstReply.set_C_replydate(replydate);
//                firstReply.set_C_chatcount(chatcount);
//                firstReply.set_C_iscarowner(iscarowner);
//                firstReply.set_C_carownerlevels(carownerlevels);
//                firstReply.set_C_carname(carname);
//                firstReply.set_C_location(location);
//                firstReply.set_C_forbidReply(forbidReply);
//                firstReply.set_C_freplyCount(freplyCount);
//                firstReply.set_C_rmemberSex(rmemberSex);
//                firstReply.set_C_robjId(robjId);
//                firstReply.set_C_rreplyDate(rreplyDate);
//                firstReply.set_C_rup(rup);
//                firstReply.set_C_freplyId(freplyId);
//                firstReply.set_C_rtargetReplyId(rtargetReplyId);
//                firstReply.set_C_rtargetMemberId(rtargetMemberId);
//                firstReply.set_C_rfloor(rfloor);
//                firstReply.set_C_rcontentLength(rcontentLength);
//                firstReply.set_C_createType(createType);
//                firstReply.set_C_chatIndex(chatIndex);
//                firstReply.set_C_ruserHeaderImage(ruserHeaderImage);
//                firstReply.set_C_rcontent(rcontent);
//                firstReply.set_C_rmemberId(rmemberId);
//                firstReply.set_C_rmemberName(rmemberName);
//                firstReply.set_C_replyId(replyId);
//                firstReply.set_C_badge_icon(badge_icon);
//                firstReply.set_C_badge_user_id(user_id);
//                firstReply.set_C_badge_achievement_id(achievement_id);
//                firstReply.set_C_badge_name(badge_name);
//                firstReply.set_C_KouBeiID(robjId);
//                firstReply.set_C_ReplyContent(rcontent);
//                firstReply.set_C_ReplyUserID(rmemberId);
//                firstReply.set_C_ReplyUserName(rmemberName);
//                firstReply.set_C_TargetUserID("");
//                firstReply.set_C_IsFinish(0);
//                firstReply.set_C_nextString(nextString);
//                firstReply.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                dataList.add(firstReply);
//            }
//        }
//        System.out.println(dataList.size());
//    }


}
