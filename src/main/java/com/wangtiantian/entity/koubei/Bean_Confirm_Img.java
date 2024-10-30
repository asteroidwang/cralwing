package com.wangtiantian.entity.koubei;

public class Bean_Confirm_Img {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ConcatString; public void set_C_ConcatString(String C_ConcatString){this.C_ConcatString=C_ConcatString.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ConcatString(){return C_ConcatString;}
    private String C_Type;public void set_C_Type(String C_Type){this.C_Type = C_Type;}public String get_C_Type(){return C_Type;}

    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}

}
