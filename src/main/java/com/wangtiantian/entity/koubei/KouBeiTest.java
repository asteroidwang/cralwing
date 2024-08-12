package com.wangtiantian.entity.koubei;

public class KouBeiTest {
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String C_KoubeiId; public void set_C_KoubeiID(String C_KoubeiID){this.C_KoubeiId =C_KoubeiID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_KoubeiID(){return C_KoubeiId;}

}
