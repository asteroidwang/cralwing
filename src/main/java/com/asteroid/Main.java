package com.asteroid;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.asteroid.dao.T_Compare;
import com.asteroid.dao.T_CompareField;
import com.asteroid.dao.T_Dao_Config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wangtiantian.dao.T_Config_File;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    // 判断数据对比的类型
    public static void main(String[] args) {
        // 1 数据的规范处理
        new Main().method_前置数据处理();
        new Main().method_规范数据_实心圈位置();
        new Main().method_规范数据_汽车之家();
        new Main().method_规范数据_易车();
        new Main().method_规范数据_汽车之家_易车();
        new Main().method_座位数的数据规范();
        new Main().method_中文数字和阿拉伯数字不统一的数据规范();
        new Main().method_电芯品牌的数据规范();
        new Main().method_辅助驾驶系统();
        new Main().method_数据规范();
        new Main().method_选配带有价格的数据规范();
        new Main().method_规范数据_实心圈位置();
        new Main().method_找出有必要规范的字段();

        // 判断数据对比的类型
//        new Main().method_判断数据对比的类型();


    }



    public void method_判断数据对比的类型() {
        String filePath = "/Users/asteroid/所有文件数据/对比结果/";
        T_Compare compare = new T_Compare(2, 1, 3);
        String idsFinish = T_Config_File.method_读取文件内容(filePath + "已匹配的版本ids.txt");

        ArrayList<String> idsList = compare.method_汽车之家和易车的版本id去重数据(idsFinish.equals("") ? "''" : idsFinish.substring(0, idsFinish.length() - 1));
        List<Map<String, Object>> dataExcelList_名称匹配的上配置匹配的上 = new ArrayList<>();
        List<Map<String, Object>> dataExcelList_名称匹配的上配置匹配不上 = new ArrayList<>();
        List<Map<String, Object>> dataExcelList_名称匹配不上配置匹配的上 = new ArrayList<>();
        List<Map<String, Object>> dataExcelList_名称匹配不上配置匹配不上 = new ArrayList<>();
        int a = 0;
        for (String ids : idsList) {
            // pre定义汽车之家的相关数据
            List<Map<String, Object>> dataList = compare.method_根据id查询交集表中的数据(ids);
            ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
            String versionNameColumnName = "C_基本参数__车型名称";
            String modelNameColumnName = "C_ModelName";
            String preVersionId = ids.split("_")[0];
            String nextVersionId = ids.split("_")[1];
            String preModelName = dataList.get(0).get(modelNameColumnName).toString();
            String nextModelName = dataList.get(1).get(modelNameColumnName).toString();
            String preVersionName = dataList.get(0).get(versionNameColumnName).toString().contains(preModelName) ? dataList.get(0).get(versionNameColumnName).toString() : preModelName + dataList.get(0).get(versionNameColumnName).toString();
            String nextVersionName = dataList.get(1).get(versionNameColumnName).toString().contains("款") && !String.valueOf(dataList.get(1).get(versionNameColumnName).toString().charAt(0)).equals("9") ? nextModelName + "20" + dataList.get(1).get(versionNameColumnName).toString() : nextModelName + "19" + dataList.get(1).get(versionNameColumnName).toString();

            StringBuilder sameConfig = new StringBuilder();
            StringBuilder noSameConfig = new StringBuilder();
            for (String columnName : columnsList) {
                String preValue = dataList.get(0).get(columnName).toString();
                String nextValue = dataList.get(1).get(columnName).toString();
                if (!preValue.equals("-") && !nextValue.equals("-")) {
                    if (preValue.equals(nextValue)) {
                        sameConfig.append(preValue);
                    } else {
//                        noSameConfig.append(columnName + "=>" + preValue + "->" + nextValue + "||");
                        noSameConfig.append(columnName).append("=>").append(preValue).append("->").append(nextValue.equals("")?"-":nextValue).append("||");
                    }
                }
            }
            String preModelYear = preModelName + extractYearFromVersionName(preVersionName);
            String nextModelYear = nextModelName + extractYearFromVersionName(nextVersionName);
            String preYear = extractYearFromVersionName(preVersionName);
            String nextYear = extractYearFromVersionName(nextVersionName);

            Map<String, Object> mapList = new HashMap<>();
            mapList.put("汽车之家_易车的版本Id", ids);
            mapList.put("汽车之家的版本Id", preVersionId);
            mapList.put("易车的版本Id", nextVersionId);
            mapList.put("汽车之家的版本名称", preVersionName);
            mapList.put("易车的版本名称", nextVersionName);


            Map<String, Object> mapListPre = new HashMap<>();
            mapListPre.put("汽车之家_易车的版本Id", ids);
            mapListPre.put("汽车之家的版本Id", "");
            mapListPre.put("易车的版本Id", "");
            mapListPre.put("汽车之家的版本名称", "");
            mapListPre.put("易车的版本名称", "");
            Map<String, Object> mapListMext = new HashMap<>();
            mapListMext.put("汽车之家_易车的版本Id", ids);
            mapListMext.put("汽车之家的版本Id", "");
            mapListMext.put("易车的版本Id", "");
            mapListMext.put("汽车之家的版本名称", "");
            mapListMext.put("易车的版本名称", "");
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "匹配结果备份12.txt", ids + "\t" + preVersionId + "\t" + nextVersionId + "\t" + preVersionName + "\t" + nextVersionName + "\t" + noSameConfig+"\n");
            T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "已匹配的版本ids.txt", "'" + ids + "',");
            // 最起码年要对得上
            if (preYear.equals(nextYear)) {

                if (preVersionName.equals(nextVersionName)) {
                    if (noSameConfig.toString().equals("") && !sameConfig.toString().equals("")) {
                    } else {

                    }
                } else {
                    if (noSameConfig.toString().equals("") && !sameConfig.toString().equals("")) {
                    } else {

                    }
                }


//                if (preVersionName.equals(nextVersionName) && noSameConfig.toString().equals("") && !sameConfig.toString().equals("")) { // 名称匹配的上 配置匹配的上
//                    dataExcelList_名称匹配的上配置匹配的上.add(mapList);
////                    System.out.println("dataExcelList_名称匹配的上配置匹配的上");
//                } else if (preVersionName.equals(nextVersionName) && !noSameConfig.toString().equals("") && !sameConfig.toString().equals("")) {  // 名称匹配的上 配置匹配不上
//                    mapListPre.put("汽车之家的版本Id", preVersionId);
//                    mapListPre.put("汽车之家的版本名称", preVersionName);
//                    mapListMext.put("易车的版本Id", nextVersionId);
//                    mapListMext.put("易车的版本名称", nextVersionName);
//                    dataExcelList_名称匹配的上配置匹配不上.add(addConfigToMap_汽车之家(mapListPre, noSameConfig));
//                    dataExcelList_名称匹配的上配置匹配不上.add(addConfigToMap_易车(mapListMext, noSameConfig));
////                    System.out.println("dataExcelList_名称匹配的上配置匹配不上\t"+dataExcelList_名称匹配的上配置匹配不上);
//                } else if (!preVersionName.equals(nextVersionName) && noSameConfig.toString().equals("") && !sameConfig.toString().equals("")) { // 名称匹配不上 配置匹配的上
//                    dataExcelList_名称匹配不上配置匹配的上.add(mapList);
//                } else if (!preVersionName.equals(nextVersionName) && !noSameConfig.toString().equals("") && !sameConfig.toString().equals("")) { // 名称匹配不上 配置匹配不上
////                    dataExcelList_名称匹配的上配置匹配不上.add(addConfigToMap(mapList, noSameConfig));
//                    mapListPre.put("汽车之家的版本Id", preVersionId);
//                    mapListPre.put("汽车之家的版本名称", preVersionName);
//                    mapListMext.put("易车的版本Id", nextVersionId);
//                    mapListMext.put("易车的版本名称", nextVersionName);
//                    dataExcelList_名称匹配不上配置匹配不上.add(addConfigToMap_汽车之家(mapListPre, noSameConfig));
//                    dataExcelList_名称匹配不上配置匹配不上.add(addConfigToMap_易车(mapListMext, noSameConfig));
//                }
            }
            a++;
            System.out.println("还有" + (idsList.size() - a) + "个");
        }

        Map<String, List<Map<String, Object>>> allExcelData = new HashMap<>();
        allExcelData.put("名称匹配的上配置匹配的上", dataExcelList_名称匹配的上配置匹配的上);
        allExcelData.put("名称匹配的上配置匹配不上", dataExcelList_名称匹配的上配置匹配不上);
        allExcelData.put("名称匹配不上配置匹配的上", dataExcelList_名称匹配不上配置匹配的上);
        allExcelData.put("名称匹配不上配置匹配不上", dataExcelList_名称匹配不上配置匹配不上);
        System.out.println(allExcelData);
        T_Config_File.method_写文件_根据路径创建文件夹(filePath, "对比结果汇总.txt", allExcelData.toString());

        method_写excel(allExcelData);

    }


    public String extractYearFromVersionName(String versionName) {
        int yearStartIndex = versionName.indexOf("款") - 4;
        return versionName.substring(yearStartIndex, yearStartIndex + 5);
    }

    public Map<String, Object> addConfigToMap_汽车之家(Map<String, Object> map, StringBuilder config) {
        String[] configList = config.toString().split("\\|\\|");
        for (String item : configList) {
            int splitIndex = item.indexOf("=>");
            String columnName = item.substring(0, splitIndex);
            String[] values = item.substring(splitIndex + 2).split("->");
            if (values.length >= 2) {
                map.put(columnName, values[0]);
            }
//            map.put(columnName,values[1]);
//            for (int j = 0; j < values.length; j++) {
//                map.put(columnName, values[j]);
//            }
        }
        return map;
    }

    public Map<String, Object> addConfigToMap_易车(Map<String, Object> map, StringBuilder config) {
        String[] configList = config.toString().split("\\|\\|");
        for (String item : configList) {
            int splitIndex = item.indexOf("=>");
            String columnName = item.substring(0, splitIndex);

            String[] values = item.substring(splitIndex + 2).split("->");
            if (values.length >= 2) {
                map.put(columnName, values[1]);
            }

//            map.put(columnName,values[1]);
//            for (int j = 0; j < values.length; j++) {
//                map.put(columnName, values[j]);
//            }
        }
        return map;
    }

    public static void method_写excel(Map<String, List<Map<String, Object>>> dataMapList) {
        String filePath = "/Users/asteroid/所有文件数据/对比结果/";
        // 创建工作簿
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath + "output.xlsx");
            Workbook workbook = new XSSFWorkbook(); // 用于创建 .xlsx 文件
            System.out.println("创建excel文件");
            //Workbook workbook = new HSSFWorkbook(); // 用于创建 .xls 文件
            Set<String> sheetList = dataMapList.keySet();
            List<String> sheetNames = sheetList.stream().distinct().collect(Collectors.toList());
            // 获取了sheet页的名称以及数量，行名称
            // 提前获取所有sheet页的表头
            Map<String, List<String>> headerMap = new HashMap<>();
            for (String sheetName : dataMapList.keySet()) {
                headerMap.put(sheetName, method_获取excel表头(dataMapList.get(sheetName)));
            }

            for (String sheetName : sheetNames) {
                List<List<Object>> sheetData = new ArrayList<>();
                Sheet sheet = workbook.createSheet(sheetName);
                List<String> headersList = headerMap.get(sheetName);
//            List<List<String>> head = headersList.stream()
//                    .map(s -> Collections.singletonList(s))
//                    .collect(Collectors.toList());
                Row headerRow = sheet.createRow(0);
                int cellIndex = 0;
                for (String header : headersList) {
                    Cell cell = headerRow.createCell(cellIndex++);
                    cell.setCellValue(header);
                }
                System.out.println(sheetName + "\t");

                int rowIndex = 1;
                List<Map<String, Object>> dataList = dataMapList.get(sheetName);
                for (Map<String, Object> map : dataList) {
                    Row row = sheet.createRow(rowIndex++);
                    int columnIndex = 0;
                    System.out.println("行\t" + rowIndex);
                    for (String header : headersList) {
                        Cell cell = row.createCell(columnIndex++);
                        System.out.println("列\t" + columnIndex);
                        cell.setCellValue(map.get(header) == null ? "" : map.get(header).toString());
                    }
                }
                System.out.println(sheetName + "\t" + rowIndex);
            }
            try {
                workbook.write(outputStream);
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                workbook.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void method_写excel_修改前(Map<String, List<Map<String, Object>>> dataMapList) {
//        String filePath = "/Users/asteroid/所有文件数据/对比结果/";
        // 创建工作簿
//        Workbook workbook = new XSSFWorkbook(); // 用于创建 .xlsx 文件
        System.out.println("创建excel文件");
        File outputFile = new File("/Users/asteroid/所有文件数据/对比结果/output_1022.xlsx");
        //Workbook workbook = new HSSFWorkbook(); // 用于创建 .xls 文件
        Set<String> sheetList = dataMapList.keySet();
        List<String> sheetNames = sheetList.stream().distinct().collect(Collectors.toList());
        // 获取了sheet页的名称以及数量，行名称
        // 提前获取所有sheet页的表头
        Map<String, List<String>> headerMap = new HashMap<>();
        for (String sheetName : dataMapList.keySet()) {
            headerMap.put(sheetName, method_获取excel表头(dataMapList.get(sheetName)));
        }

        for (String sheetName : sheetNames) {
            List<List<Object>> sheetData = new ArrayList<>();

            List<String> headersList = headerMap.get(sheetName);
            List<List<String>> head = headersList.stream()
                    .map(s -> Collections.singletonList(s))
                    .collect(Collectors.toList());

            List<Map<String, Object>> dataList = dataMapList.get(sheetName);
            List list = new ArrayList<>(headersList.size());
            for (Map<String, Object> map : dataList) {
                for (String header : headersList) {
                    list.add(map.get(header) == null ? "" : map.get(header).toString());
                }
                sheetData.add(list);
            }

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                EasyExcel.write(fos)
                        .head(head) // 通过createExcelHead方法将表头信息传递给EasyExcel
                        .sheet(sheetName)
                        .needHead(true)                     // 表示需要生成列标题
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())// 设置自适应列宽
                        .doWrite(sheetData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public static void method_写excel_修改前_通义(Map<String, List<Map<String, Object>>> dataMapList) {
        System.out.println("创建excel文件");

        // 提前获取所有sheet页的表头
        Map<String, List<String>> headerMap = new HashMap<>();
        for (String sheetName : dataMapList.keySet()) {
            headerMap.put(sheetName, method_获取excel表头(dataMapList.get(sheetName)));
        }

        // 遍历每个sheet页
        for (String sheetName : dataMapList.keySet()) {
            List<Map<String, Object>> dataList = dataMapList.get(sheetName);
            List<String> headersList = headerMap.get(sheetName);

            // 创建 Excel 文件并写入数据
            File outputFile = new File("/Users/asteroid/所有文件数据/对比结果/output" + "_" + sheetName + ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                // 构造头部数据
                List<List<String>> head = headersList.stream()
                        .map(s -> Collections.singletonList(s))
                        .collect(Collectors.toList());

                // 收集数据行
                List<List<Object>> sheetData = new ArrayList<>();
                for (Map<String, Object> map : dataList) {
                    List<Object> row = new ArrayList<>(headersList.size());
                    for (String header : headersList) {
                        row.add(map.getOrDefault(header, ""));
                    }
                    sheetData.add(row);
                }
                // 写入数据
                EasyExcel.write(fos)
                        .head(head)
                        .sheet(sheetName)
                        .needHead(true)
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                        .doWrite(sheetData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static List<String> method_获取excel表头(List<Map<String, Object>> dataList) {
        Set<String> tempColumnNames = new TreeSet<>();
        List<String> headers = new ArrayList<>(Arrays.asList("汽车之家_易车的版本Id", "汽车之家的版本Id", "易车的版本Id", "汽车之家的版本名称", "易车的版本名称"));
        for (Map<String, Object> map : dataList) {
            for (String value : map.keySet()) {
                if (!value.contains("易车的版本Id") && !value.contains("汽车之家的版本Id") && !value.contains("汽车之家的版本名称") && !value.contains("易车的版本名称") && !value.contains("汽车之家_易车的版本Id")) {
                    tempColumnNames.add(value);
                }
            }
        }
        List<String> result = new ArrayList<>(headers);
        result.addAll(tempColumnNames);
        return result;
    }


// 特殊字段的数据类型以及怎么替换
// 替换字符匹配 【轮胎等有关数据 空格，/ 等特殊字符的替换然后再规范数据】【扬声器数量 喇叭 >=  个 字符的替换后再规范数据】【电池类型 替换掉 电池】【上市时间 把-替换成.】
// 实心圈和空心圈的位置规范 ● ○ 以及 易车的带有实心圈但是汽车之家的不带实心圈 或者 汽车之家的带有实心圈但是易车的不带有实心圈
//  需要单独处理的字段 座位数 电池组质保   C_电动机__电芯品牌     电机类型  马力    C_基本参数__变速箱  C_基本参数__发动机  C_基本参数__首任车主质保政策   C_基本参数__整车质保
// 选配带有价格的跟空心圈是一样的   ○ 价格 规范成○

    public void method_前置数据处理() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
        for (String columnName : columnsList) {
            compare.method_修改可以规范的数据(columnName, "○", "○选配");
            compare.method_替换数据成统一的格式(columnName, "未知", "待查");
            compare.method_替换数据成统一的格式(columnName, "○选配/", "○/");
            compare.method_修改可以规范的数据(columnName, "○", "○○");
            compare.method_替换数据成统一的格式(columnName, "○○", "○");
            compare.method_替换数据成统一的格式(columnName, "●●", "●");
            compare.method_替换数据成统一的格式(columnName, "○选配○", "○");
            compare.method_替换数据成统一的格式(columnName, "―", "-");
            if (columnName.equals("C_车内充电__行李厢12V电源接口")) {
                compare.method_修改可以规范的数据(columnName, "●12V", "●");
                compare.method_修改可以规范的数据(columnName, "○12V", "○");
            } else if (columnName.equals("C_被动安全__主_副驾驶座安全气囊")) {
                compare.method_修改可以规范的数据(columnName, "-", "主-/副-");
            } else if (columnName.equals("C_电动机__换电")) {
                compare.method_修改可以规范的数据(columnName, "●", "支持");
                compare.method_修改可以规范的数据(columnName, "-", "不支持");
            } else if (columnName.equals("C_驾驶硬件__超声波雷达数量") || columnName.equals("C_驾驶硬件__毫米波雷达数量") || columnName.equals("C_音响_车内灯光__车内环境氛围灯") || columnName.equals("C_驾驶硬件__车内摄像头数量") || columnName.equals("C_驾驶硬件__环视摄像头像素") || columnName.equals("C_驾驶硬件__环境感知摄像头像素") || columnName.equals("C_屏幕_系统__语音助手唤醒词") || columnName.equals("C_驾驶硬件__前方感知摄像头")) {
                compare.method_替换数据成统一的格式(columnName, "-○", "○");
            } else if (columnName.equals("C_发动机__环保标准") || columnName.equals("C_基本参数__环保标准")) {
                compare.method_修改可以规范的数据(columnName, "欧V", "欧五");
                compare.method_修改可以规范的数据(columnName, "欧VI", "欧六");
                compare.method_修改可以规范的数据(columnName, "国VI", "国六");
                compare.method_修改可以规范的数据(columnName, "国IV", "国四");
                compare.method_修改可以规范的数据(columnName, "国II", "国二");
                compare.method_修改可以规范的数据(columnName, "国III", "国三");
                compare.method_修改可以规范的数据(columnName, "国V", "国五");
            } else if (columnName.equals("C_基本参数__能源类型")) {
                compare.method_修改可以规范的数据(columnName, "插电式混合动力", "插电混合");
                compare.method_修改可以规范的数据(columnName, "纯电动", "纯电");
            } else if (columnName.equals("C_基本参数__上市时间")) {
                compare.method_修改数据成统一的格式(columnName, " =SUBSTRING(replace(C_基本参数__上市时间,'-','.'), 1, 7) where C_基本参数__上市时间 !='-'");
            } else if (columnName.equals("C_基本参数__长_宽_高_mm")) {
                compare.method_替换数据成统一的格式(columnName, "×", "*");
            } else if (columnName.equals("C_驾驶硬件__激光雷达点云数量")) {
                compare.method_替换数据成统一的格式(columnName, "万/秒", "");
            } else if (columnName.equals("C_车身__货箱尺寸_mm")) {
                compare.method_修改可以规范的数据(columnName, "-", "-x-x-");
            } else if (columnName.equals("C_驾驶硬件__前_后驻车雷达")) {
                compare.method_修改可以规范的数据(columnName, "-", "前-后-");
                compare.method_替换数据成统一的格式(columnName, "-后", "-/后");
                compare.method_替换数据成统一的格式(columnName, "●后", "●/后");
                compare.method_替换数据成统一的格式(columnName, "○后", "○/后");
            } else if (columnName.equals("C_驾驶硬件__前方感知摄像头像素")) {
                compare.method_替换数据成统一的格式(columnName, "万", "");
            } else if (columnName.equals("C_屏幕_系统__副驾屏幕像素密度") || columnName.equals("C_屏幕_系统__后排液晶屏幕像素密度") || columnName.equals("C_屏幕_系统__中控屏幕像素密度") || columnName.equals("C_屏幕_系统__中控下屏幕像素密度") || columnName.equals("C_方向盘_内后视镜__仪表盘屏幕像素密度")) {
                compare.method_替换数据成统一的格式(columnName, "PPI", "");
            } else if (columnName.equals("C_屏幕_系统__副驾娱乐屏尺寸") || columnName.equals("C_屏幕_系统__后排液晶屏幕尺寸") || columnName.equals("C_屏幕_系统__中控屏幕尺寸") || columnName.equals("C_屏幕_系统__中控下屏幕尺寸") || columnName.equals("C_方向盘_内后视镜__液晶仪表尺寸")) {
                compare.method_替换数据成统一的格式(columnName, "英寸", "");
                if (columnName.equals("C_屏幕_系统__副驾娱乐屏尺寸") || columnName.equals("C_屏幕_系统__中控屏幕尺寸")) {
                    compare.method_替换数据成统一的格式(columnName, "-○", "○");
                }
            } else if (columnName.equals("C_屏幕_系统__后排多媒体屏幕数量")) {
                compare.method_替换数据成统一的格式(columnName, "个", "");
                compare.method_替换数据成统一的格式(columnName, "块屏", "");
            } else if (columnName.equals("C_屏幕_系统__中控彩色屏幕")) {
                compare.method_替换数据成统一的格式(columnName, "式", "");
            } else if (columnName.equals("C_车内充电__手机无线充电功能") || columnName.equals("C_天窗_玻璃__侧窗多层隔音玻璃") || columnName.equals("C_外观_防盗__电动吸合车门") || columnName.equals("C_外观_防盗__无钥匙进入功能") || columnName.equals("C_主动安全__安全带未系提醒")) {
                compare.method_替换数据成统一的格式(columnName, "第一排", "前排");
                compare.method_替换数据成统一的格式(columnName, "第二排", "后排");
            } else if (columnName.equals("C_天窗_玻璃__车窗一键升降功能")) {
                compare.method_替换数据成统一的格式(columnName, "第一排", "前排");
            } else if (columnName.equals("C_音响_车内灯光__扬声器数量")) {
                compare.method_替换数据成统一的格式(columnName, "喇叭", "");
                compare.method_替换数据成统一的格式(columnName, "≥12", "12个以上");
                compare.method_替换数据成统一的格式(columnName, "个", "");
                compare.method_替换数据成统一的格式(columnName, "-○", "○");
            } else if (columnName.equals("C_座椅配置__第三排座椅电动调节")) {
                compare.method_替换数据成统一的格式(columnName, "带记忆电动调节", "");
                compare.method_替换数据成统一的格式(columnName, "电动调节", "");
            } else if (columnName.equals("C_座椅配置__座椅布局")) {
                compare.method_替换数据成统一的格式_座椅布局(columnName, "-", "+");
            } else if (columnName.equals("C_被动安全__膝部气囊")) {
                compare.method_替换数据成统一的格式(columnName, "●主驾膝部气囊", "●主驾");
                compare.method_替换数据成统一的格式(columnName, "○主驾膝部气囊", "○主驾");
                compare.method_替换数据成统一的格式(columnName, "●副驾膝部气囊", "●副驾");
                compare.method_替换数据成统一的格式(columnName, "○副驾膝部气囊", "○副驾");
            } else if (columnName.contains("充电量范围")) {
                compare.method_替换数据成统一的格式(columnName, "%", "");
            } else if (columnName.equals("C_电动机__电机类型")) {
                compare.method_替换数据成统一的格式(columnName, "/", "");
                compare.method_替换数据成统一的格式(columnName, "/", "");
                compare.method_替换数据成统一的格式(columnName, "步后", "步/后");
                compare.method_替换数据成统一的格式(columnName, "电机", "");
            } else if (columnName.equals("C_车身__座位数_个")) {
                compare.method_替换数据成统一的格式(columnName, ",", "/");
                compare.method_替换数据成统一的格式(columnName, "、", "/");
            } else if (columnName.equals("C_电动机__电池类型")) {
                compare.method_替换数据成统一的格式(columnName, "电池", "");
            } else if (columnName.equals("C_基本参数__变速箱")) {
                compare.method_替换数据成统一的格式(columnName, " ", "");
                compare.method_替换数据成统一的格式(columnName, "手自一体(AT)", "手自一体");
                compare.method_替换数据成统一的格式(columnName, "自动(AT)", "自动");
                compare.method_替换数据成统一的格式(columnName, "手动(MT)", "手动");
                compare.method_替换数据成统一的格式(columnName, "混合动力专用(DHT)", "DHT");
                compare.method_替换数据成统一的格式(columnName, "CVT无级变速", "无级变速(CVT)");
                compare.method_替换数据成统一的格式(columnName, "无级变速(CVT)", "CVT无级变速");
                compare.method_替换数据成统一的格式(columnName, "ISR变速箱", "ISR");
            } else if (columnName.equals("C_天窗_玻璃__感应雨刷功能")) {
                compare.method_替换数据成统一的格式(columnName, "速度感应式", "车速感应式");
            } else if (columnName.equals("C_车轮制动__驻车制动类型")) {
                compare.method_替换数据成统一的格式(columnName, "脚踩式", "●脚刹");
                compare.method_替换数据成统一的格式(columnName, "手拉式", "●手刹");
            } else if (columnName.contains("制动器类型")) {
                compare.method_替换数据成统一的格式(columnName, "实心盘式", "盘式");
            } else if (columnName.equals("C_方向盘_内后视镜__方向盘材质")) {
                compare.method_替换数据成统一的格式(columnName, "真皮/碳纤维", "碳纤维/真皮");
            } else if (columnName.equals("C_驾驶操控__可变悬架功能")) {
                compare.method_替换数据成统一的格式(columnName, "悬架", "");
            } else if (columnName.equals("C_驾驶功能__辅助驾驶系统")) {
                compare.method_替换数据成统一的格式(columnName, "-○", "○");
                compare.method_替换数据成统一的格式(columnName, "高级智能辅助驾驶", "");
                compare.method_替换数据成统一的格式(columnName, "智能驾驶辅助系统", "");
            } else if (columnName.equals("C_驾驶硬件__激光雷达10_反射率探测距离")) {
                compare.method_替换数据成统一的格式(columnName, "米", "");
            } else if (columnName.equals("C_屏幕_系统__车机智能芯片")) {
                compare.method_替换数据成统一的格式(columnName, "龍鷹一号", "  鹰一号");
            } else if (columnName.equals("")) {
                compare.method_替换数据成统一的格式(columnName, "副驾", "副驾驶");
                compare.method_替换数据成统一的格式(columnName, "主驾", "主驾驶");
            } else if (columnName.equals("C_车轮制动__备胎规格")) {
                compare.method_替换数据成统一的格式(columnName, "无备胎", "无");
            } else if (columnName.contains("分辨率")) {
                compare.method_替换数据成统一的格式(columnName, " ", "");
            } else if (columnName.contains("摄像头像素")) {
                compare.method_替换数据成统一的格式(columnName, "万", "");
            }
        }
    }

    // 汽车之家单个网站的数据规范
    public void method_规范数据_汽车之家() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
        for (int kk = 0; kk < columnsList.size(); kk++) {
            String columnName = columnsList.get(kk);
            ArrayList<String> dataResult = compare.method_获取去重的单列数据_汽车之家(columnName);
            for (int i = 0; i < dataResult.size(); i++) {
                // 对比所有数据，如果长度一致则对比相似度
                for (int j = 0; j < dataResult.size(); j++) {
                    String preString = dataResult.get(i);
                    String nextString = dataResult.get(j);
                    String preStringReplace = preString.replace(" ", "");
                    String nextStringReplace = nextString.replace(" ", "");
                    if (columnName.contains("备胎") || columnName.contains("轮胎")) {
                        preStringReplace = preStringReplace.replace("/", "").trim();
                        nextStringReplace = nextStringReplace.replace("/", "").trim();
                    }
                    if (columnName.equals("C_驾驶硬件__辅助驾驶芯片")) {
                        preStringReplace = preStringReplace.replace("-", "").trim();
                        nextStringReplace = nextStringReplace.replace("-", "").trim();
                    }
                    if (columnName.equals("C_驾驶硬件__激光雷达型号")) {
                        preStringReplace = preStringReplace.replace("-", "").trim();
                        nextStringReplace = nextStringReplace.replace("-", "").trim();
                    }
                    if (columnName.equals("C_方向盘_内后视镜__换挡形式")) {
                        preStringReplace = preStringReplace.replace("式", "").trim();
                        nextStringReplace = nextStringReplace.replace("式", "").trim();
                    }
                    if (columnName.contains("悬架类型")) {
                        preStringReplace = preStringReplace.replace("式", "").trim();
                        nextStringReplace = nextStringReplace.replace("式", "").trim();
                    }
                    if (columnName.equals("C_底盘转向__中央差速器结构")) {
                        preStringReplace = preStringReplace.replace("差速器", "").trim();
                        nextStringReplace = nextStringReplace.replace("差速器", "").trim();
                    }
                    if (columnName.equals("C_驾驶硬件__激光雷达型号")) {
                        preStringReplace = preStringReplace.replace("-", "").trim();
                        nextStringReplace = nextStringReplace.replace("-", "").trim();
                    }
                    // preStringReplace 和 nextStringReplace 都不带有实心圈或空心圈  若数据一致那么首先长度要一致
                    if (preStringReplace.equals(nextStringReplace) && preString.length() != nextString.length()) {
                        String replaceResult = preStringReplace.replace(nextStringReplace, "");
                        String replaceResult2 = nextStringReplace.replace(preStringReplace, "");
                        if (replaceResult.equals("") && replaceResult2.equals("")) {
                            if (preString.length() >= nextString.length()) {
                                compare.method_修改可以规范的数据_汽车之家(columnName, preString, nextString);
                            } else {
                                compare.method_修改可以规范的数据_汽车之家(columnName, nextString, preString);
                            }
                        }
                    }
                    // preStringReplace 和 nextStringReplace 一个带实心圈一个不带实心圈
                    if (!preStringReplace.equals("●") && !nextStringReplace.equals("●") && !preString.equals(nextString) && !nextStringReplace.equals("○") && !preStringReplace.equals("○")) {
                        if (preStringReplace.contains("●") && !nextStringReplace.contains("●") && !preStringReplace.contains("○") && !nextStringReplace.contains("○")) {
                            if (preStringReplace.replace("●", "").replace(nextStringReplace, "").equals("")) {
                                if (preString.contains("●") && !nextString.contains("●") && preString.replace(" ", "").length() >= nextString.replace(" ", "").length()) {
                                    compare.method_修改可以规范的数据_汽车之家(columnName, preString, nextString);
                                } else if (!preString.contains("●") && nextString.contains("●") && preString.replace(" ", "").length() < nextString.replace(" ", "").length()) {
                                    compare.method_修改可以规范的数据_汽车之家(columnName, nextString, preString);
                                }
                            }
                        } else if (!preStringReplace.contains("●") && nextStringReplace.contains("●") && !preStringReplace.contains("○") && !nextStringReplace.contains("○")) {
                            if (nextStringReplace.replace("●", "").replace(preStringReplace, "").equals("")) {
                                if (preString.contains("●") && !nextString.contains("●") && preString.replace(" ", "").length() >= nextString.replace(" ", "").length()) {
                                    compare.method_修改可以规范的数据_汽车之家(columnName, preString, nextString);
                                } else if (!preString.contains("●") && nextString.contains("●") && preString.replace(" ", "").length() < nextString.replace(" ", "").length()) {
                                    compare.method_修改可以规范的数据_汽车之家(columnName, nextString, preString);
                                }
                            }
                        }
                    }
//        // 数据位置
                    if (!preString.equals(nextString) && preStringReplace.length() == nextStringReplace.length()) {
                        // 作为判断数值是否相同的条件值
                        String tempString = nextStringReplace;
                        // 实心圈或空心圈在nextStringReplace中的索引值
                        int stringIndex = 0;
                        // 循环preStringReplace 找出实际的实心圈或空心圈的索引值
                        for (int k = 0; k < preStringReplace.length(); k++) {
                            String a = String.valueOf(preStringReplace.charAt(k));
                            if ((a.equals("○") || a.equals("●")) && k != 0) {
                                String replaceString = preStringReplace.substring(stringIndex, k);
                                // 为了排除●8○10.25 ○10●8.25 这种数据被错误规范 要求替换时 选中的字符下一个必须是实心圈或者空心圈
                                // 使用正则表达式查找目标字符串的所有匹配项
                                Pattern pattern = Pattern.compile(Pattern.quote(replaceString));
                                Matcher matcher = pattern.matcher(tempString);
                                // 用来存储匹配到的索引
                                ArrayList<Integer> indices = new ArrayList<>();
                                // 查找所有匹配项
                                while (matcher.find()) {
                                    indices.add(matcher.start()); // 获取匹配的起始索引
                                }
                                for (int l = 0; l < indices.size(); l++) {
                                    int indexReplace = indices.get(l);
                                    int replaceStringLength = replaceString.length();
                                    if (indexReplace + replaceStringLength >= tempString.length()) {
                                        // 如果匹配的部分值在nextStringReplace中恰好是最后一部分且两个比较的部分长度相同即可判断两部分是否相同
                                        if (tempString.substring(indices.get(l)).equals(replaceString)) {
                                            tempString = tempString.substring(0, indices.get(l));
                                        }
                                    } else {
                                        String panduanString = tempString.substring(indices.get(l) + replaceString.length(), indices.get(l) + replaceString.length() + 1);
                                        if (panduanString.equals("●") || panduanString.equals("○")) {// 如果要替换的字符在nextStringReplace中的下一个字符是空心圈或者实心圈才可以替换
                                            tempString = tempString.substring(0, indices.get(l)) + tempString.substring(indices.get(l) + replaceString.length());
                                        }
                                    }
                                }
                                stringIndex = k;
                            }
                        }
                        String lastTempString = preStringReplace.substring(stringIndex);
                        tempString = tempString.replace(lastTempString, "");

                        // 作为判断数值是否相同的另一个条件值
                        String tempString2 = preStringReplace;
                        // 实心圈或空心圈的在preStringReplace中的索引值
                        int stringIndex2 = 0;
                        // 循环preStringReplace 找出实际的实心圈或空心圈的索引值
                        for (int k = 0; k < nextStringReplace.length(); k++) {
                            String a = String.valueOf(nextStringReplace.charAt(k));
                            if ((a.equals("○") || a.equals("●")) && k != 0) {
                                String replaceString = nextStringReplace.substring(stringIndex2, k);
                                // 为了排除●8○10.25 ○10●8.25 这种数据被错误规范 要求替换时 选中的字符下一个必须是实心圈或者空心圈
                                // 使用正则表达式查找目标字符串的所有匹配项
                                Pattern pattern = Pattern.compile(Pattern.quote(replaceString));
                                Matcher matcher = pattern.matcher(tempString2);
                                // 用来存储匹配到的索引
                                ArrayList<Integer> indices = new ArrayList<>();
                                // 查找所有匹配项
                                while (matcher.find()) {
                                    indices.add(matcher.start()); // 获取匹配的起始索引
                                }
                                for (int l = 0; l < indices.size(); l++) {
                                    int indexReplace = indices.get(l);
                                    int replaceStringLength = replaceString.length();
                                    if (indexReplace + replaceStringLength >= tempString2.length()) {
                                        // 如果匹配的部分值在nextStringReplace中恰好是最后一部分且两个比较的部分长度相同即可判断两部分是否相同
                                        if (tempString2.substring(indices.get(l)).equals(replaceString)) {
                                            tempString2 = tempString2.substring(0, indices.get(l));
                                        }
                                    } else {
                                        String panduanString = tempString2.substring(indices.get(l) + replaceString.length(), indices.get(l) + replaceString.length() + 1);
                                        if (panduanString.equals("●") || panduanString.equals("○")) {// 如果要替换的字符在nextStringReplace中的下一个字符是空心圈或者实心圈才可以替换
                                            tempString2 = tempString2.substring(0, indices.get(l)) + tempString2.substring(indices.get(l) + replaceString.length());
                                        }
                                    }
                                }
                                stringIndex2 = k;
                            }
                        }
                        String lastTempString2 = nextStringReplace.substring(stringIndex2);
                        tempString2 = tempString2.replace(lastTempString2, "");
                        if (tempString.equals("") && tempString2.equals("")) {
                            if (preString.length() >= nextString.length()) {
                                compare.method_修改可以规范的数据_汽车之家(columnName, preString, nextString);
                            } else {
                                compare.method_修改可以规范的数据_汽车之家(columnName, nextString, preString);
                            }

                        }
                    }
                    if (!preString.equals(nextString) && preStringReplace.length() != nextStringReplace.length()) {
                        String nextStringLast = nextStringReplace.replace(preStringReplace, "");
                        String preStringLast = preStringReplace.replace(nextStringReplace, "");
                        if (nextStringReplace.contains(preStringLast) && preStringReplace.contains(nextStringLast)) {
                            if (preString.length() <= nextString.length()) {
                                ;
                                compare.method_修改可以规范的数据_汽车之家(columnName, preString, nextString);
                            } else {
                                compare.method_修改可以规范的数据_汽车之家(columnName, nextString, preString);
                            }
                        }
                    }

                }
            }
        }
    }

    public void method_规范数据_易车() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
        for (int kk = 0; kk < columnsList.size(); kk++) {
            String columnName = columnsList.get(kk);
            ArrayList<String> dataResult = compare.method_获取去重的单列数据_易车(columnName);
            for (int i = 0; i < dataResult.size(); i++) {
                // 对比所有数据，如果长度一致则对比相似度
                for (int j = 0; j < dataResult.size(); j++) {
                    String preString = dataResult.get(i);
                    String nextString = dataResult.get(j);
                    String preStringReplace = preString.replace(" ", "");
                    String nextStringReplace = nextString.replace(" ", "");
                    // 需要进行替换操作的字段以及处理过程
                    if (columnName.contains("备胎") || columnName.contains("轮胎")) {
                        preStringReplace = preStringReplace.replace("/", "").trim();
                        nextStringReplace = nextStringReplace.replace("/", "").trim();
                    }
                    if (columnName.equals("C_驾驶硬件__辅助驾驶芯片")) {
                        preStringReplace = preStringReplace.replace("-", "").trim();
                        nextStringReplace = nextStringReplace.replace("-", "").trim();
                    }
                    if (columnName.equals("C_驾驶硬件__激光雷达型号")) {
                        preStringReplace = preStringReplace.replace("-", "").trim();
                        nextStringReplace = nextStringReplace.replace("-", "").trim();
                    }
                    if (columnName.equals("C_方向盘_内后视镜__换挡形式")) {
                        preStringReplace = preStringReplace.replace("式", "").trim();
                        nextStringReplace = nextStringReplace.replace("式", "").trim();
                    }
                    if (columnName.contains("悬架类型")) {
                        preStringReplace = preStringReplace.replace("式", "").trim();
                        nextStringReplace = nextStringReplace.replace("式", "").trim();
                    }
                    if (columnName.equals("C_底盘转向__中央差速器结构")) {
                        preStringReplace = preStringReplace.replace("差速器", "").trim();
                        nextStringReplace = nextStringReplace.replace("差速器", "").trim();
                    }
                    if (columnName.equals("C_驾驶硬件__激光雷达型号")) {
                        preStringReplace = preStringReplace.replace("-", "").trim();
                        nextStringReplace = nextStringReplace.replace("-", "").trim();
                    }
                    // preStringReplace 和 nextStringReplace 都不带有实心圈或空心圈  若数据一致那么首先长度要一致
                    if (preStringReplace.equals(nextStringReplace) && preString.length() != nextString.length()) {
                        String replaceResult = preStringReplace.replace(nextStringReplace, "");
                        String replaceResult2 = nextStringReplace.replace(preStringReplace, "");
                        if (replaceResult.equals("") && replaceResult2.equals("")) {
                            if (preString.length() >= nextString.length()) {
                                compare.method_修改可以规范的数据_易车(columnName, preString, nextString);
                            } else {
                                compare.method_修改可以规范的数据_易车(columnName, nextString, preString);
                            }
                        }
                    }
                    // preStringReplace 和 nextStringReplace 一个带实心圈一个不带实心圈
                    if (!preStringReplace.equals("●") && !nextStringReplace.equals("●") && !preString.equals(nextString) && !nextStringReplace.equals("○") && !preStringReplace.equals("○")) {
                        if (preStringReplace.contains("●") && !nextStringReplace.contains("●") && !preStringReplace.contains("○") && !nextStringReplace.contains("○")) {
                            if (preStringReplace.replace("●", "").replace(nextStringReplace, "").equals("")) {
                                if (preString.contains("●") && !nextString.contains("●") && preString.replace(" ", "").length() >= nextString.replace(" ", "").length()) {
                                    compare.method_修改可以规范的数据_易车(columnName, preString, nextString);
                                } else if (!preString.contains("●") && nextString.contains("●") && preString.replace(" ", "").length() < nextString.replace(" ", "").length()) {
                                    compare.method_修改可以规范的数据_易车(columnName, nextString, preString);
                                }
                            }
                        } else if (!preStringReplace.contains("●") && nextStringReplace.contains("●") && !preStringReplace.contains("○") && !nextStringReplace.contains("○")) {
                            if (nextStringReplace.replace("●", "").replace(preStringReplace, "").equals("")) {
                                if (preString.contains("●") && !nextString.contains("●") && preString.replace(" ", "").length() >= nextString.replace(" ", "").length()) {
                                    compare.method_修改可以规范的数据_易车(columnName, preString, nextString);
                                } else if (!preString.contains("●") && nextString.contains("●") && preString.replace(" ", "").length() < nextString.replace(" ", "").length()) {
                                    compare.method_修改可以规范的数据_易车(columnName, nextString, preString);
                                }
                            }
                        }
                    }
                    if (!preString.equals(nextString) && preStringReplace.length() == nextStringReplace.length()) {
                        // 作为判断数值是否相同的条件值
                        String tempString = nextStringReplace;
                        // 实心圈或空心圈在nextStringReplace中的索引值
                        int stringIndex = 0;
                        // 循环preStringReplace 找出实际的实心圈或空心圈的索引值
                        for (int k = 0; k < preStringReplace.length(); k++) {
                            String a = String.valueOf(preStringReplace.charAt(k));
                            if ((a.equals("○") || a.equals("●")) && k != 0) {
                                String replaceString = preStringReplace.substring(stringIndex, k);
                                // 为了排除●8○10.25 ○10●8.25 这种数据被错误规范 要求替换时 选中的字符下一个必须是实心圈或者空心圈
                                // 使用正则表达式查找目标字符串的所有匹配项
                                Pattern pattern = Pattern.compile(Pattern.quote(replaceString));
                                Matcher matcher = pattern.matcher(tempString);
                                // 用来存储匹配到的索引
                                ArrayList<Integer> indices = new ArrayList<>();
                                // 查找所有匹配项
                                while (matcher.find()) {
                                    indices.add(matcher.start()); // 获取匹配的起始索引
                                }
                                for (int l = 0; l < indices.size(); l++) {
                                    int indexReplace = indices.get(l);
                                    int replaceStringLength = replaceString.length();
                                    if (indexReplace + replaceStringLength >= tempString.length()) {
                                        // 如果匹配的部分值在nextStringReplace中恰好是最后一部分且两个比较的部分长度相同即可判断两部分是否相同
                                        if (tempString.substring(indices.get(l)).equals(replaceString)) {
                                            tempString = tempString.substring(0, indices.get(l));
                                        }
                                    } else {
                                        String panduanString = tempString.substring(indices.get(l) + replaceString.length(), indices.get(l) + replaceString.length() + 1);
                                        if (panduanString.equals("●") || panduanString.equals("○")) {// 如果要替换的字符在nextStringReplace中的下一个字符是空心圈或者实心圈才可以替换
                                            tempString = tempString.substring(0, indices.get(l)) + tempString.substring(indices.get(l) + replaceString.length());
                                        }
                                    }
                                }
                                stringIndex = k;
                            }
                        }
                        String lastTempString = preStringReplace.substring(stringIndex);
                        tempString = tempString.replace(lastTempString, "");

                        // 作为判断数值是否相同的另一个条件值
                        String tempString2 = preStringReplace;
                        // 实心圈或空心圈的在preStringReplace中的索引值
                        int stringIndex2 = 0;
                        // 循环preStringReplace 找出实际的实心圈或空心圈的索引值
                        for (int k = 0; k < nextStringReplace.length(); k++) {
                            String a = String.valueOf(nextStringReplace.charAt(k));
                            if ((a.equals("○") || a.equals("●")) && k != 0) {
                                String replaceString = nextStringReplace.substring(stringIndex2, k);
                                // 为了排除●8○10.25 ○10●8.25 这种数据被错误规范 要求替换时 选中的字符下一个必须是实心圈或者空心圈
                                // 使用正则表达式查找目标字符串的所有匹配项
                                Pattern pattern = Pattern.compile(Pattern.quote(replaceString));
                                Matcher matcher = pattern.matcher(tempString2);
                                // 用来存储匹配到的索引
                                ArrayList<Integer> indices = new ArrayList<>();
                                // 查找所有匹配项
                                while (matcher.find()) {
                                    indices.add(matcher.start()); // 获取匹配的起始索引
                                }
                                for (int l = 0; l < indices.size(); l++) {
                                    int indexReplace = indices.get(l);
                                    int replaceStringLength = replaceString.length();
                                    if (indexReplace + replaceStringLength >= tempString2.length()) {
                                        // 如果匹配的部分值在nextStringReplace中恰好是最后一部分且两个比较的部分长度相同即可判断两部分是否相同
                                        if (tempString2.substring(indices.get(l)).equals(replaceString)) {
                                            tempString2 = tempString2.substring(0, indices.get(l));
                                        }
                                    } else {
                                        String panduanString = tempString2.substring(indices.get(l) + replaceString.length(), indices.get(l) + replaceString.length() + 1);
                                        if (panduanString.equals("●") || panduanString.equals("○")) {// 如果要替换的字符在nextStringReplace中的下一个字符是空心圈或者实心圈才可以替换
                                            tempString2 = tempString2.substring(0, indices.get(l)) + tempString2.substring(indices.get(l) + replaceString.length());
                                        }
                                    }
                                }
                                stringIndex2 = k;
                            }
                        }
                        String lastTempString2 = nextStringReplace.substring(stringIndex2);
                        tempString2 = tempString2.replace(lastTempString2, "");
                        if (tempString.equals("") && tempString2.equals("")) {
                            if (preString.length() >= nextString.length()) {
                                compare.method_修改可以规范的数据_易车(columnName, preString, nextString);
                            } else {
                                compare.method_修改可以规范的数据_易车(columnName, nextString, preString);
                            }

                        }
                    }
                    if (!preString.equals(nextString) && preStringReplace.length() != nextStringReplace.length()) {
                        String nextStringLast = nextStringReplace.replace(preStringReplace, "");
                        String preStringLast = preStringReplace.replace(nextStringReplace, "");
                        if (nextStringReplace.contains(preStringLast) && preStringReplace.contains(nextStringLast)) {
                            if (preString.length() <= nextString.length()) {
                                ;
                                compare.method_修改可以规范的数据_易车(columnName, preString, nextString);
                            } else {
                                compare.method_修改可以规范的数据_易车(columnName, nextString, preString);
                            }
                        }
                    }

                }
            }
        }
    }

    public void method_规范数据_汽车之家_易车() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
        for (int kk = 0; kk < columnsList.size(); kk++) {
            String columnName = columnsList.get(kk);
            if (!columnName.contains("mm") && !columnName.equals("C_基本参数__厂商指导价_元")) {
                ArrayList<String> dataResult = compare.method_获取去重的单列数据_汽车之家(columnName);
                ArrayList<String> dataResult2 = compare.method_获取去重的单列数据_易车(columnName);
                for (int i = 0; i < dataResult.size(); i++) {
                    // 对比所有数据，如果长度一致则对比相似度
                    for (int j = 0; j < dataResult2.size(); j++) {
                        String preString = dataResult.get(i);
                        String nextString = dataResult2.get(j);
                        String preStringReplace = preString.replace(" ", "");
                        String nextStringReplace = nextString.replace(" ", "");
                        method_规范数据通用(columnName, preString, nextString, preStringReplace, nextStringReplace);
                    }
                }
            }
        }
    }

    public void method_规范数据_实心圈位置() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
        for (int kk = 0; kk < columnsList.size(); kk++) {
            String columnName = columnsList.get(kk);
            ArrayList<String> dataResult = compare.method_获取去重的单列数据(columnName);
            for (int i = 0; i < dataResult.size(); i++) {
                String tempString = dataResult.get(i);
                if (tempString.length() > 1 && !columnName.contains("主_副") && !columnName.contains("前_后")) {
                    String finalString = tempString.substring(tempString.length() - 1);
                    if (finalString.equals("●") || finalString.equals("○")) {
                        String rightValue = tempString.substring(tempString.length() - 1) + tempString.substring(0, tempString.length() - 1);
                        compare.method_修改可以规范的数据(columnName, rightValue, tempString);
                    }
                }
            }
        }
    }

    public void method_规范数据通用(String columnName, String preString, String nextString, String preStringReplace, String nextStringReplace) {
        T_Compare compare = new T_Compare(2, 1, 3);
        // 需要进行替换操作的字段以及处理过程
        if (columnName.contains("备胎") || columnName.contains("轮胎")) {
            preStringReplace = preStringReplace.replace("/", "").trim();
            nextStringReplace = nextStringReplace.replace("/", "").trim();
        }
        if (columnName.equals("C_驾驶硬件__辅助驾驶芯片")) {
            preStringReplace = preStringReplace.replace("-", "").trim();
            nextStringReplace = nextStringReplace.replace("-", "").trim();
        }
        if (columnName.equals("C_驾驶硬件__激光雷达型号")) {
            preStringReplace = preStringReplace.replace("-", "").trim();
            nextStringReplace = nextStringReplace.replace("-", "").trim();
        }
        if (columnName.equals("C_方向盘_内后视镜__换挡形式")) {
            preStringReplace = preStringReplace.replace("式", "").trim();
            nextStringReplace = nextStringReplace.replace("式", "").trim();
        }
        if (columnName.contains("悬架类型")) {
            preStringReplace = preStringReplace.replace("式", "").trim();
            nextStringReplace = nextStringReplace.replace("式", "").trim();
        }
        if (columnName.equals("C_底盘转向__中央差速器结构")) {
            preStringReplace = preStringReplace.replace("差速器", "").trim();
            nextStringReplace = nextStringReplace.replace("差速器", "").trim();
        }
        if (columnName.equals("C_驾驶硬件__激光雷达型号")) {
            preStringReplace = preStringReplace.replace("-", "").trim();
            nextStringReplace = nextStringReplace.replace("-", "").trim();
        }
        // preStringReplace 和 nextStringReplace 都不带有实心圈或空心圈  若数据一致那么首先长度要一致
//        System.out.println(preString + "\t" + nextString);
        if (preStringReplace.equals(nextStringReplace) && preString.length() != nextString.length()) {
            String replaceResult = preStringReplace.replace(nextStringReplace, "");
            String replaceResult2 = nextStringReplace.replace(preStringReplace, "");
            if (replaceResult.equals("") && replaceResult2.equals("")) {
                if (preString.length() >= nextString.length()) {
                    compare.method_修改可以规范的数据(columnName, preString, nextString);
                } else {
                    compare.method_修改可以规范的数据(columnName, nextString, preString);
                }
            }
        }
        // preStringReplace 和 nextStringReplace 一个带实心圈一个不带实心圈
        if (!preStringReplace.equals("●") && !nextStringReplace.equals("●") && !preString.equals(nextString) && !nextStringReplace.equals("○") && !preStringReplace.equals("○")) {
            if (preStringReplace.contains("●") && !nextStringReplace.contains("●") && !preStringReplace.contains("○") && !nextStringReplace.contains("○")) {
                if (preStringReplace.replace("●", "").replace(nextStringReplace, "").equals("")) {
                    if (preString.contains("●") && !nextString.contains("●") && preString.replace(" ", "").length() >= nextString.replace(" ", "").length()) {
                        compare.method_修改可以规范的数据(columnName, preString, nextString);
                    } else if (!preString.contains("●") && nextString.contains("●") && preString.replace(" ", "").length() < nextString.replace(" ", "").length()) {
                        compare.method_修改可以规范的数据(columnName, nextString, preString);
                    }
                }
            } else if (!preStringReplace.contains("●") && nextStringReplace.contains("●") && !preStringReplace.contains("○") && !nextStringReplace.contains("○")) {
                if (nextStringReplace.replace("●", "").replace(preStringReplace, "").equals("")) {
                    if (preString.contains("●") && !nextString.contains("●") && preString.replace(" ", "").length() >= nextString.replace(" ", "").length()) {
                        compare.method_修改可以规范的数据(columnName, preString, nextString);
                    } else if (!preString.contains("●") && nextString.contains("●") && preString.replace(" ", "").length() < nextString.replace(" ", "").length()) {
                        compare.method_修改可以规范的数据(columnName, nextString, preString);
                    }
                }
            }
        }
//        // 数据位置
        if (!preString.equals(nextString) && preStringReplace.length() == nextStringReplace.length()) {
            // 作为判断数值是否相同的条件值
            String tempString = nextStringReplace;
            // 实心圈或空心圈在nextStringReplace中的索引值
            int stringIndex = 0;
            // 循环preStringReplace 找出实际的实心圈或空心圈的索引值
            for (int k = 0; k < preStringReplace.length(); k++) {
                String a = String.valueOf(preStringReplace.charAt(k));
                if ((a.equals("○") || a.equals("●")) && k != 0) {
                    String replaceString = preStringReplace.substring(stringIndex, k);
                    // 为了排除●8○10.25 ○10●8.25 这种数据被错误规范 要求替换时 选中的字符下一个必须是实心圈或者空心圈
                    // 使用正则表达式查找目标字符串的所有匹配项
                    Pattern pattern = Pattern.compile(Pattern.quote(replaceString));
                    Matcher matcher = pattern.matcher(tempString);
                    // 用来存储匹配到的索引
                    ArrayList<Integer> indices = new ArrayList<>();
                    // 查找所有匹配项
                    while (matcher.find()) {
                        indices.add(matcher.start()); // 获取匹配的起始索引
                    }
                    for (int i = 0; i < indices.size(); i++) {
                        int indexReplace = indices.get(i);
                        int replaceStringLength = replaceString.length();
                        if (indexReplace + replaceStringLength >= tempString.length()) {
                            // 如果匹配的部分值在nextStringReplace中恰好是最后一部分且两个比较的部分长度相同即可判断两部分是否相同
                            if (tempString.substring(indices.get(i)).equals(replaceString)) {
                                tempString = tempString.substring(0, indices.get(i));
                            }
                        } else {
                            String panduanString = tempString.substring(indices.get(i) + replaceString.length(), indices.get(i) + replaceString.length() + 1);
                            if (panduanString.equals("●") || panduanString.equals("○")) {// 如果要替换的字符在nextStringReplace中的下一个字符是空心圈或者实心圈才可以替换
                                tempString = tempString.substring(0, indices.get(i)) + tempString.substring(indices.get(i) + replaceString.length());
                            }
                        }
                    }
                    stringIndex = k;
                }
            }
            String lastTempString = preStringReplace.substring(stringIndex);
            tempString = tempString.replace(lastTempString, "");

            // 作为判断数值是否相同的另一个条件值
            String tempString2 = preStringReplace;
            // 实心圈或空心圈的在preStringReplace中的索引值
            int stringIndex2 = 0;
            // 循环preStringReplace 找出实际的实心圈或空心圈的索引值
            for (int k = 0; k < nextStringReplace.length(); k++) {
                String a = String.valueOf(nextStringReplace.charAt(k));
                if ((a.equals("○") || a.equals("●")) && k != 0) {
                    String replaceString = nextStringReplace.substring(stringIndex2, k);
                    // 为了排除●8○10.25 ○10●8.25 这种数据被错误规范 要求替换时 选中的字符下一个必须是实心圈或者空心圈
                    // 使用正则表达式查找目标字符串的所有匹配项
                    Pattern pattern = Pattern.compile(Pattern.quote(replaceString));
                    Matcher matcher = pattern.matcher(tempString2);
                    // 用来存储匹配到的索引
                    ArrayList<Integer> indices = new ArrayList<>();
                    // 查找所有匹配项
                    while (matcher.find()) {
                        indices.add(matcher.start()); // 获取匹配的起始索引
                    }
                    for (int i = 0; i < indices.size(); i++) {
                        int indexReplace = indices.get(i);
                        int replaceStringLength = replaceString.length();
                        if (indexReplace + replaceStringLength >= tempString2.length()) {
                            // 如果匹配的部分值在nextStringReplace中恰好是最后一部分且两个比较的部分长度相同即可判断两部分是否相同
                            if (tempString2.substring(indices.get(i)).equals(replaceString)) {
                                tempString2 = tempString2.substring(0, indices.get(i));
                            }
                        } else {
                            String panduanString = tempString2.substring(indices.get(i) + replaceString.length(), indices.get(i) + replaceString.length() + 1);
                            if (panduanString.equals("●") || panduanString.equals("○")) {// 如果要替换的字符在nextStringReplace中的下一个字符是空心圈或者实心圈才可以替换
                                tempString2 = tempString2.substring(0, indices.get(i)) + tempString2.substring(indices.get(i) + replaceString.length());
                            }
                        }
                    }
                    stringIndex2 = k;
                }
            }
            String lastTempString2 = nextStringReplace.substring(stringIndex2);
            tempString2 = tempString2.replace(lastTempString2, "");
            if (tempString.equals("") && tempString2.equals("")) {
                if (preString.length() >= nextString.length()) {
                    compare.method_修改可以规范的数据(columnName, preString, nextString);
                } else {
                    compare.method_修改可以规范的数据(columnName, nextString, preString);
                }

            }
        }
        if (!preString.equals(nextString) && preStringReplace.length() != nextStringReplace.length()) {
            String nextStringLast = nextStringReplace.replace(preStringReplace, "");
            String preStringLast = preStringReplace.replace(nextStringReplace, "");
            if (nextStringReplace.contains(preStringLast) && preStringReplace.contains(nextStringLast)) {
                if (preString.length() <= nextString.length()) {
                    ;
                    compare.method_修改可以规范的数据(columnName, preString, nextString);
                } else {
                    compare.method_修改可以规范的数据(columnName, nextString, preString);
                }
            }
        }
    }

    public void method_座位数的数据规范() {
        T_Compare compare = new T_Compare(2, 1, 3);
        String columnName = "C_车身__座位数_个";
        ArrayList<String> dataResult = compare.method_获取去重后的座位数数据_含有横杠字符的(columnName);
        ArrayList<String> dataResult2 = compare.method_获取去重后的座位数数据_含有斜杠字符的(columnName);
        for (int i = 0; i < dataResult.size(); i++) {
            // 对比所有数据，如果长度一致则对比相似度
            for (int j = 0; j < dataResult2.size(); j++) {
                String preString = dataResult.get(i);
                String nextString = dataResult2.get(j);
                String[] tempStringList = preString.split("-");
                StringBuffer tempBuffer = new StringBuffer();
                int startNum = Integer.parseInt(tempStringList[0].replace("●", ""));
                int endNum = Integer.parseInt(tempStringList[1]);
                String[] tempNextStringList = nextString.replace("●", "").split("/");
                int startNextNum = Integer.parseInt(tempNextStringList[0]);
                int endNextNum = Integer.parseInt(tempNextStringList[tempNextStringList.length - 1]);

                if ((endNum - startNum + 1) == tempNextStringList.length) {
                    for (int k = 0; k < tempNextStringList.length; k++) {
                        int nextValue = Integer.parseInt(tempNextStringList[k]);
                        if (nextValue >= startNum && nextValue <= endNum) {
                            tempBuffer.append("1");
                        } else {
                            tempBuffer.append("0");
                        }
                    }
                    if (!tempBuffer.toString().contains("0") && startNum == startNextNum && endNum == endNextNum) {
                        compare.method_修改可以规范的数据(columnName, preString, nextString);
                    }
                    tempBuffer = new StringBuffer();
                }
            }
        }
    }

    public void method_中文数字和阿拉伯数字不统一的数据规范() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnList = new ArrayList<>();
        columnList.add("C_电动机__电池组质保");
        columnList.add("C_基本参数__首任车主质保政策");
        columnList.add("C_基本参数__整车质保");
        for (String columnName : columnList) {
            ArrayList<String> dataResult = compare.method_获取去重的单列数据(columnName);
            for (int i = 0; i < dataResult.size(); i++) {
                String valueString = dataResult.get(i);
                if (valueString.contains("十") || valueString.contains("九") || valueString.contains("八") || valueString.contains("七") || valueString.contains("六") || valueString.contains("五") || valueString.contains("四") || valueString.contains("三") || valueString.contains("二") || valueString.contains("一") || valueString.contains("两")) {
                    String rightValue = T_Dao_Config.method_汉字数字转化成阿拉伯数字(valueString);
                    compare.method_修改可以规范的数据(columnName, rightValue, valueString);
                }
            }
        }
    }

    public void method_电芯品牌的数据规范() {
        T_Compare compare = new T_Compare(2, 1, 3);
        String columnName = "C_电动机__电芯品牌";
        ArrayList<String> dataResult = compare.method_获取去重后的电芯品牌数据_含有实心圈或者空心圈(columnName);
        ArrayList<String> dataResult2 = compare.method_获取去重后的电芯品牌数据_含有斜杠字符的(columnName);
        for (int i = 0; i < dataResult.size(); i++) {
            // 对比所有数据，如果长度一致则对比相似度
            for (int j = 0; j < dataResult2.size(); j++) {
                String preString = dataResult.get(i);
                String nextString = dataResult2.get(j);
                String tempPreString = preString.replace("●", "");
                String tempNextString = nextString.replace("/", "");
                String[] preStringList = preString.split("●");
                String[] nextStringList = nextString.split("/");
                for (int k = 0; k < preStringList.length; k++) {
                    tempNextString = tempNextString.replace(preStringList[k], "");
                }
                for (int k = 0; k < nextStringList.length; k++) {
                    tempPreString = tempPreString.replace(nextStringList[k], "");
                }
                if (tempNextString.equals("") && tempPreString.equals("")) {
                    compare.method_修改可以规范的数据(columnName, preString, nextString);
                }
            }
        }
    }

    public void method_数据规范() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
        for (int kk = 0; kk < columnsList.size(); kk++) {
            String columnName = columnsList.get(kk);
            if (!columnName.contains("mm") && !columnName.equals("C_基本参数__厂商指导价_元")) {
                ArrayList<String> dataResult = compare.method_获取去重的单列数据(columnName);
                ArrayList<String> dataResult2 = compare.method_获取去重的单列数据(columnName);
                for (int i = 0; i < dataResult.size(); i++) {
                    // 对比所有数据，如果长度一致则对比相似度
                    for (int j = 0; j < dataResult2.size(); j++) {
                        String preString = dataResult.get(i);
                        String nextString = dataResult2.get(j);
                        String preStringReplace = preString.replace(" ", "");
                        String nextStringReplace = nextString.replace(" ", "");
                        method_规范数据通用(columnName, preString, nextString, preStringReplace, nextStringReplace);
                    }
                }
            }

        }
    }

    public void method_选配带有价格的数据规范() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
        for (int kk = 0; kk < columnsList.size(); kk++) {
            String columnName = columnsList.get(kk);
            ArrayList<String> dataResult = compare.method_获取去重后的选配带有价格的数据(columnName);
            for (int i = 0; i < dataResult.size(); i++) {
                // 对比所有数据，如果长度一致则对比相似度
                String preString = dataResult.get(i);
                String preStringReplace = preString;
                String rightValue = "";
                Pattern pattern = Pattern.compile(Pattern.quote("元"));
                Matcher matcher = pattern.matcher(preStringReplace);
                // 用来存储匹配到的索引
                ArrayList<Integer> indices = new ArrayList<>();
                // 查找所有匹配项
                while (matcher.find()) {
                    indices.add(matcher.start()); // 获取匹配的起始索引
                }
                for (int k = 0; k < indices.size(); k++) {
                    if (preStringReplace.contains("○") && preStringReplace.contains("元")) {
                        String priceValue = preStringReplace.substring(preStringReplace.indexOf("["), preStringReplace.indexOf("]") + 1);
                        rightValue = preStringReplace.replace(priceValue, "").replace("选配", "");
                        preStringReplace = rightValue;
                    }
                }
                if (rightValue.equals("○○")) {
                    rightValue = rightValue.replace("○○", "○");
                }
                compare.method_修改可以规范的数据(columnName, rightValue, preString);
            }
        }
    }

    public void method_辅助驾驶系统() {
        T_Compare compare = new T_Compare(2, 1, 3);
        String columnName = "C_驾驶功能__辅助驾驶系统";
        ArrayList<String> dataResult = compare.method_获取去重的单列数据(columnName);
        for (int i = 0; i < dataResult.size(); i++) {
            String preString = dataResult.get(i);
//            String preStringReplace = preString.replace(" ", "").replace("-", "").replace("・", "").replace("–", "").replace("●", "").replace("○", "");
            compare.method_修改可以规范的数据(columnName, preString.toLowerCase().replace(" ", "").replace("-", "").replace("・", "").replace("–", ""), preString);
        }
    }

    public void method_找出有必要规范的字段() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<String> columnsList = T_CompareField.method_除Id外所有字段();
        for (String columnName : columnsList) {
            if (!columnName.contains("厂商")) {
                ArrayList<String> dataResult = compare.method_获取去重的单列数据(columnName);
                if (dataResult.size() > 3 && dataResult.size() < 4000) {
                    String listValue = dataResult.toString().substring(1, dataResult.toString().length() - 1).replace(",", "").replace(".", "").replace(" ", "").replace("-", "").replace("待查", "");
                    Pattern pattern = Pattern.compile("[0-9]*");
                    Matcher matcher = pattern.matcher((CharSequence) listValue);
                    if (!matcher.matches()) {
                        System.out.println("select distinct " + columnName + ",'" + columnName + "' from T_汽车之家_易车_数据处理表 union");
                    }
                }
            }
        }
    }

//     else {


    // 配置
//                if (noSameConfig.toString().equals("") || !sameConfig.toString().equals("")) {
////                    T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "配置匹配上的", ids + "\t" + preVersionName + "\t" + nextVersionName + "\n");
////                    System.out.println("匹配不上\t" + preVersionId + "\t" + nextVersionId+"\t"+preVersionName+"\t"+nextVersionName+"\n");
//
//                }
//                if (!noSameConfig.toString().equals("")) {
//                    if (preVersionName.equals(nextVersionName.replace(" ", ""))) {
//                        T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "配置匹配不上名称匹配上的", ids + "\t" + preVersionName + "\t" + nextVersionName + "\t" + noSameConfig + "\n");
//                    } else {
//                        T_Config_File.method_重复写文件_根据路径创建文件夹(filePath, "配置匹配不上名称也匹配不上", ids + "\t" + preVersionName + "\t" + nextVersionName + "\t" + noSameConfig + "\n");
//                    }
//                }
//    }
}

