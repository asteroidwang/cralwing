package com.wangtiantian.entity.koubei;

public class KouBeiPicture {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ShowID; public void set_C_ShowID(String C_ShowID){this.C_ShowID=C_ShowID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ShowID(){return C_ShowID;}
    private String  C_PictureUrl; public void set_C_PictureUrl(String C_PictureUrl){this.C_PictureUrl=C_PictureUrl.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_PictureUrl(){return C_PictureUrl;}
    private int C_IsFinish;public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish = C_IsFinish;}public int get_C_IsFinish(){return C_IsFinish;}
    private int C_NumCount;public void set_C_NumCount(int C_NumCount){this.C_NumCount = C_NumCount;}public int get_C_NumCount(){return C_NumCount;}
    private String  C_Position; public void set_C_Position(String C_Position){this.C_Position=C_Position.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_Position(){return C_Position;}
    private String  C_KouBeiID; public void set_C_KouBeiID(String C_KouBeiID){this.C_KouBeiID=C_KouBeiID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_KouBeiID(){return C_KouBeiID;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}

}
