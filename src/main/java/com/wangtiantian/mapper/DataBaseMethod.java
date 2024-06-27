package com.wangtiantian.mapper;

import com.wangtiantian.dao.T_Config_AutoHome;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.*;

import java.util.*;

public class DataBaseMethod {
    //选择数据库与
    private static int chooseDataBase = 10;
    //选择要操作的数据库表
    private static T_Config_AutoHome brandDao = new T_Config_AutoHome(0, chooseDataBase, 0);
    private static T_Config_AutoHome fctDao = new T_Config_AutoHome(0, chooseDataBase, 1);
    private static T_Config_AutoHome modDao = new T_Config_AutoHome(0, chooseDataBase, 2);
    private static T_Config_AutoHome verDao = new T_Config_AutoHome(0, chooseDataBase, 3);
    private static T_Config_AutoHome paramsDao = new T_Config_AutoHome(0, chooseDataBase, 4);
    private static T_Config_AutoHome configDao = new T_Config_AutoHome(0, chooseDataBase, 5);
    private static T_Config_AutoHome bagDao = new T_Config_AutoHome(0, chooseDataBase, 6);
    private static T_Config_AutoHome modURLDao = new T_Config_AutoHome(0, chooseDataBase, 7);
    private static T_Config_AutoHome versionIdsGroup = new T_Config_AutoHome(0, chooseDataBase, 8);
    // 返回所有的查找数据
    public static ArrayList<Object> findDataFromDataBase(String type) {
        ArrayList<Object> result = new ArrayList<>();
        if (type.equals("brand")) {
            result = brandDao.method_查找();
        } else if (type.equals("factory")) {
            result = fctDao.method_查找();
        } else if (type.equals("model")) {
            result = modDao.method_查找();
        } else if (type.equals("version")) {
            result = verDao.method_查找();
        } else if (type.equals("modelURL")) {
            result = modURLDao.method_查找();
        }else  if (type.equals("versionIds")){
            result = versionIdsGroup.method_查找();
        }
        return result;
    }

    //返回所有的未下载数据集
    public static ArrayList<Object> findDataByDownLoadStatus(int status, String type) {
        ArrayList<Object> result = new ArrayList<>();
        if (type.equals("model")) {
            result = modDao.method_根据下载状态查找数据(status);
        } else if (type.equals("version")) {
            result = verDao.method_根据下载状态查找数据(status);
        } else if (type.equals("params")) {
            result = verDao.method_查找未下载的params(status);
        } else if (type.equals("config")) {
            result = verDao.method_查找未下载的config(status);
        } else if (type.equals("bag")) {
            result = verDao.method_查找未下载的bag(status);
        } else if (type.equals("modelURL在售")) {
            result = modURLDao.method_在售(status);
        } else if (type.equals("modelURL停售")) {
            result = modURLDao.method_停售(status);
        } else if (type.equals("modelURL图片在售")) {
            result = modURLDao.method_图片在售(status);
        } else if (type.equals("modelURL图片停售")) {
            result = modURLDao.method_图片停售(status);
        }

        return result;
    }

    // 查找分组中的版本ID
    public static ArrayList<Object> findVersionIDByGroup(int group) {
        ArrayList<Object> verItems = verDao.method_查找未下载的分组版本ID(group);
        return verItems;
    }

    // 查询所有分组
    public static ArrayList<Object> findAllGroup() {
        ArrayList<Object> result = verDao.method_查找所有版本分组();
        return result;
    }

    // 修改数据下载状态
    public static void updateDownLoadStatus(int status, int group, String ziduan) {
        try {
            verDao.method_修改下载状态(ziduan, status, group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //updateModelStatus
    public static void updateModelStatus(int status, String conditionId, String type) {
        try {
            modURLDao.method_修改车型页面下载状态(type, status, conditionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 所有的数据库插入操作
    public static void dataBase_i_d_u(ArrayList<Object> dataList, String type) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            if (type.equals("brand")) {
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = brandDao.getColumnList(dataList.get(i));
                for (Object brand : batchList) {
                    valueBuffer.append(brandDao.getValueList(brand)).append(",");
                }
                String tempString = valueBuffer.toString();
                brandDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
                System.out.println("brand数据表操作");
            } else if (type.equals("factory")) {
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = fctDao.getColumnList(dataList.get(i));
                for (Object factory : batchList) {
                    valueBuffer.append(fctDao.getValueList(factory)).append(",");
                }
                String tempString = valueBuffer.toString();
                fctDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
                System.out.println("厂商表操作");
            } else if (type.equals("model")) {
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = modDao.getColumnList(dataList.get(i));
                for (Object model : batchList) {
                    valueBuffer.append(modDao.getValueList(model)).append(",");
                }
                String tempString = valueBuffer.toString();
                modDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
                System.out.println("车型表操作");
            } else if (type.equals("version")) {
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = verDao.getColumnList(dataList.get(i));
                for (Object version : batchList) {
                    valueBuffer.append(verDao.getValueList(version)).append(",");
                }
                String tempString = valueBuffer.toString();
                verDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
                System.out.println("版本表操作");
            } else if (type.equals("params")) {
                System.out.println("参数表操作");
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = paramsDao.getColumnList(dataList.get(i));
                for (Object params : batchList) {
                    valueBuffer.append(paramsDao.getValueList(params)).append(",");
                }
                String tempString = valueBuffer.toString();
                paramsDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            } else if (type.equals("config")) {
                System.out.println("配置表操作");
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = configDao.getColumnList(dataList.get(i));
                for (Object config : batchList) {
                    valueBuffer.append(configDao.getValueList(config)).append(",");
                }
                String tempString = valueBuffer.toString();
                configDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            } else if (type.equals("bag")) {
                System.out.println("选装包表操作");
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = bagDao.getColumnList(dataList.get(i));
                for (Object bag : batchList) {
                    valueBuffer.append(bagDao.getValueList(bag)).append(",");
                }
                String tempString = valueBuffer.toString();
                bagDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            } else if (type.equals("modelURL")) {
                System.out.println("车型URL");
                List<Object> batchList = dataList.subList(i, end);
                StringBuffer valueBuffer = new StringBuffer();
                String columnList = modURLDao.getColumnList(dataList.get(i));
                for (Object o : batchList) {
                    valueBuffer.append(modURLDao.getValueList(o)).append(",");
                }
                String tempString = valueBuffer.toString();
                modURLDao.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            }
        }

    }

    public static void dataBase_i_少量数据(ArrayList<Object> dataList, String type) {
        try {
            if (type.equals("params")) {
                for (Object o : dataList) {
                    paramsDao.method_更新Params数据(o, ((Bean_Params) o).get_C_PID());
                }
            } else if (type.equals("config")) {
                for (Object o : dataList) {
                    configDao.method_更新config数据(o, ((Bean_Config) o).get_C_PID());
                }
            } else if (type.equals("bag")) {
                for (Object o : dataList) {
                    bagDao.method_更新bag数据(o, ((Bean_Bag) o).get_C_PID(), ((Bean_Bag) o).get_C_BagID());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void dataBase_i_对比数据(ArrayList<Object> dataList, String type) {
        try {
            if (type.equals("params")) {
                for (Object o : dataList) {
                    paramsDao.method_更新Params数据(o, ((Bean_Params) o).get_C_PID());
                }
            } else if (type.equals("config")) {
                for (Object o : dataList) {
                    configDao.method_更新config数据(o, ((Bean_Config) o).get_C_PID());
                }
            } else if (type.equals("bag")) {
                for (Object o : dataList) {
                    bagDao.method_更新bag数据(o, ((Bean_Bag) o).get_C_PID(), ((Bean_Bag) o).get_C_BagID());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取要下载的URL
    public static ArrayList<Bean_P_C_B_URL> getUrlList() {
        ArrayList<Bean_P_C_B_URL> dataList = new ArrayList<>();
        ArrayList<Object> allData = DataBaseMethod.findDataFromDataBase("version");
        String versions = "";
        int b = 0;
        for (int i = 0; i < allData.size(); i++) {
            String versionID = ((Bean_Version) allData.get(i)).get_C_VersionID();
            versions += versionID + ",";
            b++;
            if (b >= 10) {
                b = 0;
                int group = verDao.getVersionGroup(versions.substring(0, versions.length() - 1));
//                System.out.println(group);
                Bean_P_C_B_URL beanPCBUrl = new Bean_P_C_B_URL();
//                beanPCBUrl.set_Url("https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + versions.substring(0, versions.length() - 1) + "&_=1704953414626&_callback=__config3");
                beanPCBUrl.set_C_VersionIds(versions.substring(0, versions.length() - 1));
                beanPCBUrl.set_C_Group(group);
                dataList.add(beanPCBUrl);
                System.out.println(versions);
                versions = "";
            }
        }
        int group = verDao.getVersionGroup(versions.substring(0, versions.length() - 1));
        Bean_P_C_B_URL beanPCBUrl = new Bean_P_C_B_URL();
        beanPCBUrl.set_C_VersionIds(versions.substring(0, versions.length() - 1));
//        beanPCBUrl.set_Url("https://carif.api.autohome.com.cn/Car/v2/Config_ListBySpecIdList.ashx?speclist=" + versions.substring(0, versions.length() - 1) + "&_=1704953414626&_callback=__config3");
        beanPCBUrl.set_C_Group(group);
        dataList.add(beanPCBUrl);
        return dataList;
    }

    public static void dataBase_i_d_u(ArrayList<Bean_P_C_B_URL> dataList) {
        int batchSize = 100;
        for (int i = 0; i < dataList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, dataList.size());
            List<Bean_P_C_B_URL> batchList = dataList.subList(i, end);
            StringBuffer valueBuffer = new StringBuffer();
            String columnList = versionIdsGroup.getColumnList(dataList.get(i));
            for (Bean_P_C_B_URL bean : batchList) {
                valueBuffer.append(versionIdsGroup.getValueList(bean)).append(",");
            }
            String tempString = valueBuffer.toString();
            versionIdsGroup.method_批量插入数据(tempString.substring(0, tempString.length() - 1), columnList);
            System.out.println("versionids数据表操作");
        }

    }
}
