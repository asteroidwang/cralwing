package com.asteroid.dao;

public class T_Dao_Config {
    public static String method_汉字数字转化成阿拉伯数字(String yuanString) {
        String tempString = "";
        for (int k = 0; k < yuanString.length(); k++) {
            String a = String.valueOf(yuanString.charAt(k));
            switch (a) {
                case "十":
                    tempString = yuanString.replace("十", "1");
                    break;
                case "九":
                    tempString = yuanString.replace("九", "9");
                    break;
                case "八":
                    tempString = yuanString.replace("八", "8");
                    break;
                case "七":
                    tempString = yuanString.replace("七", "7");
                    break;
                case "六":
                    tempString = yuanString.replace("六", "6");
                    break;
                case "五":
                    tempString = yuanString.replace("五", "5");
                    break;
                case "四":
                    tempString = yuanString.replace("四", "4");
                    break;
                case "三":
                    tempString = yuanString.replace("三", "3");
                    break;
                case "二":
                    tempString = yuanString.replace("二", "2");
                    break;
                case "一":
                    tempString = yuanString.replace("一", "1");
            }
        }
        return tempString;
    }
}
