package com.wangtiantian.entity;

import java.util.Objects;

public class Bean_Version {
        private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
        private String  C_VersionID;public String  get_C_VersionID(){return C_VersionID;}public void set_C_VersionID(String C_VersionID){this.C_VersionID=C_VersionID==null?"-":C_VersionID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_VersionName;public String  get_C_VersionName(){return C_VersionName;}public void set_C_VersionName(String C_VersionName){this.C_VersionName=C_VersionName==null?"-":C_VersionName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_VersionURL;public String  get_C_VersionURL(){return C_VersionURL;}public void set_C_VersionURL(String C_VersionURL){this.C_VersionURL=C_VersionURL==null?"-":C_VersionURL.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_VersionStatus;public String  get_C_VersionStatus(){return C_VersionStatus;}public void set_C_VersionStatus(String C_VersionStatus){this.C_VersionStatus=C_VersionStatus==null?"-":C_VersionStatus.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_ModelID;public String  get_C_ModelID(){return C_ModelID;}public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID==null?"-":C_ModelID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private int  C_Group;public int  get_C_Group(){return C_Group;}public void set_C_Group(int C_Group){this.C_Group=C_Group;}
        private int  params;public int  get_params(){return params;}public void set_params(int params){this.params=params;}
        private int  config;public int  get_config(){return config;}public void set_config(int config){this.config=config;}
        private int  bag;public int  get_bag(){return bag;}public void set_bag(int bag){this.bag=bag;}
//        private int  C_是否下载;public int  get_C_是否下载(){return C_是否下载;}public void set_C_是否下载(int C_是否下载){this.C_是否下载=C_是否下载;}
        private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Bean_Version that = (Bean_Version) o;
                return Objects.equals(C_VersionID, that.C_VersionID); // Compare relevant fields
        }

        @Override
        public int hashCode() {
                return Objects.hash(C_VersionID); // Use relevant fields
        }
}
