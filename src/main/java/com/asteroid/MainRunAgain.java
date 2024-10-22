package com.asteroid;

import com.asteroid.dao.T_Compare;
import com.asteroid.entity.Bean_Two;
import com.wangtiantian.dao.T_Config_File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.asteroid.Main.method_获取excel表头;

public class MainRunAgain {
    public static void main(String[] args) {
        try {
            MainRunAgain again = new MainRunAgain();
            // 交集表
            // new MainRunAgain().method_之后再处理();
            // 差集表
//        again.method_交集表数据();
            test();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void method_交集表数据() {
        T_Compare compare = new T_Compare(2, 1, 3);
        ArrayList<Object> dataList = compare.method_差集表_汽车之家();
        ArrayList<Object> dataList2 = compare.method_差集表_易车();
        for (Object o : dataList) {
            String C_汽车之家_品牌 = ((Bean_Two) o).get_C_品牌();
            String C_汽车之家_厂商 = ((Bean_Two) o).get_C_厂商();
            String C_汽车之家_车型 = ((Bean_Two) o).get_C_车型();
            String C_汽车之家_车款 = ((Bean_Two) o).get_C_车款();
            String C_汽车之家_变速器 = ((Bean_Two) o).get_C_变速器();
            String C_汽车之家_发动机简称 = ((Bean_Two) o).get_C_发动机简称();
            String C_汽车之家_驱动形式 = ((Bean_Two) o).get_C_驱动形式();
            String C_汽车之家_燃料种类 = ((Bean_Two) o).get_C_燃料种类();
            String C_汽车之家_版本名称 = ((Bean_Two) o).get_C_车型名称();
            String C_汽车之家_年 = "";
            if (C_汽车之家_车款.contains("款")) {
                C_汽车之家_年 = C_汽车之家_车款.substring(C_汽车之家_车款.indexOf("款") - 5, C_汽车之家_车款.indexOf("款"));
            }
            for (Object o2 : dataList2) {
                String C_易车_年 = "";
                String C_易车_车款 = ((Bean_Two) o2).get_C_车款();
                if (C_易车_车款.contains("款")) {
                    C_易车_年 = C_易车_车款.substring(((Bean_Two) o2).get_C_车款().indexOf("款") - 5, ((Bean_Two) o2).get_C_车款().indexOf("款"));
                }
                String C_易车_品牌 = ((Bean_Two) o2).get_C_品牌();
                String C_易车_厂商 = ((Bean_Two) o2).get_C_厂商();
                String C_易车_车型 = ((Bean_Two) o2).get_C_车型();
                String C_易车_变速器 = ((Bean_Two) o2).get_C_变速器();
                String C_易车_发动机简称 = ((Bean_Two) o2).get_C_发动机简称();
                String C_易车_驱动形式 = ((Bean_Two) o2).get_C_驱动形式();
                String C_易车_燃料种类 = ((Bean_Two) o2).get_C_燃料种类();
                String C_易车_版本名称 = ((Bean_Two) o).get_C_车型名称();
                if (C_汽车之家_品牌.equals(C_易车_品牌) && C_汽车之家_厂商.equals(C_易车_厂商) && C_汽车之家_车型.equals(C_易车_车型)) {
                    if (!C_汽车之家_年.equals("") && !C_易车_年.equals("")) {
                        if (C_汽车之家_版本名称.equals(C_易车_版本名称)) {
                            System.out.println("年款相同\t" + C_汽车之家_版本名称 + "\t" + C_易车_版本名称 + "\t" + C_汽车之家_变速器 + "\t" + C_汽车之家_发动机简称 + "\t" + C_汽车之家_驱动形式 + "\t" + C_汽车之家_燃料种类 + "\t" + C_易车_变速器 + "\t" + C_易车_发动机简称 + "\t" + C_易车_驱动形式 + "\t" + C_易车_燃料种类);
                        }

                    } else {
                        System.out.println("年款不相同\t" + C_汽车之家_版本名称 + "\t" + C_易车_版本名称 + "\t" + C_汽车之家_变速器 + "\t" + C_汽车之家_发动机简称 + "\t" + C_汽车之家_驱动形式 + "\t" + C_汽车之家_燃料种类 + "\t" + C_易车_变速器 + "\t" + C_易车_发动机简称 + "\t" + C_易车_驱动形式 + "\t" + C_易车_燃料种类);
                    }

                }
            }

        }
    }

    public void method_之后再处理() {
        // 读取匹配结果txt
        ArrayList<String> contentList = T_Config_File.method_按行读取文件("/Users/asteroid/所有文件数据/对比结果/匹配结果备份12.txt");
        List<Map<String, Object>> dataExcelList_名称匹配的上配置匹配的上 = new ArrayList<>();
        List<Map<String, Object>> dataExcelList_名称匹配的上配置匹配不上 = new ArrayList<>();
        List<Map<String, Object>> dataExcelList_名称匹配不上配置匹配的上 = new ArrayList<>();
        List<Map<String, Object>> dataExcelList_名称匹配不上配置匹配不上 = new ArrayList<>();

        for (String rowValue : contentList) {
            String[] rowValueList = rowValue.split("\t");
            String ids = rowValueList[0];
            String qczjId = rowValueList[1];
            String yiCheId = rowValueList[2];
            String qczjName = rowValueList[3];
            String yiCheName = rowValueList[4];
            String columnName = "";
            String qczjValue = "";
            String yiCheValue = "";
            Boolean isEquals = qczjName.replace(" ", "").trim().equals(yiCheName.replace(" ", "").trim());
            if (rowValueList.length > 5) {// 有不同配置的数据
                String noSameConfig = rowValueList[5];
                if (isEquals) {//名称一样
                    if (!noSameConfig.equals("")) { // 配置不一样
                        String[] valuesList = noSameConfig.split("\\|\\|");
                        for (int j = 0; j < valuesList.length; j++) {
                            columnName = valuesList[j].substring(0, valuesList[j].indexOf("=>"));
                            String[] values = valuesList[j].substring(valuesList[j].indexOf("=>") + 2).split("->");
                            qczjValue = values[0];
                            if (values.length > 1) {
                                yiCheValue = values[1];
                            }
                            Map<String, Object> map = new LinkedHashMap<>();
                            map.put("汽车之家版本Id_易车版本Id", ids);
                            map.put("汽车之家版本名称", qczjName);
                            map.put("汽车之家版本Id", qczjId);
                            map.put("易车版本Id", yiCheId);
                            map.put("易车版本名称", yiCheName);
                            map.put("列名", columnName);
                            map.put("值_汽车之家", qczjValue);
                            map.put("值_易车", yiCheValue);
                            dataExcelList_名称匹配的上配置匹配不上.add(map);
                        }
                    }
                } else if (!isEquals) {// 名称一样
                    if (!noSameConfig.equals("")) {//配置不一样
                        String[] valuesList = noSameConfig.split("\\|\\|");
                        for (int j = 0; j < valuesList.length; j++) {
                            columnName = valuesList[j].substring(0, valuesList[j].indexOf("=>"));
                            String[] values = valuesList[j].substring(valuesList[j].indexOf("=>") + 2).split("->");
                            qczjValue = values[0];
                            if (values.length > 1) {
                                yiCheValue = values[1];
                            }
                            Map<String, Object> map = new LinkedHashMap<>();
                            map.put("汽车之家版本Id_易车版本Id", ids);
                            map.put("汽车之家版本名称", qczjName);
                            map.put("汽车之家版本Id", qczjId);
                            map.put("易车版本Id", yiCheId);
                            map.put("易车版本名称", yiCheName);
                            map.put("列名", columnName);
                            map.put("值_汽车之家", qczjValue);
                            map.put("值_易车", yiCheValue);
                            dataExcelList_名称匹配不上配置匹配不上.add(map);

                        }
                    }
                }
            } else {
                if (!isEquals) {
                    Map<String, Object> mapQ = new LinkedHashMap<>();
                    mapQ.put("汽车之家版本Id_易车版本Id", ids);
                    mapQ.put("汽车之家版本名称", qczjName);
                    mapQ.put("汽车之家版本Id", qczjId);
                    mapQ.put("易车版本Id", yiCheId);
                    mapQ.put("易车版本名称", yiCheName);
                    dataExcelList_名称匹配不上配置匹配的上.add(mapQ);
                } else {
                    Map<String, Object> mapQ = new LinkedHashMap<>();
                    mapQ.put("汽车之家版本Id_易车版本Id", ids);
                    mapQ.put("汽车之家版本名称", qczjName);
                    mapQ.put("汽车之家版本Id", qczjId);
                    mapQ.put("易车版本Id", yiCheId);
                    mapQ.put("易车版本名称", yiCheName);
                    dataExcelList_名称匹配的上配置匹配的上.add(mapQ);
                }
            }
        }
        Map<String, List<Map<String, Object>>> dataAllList = new LinkedHashMap<>();
        dataAllList.put("名称匹配的上配置匹配的上", dataExcelList_名称匹配的上配置匹配的上);
        dataAllList.put("名称匹配的上配置匹配不上", dataExcelList_名称匹配的上配置匹配不上);
        dataAllList.put("名称匹配不上配置匹配的上", dataExcelList_名称匹配不上配置匹配的上);
        dataAllList.put("名称匹配不上配置匹配不上", dataExcelList_名称匹配不上配置匹配不上);
//        System.out.println(dataAllList.get("名称匹配的上配置匹配的上"));
//        System.out.println(dataAllList.get("名称匹配的上配置匹配不上"));
//        System.out.println(dataAllList.get("名称匹配不上配置匹配的上"));
//        System.out.println(dataAllList.get("名称匹配不上配置匹配不上"));
        method_写excel(dataAllList);
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
//            System.out.println(sheetNames);

            Map<String, List<String>> headerMap = new HashMap<>();
            for (String sheetName : dataMapList.keySet()) {
                headerMap.put(sheetName, valueHead(sheetName));
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

    public static List<String> valueHead(String sheetName) {
        if (sheetName.contains("配置匹配不上")) {
            List<String> list = new ArrayList<>(Arrays.asList("汽车之家版本Id_易车版本Id", "汽车之家版本Id", "易车版本Id", "汽车之家版本名称", "易车版本名称", "列名", "值_汽车之家", "值_易车"));
            return list;
        } else {
            List<String> list = new ArrayList<>(Arrays.asList("汽车之家版本Id_易车版本Id", "汽车之家版本Id", "易车版本Id", "汽车之家版本名称", "易车版本名称"));
            return list;
        }
    }

    public void method_规范差集表数据() {

    }

    public static void test() throws InvocationTargetException, IllegalAccessException {
//        ReadUntil readUntil = new ReadUntil();
//        List<String> contentList =  readUntil.Method_ReadbyLine("F:\\ZKZD\\Java项目\\Data-crawling-2024\\merge-data-car\\src\\main\\resources\\T_汽车之家_1008.csv");
        List<String> contentList = T_Config_File.method_按行读取文件("/Users/asteroid/所有文件数据/对比结果/差集表_易车.csv");
        List<List<Map<String, Object>>> list = makeBeanListCarHome(contentList);
        System.out.println(list.size());
        System.out.println(list.get(0));
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//        }

        // 获取具体一列的值
//        List<Integer> coumn_valueList = list1.stream().map(Bean_Two::get_C_ID).collect(Collectors.toList());
//
//        for (int i = 0; i < 20; i++) {
//            System.out.println(coumn_valueList.get(i));
//        }

        // 获取部分列
//        List<Map<String, Map<String,String>>> columnList = list1.stream().map(beanCarHome -> {
//            Map<String, Map<String,String>> map = new HashMap<>();
//            Map<String, String> map1 = new HashMap<>();
//            map1.put("C_BrandName", beanCarHome.getC_BrandName());
//            map1.put("C_PID", beanCarHome.getC_PID());
//            map.put(beanCarHome.getC_PID(), map1);
//            return map;
//        }).collect(Collectors.toList());
//        int b = 0;
//        for (Map<String, Map<String,String>> stringObjectMap : columnList) {
//
//            for (String key : stringObjectMap.keySet()) {
//                // 现在可以使用 key 进行操作
//                Map<String, String> map =  stringObjectMap.get(key);
//                // 处理 key 和 value
//                String value = map.get("C_BrandName");
//                System.out.println(value);
//                String value1 = map.get("C_PID");
//                b++;
//            }
//            if (b>20){
//                break;
//            }
//        }

    }


    public static List<List<Map<String, Object>>> makeBeanListCarHome(List<String> content) throws InvocationTargetException, IllegalAccessException {
//        System.out.println(content);
        List<List<Map<String, Object>>> dataList = new ArrayList<>();

        String[] split = content.get(0).split(",");
        int i = 0;
        for (String s : content) {
            List<Map<String, Object>> list = new ArrayList<>();
            if (i != 0) {
                String[] values = s.split(",");
//                Class<?> class1 = Bean_Two.class;
//                Method[] methods = class1.getMethods();
//                Bean_Two beanCarHome = new Bean_Two();
                Map<String, Object> mapList = new HashMap<>();
                for (int j = 0; j < split.length; j++) {
                    String columnName = split[j];
                    String value = values[j];
                    mapList.put(columnName, value);
//                    System.out.println(columnName + "\t" + value);
                }
                list.add(mapList);
            }
            i++;
            dataList.add(list);
        }
        return dataList;
    }

}