package com.wangtiantian.entity.koubei;

import java.util.Objects;

public class KouBeiInfo {
    // 口碑的基本信息
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ShowID; public void set_C_ShowID(String C_ShowID){this.C_ShowID=C_ShowID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ShowID(){return C_ShowID;}
    private String  C_ModelID;public String  get_C_ModelID(){return C_ModelID;}public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID==null?"-":C_ModelID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_VersionID; public void set_C_VersionID(String C_VersionID){this.C_VersionID=C_VersionID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_VersionID(){return C_VersionID;}
    private String  C_DealerID; public void set_C_DealerID(String C_DealerID){this.C_DealerID=C_DealerID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_DealerID(){return C_DealerID;}
    private String  C_KouBeiUrl;public String  get_C_KouBeiUrl(){return C_KouBeiUrl;}public void set_C_KouBeiUrl(String C_KouBeiUrl){this.C_KouBeiUrl=C_KouBeiUrl==null?"-":C_KouBeiUrl.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KouBeiInfo that = (KouBeiInfo) o;
        return Objects.equals(C_KouBeiUrl,that.C_KouBeiUrl); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_KouBeiUrl); // Use relevant fields
    }
}
