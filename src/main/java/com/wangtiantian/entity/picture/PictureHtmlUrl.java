package com.wangtiantian.entity.picture;

import java.util.Objects;

public class PictureHtmlUrl {
    private int C_ID;public int get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID = C_ID;}
    private int C_IsFinish;public int get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish = C_IsFinish;}
    private String  C_VersionId; public void set_C_VersionId(String C_VersionId){this.C_VersionId=C_VersionId.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_VersionId(){return C_VersionId;}
    private String  C_ImgId; public void set_C_ImgId(String C_ImgId){this.C_ImgId=C_ImgId.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ImgId(){return C_ImgId;}
    private String  C_BrandId; public void set_C_BrandId(String C_BrandId){this.C_BrandId=C_BrandId.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_BrandId(){return C_BrandId;}
    private String  C_ModelId; public void set_C_ModelId(String C_ModelId){this.C_ModelId=C_ModelId.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ModelId(){return C_ModelId;}
    private String  C_ImgType; public void set_C_ImgType(String C_ImgType){this.C_ImgType=C_ImgType.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_ImgType(){return C_ImgType;}
    private String  C_PictureHtmlUrl; public void set_C_PictureHtmlUrl(String C_PictureHtmlUrl){this.C_PictureHtmlUrl=C_PictureHtmlUrl.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_PictureHtmlUrl(){return C_PictureHtmlUrl;}
    private String  C_UpdateTime; public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_UpdateTime(){return C_UpdateTime;}
    private String  C_FileName; public void set_C_FileName(String C_FileName){this.C_FileName=C_FileName.replace("\n","").replace("\r","").replace("\t","").trim();}public String get_C_FileName(){return C_FileName;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureHtmlUrl that = (PictureHtmlUrl) o;
        return Objects.equals(C_PictureHtmlUrl, that.C_PictureHtmlUrl); // Compare relevant fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(C_PictureHtmlUrl); // Use relevant fields
    }
}
