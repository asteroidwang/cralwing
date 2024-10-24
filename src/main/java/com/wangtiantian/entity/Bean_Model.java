package com.wangtiantian.entity;

import java.util.Objects;

public class Bean_Model {
        private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
        private String  C_ModelID;public String  get_C_ModelID(){return C_ModelID;}public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID==null?"-":C_ModelID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_ModelName;public String  get_C_ModelName(){return C_ModelName;}public void set_C_ModelName(String C_ModelName){this.C_ModelName=C_ModelName==null?"-":C_ModelName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_ModelURL;public String  get_C_ModelURL(){return C_ModelURL;}public void set_C_ModelURL(String C_ModelURL){this.C_ModelURL=C_ModelURL==null?"-":C_ModelURL.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
//        private String  C_ModelStatus;public String  get_C_ModelStatus(){return C_ModelStatus;}public void set_C_ModelStatus(String C_ModelStatus){this.C_ModelStatus=C_ModelStatus==null?"-":C_ModelStatus.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_FactoryID;public String  get_C_FactoryID(){return C_FactoryID;}public void set_C_FactoryID(String C_FactoryID){this.C_FactoryID=C_FactoryID==null?"-":C_FactoryID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_BrandID;public String  get_C_BrandID(){return C_BrandID;}public void set_C_BrandID(String C_BrandID){this.C_BrandID=C_BrandID==null?"-":C_BrandID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
        private int  C_在售;public int  get_C_在售(){return C_在售;}public void set_C_在售(int C_在售){this.C_在售=C_在售;}
        private int  C_停售;public int  get_C_停售(){return C_停售;}public void set_C_停售(int C_停售){this.C_停售=C_停售;}
        private int  C_即将销售;public int  get_C_即将销售(){return C_即将销售;}public void set_C_即将销售(int C_即将销售){this.C_即将销售=C_即将销售;}
        private int  C_图片页面在售;public int  get_C_图片页面在售(){return C_图片页面在售;}public void set_C_图片页面在售(int C_图片页面在售){this.C_图片页面在售=C_图片页面在售;}
        private int  C_图片页面停售;public int  get_C_图片页面停售(){return C_图片页面停售;}public void set_C_图片页面停售(int C_图片页面停售){this.C_图片页面停售=C_图片页面停售;}
        private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Bean_Model that = (Bean_Model) o;
                return Objects.equals(C_ModelID, that.C_ModelID)&&Objects.equals(C_BrandID,that.C_BrandID)&&Objects.equals(C_FactoryID,that.C_FactoryID); // Compare relevant fields
        }

        @Override
        public int hashCode() {
                return Objects.hash(C_ModelID,C_BrandID,C_FactoryID); // Use relevant fields
        }
}
