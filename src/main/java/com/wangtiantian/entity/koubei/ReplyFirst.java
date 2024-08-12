package com.wangtiantian.entity.koubei;

public class ReplyFirst {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ShowID; public void set_C_ShowID(String C_ShowID){this.C_ShowID=C_ShowID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ShowID(){return C_ShowID;}
    private String C_Next;public void set_C_Next(String C_Next){this.C_Next = C_Next;}public String get_C_Next(){return C_Next;}
    private int C_IsFinish;public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish = C_IsFinish;}public int get_C_IsFinish(){return C_IsFinish;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}

}
