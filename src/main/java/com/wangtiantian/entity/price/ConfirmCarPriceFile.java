package com.wangtiantian.entity.price;

import java.util.Objects;

public class ConfirmCarPriceFile {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_DealerId; public void set_C_DealerId(String C_DealerId){this.C_DealerId=C_DealerId.replace("\n","").replace("\r","").replace("\t","").replace("'","`").trim();}public String get_C_DealerId(){return C_DealerId;}
    private String  C_ModelId; public void set_C_ModelId(String C_ModelId){this.C_ModelId=C_ModelId.replace("\n","").replace("\r","").replace("\t","").replace("'","`").trim();}public String get_C_ModelId(){return C_ModelId;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").replace("'","`").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmCarPriceFile that = (ConfirmCarPriceFile) o;
        return Objects.equals(C_DealerId, that.C_DealerId)&&Objects.equals(C_ModelId, that.C_ModelId); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_DealerId,C_ModelId); // Use relevant fields
    }
}
