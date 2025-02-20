package com.wangtiantian.entity;

public class Bean_Params {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_PID;public void set_C_PID(String C_PID){this.C_PID=C_PID;}public String  get_C_PID(){return C_PID;}    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
}
