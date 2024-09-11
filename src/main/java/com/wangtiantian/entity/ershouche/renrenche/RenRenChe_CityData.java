package com.wangtiantian.entity.ershouche.renrenche;

public class RenRenChe_CityData {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_quanpin; public void set_C_quanpin(String C_quanpin){this.C_quanpin=C_quanpin.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_quanpin(){return C_quanpin;}
    private String  C_name; public void set_C_name(String C_name){this.C_name=C_name.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_name(){return C_name;}
    private String  C_CityId; public void set_C_CityId(String C_CityId){this.C_CityId=C_CityId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_CityId(){return C_CityId;}    private String  C_listName; public void set_C_listName(String C_listName){this.C_listName=C_listName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_listName(){return C_listName;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
}
