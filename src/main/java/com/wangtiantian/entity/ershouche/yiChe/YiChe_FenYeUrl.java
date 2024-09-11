package com.wangtiantian.entity.ershouche.yiChe;

public class YiChe_FenYeUrl {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String C_FenYeUrl;public String get_C_FenYeUrl(){return C_FenYeUrl;}public void set_C_FenYeUrl(String C_FenYeUrl){this.C_FenYeUrl = C_FenYeUrl;}
    private String C_CityPinYin;public String get_C_CityPinYin(){return C_CityPinYin;}public void set_C_CityPinYin(String C_CityPinYin){this.C_CityPinYin = C_CityPinYin;}
    private int  C_Page;public int  get_C_Page(){return C_Page;}public void set_C_Page(int C_Page){this.C_Page=C_Page;}
    private int  C_PageCount;public int  get_C_PageCount(){return C_PageCount;}public void set_C_PageCount(int C_PageCount){this.C_PageCount=C_PageCount;}
    private String C_CountCar;public String  get_C_CountCar(){return C_CountCar;}public void set_C_CountCar(String C_CountCar){this.C_CountCar=C_CountCar;}
    private int C_IsFinish;public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish = C_IsFinish;}public int get_C_IsFinish(){return C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_EngName; public void set_C_EngName(String C_EngName){this.C_EngName=C_EngName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_EngName(){return C_EngName;}
    private String  C_CityName; public void set_C_CityName(String C_CityName){this.C_CityName=C_CityName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_CityName(){return C_CityName;}
    private String  C_RegionId; public void set_C_RegionId(String C_RegionId){this.C_RegionId=C_RegionId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_RegionId(){return C_RegionId;}
    private String  C_CityId; public void set_C_CityId(String C_CityId){this.C_CityId=C_CityId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_CityId(){return C_CityId;}
}
