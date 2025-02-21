package com.wangtiantian.entity.picture;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureInfo {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_BrandID;public String  get_C_BrandID(){return C_BrandID;}public void set_C_BrandID(String C_BrandID){this.C_BrandID=C_BrandID;}
    private String  C_ModelID;public String  get_C_ModelID(){return C_ModelID;}public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID;}
    private String  C_FactoryID;public String  get_C_FactoryID(){return C_FactoryID;}public void set_C_FactoryID(String C_FactoryID){this.C_FactoryID=C_FactoryID;}
    private String  C_VersionID;public String  get_C_VersionID(){return C_VersionID;}public void set_C_VersionID(String C_VersionID){this.C_VersionID=C_VersionID;}
    private String  C_CategoryCode;public String  get_C_CategoryCode(){return C_CategoryCode;}public void set_C_CategoryCode(String C_CategoryCode){this.C_CategoryCode=C_CategoryCode;}
    private String  C_PictureCode;public String  get_C_PictureCode(){return C_PictureCode;}public void set_C_PictureCode(String C_PictureCode){this.C_PictureCode=C_PictureCode;}
    private String  C_PictureUrl;public String  get_C_PictureUrl(){return C_PictureUrl;}public void set_C_PictureUrl(String C_PictureUrl){this.C_PictureUrl=C_PictureUrl;}
    private String  C_PictureHighUrl;public String  get_C_PictureHighUrl(){return C_PictureHighUrl;}public void set_C_PictureHighUrl(String C_PictureHighUrl){this.C_PictureHighUrl=C_PictureHighUrl;}
    private String  C_PictureHtmlCode;public String  get_C_PictureHtmlCode(){return C_PictureHtmlCode;}public void set_C_PictureHtmlCode(String C_PictureHtmlCode){this.C_PictureHtmlCode=C_PictureHtmlCode;}
    private String  C_PictureHtmlUrl;public String  get_C_PictureHtmlUrl(){return C_PictureHtmlUrl;}public void set_C_PictureHtmlUrl(String C_PictureHtmlUrl){this.C_PictureHtmlUrl=C_PictureHtmlUrl;}
    private int  C_IsHigh;public int  get_C_IsHigh(){return C_IsHigh;}public void set_C_IsHigh(int C_IsHigh){this.C_IsHigh=C_IsHigh;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());}
}
