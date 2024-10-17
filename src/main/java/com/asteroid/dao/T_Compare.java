package com.asteroid.dao;

import com.wangtiantian.dao.T_Config_Father;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.*;

public class T_Compare extends T_Config_Father {
    public T_Compare(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        super(chooseDataBaseType, chooseDataBase, chooseTable);
    }

    public ArrayList<String> method_获取去重的单列数据(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName);

    }

    public ArrayList<String> method_获取去重的单列数据_汽车之家(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName + " where C_易车的版本Id is null");
    }

    public ArrayList<String> method_获取去重的单列数据_易车(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName + " where C_汽车之家的版本Id is null");
    }

    public ArrayList<String> method_去重的查询(String sql) {
//        System.out.println(sql);
        ArrayList<String> result = new ArrayList<>();
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
                    result.add((String) cobj);
                }
            }
            resultSet.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void method_修改可以规范的数据(String columnName, String rightValue, String failValue) {
        String sql = "update " + tableName + " set " + columnName + " = '" + rightValue + "' where " + columnName + " ='" + failValue + "'";
        System.out.println(sql + "\t" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        method_i_d_u(sql);
    }

    public void method_修改可以规范的数据_汽车之家(String columnName, String rightValue, String failValue) {
        String sql = "update " + tableName + " set " + columnName + " = '" + rightValue + "' where " + columnName + " ='" + failValue + "' and C_易车的版本Id is null";
        System.out.println(sql + "\t" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        method_i_d_u(sql);
    }

    public void method_修改可以规范的数据_易车(String columnName, String rightValue, String failValue) {
        String sql = "update " + tableName + " set " + columnName + " = '" + rightValue + "' where " + columnName + " ='" + failValue + "' and C_汽车之家的版本Id is null";
        System.out.println(sql + "\t" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        method_i_d_u(sql);
    }

    public void method_修改数据成统一的格式(String columnName, String replaceString) {
        String sql = "update " + tableName + " set " + columnName + replaceString;
        System.out.println(sql + "\t" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        method_i_d_u(sql);
    }

    public void method_替换数据成统一的格式(String columnName, String C_被替换的, String C_要替换的) {
        String sql = "update " + tableName + " set " + columnName + "=replace(" + columnName + ",'" + C_被替换的 + "','" + C_要替换的 + "')";
        System.out.println(sql + "\t" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        method_i_d_u(sql);
    }

    public void method_替换数据成统一的格式_座椅布局(String columnName, String C_被替换的, String C_要替换的) {
        String sql = "update " + tableName + " set " + columnName + "=replace(" + columnName + ",'" + C_被替换的 + "','" + C_要替换的 + "') where " + columnName + " !='-'";
        System.out.println(sql + "\t" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        method_i_d_u(sql);
    }

    public ArrayList<String> method_获取去重后的座位数数据_含有横杠字符的(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName + " where " + columnName + " like '%-%' and " + columnName + " != '-'");
    }

    public ArrayList<String> method_获取去重后的座位数数据_含有斜杠字符的(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName + " where " + columnName + " like '%/%'");
    }

    public ArrayList<String> method_获取去重后的电芯品牌数据_含有斜杠字符的(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName + " where " + columnName + " like '%/%'");
    }

    public ArrayList<String> method_获取去重后的电芯品牌数据_含有实心圈或者空心圈(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName + " where " + columnName + "  like '%○%' or " + columnName + "  like '%●%'");
    }

    public ArrayList<String> method_获取去重后的变速箱数据_含档位的(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName + " where " + columnName + " like '%挡%' and " + columnName + " not like '%待查%'");
    }

    public ArrayList<String> method_获取去重后的选配带有价格的数据(String columnName) {
        return method_去重的查询("select distinct " + columnName + " from " + tableName + " where " + columnName + "  like '%○%' and " + columnName + "  like '%元%'");
    }

    public ArrayList<String> method_汽车之家和易车的版本id去重数据() {
        return method_去重的查询("select distinct C_汽车之家版本Id_易车版本Id from " + tableName);
    }

    public List<Map<String, Object>> method_根据id查询交集表中的数据(String id) {
        List<Map<String, Object>> resultData = new ArrayList<>();
        String sql = "select * from " + tableName + " where C_汽车之家版本Id_易车版本Id='" + id + "' order by  C_易车的版本Id";
        try {
            method_连接数据库();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                Class c = Class.forName(packBag);
                Object o = c.newInstance();
                ResultSetMetaData rsmd = resultSet.getMetaData();
                Map<String, Object> result = new HashMap<>();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String cname = rsmd.getColumnName(i + 1);
                    Object cobj = resultSet.getObject(i + 1);
                    result.put(cname, cobj);
                }
                resultData.add(result);
            }
            resultSet.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData;
    }
}
