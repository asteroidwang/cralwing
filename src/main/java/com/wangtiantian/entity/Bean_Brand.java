package com.wangtiantian.entity;

import java.util.Objects;

public class Bean_Brand {
        private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
        private String  C_BrandID;public String  get_C_BrandID(){return C_BrandID;}public void set_C_BrandID(String C_BrandID){this.C_BrandID=C_BrandID==null?"-":C_BrandID.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_BrandName;public String  get_C_BrandName(){return C_BrandName;}public void set_C_BrandName(String C_BrandName){this.C_BrandName=C_BrandName==null?"-":C_BrandName.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_BrandURL;public String  get_C_BrandURL(){return C_BrandURL;}public void set_C_BrandURL(String C_BrandURL){this.C_BrandURL=C_BrandURL==null?"-":C_BrandURL.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private int  factoryNumber;public int  get_factoryNumber(){return factoryNumber;}public void set_C_FactoryNumber(int factoryNumber){this.factoryNumber=factoryNumber;}
        private String  C_BrandLogo;public String  get_C_BrandLogo(){return C_BrandLogo;}public void set_C_BrandLogo(String C_BrandLogo){this.C_BrandLogo=C_BrandLogo==null?"-":C_BrandLogo.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Bean_Brand that = (Bean_Brand) o;
                return Objects.equals(C_BrandID, that.C_BrandID); // Compare relevant fields
        }

        @Override
        public int hashCode() {
                return Objects.hash(C_BrandID); // Use relevant fields
        }
}
