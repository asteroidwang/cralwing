package com.wangtiantian.entity.price;

import java.util.Objects;

public class SaleModData {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_DealerID; public void set_C_DealerID(String C_DealerID){this.C_DealerID=C_DealerID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_DealerID(){return C_DealerID;}
    private String  C_FactoryID; public void set_C_FactoryID(String C_FactoryID){this.C_FactoryID=C_FactoryID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_FactoryID(){return C_FactoryID;}
    private String  C_ModelID; public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ModelID(){return C_ModelID;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}
    private String  C_PriceDataUrl; public void set_C_PriceDataUrl(String C_PriceDataUrl){this.C_PriceDataUrl=C_PriceDataUrl.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_PriceDataUrl(){return C_PriceDataUrl;}
    private String  C_数据来源; public void set_C_数据来源(String C_数据来源){this.C_数据来源=C_数据来源.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_数据来源(){return C_数据来源;}
    private Integer C_IsFinish;public Integer get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(Integer C_IsFinish){this.C_IsFinish = C_IsFinish;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleModData that = (SaleModData) o;
        return Objects.equals(C_DealerID, that.C_DealerID)&&Objects.equals(C_ModelID, that.C_ModelID); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_DealerID,C_ModelID); // Use relevant fields
    }
}
