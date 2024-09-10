package com.wangtiantian.entity.koubei;

public class ConfirmKouBeiPicture {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ShowID; public void set_C_ShowID(String C_ShowID){this.C_ShowID=C_ShowID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ShowID(){return C_ShowID;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}

}
