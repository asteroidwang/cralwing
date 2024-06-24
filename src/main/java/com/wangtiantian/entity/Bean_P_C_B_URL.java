package com.wangtiantian.entity;

public class Bean_P_C_B_URL {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private int  C_Group;public int  get_C_Group(){return C_Group;}public void set_C_Group(int C_Group){this.C_Group=C_Group;}
    private String  C_VersionIds;public String  get_C_VersionIds(){return C_VersionIds;}public void set_C_VersionIds(String C_VersionIds){this.C_VersionIds=C_VersionIds==null?"-":C_VersionIds.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
}
