package com.wangtiantian.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangtiantian.entity.price.ConfirmCarPriceFile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        dbDriver = jsonRoot.getString("driver");
        dbURL = jsonRoot.getString("dbURL");
        userName = jsonRoot.getString("userName");
        password = jsonRoot.getString("password");
        JSONArray mainJson = jsonRoot.getJSONArray("dbItems");
        JSONObject mainDataBase = mainJson.getJSONObject(chooseDataBase);
        JSONArray tableItems = mainDataBase.getJSONArray("db_table");
        JSONObject tableObject = tableItems.getJSONObject(chooseTable);
        dbName = mainDataBase.getString("dbName");
        packBag = tableObject.getString("entity");
        tableName = tableObject.getString("tableName");
        if (dbDriver.contains("mysql")) {
            dbString = dbURL + dbName + jsonRoot.getString("charset");
            System.out.println("当前使用的是" + dbURL + " 连接的数据库是->" + dbName + "\t当前使用的表是->" + tableName);
        } else {
            dbString = dbURL + dbName;
            System.out.println("当前使用的是" + dbURL + " 连接的数据库是->" + dbName + "\t当前使用的表是->" + tableName);
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
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
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
        System.out.println(sql);
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
                    String value = methods[i].invoke(o) == null ? "-" : methods[i].invoke(o).equals("") ? "-" : methods[i].invoke(o).toString().trim();
                    if (methods[i].getReturnType().equals(new String().getClass())) {
                        valueList += "N'" + value.replace("'", "''") + "',";
                    } else {
                        valueList += "" + value.replace("'", "''") + ",";
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

    public int get_获取表中数据数量() {
        int num = 0;
        try {
            method_连接数据库();
            String sql = "select count(*) from " + tableName;
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                num = (int) resultSet.getObject(1);
            }

            resultSet.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public ArrayList<Object> get_查找未下载的数据() {
        return method_有条件的查询("select * from " + tableName + " where C_IsFinish =0 ");
    }

    public int get_获取表中数据数量_有查询条件(String sql) {
        int num = 0;
        try {
            method_连接数据库();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                num = (int) resultSet.getObject(1);
            }

            resultSet.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public void insertForeach(ArrayList<Object> dataList) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<Object> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = getColumnList(dataList.get(i));
            for (Object bean : batchList) {
                valueBuffer.append(getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            String sql = "insert into " + tableName + columnList + " values" + tempString.substring(0, tempString.length() - 1);
            method_i_d_u(sql);
            System.out.println(tableName + "分批入库一次");
        }
    }

    public ArrayList<Object> method_分页查询未下载的数据10000条每次(int begin) {
        String sql = "SELECT * FROM " + tableName + " where C_IsFinish = 0  ORDER BY C_ID OFFSET " + begin + " ROWS FETCH NEXT 10000 ROWS ONLY";
        return method_有条件的查询(sql);
    }

    public ArrayList<Object> get_查找已下载的数据() {
        return method_有条件的查询("select * from " + tableName + " where C_IsFinish =1 ");
    }

    public ArrayList<Object> method_分页查询已下载的数据10000条每次(int begin) {
        return method_有条件的查询("SELECT * FROM " + tableName + " where C_IsFinish = 1  ORDER BY C_ID OFFSET " + begin + " ROWS FETCH NEXT 10000 ROWS ONLY");
    }

    public ArrayList<Object> method_查询未下载的数据() {
        String sql = "SELECT * FROM " + tableName + " where C_IsFinish = 0";
        return method_有条件的查询(sql);
    }

    public void truncate_清空表中数据() {
        String sql = "truncate table " + tableName;
        method_i_d_u(sql);
    }

    public void method_删除已存在的表(String nameTable) {
        try {
            String sql = "drop table " + nameTable;
            method_i_d_u(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean method_判断要创建的表是否已存在(String nameTable) {
        int num = 0;
        try {
            method_连接数据库();
            String sql = "select count(*) from sysobjects where name ='" + nameTable + "'";

            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                num = (int) resultSet.getObject(1);
            }

            resultSet.close();
            stmt.close();
            conn.close();
            System.out.println(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num == 0) {
            return false;
        } else {
            return true;
        }
    }
}
