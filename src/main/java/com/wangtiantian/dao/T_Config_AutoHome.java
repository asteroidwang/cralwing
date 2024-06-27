package com.wangtiantian.dao;

import com.wangtiantian.dao.T_Config_Father;
import com.wangtiantian.entity.Bean_Version;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class T_Config_AutoHome extends T_Config_Father {
    public T_Config_AutoHome(int chooseDataBaseType,int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType,chooseDataBase, chooseTable);
    }

    public ArrayList<Object> method_有条件的查询(String sql) {
//        System.out.println(sql);
        ArrayList<Object> result = new ArrayList<>();
        try {
            method_连接数据库();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                Class c = Class.forName(packBag);
                Object o = c.newInstance();
                ResultSetMetaData rsmd = resultSet.getMetaData();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String cname = rsmd.getColumnName(i + 1);
                    Object cobj = resultSet.getObject(i + 1);
                    Field f = c.getDeclaredField(cname);
                    f.setAccessible(true);
                    f.set(o, cobj);
                }
                result.add(o);
            }
            resultSet.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Boolean method_是否插入数据(String sql) {
        ArrayList<Object> result = method_有条件的查询(sql);
        if (result.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int method_是否删除数据(String sql) {
        ArrayList<Object> result = method_有条件的查询(sql);
        return result.size();
    }

    public void method_更新品牌数据(Object o, String id) {
        String sql = "select * from "+tableName+" where C_BrandID=" + id;
        Boolean isInsert = method_是否插入数据(sql);
        if (isInsert) {
            method_新增(o);
        } else {
            String wordsList = method_更新数据(o);
            sql = "update " + tableName + " set " + wordsList + " where C_BrandID = " + id;
            method_i_d_u(sql);
        }
    }

    public void method_更新厂商数据(Object o, String id, String id2) {
        String sql = "select * from " + tableName + " where C_FactoryID='" + id + "' and C_BrandID='" + id2 + "'";
        Boolean isInsert = method_是否插入数据(sql);
        if (isInsert) {
            method_新增(o);
        } else {
            String wordsList = method_更新数据(o);
            sql = "update " + tableName + " set " + wordsList + " where C_FactoryID='" + id + "' and C_BrandID='" + id2 + "'";
            method_i_d_u(sql);
        }
    }

    public void method_更新车系数据(Object o, String id, String id2, String id3) {
        String sql = "select * from " + tableName + " where C_ModelID='" + id + "' and C_BrandID='" + id2 + "' and C_FactoryID='" + id3 + "'";
        Boolean isInsert = method_是否插入数据(sql);
        if (isInsert) {
            method_新增(o);
        } else {
            String wordsList = method_更新数据(o);
            sql = "update " + tableName + " set " + wordsList + " where C_ModelID='" + id + "' and C_BrandID='" + id2 + "' and C_FactoryID='" + id3 + "'";
            method_i_d_u(sql);
        }
    }

    public void method_更新版本数据(Object o, String id, String id2) {
        String sql = "select * from " + tableName + " where C_VersionID='" + id + "' and C_ModelID='" + id2 + "'";
        Boolean isInsert = method_是否插入数据(sql);
        if (isInsert) {
            method_新增(o);
//            System.out.println("插入");
        } else{
            String wordsList = method_更新数据(o);
            sql = "update " + tableName + " set " + wordsList + " where C_VersionID='" + id + "' and C_ModelID='" + id2 + "'";
            method_i_d_u(sql);
//            System.out.println("更新");
        }
    }

    public void method_更新Params数据(Object o, String id) {
        String sql = "select * from " + tableName + " where C_PID=" + id;
        Boolean isInsert = method_是否插入数据(sql);
//        System.out.println(isInsert+"\t"+sql);
        if (isInsert) {
            method_新增(o);
        }
//        else {
//            String wordsList = method_更新数据(o);
//            sql = "update " + tableName + " set " + wordsList + " where C_PID = " + id;
//            method_i_d_u(sql);
//        }
    }

    public void method_更新config数据(Object o, String id) {
        String sql = "select * from " + tableName + " where C_PID='" + id+"'";
        Boolean isInsert = method_是否插入数据(sql);
        if (isInsert) {
            method_新增(o);
        }
//        else {
//            String wordsList = method_更新数据(o);
//            sql = "update " + tableName + " set " + wordsList + " where C_PID = '" + id+"'";
//            method_i_d_u(sql);
//        }
    }

    public void method_更新bag数据(Object o, String id, String id2) {
        String sql = "select * from " + tableName + " where C_PID='" + id + "' and C_BagID='" + id2+"'";
        Boolean isInsert = method_是否插入数据(sql);
        System.out.println(isInsert+"\t"+sql);
        if (isInsert) {
            method_新增(o);
        }
//        else {
//            String wordsList = method_更新数据(o);
//            sql = "update " + tableName + " set " + wordsList + " where C_PID=" + id + " and C_BagID=" + id2;
//            method_i_d_u(sql);
//        }
    }
    public void method_更新图片URL数据(Object o, String id, String id2,String picType) {
        String sql = "select * from " + tableName + " where C_PicID='" + id + "' and C_PID='" + id2+"'"+" and C_PicType='"+picType+"'";
        Boolean isInsert = method_是否插入数据(sql);
//        System.out.println(sql);
//        System.out.println(isInsert);
        if (isInsert) {
            method_新增(o);
        }else {
            System.out.println("已存在");
        }
//        else {
//            String wordsList = method_更新数据(o);
//            sql = "update " + tableName + " set " + wordsList + " where C_PID=" + id + " and C_PID=" + id2+" and C_PicType='"+picType+"'";
////            System.out.println(sql);
//            method_i_d_u(sql);
//        }
    }

    public ArrayList<Object> method_查找未下载的分组(int status) {
        String sql = "select distinct C_Group from " + tableName + " where C_是否下载=" + status + " ORDER BY C_Group";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }

    //查找未下载的params文件
    public ArrayList<Object> method_查找未下载的params(int status) {
        String sql = "select distinct C_Group from " + tableName + " where params=" + status + " or params is null   ORDER BY C_Group";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    //查找未下载的config
    public ArrayList<Object> method_查找未下载的config(int status) {
        String sql = "select distinct C_Group from " + tableName + " where config=" + status + " or config is null  ORDER BY C_Group";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    //查找未下载的bag
    public ArrayList<Object> method_查找未下载的bag(int status) {
        String sql = "select distinct C_Group from " + tableName + " where bag=" + status + " or bag is null  ORDER BY C_Group";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }

    public ArrayList<Object> method_查找未下载的分组版本ID(int group) {
        String sql = "select * from " + tableName + " where C_Group=" + group;
//        String sql ="select distinct C_Group from "+tableName+" where C_是否下载="+status+" ORDER BY C_Group";
//        System.out.println(sql);
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }

    //修改版本表的下载状态 分组
//    public void method_修改下载状态(String ziduan,int status, int id) {
//        String sql = "update " + tableName + " set "+ziduan+"=" + status + " where C_Group=" + id;
//        method_i_d_u(sql);
//    }
    public void method_修改车型页面下载状态(String ziduan,int status, String modelID) {
        String sql = "update " + tableName + " set "+ziduan+"=" + status + " where C_ModelID=" + modelID;
        method_i_d_u(sql);
    }

    public ArrayList<Object> method_查找未下载的车型ID(int status) {
        String sql = "select distinct C_ModelID from " + tableName + " where C_是否下载=" + status;
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }

    public void method_修改车型页面下载状态(String modID) {
        String sql = "update T_车系 set C_是否下载=1 where C_ModelID='" + modID + "'";
        System.out.println(sql);
//        method_i_d_u(sql);
    }

    //查找版本ID中含有英文的数据
    public ArrayList<Object> method_查找版本ID中含有英文的数据() {
        String sql = "select * from "+tableName+" where C_VersionID like '%[a-z]%' or C_VersionID like '%[A-Z]%'";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }

    //修改含英文字符版本ID为全数字
    public void method_修改版本ID(String id, String id2) {
        String sql = "update " + tableName + " set C_VersionID ='" + id + "' where C_VersionID='" + id2 + "'";
//        System.out.println(sql);
        method_i_d_u(sql);
    }
    //查看所有版本分组
    public ArrayList<Object> method_查找所有版本分组(){
        String sql="select distinct C_Group from "+tableName;
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    //查看未下载的
    public ArrayList<Object> method_查找未下载图片的版本(){
        String sql = "select * from "+tableName+" where C_是否下载=0 or C_是否下载 is null";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    //修改图片的版本下载状态
    public void method_修改下载图片的状态(String verID){
        String sql = "update "+tableName+" set C_是否下载=1 where C_VersionID = "+verID;
        method_i_d_u(sql);
    }
    //查找未下载的图片brandID
    public ArrayList<Object> method_查找未下载图片的品牌ID(){
        String sql = "select distinct C_BrandID from "+tableName+" where C_是否下载=0 or C_是否下载 is null";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    public ArrayList<Object> method_根据下载状态查找数据(int status) {
        String sql = "select * from " + tableName + " where C_是否下载=" + status;
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    //根据品牌ID查找车型
    public ArrayList<Object> method_根据品牌ID查找车型(String brandID) {
        String sql = "select * from " + tableName + " where C_BrandID='" + brandID + "'";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    //根据车型ID查找版本
    public ArrayList<Object> method_根据车型ID查找版本(String modID) {
        String sql = "select * from " + tableName + " where C_ModelID='" + modID + "'";
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }

    public ArrayList<Object> method_在售(int status) {
        String sql = "select * from " + tableName + " where C_在售=" + status;
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    public ArrayList<Object> method_停售(int status) {
        String sql = "select * from " + tableName + " where C_停售=" + status;
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    public ArrayList<Object> method_图片在售(int status) {
        String sql = "select * from " + tableName + " where C_图片页面在售=" + status;
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }
    public ArrayList<Object> method_图片停售(int status) {
        String sql = "select * from " + tableName + " where C_图片页面停售=" + status;
        ArrayList<Object> result = method_有条件的查询(sql);
        return result;
    }

    public int getVersionGroup(String versionIds){
        String sql = "select distinct C_Group from "+tableName+" where C_VersionID in ("+versionIds+")";
        ArrayList<Object> data = method_有条件的查询(sql);
        int group = ((Bean_Version)data.get(0)).get_C_Group();
        return group;
    }
}
