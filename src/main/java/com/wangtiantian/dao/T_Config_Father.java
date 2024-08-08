package com.wangtiantian.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;

public class T_Config_Father {
    protected String dbName;
    protected String tableName;
    protected String primaryKey;
    protected String userName;
    protected String password;
    protected String dbDriver;
    protected String dbURL;
    protected String packBag;
    protected Connection conn = null;
    protected Statement stmt = null;
    protected String dbString;

    public T_Config_Father(int chooseDataBaseType, int chooseDataBase, int chooseTable) {
        String content = T_Config_File.method_读取文件内容("config.json");
        JSONArray mainRoot = JSON.parseArray(content);
        JSONObject jsonRoot = mainRoot.getJSONObject(chooseDataBaseType);
        JSONArray mainJson = jsonRoot.getJSONArray("dbItems");
        JSONObject mainDataBase = mainJson.getJSONObject(chooseDataBase);
        JSONArray tableItems = mainDataBase.getJSONArray("db_table");
        JSONObject tableObject = tableItems.getJSONObject(chooseTable);
        dbDriver = jsonRoot.getString("driver");
        dbURL = jsonRoot.getString("dbURL");
        userName = mainDataBase.getString("userName");
        password = mainDataBase.getString("password");
        dbName = mainDataBase.getString("dbName");
        packBag = tableObject.getString("entity");
        tableName = tableObject.getString("tableName");
        if (chooseDataBaseType == 0) {
            dbString = dbURL + dbName;
        } else if (chooseDataBaseType == 1) {
            dbString = dbURL + dbName + jsonRoot.getString("charset");
        } else if (chooseDataBaseType == 2) {
            dbString = dbURL + dbName;
            System.out.println("当前正在使用的数据库是 -> " + dbName);
        }
    }

    public void method_连接数据库() {
        try {
            Class.forName(dbDriver);
            if (null == conn || conn.isClosed()) {
                conn = DriverManager.getConnection(dbString, userName, password);
            }
            if (null == stmt || stmt.isClosed()) {
                stmt = conn.createStatement();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method_i_d_u(String sql) {
        try {
            method_连接数据库();
//            System.out.println(sql);
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            System.out.println("数据库连接已断开");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method_新增(Object o) {
        try {
            Class c = o.getClass();
            Method[] methods = c.getDeclaredMethods();
            String columnList = "";
            String valueList = "";
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith("get_")) {
                    if (methods[i].getName().equals("get_C_ID")) {
                        continue;
                    }
                    columnList += methods[i].getName().replace("get_", "") + ",";
                    String value = methods[i].invoke(o) == null ? "-" : methods[i].invoke(o).toString().trim();
                    if (methods[i].getReturnType().equals(new String().getClass())) {
                        valueList += "'" + value + "',";
                    } else {
                        valueList += "N" + value + ",";
                    }
                }
            }
            columnList = columnList.substring(0, columnList.length() - 1);
            valueList = valueList.substring(0, valueList.length() - 1);
            String sql = "insert into " + tableName + "(" + columnList + ") values(" + valueList + ")";
//            System.out.println(sql);
            method_i_d_u(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Object> method_查找() {
        ArrayList<Object> result = new ArrayList<>();
        try {
            method_连接数据库();
            String sql = "select * from " + tableName + " order by C_ID";
            ResultSet resultSet = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            while (resultSet.next()) {
                Class c = Class.forName(packBag);
                Object o = c.newInstance();
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

    public String method_更新数据(Object o) {
        String wordsList = "";
        try {
            Class c = o.getClass();
            Method[] methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith("get_")) {
                    if (methods[i].getName().equals("get_C_ID")) {
                        continue;
                    }
                    String columnName = methods[i].getName().replace("get_", "");
                    String value = methods[i].invoke(o) == null ? "-" : methods[i].invoke(o).toString();
                    String words = columnName + "=" + "'" + value + "'";
                    wordsList += words + ",";
                }
            }
            wordsList = wordsList.substring(0, wordsList.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordsList;
    }

    public ArrayList<Object> method_有条件的查询(String sql) {
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
            System.out.println("数据库连接已断开");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getValueList(Object o) {
        String sql = "";
        try {
            Class c = o.getClass();
            Method[] methods = c.getDeclaredMethods();
            String valueList = "";
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith("get_")) {
                    if (methods[i].getName().equals("get_C_ID")) {
                        continue;
                    }
                    String value = methods[i].invoke(o) == null ? "-" : methods[i].invoke(o).equals("")?"-": methods[i].invoke(o).toString().trim();
                    if (methods[i].getReturnType().equals(new String().getClass())) {
                        valueList += "N'" + value + "',";
                    } else {
                        valueList += "" + value + ",";
                    }
                }
            }
            valueList = valueList.substring(0, valueList.length() - 1);
            sql = "(" + valueList + ")";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }

    public String getColumnList(Object o) {
        String columnList = "";
        try {
            Class c = o.getClass();
            Method[] methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith("get_")) {
                    if (methods[i].getName().equals("get_C_ID")) {
                        continue;
                    }
                    columnList += methods[i].getName().replace("get_", "") + ",";
                }
            }
            columnList = columnList.substring(0, columnList.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "(" + columnList + ")";
    }

    public void method_批量插入数据(String values, String columns) {
        String sql = "insert into " + tableName + columns + " values" + values;
//        System.out.println(sql);
        method_i_d_u(sql);
    }

    // 更新数据方法
//    public void Method_UpdateGroup(String groupNum) {
//        //update T_汽车之家_版本表_20240620 set config=1 where C_Group=6754
//        String sql = "update " + tableName + " set config = 1  where C_Group= "+ groupNum;
//        method_i_d_u(sql);
//    }
    public void method_修改下载状态(String ziduan, int status, int id) {
        String sql = "update " + tableName + " set " + ziduan + "=" + status + " where C_Group=" + id;
        method_i_d_u(sql);
    }


    private static final Object lock = new Object();

    // 修改未下载的车辆信息的数据的url的状态
    public void updateNoDealerModelStatus(String dealerID, String modID) {
        synchronized (lock) {
            String sql = "update " + tableName + " set C_IsFinish = 1 where C_DealerID=" + dealerID + " and C_ModelID = " + modID;
            method_i_d_u(sql);
        }
    }
}
