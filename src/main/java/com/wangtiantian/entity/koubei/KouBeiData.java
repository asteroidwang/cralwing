package com.wangtiantian.entity.koubei;

import com.wangtiantian.entity.price.ModelDealerData;

import java.util.Objects;

public class KouBeiData {
    // 口碑具体数据
    private int  C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ShowID; public void set_C_ShowID(String C_ShowID){this.C_ShowID=C_ShowID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ShowID(){return C_ShowID;}
    private String  C_DealerID; public void set_C_DealerID(String C_DealerID){this.C_DealerID=C_DealerID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_DealerID(){return C_DealerID;}
    private String  C_DealerName; public void set_C_DealerName(String C_DealerName){this.C_DealerName=C_DealerName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_DealerName(){return C_DealerName;}
    private String  C_KouBeiContent; public void set_C_KouBeiContent(String C_KouBeiContent){this.C_KouBeiContent=C_KouBeiContent.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_KouBeiContent(){return C_KouBeiContent;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}
    private String  C_UserName; public void set_C_UserName(String C_UserName){this.C_UserName=C_UserName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UserName(){return C_UserName;}
    private String  C_UserID; public void set_C_UserID(String C_UserID){this.C_UserID=C_UserID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UserID(){return C_UserID;}
    private String  C_VersionID; public void set_C_VersionID(String C_VersionID){this.C_VersionID=C_VersionID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_VersionID(){return C_VersionID;}
    private String  C_VersionName; public void set_C_VersionName(String C_VersionName){this.C_VersionName=C_VersionName.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_VersionName(){return C_VersionName;}
    private String  C_ModelID; public void set_C_ModelID(String C_ModelID){this.C_ModelID=C_ModelID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ModelID(){return C_ModelID;}
    private String  C_UpTime; public void set_C_UpTime(String C_UpTime){this.C_UpTime=C_UpTime.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_UpTime(){return C_UpTime;}
    private String  C_KoubeiID; public void set_C_KoubeiID(String C_KoubeiID){this.C_KoubeiID=C_KoubeiID.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_KoubeiID(){return C_KoubeiID;}
    private String  C_Title; public void set_C_Title(String C_Title){this.C_Title=C_Title.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_Title(){return C_Title;}
    private String  C_XingShiLiCheng; public void set_C_XingShiLiCheng(String C_XingShiLiCheng){this.C_XingShiLiCheng=C_XingShiLiCheng.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_XingShiLiCheng(){return C_XingShiLiCheng;}
    private String  C_BaiGongLiYouHao; public void set_C_BaiGongLiYouHao(String C_BaiGongLiYouHao){this.C_BaiGongLiYouHao=C_BaiGongLiYouHao.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_BaiGongLiYouHao(){return C_BaiGongLiYouHao;}
    private String  C_LuoCheGouMaiJia; public void set_C_LuoCheGouMaiJia(String C_LuoCheGouMaiJia){this.C_LuoCheGouMaiJia=C_LuoCheGouMaiJia.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_LuoCheGouMaiJia(){return C_LuoCheGouMaiJia;}
    private String  C_GouMaiShiJian; public void set_C_GouMaiShiJian(String C_GouMaiShiJian){this.C_GouMaiShiJian=C_GouMaiShiJian.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_GouMaiShiJian(){return C_GouMaiShiJian;}
    private String  C_GouMaiDiDian; public void set_C_GouMaiDiDian(String C_GouMaiDiDian){this.C_GouMaiDiDian=C_GouMaiDiDian.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_GouMaiDiDian(){return C_GouMaiDiDian;}
    private String  C_ZuiManYi; public void set_C_ZuiManYi(String C_ZuiManYi){this.C_ZuiManYi=C_ZuiManYi.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ZuiManYi(){return C_ZuiManYi;}
    private String  C_ZuiBuManyi; public void set_C_ZuiBuManyi(String C_ZuiBuManyi){this.C_ZuiBuManyi=C_ZuiBuManyi.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ZuiBuManyi(){return C_ZuiBuManyi;}
    private String  C_CaoKong; public void set_C_CaoKong(String C_CaoKong){this.C_CaoKong=C_CaoKong.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_CaoKong(){return C_CaoKong;}
    private String  C_ShuShiXing; public void set_C_ShuShiXing(String C_ShuShiXing){this.C_ShuShiXing=C_ShuShiXing.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ShuShiXing(){return C_ShuShiXing;}
    private String  C_NeiShi; public void set_C_NeiShi(String C_NeiShi){this.C_NeiShi=C_NeiShi.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_NeiShi(){return C_NeiShi;}
    private String  C_YouHao; public void set_C_YouHao(String C_YouHao){this.C_YouHao=C_YouHao.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_YouHao(){return C_YouHao;}
    private String  C_XingJiaBi; public void set_C_XingJiaBi(String C_XingJiaBi){this.C_XingJiaBi=C_XingJiaBi.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_XingJiaBi(){return C_XingJiaBi;}
    private String  C_KongJian; public void set_C_KongJian(String C_KongJian){this.C_KongJian=C_KongJian.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_KongJian(){return C_KongJian;}
    private String  C_WaiGuan; public void set_C_WaiGuan(String C_WaiGuan){this.C_WaiGuan=C_WaiGuan.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_WaiGuan(){return C_WaiGuan;}
    private String  C_JiaShiGanShou; public void set_C_JiaShiGanShou(String C_JiaShiGanShou){this.C_JiaShiGanShou=C_JiaShiGanShou.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_JiaShiGanShou(){return C_JiaShiGanShou;}
    private int C_IsFinish;public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish = C_IsFinish;}public int C_IsFinish(){return C_IsFinish;}

    @Override
    public String toString() {
        return "KouBeiData{" +
                "C_ID=" + C_ID +
                ", C_ShowID='" + C_ShowID + '\'' +
                ", C_DealerID='" + C_DealerID + '\'' +
                ", C_DealerName='" + C_DealerName + '\'' +
                ", C_KouBeiContent='" + C_KouBeiContent + '\'' +
                ", C_UpdateTime='" + C_UpdateTime + '\'' +
                ", C_UserName='" + C_UserName + '\'' +
                ", C_UserID='" + C_UserID + '\'' +
                ", C_VersionID='" + C_VersionID + '\'' +
                ", C_VersionName='" + C_VersionName + '\'' +
                ", C_ModelID='" + C_ModelID + '\'' +
                ", C_UpTime='" + C_UpTime + '\'' +
                ", C_KoubeiID='" + C_KoubeiID + '\'' +
                ", C_Title='" + C_Title + '\'' +
                ", C_XingShiLiCheng='" + C_XingShiLiCheng + '\'' +
                ", C_BaiGongLiYouHao='" + C_BaiGongLiYouHao + '\'' +
                ", C_LuoCheGouMaiJia='" + C_LuoCheGouMaiJia + '\'' +
                ", C_GouMaiShiJian='" + C_GouMaiShiJian + '\'' +
                ", C_GouMaiDiDian='" + C_GouMaiDiDian + '\'' +
                ", C_ZuiManYi='" + C_ZuiManYi + '\'' +
                ", C_ZuiBuManyi='" + C_ZuiBuManyi + '\'' +
                ", C_CaoKong='" + C_CaoKong + '\'' +
                ", C_ShuShiXing='" + C_ShuShiXing + '\'' +
                ", C_NeiShi='" + C_NeiShi + '\'' +
                ", C_YouHao='" + C_YouHao + '\'' +
                ", C_XingJiaBi='" + C_XingJiaBi + '\'' +
                ", C_KongJian='" + C_KongJian + '\'' +
                ", C_WaiGuan='" + C_WaiGuan + '\'' +
                ", C_JiaShiGanShou='" + C_JiaShiGanShou + '\'' +
                ", C_IsFinish=" + C_IsFinish +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KouBeiData that = (KouBeiData) o;
        return Objects.equals(C_KoubeiID, that.C_KoubeiID)&&Objects.equals(C_ShowID, that.C_ShowID)&&Objects.equals(C_VersionID,that.C_VersionID); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_KoubeiID,C_ShowID,C_VersionID); // Use relevant fields
    }
}
