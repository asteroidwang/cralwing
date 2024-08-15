package com.wangtiantian.runKouBei;

public class replaceDemo {
    public static void main(String[] args) {
        // 错误 JSON 字符串
        String incorrectJson = "{\"returncode\":0,\"message\":\"\",\"result\":{\"hasmore\":false,\"next\":263252,\"list\":[{\"replydate\":\"2015-05-31\",\"chatcount\":0,\"iscarowner\":0,\"carownerlevels\":0,\"carname\":\"\",\"location\":\"湖南\",\"forbidReply\":false,\"rcontentLength\":196,\"createType\":0,\"rup\":1,\"chatIndex\":0,\"subQuoteList\":[],\"freplyId\":0,\"rreplyDate\":\"2015-05-31 15:22:13\",\"freplyCount\":0,\"rcontent\":\"\"【最满意的一点】：空间设计什么的都很满意,配置也丰富\\n【最不满意的一点】：噪音有点大\\n【外观】：高大上吧，个人很喜欢\\n【内饰】：用料吧是省了点,但是这价钱也别指望了,看着舒心不错了。\\n【空间】：空间足够用,特别是后备箱超级大\\n【动力】：每次重踩油门那推背感真是舒服,游刃有余\\n【操控】：高速时定位还不错,至少不会让我感觉飘。\\n【油耗】：8.2个油\\n【舒适性】：空间较大,舒适性良好\",\"rmemberId\":17885146,\"rmemberName\":\"Tengxunwang\",\"replyId\":263252,\"rfloor\":1,\"rtargetMemberId\":0,\"robjId\":293272,\"rtargetReplyId\":0,\"rmemberSex\":1,\"ruserHeaderImage\":\"http://x.autoimg.cn/space/images/head_120X120.png\"}],\"wonderfullist\":[],\"freplyCount\":1}}";

        // 修正 JSON 字符串
        String correctedJson = correctJsonString(incorrectJson);

        // 尝试解析修正后的 JSON 字符串
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Object json = mapper.readValue(correctedJson, Object.class);
//            String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//            System.out.println("Corrected JSON: " + prettyJson);
//        } catch (JsonProcessingException e) {
//            System.out.println("Failed to process JSON: " + e.getMessage());
//        }
    }

    private static String correctJsonString(String json) {
        // 将连字符（例如双引号）替换为一个双引号
        json = json.replaceAll("\"{2,}", "\"");

        // 处理字符串中的特殊字符
        json = json.replaceAll("\\\\n", "\\n");
        json = json.replaceAll("\\\\r", "\\r");
        json = json.replaceAll("\\\\t", "\\t");

        // 确保所有字符串都用双引号括起来
        json = json.replaceAll("\"([^\"]*?)\"(?=:)", "\"$1\"");

        // 删除多余的逗号
        json = json.replaceAll(",(\\s*[}\\]])", "$1");

        // 确保 JSON 字符串正确终止
        if (!json.endsWith("}")) {
            json = json + "}";
        }

        return json;
    }
}
