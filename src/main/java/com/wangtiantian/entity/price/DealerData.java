package com.wangtiantian.entity.price;

import com.wangtiantian.entity.Bean_Factory;

import java.util.Objects;

public class DealerData {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_DealerName; public void set_C_DealerName(String C_DealerName){this.C_DealerName=C_DealerName.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_DealerName(){return C_DealerName;}
    private String  C_DealerID; public void set_C_DealerID(String C_DealerID){this.C_DealerID=C_DealerID.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_DealerID(){return C_DealerID;}
    private String  C_Address; public void set_C_Address(String C_Address){this.C_Address=C_Address.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_Address(){return C_Address;}
    private String  C_SaleBrandName; public void set_C_SaleBrandName(String C_SaleBrandName){this.C_SaleBrandName=C_SaleBrandName.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_SaleBrandName(){return C_SaleBrandName;}
    private int  C_SaleNum; public void set_C_SaleNum(int C_SaleNum){this.C_SaleNum=C_SaleNum;}public int get_C_SaleNum(){return C_SaleNum;}
    private String  C_Phone; public void set_C_Phone(String C_Phone){this.C_Phone=C_Phone.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_Phone(){return C_Phone;}
    private String  C_CityName; public void set_C_CityName(String C_CityName){this.C_CityName=C_CityName.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_CityName(){return C_CityName;}
    private String  C_Type; public void set_C_Type(String C_Type){this.C_Type=C_Type.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_Type(){return C_Type;}
    private String  C_SaleAddress; public void set_C_SaleAddress(String C_SaleAddress){this.C_SaleAddress=C_SaleAddress.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_SaleAddress(){return C_SaleAddress;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}
    private Integer C_IsFinish;
    public Integer get_C_IsFinish(){return C_IsFinish;}
    public void set_C_IsFinish(Integer C_IsFinish){this.C_IsFinish = C_IsFinish;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DealerData that = (DealerData) o;
        return Objects.equals(C_DealerID, that.C_DealerID); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_DealerID); // Use relevant fields
    }
}
