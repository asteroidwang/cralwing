package com.wangtiantian.entity.picture;

public class PictureHtmlFileData {
    private int C_ID;public int get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID = C_ID;}
    private String  C_PictureHtmlUrl; public void set_C_PictureHtmlUrl(String C_PictureHtmlUrl){this.C_PictureHtmlUrl=C_PictureHtmlUrl.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_PictureHtmlUrl(){return C_PictureHtmlUrl;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}

}
