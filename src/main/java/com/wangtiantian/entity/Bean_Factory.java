package com.wangtiantian.entity;

import java.util.Objects;

public class Bean_Factory {

        private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
        private String  C_FactoryID;public String  get_C_FactoryID(){return C_FactoryID;}public void set_C_FactoryID(String C_FactoryID){this.C_FactoryID=C_FactoryID==null?"-":C_FactoryID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_FactoryName;public String  get_C_FactoryName(){return C_FactoryName;}public void set_C_FactoryName(String C_FactoryName){this.C_FactoryName=C_FactoryName==null?"-":C_FactoryName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_FactoryURL;public String  get_C_FactoryURL(){return C_FactoryURL;}public void set_C_FactoryURL(String C_FactoryURL){this.C_FactoryURL=C_FactoryURL==null?"-":C_FactoryURL.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_BrandID;public String  get_C_BrandID(){return C_BrandID;}public void set_C_BrandID(String C_BrandID){this.C_BrandID=C_BrandID==null?"-":C_BrandID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private int  modNumber;public int  get_modNumber(){return modNumber;}public void set_modNumber(int modNumber){this.modNumber=modNumber;}
        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Bean_Factory that = (Bean_Factory) o;
                return Objects.equals(C_FactoryID, that.C_FactoryID)&&Objects.equals(C_BrandID,that.C_BrandID); // Compare relevant fields
        }

        @Override
        public int hashCode() {
                return Objects.hash(C_FactoryID,C_BrandID); // Use relevant fields
        }

}
