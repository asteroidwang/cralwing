package com.wangtiantian.entity.ershouche.yiChe;

public class YiChe_ConfirmDetails {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ucarId; public void set_C_ucarId(String C_ucarId){this.C_ucarId=C_ucarId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ucarId(){return C_ucarId;}
    private String C_CityPinYin;public String get_C_CityPinYin(){return C_CityPinYin;}public void set_C_CityPinYin(String C_CityPinYin){this.C_CityPinYin = C_CityPinYin;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
}
