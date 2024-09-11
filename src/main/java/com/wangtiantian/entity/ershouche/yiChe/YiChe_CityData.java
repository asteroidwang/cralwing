package com.wangtiantian.entity.ershouche.yiChe;

public class YiChe_CityData {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ParentId; public void set_C_ParentId(String C_ParentId){this.C_ParentId=C_ParentId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ParentId(){return C_ParentId;}
    private String  C_CityId; public void set_C_CityId(String C_CityId){this.C_CityId=C_CityId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_CityId(){return C_CityId;}
    private String  C_CityLevel; public void set_C_CityLevel(String C_CityLevel){this.C_CityLevel=C_CityLevel.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_CityLevel(){return C_CityLevel;}
    private String  C_ParentRegionId; public void set_C_ParentRegionId(String C_ParentRegionId){this.C_ParentRegionId=C_ParentRegionId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ParentRegionId(){return C_ParentRegionId;}
    private String  C_EngName; public void set_C_EngName(String C_EngName){this.C_EngName=C_EngName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_EngName(){return C_EngName;}
    private String  C_CityName; public void set_C_CityName(String C_CityName){this.C_CityName=C_CityName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_CityName(){return C_CityName;}
    private String  C_RegionId; public void set_C_RegionId(String C_RegionId){this.C_RegionId=C_RegionId.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_RegionId(){return C_RegionId;}
    private String  C_Initial; public void set_C_Initial(String C_Initial){this.C_Initial=C_Initial.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_Initial(){return C_Initial;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
}
