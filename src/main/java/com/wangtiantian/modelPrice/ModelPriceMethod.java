package com.wangtiantian.modelPrice;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wangtiantian.dao.T_Config_File;
import com.wangtiantian.entity.Bean_Model;
import com.wangtiantian.entity.Bean_Version;
import com.wangtiantian.entity.price.ConfirmCarPriceFile;
import com.wangtiantian.entity.price.ModelDealerData;
import com.wangtiantian.entity.price.SaleModData;
import com.wangtiantian.mapper.PriceDataBase;
import com.wangtiantian.runPrice.PriceMoreThread;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModelPriceMethod {
    // 1.
    public void method_根据车型id获取经销商数据(String filePath) {
        try {
            ArrayList<Object> modelList = new PriceDataBase().getModelData();
            for (Object o : modelList) {
                String modID = ((Bean_Version) o).get_C_ModelID();
                for (int i = 1; i < 9999; i++) {
                    String interfaceUrl = "https://www.autohome.com.cn/ashx/dealer/AjaxDealersBySeriesId.ashx?seriesId=" + modID + "&cityId=0&provinceId=0&countyId=0&orderType=0&kindId=1&pageindex=" + i;
                    if (!T_Config_File.method_判断文件是否存在(filePath + modID + "_" + i + ".txt")) {
                        Document mainDoc = null;
                        try {
                            mainDoc = Jsoup.parse(new URL(interfaceUrl).openStream(), "UTF-8", interfaceUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        JSONArray jsonObject = JSONObject.parseObject(mainDoc.text()).getJSONObject("result").getJSONArray("list");
                        if (jsonObject.size() == 0) {
                            break;
                        } else {
                            T_Config_File.method_写文件_根据路径创建文件夹(filePath, modID + "_" + i + ".txt", mainDoc.text());
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 2.解析上一步的数据获取，入库经销商的数据和车型的ID的数据
    public void method_解析上一步的车型得到的经销商数据(String filePath) {
        try {
            ArrayList<String> fileNameList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataArrayList = new ArrayList<>();
            for (String fileName : fileNameList) {
                String content = T_Config_File.method_读取文件内容(filePath + fileName);
                JSONArray jsonRoot = JSONObject.parseObject(content).getJSONObject("result").getJSONArray("list");
                for (int i = 0; i < jsonRoot.size(); i++) {
                    StringBuffer brandIds = new StringBuffer();
                    StringBuffer brandNames =new StringBuffer();
                    for (int j = 0; j < ((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getJSONArray("brandList").size(); j++) {
                        brandIds.append(((JSONObject)((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getJSONArray("brandList").get(j)).getString("brandId"));
                        brandNames.append(((JSONObject)((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getJSONArray("brandList").get(j)).getString("brandName"));
                    }
                    ModelDealerData modelDealerData = new ModelDealerData();
                    modelDealerData.set_C_MainBrandImg(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerShowImageOut").getString("mainBrandImg"));
                    modelDealerData.set_C_OutScenceImg(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerShowImageOut").getString("outScenceImg"));
                    modelDealerData.set_C_SaleBrandImg(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerShowImageOut").getString("saleBrandImg"));
                    modelDealerData.set_C_DealerId(((JSONObject) jsonRoot.get(i)).getString("dealerId"));
                    modelDealerData.set_C_DealerInfoId(((JSONObject) jsonRoot.get(i)).getString("dealerInfoId"));
                    modelDealerData.set_C_ModelId(((JSONObject) jsonRoot.get(i)).getString("seriesId"));
                    modelDealerData.set_C_MinNewsPrice(((JSONObject) jsonRoot.get(i)).getString("minNewsPrice"));
                    modelDealerData.set_C_MaxNewsPrice(((JSONObject) jsonRoot.get(i)).getString("maxNewsPrice"));
                    modelDealerData.set_C_MinOriginalPrice(((JSONObject) jsonRoot.get(i)).getString("minOriginalPrice"));
                    modelDealerData.set_C_MaxOriginalPrice(((JSONObject) jsonRoot.get(i)).getString("maxOriginalPrice"));
                    modelDealerData.set_C_MaxPriceOff(((JSONObject) jsonRoot.get(i)).getString("maxPriceOff"));
                    modelDealerData.set_C_ServiceFeedbackScore(((JSONObject) jsonRoot.get(i)).getString("serviceFeedbackScore"));
                    modelDealerData.set_C_KindId(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("kindId"));
                    modelDealerData.set_C_Is24h(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("is24h"));
                    modelDealerData.set_C_DealerName(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("dealerName"));
                    modelDealerData.set_C_CompanySimple(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("companySimple"));
                    modelDealerData.set_C_Address(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("address").replace("'","`"));
                    modelDealerData.set_C_StarLevel(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("starLevel"));
                    modelDealerData.set_C_SellPhone(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("sellPhone"));
                    modelDealerData.set_C_AppSellPhone(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("appSellPhone"));
                    modelDealerData.set_C_ShowPhone(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("showPhone"));
                    modelDealerData.set_C_BusinessArea(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("businessArea"));
                    modelDealerData.set_C_OrderRange(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("orderRange"));
                    modelDealerData.set_C_OrderRangeTitle(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("orderRangeTitle"));
                    modelDealerData.set_C_ProvinceId(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("provinceId"));
                    modelDealerData.set_C_CityId(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("cityId"));
                    modelDealerData.set_C_CountyId(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("countyId"));
                    modelDealerData.set_C_Longitude(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("longitude"));
                    modelDealerData.set_C_Latitude(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("latitude"));
                    modelDealerData.set_C_ProvinceName(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("provinceName"));
                    modelDealerData.set_C_CityName(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("cityName"));
                    modelDealerData.set_C_CountyName(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("countyName"));
                    modelDealerData.set_C_BrandIds(brandIds.toString());
                    modelDealerData.set_C_BrandNames(brandNames.toString());
                    modelDealerData.set_C_PayType(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("payType"));
                    modelDealerData.set_C_CallRate400(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("callRate400")==null?"-":((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("callRate400"));
                    modelDealerData.set_C_LeadsRatingScore(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("leadsRatingScore"));
                    modelDealerData.set_C_TradeReduce(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("tradeReduce"));
                    modelDealerData.set_C_OpenIM(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("openIM"));
                    modelDealerData.set_C_ContractType(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("contractType"));
                    modelDealerData.set_C_UrlH5(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("urlH5"));
                    modelDealerData.set_C_UrlPC(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("urlPC"));
                    modelDealerData.set_C_UrlApp(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("urlApp"));
                    modelDealerData.set_C_SalesLevel(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("salesLevel"));
                    modelDealerData.set_C_IsZhiXiao(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("isZhiXiao"));
                    modelDealerData.set_C_Url400(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("url400"));
                    modelDealerData.set_C_Ext400(((JSONObject) jsonRoot.get(i)).getJSONObject("dealerInfoBaseOut").getString("ext400"));
                    modelDealerData.set_C_StatsParam(((JSONObject) jsonRoot.get(i)).getString("statsParam"));
                    modelDealerData.set_C_IsShow(((JSONObject) jsonRoot.get(i)).getString("isShow"));
                    modelDealerData.set_C_MainDealerSimpleName(((JSONObject) jsonRoot.get(i)).getString("mainDealerSimpleName"));
                    modelDealerData.set_C_MainDealerId(((JSONObject) jsonRoot.get(i)).getString("mainDealerId"));
                    modelDealerData.set_C_KindStr(((JSONObject) jsonRoot.get(i)).getString("kindStr"));
                    modelDealerData.set_C_VirtualShowTips(((JSONObject) jsonRoot.get(i)).getString("virtualShowTips"));
                    modelDealerData.set_C_IsFactory(((JSONObject) jsonRoot.get(i)).getString("isFactory"));
                    modelDealerData.set_C_IsVirtualShop(((JSONObject) jsonRoot.get(i)).getString("isVirtualShop"));
                    modelDealerData.set_C_HasVideo(((JSONObject) jsonRoot.get(i)).getString("hasVideo"));
                    modelDealerData.set_C_NewsId(((JSONObject) jsonRoot.get(i)).getString("newsId"));
                    modelDealerData.set_C_IsSupplyDealer(((JSONObject) jsonRoot.get(i)).getString("isSupplyDealer"));
                    modelDealerData.set_C_Distance("");
                    modelDealerData.set_C_EquipCarId(((JSONObject) jsonRoot.get(i)).getJSONObject("newsInfoOut").getString("equipCarId"));
                    modelDealerData.set_C_Title(((JSONObject) jsonRoot.get(i)).getJSONObject("newsInfoOut").getString("title"));
                    modelDealerData.set_C_IsFinish(0);
                    modelDealerData.set_C_Yphone(((JSONObject) jsonRoot.get(i)).getString("yphone")==null?"-":((JSONObject) jsonRoot.get(i)).getString("yphone"));
                    modelDealerData.set_C_GroupContent(((JSONObject) jsonRoot.get(i)).getString("groupContent")==null?"-":((JSONObject) jsonRoot.get(i)).getString("groupContent"));
                    modelDealerData.set_C_JsonText(((JSONObject) jsonRoot.get(i)).toString());
                    modelDealerData.set_C_UpdateTime(new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date()));
                    dataArrayList.add(modelDealerData);
                }
            }
            HashSet<Object> set = new HashSet<>(dataArrayList);
            dataArrayList.clear();
            dataArrayList.addAll(set);
            new PriceDataBase().modelDealerDataInsert(dataArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method_根据经销商id获取下载车辆价格信息文件(String filePath) {
        try {
            ArrayList<Object> dealerList = new PriceDataBase().getNoFinishModelDealerData();
            List<List<Object>> list = IntStream.range(0, 6).mapToObj(i -> dealerList.subList(i * (dealerList.size() + 5) / 6, Math.min((i + 1) * (dealerList.size() + 5) / 6, dealerList.size())))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                ModelPriceMoreThread modelPriceMoreThread = new ModelPriceMoreThread(list.get(i), filePath);
                Thread thread = new Thread(modelPriceMoreThread);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void method_补充未下载的车辆价格信息页面(String filePath) {
        try {
            ArrayList<String> fileList = T_Config_File.method_获取文件名称(filePath);
            ArrayList<Object> dataList = new ArrayList<>();
            for (String fileName : fileList) {
                ConfirmCarPriceFile confirmCarPriceFile = new ConfirmCarPriceFile();
                confirmCarPriceFile.set_C_DealerId(fileName.replace(".txt", "").split("_")[0]);
                confirmCarPriceFile.set_C_ModelId(fileName.replace(".txt", "").split("_")[1]);
                confirmCarPriceFile.set_C_UpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                dataList.add(confirmCarPriceFile);
            }
            HashSet<Object> set2 = new HashSet<>(dataList);
            dataList.clear();
            dataList.addAll(set2);
            new PriceDataBase().insertConfirmCarPriceFileModel(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
