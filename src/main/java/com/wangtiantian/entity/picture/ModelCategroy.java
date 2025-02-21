package com.wangtiantian.entity.picture;

public class ModelCategroy {
    private int C_ID;public int  get_C_ID(){return C_ID;}public void set_C_ID(int C_ID){this.C_ID=C_ID;}
    private String  C_ModelCategoryCode; public void set_C_ModelCategoryCode(String C_ModelCategoryCode){this.C_ModelCategoryCode=C_ModelCategoryCode.replace("\n","").replace("\r","").replace("\t","").replace("'","''").trim();}public String get_C_ModelCategoryCode(){return C_ModelCategoryCode;}
    private String  C_PictureCategoryMoreHtml;public String  get_C_PictureCategoryMoreHtml(){return C_PictureCategoryMoreHtml;}public void set_C_PictureCategoryMoreHtml(String C_PictureCategoryMoreHtml){this.C_PictureCategoryMoreHtml=C_PictureCategoryMoreHtml;}
    private int  C_IsFinish;public int  get_C_IsFinish(){return C_IsFinish;}public void set_C_IsFinish(int C_IsFinish){this.C_IsFinish=C_IsFinish;}
    private int  C_Page;public int  get_C_Page(){return C_Page;}public void set_C_Page(int C_Page){this.C_Page=C_Page;}
    private int  C_Number;public int  get_C_Number(){return C_Number;}public void set_C_Number(int C_Number){this.C_Number=C_Number;}
    private String  C_UpdateTime;public String  get_C_UpdateTime(){return C_UpdateTime;}public void set_C_UpdateTime(String C_UpdateTime){this.C_UpdateTime=C_UpdateTime==null?"-":C_UpdateTime.replace("\t","").replace("\r","").replace("\r\n","").replace("\n","");}



}
