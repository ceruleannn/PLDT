package com.hyhello.priceless.douyin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class DouYin {

    public static void main(String[] args) throws IOException, InterruptedException {
        //importFiddlerRawRequest();
        //httpGet();

        crawlFavorite();

    }

    public static String httpGet() throws IOException {
        //String url = "https://api3-normal-c-lf.amemv.com/aweme/v1/aweme/favorite/?invalid_item_count=0&is_hiding_invalid_item=0&max_cursor=1612888857000&hotsoon_filtered_count=0&hotsoon_has_more=0&sec_user_id=MS4wLjABAAAAAfIslwIsnLyDzLPEBlWSzM2jvfvgsWssnz1CT-VN_qg&count=10&is_order_flow=0&longitude=113.37074661018742&latitude=23.1177925792951&manifest_version_code=140702&_rticket=1613976704540&app_type=normal&iid=1495761218187389&channel=xiaomi&device_type=M2002J9E&language=en&cpu_support64=true&host_abi=arm64-v8a&resolution=1080*2201&openudid=dbe93c64ed9467be&update_version_code=14719900&cdid=33618535-869e-4a6f-b8d1-66b264336120&appTheme=dark&os_api=29&dpi=440&oaid=dd9529842e226dd0&ac=wifi&device_id=2093895520888941&os_version=10&version_code=140701&app_name=aweme&version_name=14.7.1&device_brand=Xiaomi&ssmix=a&device_platform=android&aid=1128&ts=1613976702 HTTP/1.1\n";
        String url = "https://api3-normal-c-lf.amemv.com/aweme/v1/aweme/favorite/?invalid_item_count=0&is_hiding_invalid_item=0&max_cursor=0&hotsoon_filtered_count=0&hotsoon_has_more=0&sec_user_id=MS4wLjABAAAAAfIslwIsnLyDzLPEBlWSzM2jvfvgsWssnz1CT-VN_qg&count=20&is_order_flow=0&longitude=113.37074661018742&latitude=23.1177925792951&manifest_version_code=140702&_rticket=1613976697931&app_type=normal&iid=1495761218187389&channel=xiaomi&device_type=M2002J9E&language=en&cpu_support64=true&host_abi=arm64-v8a&resolution=1080*2201&openudid=dbe93c64ed9467be&update_version_code=14719900&cdid=33618535-869e-4a6f-b8d1-66b264336120&appTheme=dark&os_api=29&dpi=440&oaid=dd9529842e226dd0&ac=wifi&device_id=2093895520888941&os_version=10&version_code=140701&app_name=aweme&version_name=14.7.1&device_brand=Xiaomi&ssmix=a&device_platform=android&aid=1128&ts=1613976696";
        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url(url);
        for (Map.Entry<String, String> entry : importFiddlerRawRequest().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }


        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        byte[] bytes = response.body().bytes();
        String json = new String(bytes, "UTF-8");
        System.out.println(json);
        return json;
    }

    public static Map<String, String> importFiddlerRawRequest(){
        String raw = "Host: api3-normal-c-lf.amemv.com\n" +
                "Connection: keep-alive\n" +
                "Cookie: install_id=1495761218187389; ttreq=1$bd545d1d2205dffe408c24df6ffd94ab64a76a4c; passport_csrf_token=df68367f6aa0a9b4436d392e316cd45a; passport_csrf_token_default=df68367f6aa0a9b4436d392e316cd45a; tt_webid=56ac1bdae60b07bc44127b378baf742b; d_ticket=7f39ae44600cf52ce20a19c2844284c2b1fdf; multi_sids=98769718495%3Afcfb9b2ffe0969df864c9d326750ac64; odin_tt=2f0088ad7ad4d40015831c00ba92838385101d325ebb6e18f8ab05d0b1b0276a0879b4f779ec163bb2ca9e7f0d5cefcc; n_mh=pXal-51JPKp_Pj0ejKIWa3eg2RBSzl_wTSni-G1fcU0; sid_guard=fcfb9b2ffe0969df864c9d326750ac64%7C1613976670%7C5184000%7CFri%2C+23-Apr-2021+06%3A51%3A10+GMT; uid_tt=e24bd2812cfe0d8cb40a71662acb6a16; uid_tt_ss=e24bd2812cfe0d8cb40a71662acb6a16; sid_tt=fcfb9b2ffe0969df864c9d326750ac64; sessionid=fcfb9b2ffe0969df864c9d326750ac64; sessionid_ss=fcfb9b2ffe0969df864c9d326750ac64\n" +
                "x-tt-dt: AAA4B2ODMF752YHF4QTAJWOPFTXV2WWT4WX2ODB4BJ4UFWGILN3YAZ7LDIKO4CJAA6WTAA5QH7GKYYGAOCVWBLL7VTNPKD2MG6A6MS2YJPIZNUKFEXO4DST5PYM6G\n" +
                "tc_2021_now_client: 1613976699717\n" +
                "sdk-version: 2\n" +
                "X-Tt-Token: 00fcfb9b2ffe0969df864c9d326750ac6405bd155fb2a6fca9d6ae1f70a3f2e82af358eb0b171c8b2f53c59c9380dc5133a7b56aec16c56f37df89d0bb33d24f1de3a915a45c4b1c82444aad3ca2e8b763c63bfb6f81155e54bbe4f45d4687d7bc941-1.0.1\n" +
                "passport-sdk-version: 18\n" +
                "X-SS-REQ-TICKET: 1613976697933\n" +
                "x-bd-kmsv: 1\n" +
                "X-SS-DP: 1128\n" +
                "x-tt-trace-id: 00-c881fdcc0d770630ab8286d28c320468-c881fdcc0d770630-01\n" +
                "User-Agent: com.ss.android.ugc.aweme/140702 (Linux; U; Android 10; en_US; M2002J9E; Build/QKQ1.191222.002; Cronet/TTNetVersion:3078b6b4 2021-01-18 QuicVersion:47946d2a 2020-10-14)\n" +
                //"Accept-Encoding: gzip, deflate, br\n" +
                "X-Argus: rQtoqZTUW5kthk3IoxRp3BrrO3JWPKfTDnYxEkSsdzd8B30cs9sR6EdTNsgAlKLCo7eDiWjU8G2g/HQdxEy9YNZG8wTDTYNIEaoB6U+zchyhGFDelRljfZseoq696/2u4tRVHK0HJZVsy9RBCnHgcld87YW1PUTynGKDpOvTAamn1LdmjS9vQvLTccHxD1JWhrfBjMZtGU8/QzOyelXvr4NsLE6mQGbevtV1dUTSi4l++PY3Iln6WlV3raLwERCOp1hSKbiUuA2c3Ez7uarBTvv6KKKJXFiRDvDZ8oCP0SjHAL5Wy83XJ6epsuz1fh36V3Q80wTtijO37HKSqtNrFYZz\n" +
                "X-Gorgon: 8404a0190001bc0fdaeada3113d0b30f0ed41cc3b99fe31e351a\n" +
                "X-Khronos: 1613976697\n" +
                "X-Ladon: tnAMZWDMET+oaoUhd39b+BhDrAkj8OAEHGt3r/w68qpRwX4G\n" +
                "X-Tyhon: NDpQgvmZW9ryrUiHzZNbsMGWRIzNsniE6alrgMQ=";

        String[] lines = raw.split("\n");
        Map<String, String> headers = new HashMap<>();
        for (String line : lines) {
            int index = line.indexOf(": ");
            headers.put(line.substring(0,index), line.substring(index + 2, line.length()));
        }
        for (String s : headers.keySet()) {
            System.out.println(s+": "+headers.get(s));
        }
        return headers;

    }

    public static void crawlFavorite() throws IOException, InterruptedException {

        int elecount = 0;
        int count = 0;
        
        File dir  = new File("C:\\Users\\Administrator\\Desktop\\dyff");
        File[] jsons = dir.listFiles();
        for (File file : jsons) {
            if (!FilenameUtils.isExtension(file.getName(), "json")){
                continue;
            }
            String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            JsonNode aweme_list = root.get("aweme_list");
            for (JsonNode ele : aweme_list) {
                elecount++ ;
                String aweme_id = ele.get("aweme_id").asText();
                File store = new File("C:\\Users\\Administrator\\Desktop\\dyff", aweme_id + ".mp4");
                if (store.exists()){
                    continue;
                }

                for (JsonNode urlnode : ele.get("video").get("download_addr").get("url_list")) {
                    String url = urlnode.asText();


                    //FileUtils.copyURLToFile(new URL(url), store, 3000, 10000);
                    DownloadUtil.get().downloadSync(url, "C:\\Users\\Administrator\\Desktop\\dyff", aweme_id + ".mp4", null );

                    System.out.println(url);
                    System.out.println(store.length());
                    if (store.length() > 3000){
                        count++;
                        break;
                    }
                }
            }
        }
        System.out.println("final count = " + count);
        System.out.println("ele count = " + elecount);
    }
}
