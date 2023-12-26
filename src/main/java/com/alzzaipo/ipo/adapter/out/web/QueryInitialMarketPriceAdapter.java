package com.alzzaipo.ipo.adapter.out.web;

import com.alzzaipo.common.exception.CustomException;
import com.alzzaipo.ipo.application.port.out.QueryInitialMarketPricePort;
import com.alzzaipo.ipo.application.port.out.dto.QueryInitialMarketPriceResult;
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
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueryInitialMarketPriceAdapter implements QueryInitialMarketPricePort {

    @Value("${data.go.kr.apiKey}")
    private String serviceKey;

    private final String endpoint = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo";

    @Override
    public QueryInitialMarketPriceResult queryInitialMarketPrice(int stockCode, LocalDate listedDate) {
        if (LocalDate.now().isBefore(listedDate)) {
            return new QueryInitialMarketPriceResult(false, 0);
        }

        try {
            URL url = buildURL(listedDate, stockCode);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            if (conn.getResponseCode() < 200 || conn.getResponseCode() >= 300) {
                throw new CustomException(HttpStatus.valueOf(conn.getResponseCode()), "시초가 조회 실패", conn.getResponseMessage());
            }

            String response = readApiResponse(conn);
            conn.disconnect();

            int initialMarketPrice = parseInitialMarketPrice(response);
            return new QueryInitialMarketPriceResult(true, initialMarketPrice);
        } catch (Exception e) {
            log.error("Query Initial Market Price Error : {}", e.getMessage());
            return new QueryInitialMarketPriceResult(false, 0);
        }
    }

    private URL buildURL(LocalDate date, int stockCode) throws MalformedURLException {
        String strDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String queryString = String.format("?%s=%s&%s=%s&%s=%s&%s=%s",
            URLEncoder.encode("serviceKey", StandardCharsets.UTF_8),
            URLEncoder.encode(serviceKey, StandardCharsets.UTF_8),
            URLEncoder.encode("resultType", StandardCharsets.UTF_8),
            URLEncoder.encode("json", StandardCharsets.UTF_8),
            URLEncoder.encode("basDt", StandardCharsets.UTF_8),
            URLEncoder.encode(strDate, StandardCharsets.UTF_8),
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
