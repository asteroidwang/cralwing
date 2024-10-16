package com.wangtiantian.runErShouChe.renRenChe;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

import java.util.HashMap;
import java.util.Map;

public class RenRenChePlayWright {
    public String Method_playwright(String url) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch();
//            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));

            browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true)
                    .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36")
                    .setViewportSize(2880, 1800));
            Page page = browser.newPage();
            page.setDefaultTimeout(300000);

            Map<String, String> headers = new HashMap<>();
//            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");

            // 81参数一般不进行启动
            //headers.put("X-Zst-81","3_2.0aR_sn77yn6O92wOB8hPZnQr0EMYxc4f18wNBUgpTQ6nxERFZ1TY0-4Lm-h3_tufIwJS8gcxTgJS_AuPZNcXCTwxI78YxEM20s4PGDwN8gGcYAupMWufIeQuK7AFpS6O1vukyQ_R0rRnsyukMGvxBEqeCiRnxEL2ZZrxmDucmqhPXnXFMTAoTF6RhRuLPFeSqRhS0uJXL6AXMVgxBiqt06RVC6GgVAB3CG0S8jbefh9XVcTOLj931DgeTv_Y_xh3LkGp0JvuyXcX9jCHmfgY83uwLIwS_6MeM1JSCkULqygLp9JHKKDS9crH0NBNs6_NGMXV0SrLK9gLpn9HOgDoMAqN_OGHO2Hg17rSYcv3_8wexCqfz2JN9-qufoU29IUYfFbxKwUg_shS1jucf8HpyzggOW9xVnGFY-reL0bpGaBL1_htO0DxyVUgq-wt_SG3LwGF8uJ9BjJN9eTLBDuV0BrCqEuCBChLCBrOC");

            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            headers.put("Accept-Encoding", "gzip, deflate, br, zstd");
            headers.put("X-Requested-With", "fetch");
            headers.put("Accept-Language", "zh-CN,zh;q=0.9");
            headers.put("Connection", "keep-alive");
            headers.put("Cookie", "id58=CrINb2bhrdYqTtVVCtcTAg==; rrc_rrc_session=h302itvhmpobn503vopl496em1; rrc_rrc_signed=s%2Ch302itvhmpobn503vopl496em1%2C379da28a56bf9dea8dcc5f53cd59bc1d; rrc_ip_province=%E6%B2%B3%E5%8C%97; rrc_record_city=bd; new_visitor_uuid=660bb13b49f079de845d0fbe8ad80fac; fzq_h=631c304e054c19c65f4e6a1e0aa6ace7_1726066153301_26cdae6b244244519b9aba72f40a1d22_1696328864; sessionid=db24a2a9-2480-42e5-a205-4cdfe3f0abe3; 58tj_uuid=e0f00ba9-63b0-4e2f-884b-e559af7c526c; 58ua=rrcpc; als=0; wmda_uuid=8e3cb6917337e9749819c447874ce19b; wmda_new_uuid=1; xxzlclientid=a5027690-572b-489b-a60d-1726066164770; xxzlxxid=pfmxYSubEL38uhwO+Plj65MNyNDkeo95FKmEPqXx8DXvkrjnXhK/PbyCJ5pYN8gBfMCm; fzq_js_usdt_infodetail_car=2d791d8b98335c0dc3c4e707c21e8e0e_1726110260868_6; wmda_visited_projects=%3B1732038237441%3B1732039838209; wmda_session_id_1732038237441=1726134242272-95b28fff-560d-f9a2; rrc_common_head_ipcity=jining%7C%E6%B5%8E%E5%AE%81%7C450; new_uv=9; utm_source=; spm=; new_session=0; fzq_js_usdt_infolist_car=a5b6afd4357eaaa158822192979d398d_1726146089553_2; init_refer=https%253A%252F%252Fwww.renrenche.com%252Fjining%252Fershouche; xxzlbbid=pfmbM3wxMDM2OHwxLjEwLjF8MTcyNjE0NjA5MTA4MDIzMDAzNXxJUjZiMXYyT0N1WW16N1FRT3J1akJKQ0dFS1N6OUE3YWJrLzBrbWt2MEQwPXxjYTkwZmQ1NGM2N2M1Yjg1ZmE3YWEyNzY1MDA5NjNkYV8xNzI2MTQ2MDkxMjcyXzMwOTU2Y2UzMWRlZTQxYWI4OTVmNjNkOWI4YWQ3YmM1XzE2OTYzMjkwODV8NTY4NzBkZDc1NDcxZWM5Yzg1MGNmMmNmOTI1MWEyZjRfMTcyNjE0NjA5MDA2N18yNTY=");
            headers.put("Upgrade-Insecure-Requests", "1");
            headers.put("Sec-Fetch-Dest", "document");
            headers.put("Sec-Fetch-Mode", "navigate");
            headers.put("Sec-Fetch-Site", "same-origin");
            headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");
            headers.put("Sec-Fetch-User","?1");


            page.setExtraHTTPHeaders(headers);
            // 启用 JavaScript
            page.navigate(url);

            // 等待页面加载完成
            page.waitForLoadState(LoadState.LOAD);

            // 获取页面源码
//            String pageSource = page.content();

            //System.out.println(pageSource);
            Thread.sleep(1800);

            String content = page.content();
//            List<ElementHandle> lis = page.querySelectorAll("pre");
//
//            StringBuilder sb = new StringBuilder();
//
//            for (ElementHandle li : lis) {
//                sb.append(li.innerHTML());
//            }

//            System.out.println(sb.toString());

            Thread.sleep(5200);
            // 关闭浏览器
//            Thread.sleep(100000);
            browser.close();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
