package com.wangtiantian.entity.picture;

public class PictureHtml {
    private int C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_VersionID;public String  get_C_VersionID(){return C_VersionID;}public void set_C_VersionID(String C_VersionID){this.C_VersionID=C_VersionID==null?"-":C_VersionID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_PictureHtmlCode; public void set_C_PictureHtmlCode(String C_PictureHtmlCode){this.C_PictureHtmlCode=C_PictureHtmlCode.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_PictureHtmlCode(){return C_PictureHtmlCode;}
    private String  C_PictureHtml;public String  get_C_PictureHtml(){return C_PictureHtml;}public void set_C_PictureHtml(String C_PictureHtml){this.C_PictureHtml=C_PictureHtml;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
}
