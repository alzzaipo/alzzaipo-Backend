package com.alzzaipo.ipo.adapter.out.web;

import com.alzzaipo.ipo.application.port.out.QueryInitialMarketPricePort;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryInitialMarketPriceAdapter implements QueryInitialMarketPricePort {

    @Value("${data.go.kr.apiKey}")
    private String serviceKey;

    @Value("${data.go.kr.endpoint}")
    private String endpoint;

    @Override
    public int query(int stockCode, LocalDate listedDate) {
        // 상장일 전에는 조회 불가
        if (LocalDate.now().isBefore(listedDate)) {
            return -1;
        }

        try {
            URL url = buildURL(listedDate, stockCode);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            if (conn.getResponseCode() < 200 || conn.getResponseCode() >= 300) {
                throw new RuntimeException("Bad Response Code / " + conn.getResponseMessage());
            }

            String response = readApiResponse(conn);
            conn.disconnect();

            return parseInitialMarketPrice(response);
        } catch (Exception e) {
            return -1;
        }
    }

    private URL buildURL(LocalDate date, int stockCode) throws MalformedURLException {
        String queryString = String.format("?%s=%s&%s=%s&%s=%s&%s=%s",
            URLEncoder.encode("serviceKey", StandardCharsets.UTF_8),
            URLEncoder.encode(serviceKey, StandardCharsets.UTF_8),
            URLEncoder.encode("resultType", StandardCharsets.UTF_8),
            URLEncoder.encode("json", StandardCharsets.UTF_8),
            URLEncoder.encode("basDt", StandardCharsets.UTF_8),
            URLEncoder.encode(date.format(DateTimeFormatter.ofPattern("yyyyMMdd")), StandardCharsets.UTF_8),
            URLEncoder.encode("likeSrtnCd", StandardCharsets.UTF_8),
            URLEncoder.encode(String.valueOf(stockCode), StandardCharsets.UTF_8));
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
        return Integer.parseInt((String) stockInfo.get("mkp"));
    }
}
