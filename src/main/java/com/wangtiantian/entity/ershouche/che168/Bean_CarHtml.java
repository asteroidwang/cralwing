package com.wangtiantian.entity.ershouche.che168;

public class Bean_CarHtml {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_CarHtml; public void set_C_CarHtml(String C_CarHtml){this.C_CarHtml=C_CarHtml.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_CarHtml(){return C_CarHtml;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
}
