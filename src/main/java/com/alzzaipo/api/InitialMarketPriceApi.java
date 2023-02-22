package com.alzzaipo.api;

import com.alzzaipo.config.ApiConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InitialMarketPriceApi {

    public static int getInitialMarketPrice(int stockCode, LocalDate listedDate) {

        int initialMarketPrice = 0;
        String serviceKey = ApiConfig.getServiceKey();  /* ApiConfig : 인증키가 저장된 클래스 */
        String strListedDate = listedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        if(!listedDate.isBefore(LocalDate.now())) {
            return 0;
        }

        try {
            // 1. URL을 만들기 위한 StringBuilder.
            // 사용 API : 공공데이터포털 - '금융위원회_주식시세정보'
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("resultType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("basDt", "UTF-8") + "=" + URLEncoder.encode(strListedDate, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("likeSrtnCd", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(stockCode), "UTF-8"));

            // 3. URL 객체 생성.
            URL url = new URL(urlBuilder.toString());

            // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 5. 통신을 위한 메소드 SET.
            conn.setRequestMethod("GET");

            // 6. 통신을 위한 Content-type SET.
            conn.setRequestProperty("Content-type", "application/json");

            // 7. 통신 응답 코드 확인.
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                // 8. 전달받은 데이터를 BufferedReader 객체로 저장
                BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                // 9. BufferedReader로 부터 최초시장가격 추출
                String result = bf.readLine();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
                JSONObject jsonResponse = (JSONObject)jsonObject.get("response");
                JSONObject jsonBody = (JSONObject)jsonResponse.get("body");
                JSONObject jsonItems = (JSONObject)jsonBody.get("items");
                JSONArray jsonItem = (JSONArray)jsonItems.get("item");

                if(jsonItem.isEmpty()) {
                    initialMarketPrice = -1;
                } else {
                    JSONObject stockInfo = (JSONObject) jsonItem.get(0);
                    String strInitialMarketPrice = (String)stockInfo.get("mkp");
                    initialMarketPrice = Integer.valueOf(strInitialMarketPrice);
                }

                bf.close();
            } else {
                throw new IllegalStateException("InitialMarketPriceAPI 오류 - ResponseCode : " + conn.getResponseCode());
            }

            conn.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
            initialMarketPrice = -1;
        }

        return initialMarketPrice;
    }
}