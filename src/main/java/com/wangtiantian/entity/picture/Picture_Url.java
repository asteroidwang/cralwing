package com.wangtiantian.entity.picture;

import com.wangtiantian.entity.Bean_Bag;

import java.util.Objects;

public class Picture_Url {
    private int C_ID;public int get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID = C_ID;}
    private int C_IsFinish;public int get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish = C_IsFinish;}
    private String C_VersionId;public String  get_C_VersionId(){return C_VersionId;}public void set_C_VersionId(String C_VersionId){this.C_VersionId =C_VersionId==null?"-":C_VersionId.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","").trim();}
    private String C_FenYeUrl;public String  get_C_FenYeUrl(){return C_FenYeUrl;}public void set_C_FenYeUrl(String C_FenYeUrl){this.C_FenYeUrl =C_FenYeUrl==null?"-":C_FenYeUrl.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","").trim();}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture_Url that = (Picture_Url) o;
        return Objects.equals(C_FenYeUrl, that.C_FenYeUrl); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_FenYeUrl); // Use relevant fields
    }
}
