package com.wangtiantian.dao;

import java.util.ArrayList;

public class T_Config_KouBei extends T_Config_Father {
    public T_Config_KouBei(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }

    public void method修改车型口碑页面的下载状态(String url) {
        method_i_d_u("update " + tableName + " set C_IsFinish = 1 where C_ModelKouBeiUrl= '" + url + "'");
    }

    public ArrayList<Object> getUrl未下载() {
        return method_有条件的查询("select * from  " + tableName + " where C_IsFinish = 0 ");
    }

    public ArrayList<Object> getUrl未下载的ShowID() {
        return method_有条件的查询("select C_ShowID,C_KouBeiUrl from  " + tableName + " where C_IsFinish = 0 ");
    }

    public ArrayList<Object> getUrlPageCount不为0() {
        return method_有条件的查询("select * from  " + tableName + " where C_IsFinish = 0 and C_CountPage!=0");
    }

    public void method修改具体口碑页面的下载状态(String url) {
        method_i_d_u("update " + tableName + " set C_IsFinish = 1 where C_KouBeiUrl in (" + url + ")");
    }

    // 已下载数据
    public ArrayList<Object> getUrl已下载() {
        return method_有条件的查询("select * from  " + tableName + " where C_IsFinish = 1 ");
    }

    public ArrayList<Object> getNoDownLoad(String showIds) {
        return method_有条件的查询("select * from " + tableName + " where C_ShowID not in (" + showIds + ")");
    }


    public ArrayList<Object> method_查找所有未下载回复的口碑id(int begin) {
        return method_有条件的查询("SELECT * FROM " + tableName + " where C_IsFinish = 0 and C_KoubeiID !='-' ORDER BY C_ID OFFSET " + begin + " ROWS FETCH NEXT 10000 ROWS ONLY");
    }

     public  void update修改一级评论的下载状态(String kbId) {
        method_i_d_u("update " + tableName + " set C_IsFinish = 1 where C_KoubeiID  in (" + kbId + ")");
    }
    public ArrayList<Object> method_分页查询未下载的有二级评论的数据10000条每次(int begin) {
        String sql = "SELECT * FROM " + tableName + " where C_IsFinish = 0  and C_replyId !='-' and C_freplyCount_First !='0'  and C_freplyCount_First !='-' ORDER BY C_ID OFFSET " + begin + " ROWS FETCH NEXT 10000 ROWS ONLY";
        return method_有条件的查询(sql);
    }
    public int get_回复表中数据总量(){
        String sql ="select count(*) from "+tableName+ "  where C_IsFinish = 0  and C_replyId !='-' and C_freplyCount_First !='0'  and C_freplyCount_First !='-' ";
       return get_获取表中数据数量_有查询条件(sql);
    }

    public int get_一级评论未完成的数量(){
        String sql ="select count(*) from "+tableName+ "  where C_IsFinish = 0";
        return get_获取表中数据数量_有查询条件(sql);
    }
}
