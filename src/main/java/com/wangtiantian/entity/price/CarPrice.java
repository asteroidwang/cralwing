package com.wangtiantian.entity.price;

import java.util.Objects;

public class CarPrice {
    private int C_ID; public void set_C_ID(int C_ID){this.C_ID=C_ID;}public int get_C_ID(){return C_ID;}
    private String  C_VersionID; public void set_C_VersionID(String C_VersionID){this.C_VersionID=C_VersionID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_VersionID(){return C_VersionID;}
    private String  C_VersionName; public void set_C_VersionName(String C_VersionName){this.C_VersionName=C_VersionName.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_VersionName(){return C_VersionName;}
    private String  C_DealerID; public void set_C_DealerID(String C_DealerID){this.C_DealerID=C_DealerID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_DealerID(){return C_DealerID;}
    private String  C_GroupName; public void set_C_GroupName(String C_GroupName){this.C_GroupName=C_GroupName.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_GroupName(){return C_GroupName;}
    private String  C_ModelID; public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ModelID(){return C_ModelID;}
    private String  C_ModelName; public void set_C_ModelName(String C_ModelName){this.C_ModelName=C_ModelName.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ModelName(){return C_ModelName;}
    private String  C_DealerMaxPrice; public void set_C_DealerMaxPrice(String C_DealerMaxPrice){this.C_DealerMaxPrice=C_DealerMaxPrice.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_DealerMaxPrice(){return C_DealerMaxPrice;}
    private String  C_DealerMinPrice; public void set_C_DealerMinPrice(String C_DealerMinPrice){this.C_DealerMinPrice=C_DealerMinPrice.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_DealerMinPrice(){return C_DealerMinPrice;}
    private String  C_FctMaxPrice; public void set_C_FctMaxPrice(String C_FctMaxPrice){this.C_FctMaxPrice=C_FctMaxPrice.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_FctMaxPrice(){return C_FctMaxPrice;}
    private String  C_FctMinPrice; public void set_C_FctMinPrice(String C_FctMinPrice){this.C_FctMinPrice=C_FctMinPrice.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_FctMinPrice(){return C_FctMinPrice;}
    private String  C_PriceTime; public void set_C_PriceTime(String C_PriceTime){this.C_PriceTime=C_PriceTime.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_PriceTime(){return C_PriceTime;}
    private String  C_SaleState; public void set_C_SaleState(String C_SaleState){this.C_SaleState=C_SaleState.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_SaleState(){return C_SaleState;}
    private String  C_PromotionType; public void set_C_PromotionType(String C_PromotionType){this.C_PromotionType=C_PromotionType.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_PromotionType(){return C_PromotionType;}
    private String  C_NewsID; public void set_C_NewsID(String C_NewsID){this.C_NewsID=C_NewsID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_NewsID(){return C_NewsID;}
    private String  C_NewsPrice; public void set_C_NewsPrice(String C_NewsPrice){this.C_NewsPrice=C_NewsPrice.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_NewsPrice(){return C_NewsPrice;}
    private String  C_ImageUrl; public void set_C_ImageUrl(String C_ImageUrl){this.C_ImageUrl=C_ImageUrl.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ImageUrl(){return C_ImageUrl;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarPrice that = (CarPrice) o;
        return Objects.equals(C_DealerID, that.C_DealerID)&&Objects.equals(C_VersionID, that.C_VersionID); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_DealerID,C_VersionID); // Use relevant fields
    }
}
