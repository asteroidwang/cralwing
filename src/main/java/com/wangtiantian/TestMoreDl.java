package com.wangtiantian;

import com.wangtiantian.controller.DownLoadData;
import com.wangtiantian.entity.Bean_P_C_B_URL;
import com.wangtiantian.mapper.DataBaseMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestMoreDl {
    public static void main(String[] args) {
        TestMoreDl testMoreDl = new TestMoreDl();
        testMoreDl.methodTest();


//        DataBaseMethod.dataBase_i_d_u(allparamsUrl);
    }

    public void methodTest() {
        ArrayList<Object> allparamsUrl = DataBaseMethod.findDataFromDataBase("versionIds");
//        List<Bean_P_C_B_URL> allparamsUrl = DataBaseMethod.getUrlList();
        List<List<Object>> paramsUrl = IntStream.range(0, 6).mapToObj(i -> allparamsUrl.subList(i * (allparamsUrl.size() + 5) / 6, Math.min((i + 1) * (allparamsUrl.size() + 5) / 6, allparamsUrl.size())))
                .collect(Collectors.toList());
        for (int i = 0; i < paramsUrl.size(); i++) {
            List<Object> groupList = paramsUrl.get(i);
            Method_MoreThread methodMoreThread = new Method_MoreThread(groupList, "/Users/wangtiantian/MyDisk/所有文件数据/汽车之家/汽车之家_240801/params/", ((Bean_P_C_B_URL)groupList.get(i)).get_C_Group());
            Thread thread = new Thread(methodMoreThread);
            thread.start();
        }
    }
}
