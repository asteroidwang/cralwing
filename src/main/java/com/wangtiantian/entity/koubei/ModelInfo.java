package com.wangtiantian.entity.koubei;

import com.wangtiantian.entity.Bean_Model;

import java.util.Objects;

public class ModelInfo {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ModelID;public String  get_C_ModelID(){return C_ModelID;}public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID==null?"-":C_ModelID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_ModelName;public String  get_C_ModelName(){return C_ModelName;}public void set_C_ModelName(String C_ModelName){this.C_ModelName=C_ModelName==null?"-":C_ModelName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_FactoryID;public String  get_C_FactoryID(){return C_FactoryID;}public void set_C_FactoryID(String C_FactoryID){this.C_FactoryID=C_FactoryID==null?"-":C_FactoryID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_FactoryName;public String  get_C_FactoryName(){return C_FactoryName;}public void set_C_FactoryName(String C_FactoryName){this.C_FactoryName=C_FactoryName==null?"-":C_FactoryName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_BrandID;public String  get_C_BrandID(){return C_BrandID;}public void set_C_BrandID(String C_BrandID){this.C_BrandID=C_BrandID==null?"-":C_BrandID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_BrandName;public String  get_C_BrandName(){return C_BrandName;}public void set_C_BrandName(String C_BrandName){this.C_BrandName=C_BrandName==null?"-":C_BrandName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private int C_IsFinish;public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish = C_IsFinish;}public int C_IsFinish(){return C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelInfo that = (ModelInfo) o;
        return Objects.equals(C_ModelID, that.C_ModelID)&&Objects.equals(C_BrandID,that.C_BrandID)&&Objects.equals(C_FactoryID,that.C_FactoryID); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_ModelID,C_BrandID,C_FactoryID); // Use relevant fields
    }
}
