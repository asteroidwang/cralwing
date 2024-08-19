package com.wangtiantian.entity.price;

import java.util.Objects;

public class Bean_DealerFenYeUrl {
    private int C_ID; public void set_C_ID(int C_ID){this.C_ID=C_ID;}public int get_C_ID(){return C_ID;}
    private String  C_DealerFenYeUrl; public void set_C_DealerFenYeUrl(String C_DealerFenYeUrl){this.C_DealerFenYeUrl=C_DealerFenYeUrl.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_DealerFenYeUrl(){return C_DealerFenYeUrl;}
    private Integer C_IsFinish;public Integer get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(Integer C_IsFinish){this.C_IsFinish = C_IsFinish;}

    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bean_DealerFenYeUrl that = (Bean_DealerFenYeUrl) o;
        return Objects.equals(C_DealerFenYeUrl, that.C_DealerFenYeUrl); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_DealerFenYeUrl); // Use relevant fields
    }
}
