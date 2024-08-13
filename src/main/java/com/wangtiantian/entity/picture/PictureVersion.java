package com.wangtiantian.entity.picture;

public class PictureVersion {
    private int C_ID;public int get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID = C_ID;}
    private int C_IsFinish;public int get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish = C_IsFinish;}
    private String  C_VersionID;public String  get_C_VersionID(){return C_VersionID;}public void set_C_VersionID(String C_VersionID){this.C_VersionID=C_VersionID==null?"-":C_VersionID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_VersionName;public String  get_C_VersionName(){return C_VersionName;}public void set_C_VersionName(String C_VersionName){this.C_VersionName=C_VersionName==null?"-":C_VersionName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","").trim();}
    private String  C_ModelID;public String  get_C_ModelID(){return C_ModelID;}public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID==null?"-":C_ModelID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}    private String  C_ModelName;public String  get_C_ModelName(){return C_ModelName;}public void set_C_ModelName(String C_ModelName){this.C_ModelName=C_ModelName==null?"-":C_ModelName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_FactoryName;public String  get_C_FactoryName(){return C_FactoryName;}public void set_C_FactoryName(String C_FactoryName){this.C_FactoryName=C_FactoryName==null?"-":C_FactoryName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_BrandName;public String  get_C_BrandName(){return C_BrandName;}public void set_C_BrandName(String C_BrandName){this.C_BrandName=C_BrandName==null?"-":C_BrandName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_FactoryID;public String  get_C_FactoryID(){return C_FactoryID;}public void set_C_FactoryID(String C_FactoryID){this.C_FactoryID=C_FactoryID==null?"-":C_FactoryID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_BrandID;public String  get_C_BrandID(){return C_BrandID;}public void set_C_BrandID(String C_BrandID){this.C_BrandID=C_BrandID==null?"-":C_BrandID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
}
