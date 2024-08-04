package com.wangtiantian.entity.koubei;

import com.wangtiantian.entity.Bean_Model;

import java.util.Objects;

public class ModelKouBei {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ModelID;public String  get_C_ModelID(){return C_ModelID;}public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID==null?"-":C_ModelID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_ModelKouBeiUrl;public String  get_C_ModelKouBeiUrl(){return C_ModelKouBeiUrl;}public void set_C_ModelKouBeiUrl(String C_ModelKouBeiUrl){this.C_ModelKouBeiUrl=C_ModelKouBeiUrl==null?"-":C_ModelKouBeiUrl.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private int  C_Page;public int  get_C_Page(){return C_Page;}public void set_C_Page(int C_Page){this.C_Page=C_Page;}
    private int  C_CountPage;public int  get_C_CountPage(){return C_CountPage;}public void set_C_CountPage(int C_CountPage){this.C_CountPage=C_CountPage;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelKouBei that = (ModelKouBei) o;
        return Objects.equals(C_ModelID, that.C_ModelID)&&Objects.equals(C_Page,that.C_Page)&&Objects.equals(C_ModelKouBeiUrl,that.C_ModelKouBeiUrl); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_ModelID,C_Page,C_ModelKouBeiUrl); // Use relevant fields
    }
}
