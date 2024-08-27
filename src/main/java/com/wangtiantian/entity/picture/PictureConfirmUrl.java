package com.wangtiantian.entity.picture;

public class PictureConfirmUrl {
    private int C_ID;public int get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID = C_ID;}
    private String  C_ImgUrl; public void set_C_ImgUrl(String C_ImgUrl){this.C_ImgUrl=C_ImgUrl.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ImgUrl(){return C_ImgUrl;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}

}
