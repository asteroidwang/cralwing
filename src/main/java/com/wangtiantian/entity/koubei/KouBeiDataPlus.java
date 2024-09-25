package com.wangtiantian.entity.koubei;

public class KouBeiDataPlus {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ShowID; public void set_C_ShowID(String C_ShowID){this.C_ShowID=C_ShowID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ShowID(){return C_ShowID;}
    private String  C_KouBeiID; public void set_C_KouBeiID(String C_KouBeiID){this.C_KouBeiID=C_KouBeiID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_KouBeiID(){return C_KouBeiID;}
    private String  C_Content_追加; public void set_C_Content_追加(String C_Content_追加){this.C_Content_追加=C_Content_追加.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_Content_追加(){return C_Content_追加;}
    private String  C_购车后追评时间间隔; public void set_C_购车后追评时间间隔(String C_购车后追评时间间隔){this.C_购车后追评时间间隔=C_购车后追评时间间隔.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_购车后追评时间间隔(){return C_购车后追评时间间隔;}
    private String  C_追评时间; public void set_C_追评时间(String C_追评时间){this.C_追评时间=C_追评时间.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_追评时间(){return C_追评时间;}
    private String  C_Html; public void set_C_Html(String C_Html){this.C_Html=C_Html.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_Html(){return C_Html;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}
}
