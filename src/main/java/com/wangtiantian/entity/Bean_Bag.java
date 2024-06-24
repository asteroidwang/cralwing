package com.wangtiantian.entity;

import java.util.Objects;

public class Bean_Bag {
    private int C_ID;

    public int get_C_ID() {
        return C_ID;
    }

    public void set_C_ID(int C_ID) {
        this.C_ID = C_ID;
    }

    private String C_PID;

    public String get_C_PID() {
        return C_PID;
    }

    public void set_C_PID(String C_PID) {
        this.C_PID = C_PID.replace("\t", "").replace("\n", "").replace("\r", "").replace("\r\n", "");
    }

    private String C_BagID;

    public String get_C_BagID() {
        return C_BagID;
    }

    public void set_C_BagID(String C_BagID) {
        this.C_BagID = C_BagID.replace("\t", "").replace("\n", "").replace("\r", "").replace("\r\n", "");
    }

    private String C_Price;

    public String get_C_Price() {
        return C_Price;
    }

    public void set_C_Price(String C_Price) {
        this.C_Price = C_Price.replace("\t", "").replace("\n", "").replace("\r", "").replace("\r\n", "");
    }

    private String C_Name;

    public String get_C_Name() {
        return C_Name;
    }

    public void set_C_Name(String C_Name) {
        this.C_Name = C_Name.replace("\t", "").replace("\n", "").replace("\r", "").replace("\r\n", "");
    }

    private String C_PriceDesc;

    public String get_C_PriceDesc() {
        return C_PriceDesc;
    }

    public void set_C_PriceDesc(String C_PriceDesc) {
        this.C_PriceDesc = C_PriceDesc.replace("\t", "").replace("\n", "").replace("\r", "").replace("\r\n", "");
    }

    private String C_Description;

    public String get_C_Description() {
        return C_Description;
    }

    public void set_C_Description(String C_Description) {
        this.C_Description = C_Description.replace("\n", ";").replace("\t", ";").replace("\r\n", ";").replace("\r", ";").replace("\n", ";");
    }

    private String C_UpdateTime;

    public String get_C_UpdateTime() {
        return C_UpdateTime;
    }

    public void set_C_UpdateTime(String C_UpdateTime) {
        this.C_UpdateTime = C_UpdateTime == null ? "-" : C_UpdateTime.replace("\t", "").replace("\r", "").replace("\r\n", "").replace("\n", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bean_Bag that = (Bean_Bag) o;
        return Objects.equals(C_PID, that.C_PID) && Objects.equals(C_BagID, that.C_BagID); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_PID, C_BagID); // Use relevant fields
    }
}
