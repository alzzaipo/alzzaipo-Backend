package com.alzzaipo.hexagonal.ipo.adapter.out.web;

import com.alzzaipo.hexagonal.ipo.application.port.out.QueryInitialMarketPricePort;
import com.alzzaipo.hexagonal.ipo.application.port.out.dto.QueryInitialMarketPriceResult;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class QueryInitialMarketPriceAdapter implements QueryInitialMarketPricePort {

    @Value("${data.go.kr.apiKey}")
    private String serviceKey;

    private final String endpoint = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo";

    @Override
    public QueryInitialMarketPriceResult queryInitialMarketPrice(int stockCode, LocalDate date) {
        boolean success = true;
        int initialMarketPrice = 0;

        if (date.isAfter(LocalDate.now())) {
            return new QueryInitialMarketPriceResult(false, 0);
        }

        try {
            URL url = buildURL(date, stockCode);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            if (conn.getResponseCode() < 200 || conn.getResponseCode() >= 300) {
                throw new RuntimeException();
            }

            String response = readApiResponse(conn);

            initialMarketPrice = parseInitialMarketPrice(response);

            conn.disconnect();
        } catch (Exception e) {
            success = false;
        }

        return new QueryInitialMarketPriceResult(success, initialMarketPrice);
    }

    private URL buildURL(LocalDate date, int stockCode) throws Exception {
        String strDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String queryString = String.format("?%s=%s&%s=%s&%s=%s&%s=%s",
                URLEncoder.encode("serviceKey", StandardCharsets.UTF_8), URLEncoder.encode(serviceKey, StandardCharsets.UTF_8),
                URLEncoder.encode("resultType", StandardCharsets.UTF_8), URLEncoder.encode("json", StandardCharsets.UTF_8),
                URLEncoder.encode("basDt", StandardCharsets.UTF_8), URLEncoder.encode(strDate, StandardCharsets.UTF_8),
                URLEncoder.encode("likeSrtnCd", StandardCharsets.UTF_8), URLEncoder.encode(String.valueOf(stockCode), StandardCharsets.UTF_8));

        return new URL(endpoint + queryString);
    }

    private String readApiResponse(HttpURLConnection conn) throws Exception {
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return bf.readLine();
        }
    }

    private int parseInitialMarketPrice(String response) throws Exception {
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
        JSONObject jsonResponse = (JSONObject) jsonObject.get("response");
        JSONObject jsonBody = (JSONObject) jsonResponse.get("body");
        JSONObject jsonItems = (JSONObject) jsonBody.get("items");
        JSONArray jsonItem = (JSONArray) jsonItems.get("item");
        JSONObject stockInfo = (JSONObject) jsonItem.get(0);

        return Integer.valueOf((String) stockInfo.get("mkp"));
    }
}
