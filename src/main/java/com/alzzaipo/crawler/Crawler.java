package com.alzzaipo.crawler;

import com.alzzaipo.service.IpoService;
import com.alzzaipo.domain.IPO.IPO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class Crawler {

    private final IpoService ipoService;
    private final InitialMarketPriceApi initialMarketPriceApi;

    // 작년 공모주 정보를 데이터베이스에 저장
    @Transactional
    public void updateIPOListFrom(int from) {
        int pageNumber = 1;
        boolean stopFlag = false;

        try {
            while (true) {
                List<IPO> ipoList = getIPOListFromPage(pageNumber);

                for (IPO ipo : ipoList) {
                    if (ipo.getSubscribeStartDate().getYear() >= from) {
                        ipoService.save(ipo);
                    } else {
                        if(ipo.getSubscribeStartDate().getYear() == 1000) {
                            continue;
                        } else {
                            stopFlag = true;
                            break;
                        }
                    }
                }
                if (stopFlag) {
                    break;
                } else {
                    pageNumber += 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<IPO> getIPOListFromPage(int pageNumber) {
        List<IPO> ipoList = new ArrayList<>();

        List<CrawlerDto> crawlerDtoList = getCrawlerDtoListFromPage(pageNumber);

        for (CrawlerDto crawlerDto : crawlerDtoList) {
            int initialMarketPrice = initialMarketPriceApi.getInitialMarketPrice(crawlerDto.getStockCode(), crawlerDto.getListedDate());
            IPO ipo = createIPO(crawlerDto, initialMarketPrice);
            ipoList.add(ipo);
        }

        return ipoList;
    }

    public List<CrawlerDto> getCrawlerDtoListFromPage(int pageNumber) {
        List<CrawlerDto> result = new ArrayList<>();
        String url = "http://www.ipo38.co.kr/ipo/index.htm?o=&key=5&page=";
        String selector1 = "body > table:nth-child(5) > tbody > tr > td > table > tbody > tr > td:nth-child(1) >"
                + "table:nth-child(10) > tbody > tr:nth-child(2) > td > table > tbody > tr";

        try {
            // 스크래핑 페이지 주소
            String targetURL = url + String.valueOf(pageNumber);
            // 문서 데이터
            Document doc = Jsoup.connect(targetURL).get();
            // 수요예측결과 정보들
            Elements ipoRawDataList = doc.select(selector1);

            for(int i=0; i<ipoRawDataList.size(); i++) {
                // 단건 수요예측결과
                Element ipoRawData = ipoRawDataList.get(i);
                // 수요예측결과로부터 청약정보객체 생성
                CrawlerDto crawlerDto = createCrawlerDto(ipoRawData);
                result.add(crawlerDto);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private CrawlerDto createCrawlerDto(Element ipoRawData) throws Exception {

        // 종목명
        String stockName = "조회불가" ;
        String rawData = ipoRawData.child(0).text();
        if(rawData != null && !rawData.isEmpty()) {
            stockName = rawData;
        }

        // 희망공모가 하단, 상단
        int expectedOfferingPriceMin = -1, expectedOfferingPriceMax = -1;
        rawData = ipoRawData.child(2).text();

        if (rawData != null && !rawData.isEmpty()) {
            String[] offeringPrices = rawData.split("~");
            if(offeringPrices.length > 0) {
                expectedOfferingPriceMin = Integer.parseInt(offeringPrices[0].replace(",", ""));
            }

            if(offeringPrices.length > 1) {
                expectedOfferingPriceMax = Integer.parseInt(offeringPrices[1].replace(",", ""));
            }
        }


        // 최종공모가
        int fixedOfferingPrice = -1;
        rawData = ipoRawData.child(3).text();
        if (rawData != null && !rawData.isEmpty()) {
            fixedOfferingPrice = Integer.parseInt(rawData.replace(",", ""));
        }

        // 공모금액
        int totalAmount = -1;
        rawData = ipoRawData.child(4).text();
        if (rawData != null && !rawData.isEmpty()) {
            totalAmount = Integer.parseInt(rawData.replace(",", ""));
        }

        // 기관경쟁률
        int competitionRate = -1;
        rawData = ipoRawData.child(5).text();
        if (rawData != null && !rawData.isEmpty()) {
            competitionRate = getCleanRate(rawData);
        }

        // 의무보유확약비율
        int lockupRate = -1;
        rawData = ipoRawData.child(6).text();
        if (rawData != null && !rawData.isEmpty()) {
            lockupRate = getCleanRate(rawData);
        }

        // 주간사
        String agents = "조회불가";
        rawData = ipoRawData.child(7).text();
        if(rawData != null && !rawData.isEmpty()) {
            agents = rawData;
        }



        /* 상세 페이지에서 추가 청약정보를 가져옵니다 */
        String newUrl = "http://www.ipo38.co.kr/ipo/" + ipoRawData.child(0).child(0).attr("href").substring(2);
        List<String> additionalData = getAdditionalIPOData(newUrl);     // 조회불가 시, "error" 반환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");


        /* 청약 시작일, 종료일*/
        LocalDate subscribeStartDate = LocalDate.of(1000,1,1);
        LocalDate subscribeEndDate = LocalDate.of(1000,1,1);
        if(!additionalData.get(0).equals("error")) {
            subscribeStartDate = LocalDate.parse(additionalData.get(0), formatter);
        }
        if(!additionalData.get(1).equals("error")) {
            subscribeEndDate = LocalDate.parse(additionalData.get(1), formatter);
        }

        /* 상장일 */
        LocalDate listedDate = LocalDate.of(1000,1,1);
        if(!additionalData.get(2).equals("error")) {
            listedDate = LocalDate.parse(additionalData.get(2), formatter);
        }

        /* 종목코드 */
        int stockCode = -1;
        if(!additionalData.get(3).equals("error")) {
            stockCode = Integer.parseInt(additionalData.get(3));
        }

        CrawlerDto crawlerDto = CrawlerDto.builder()
                .stockName(stockName)
                .expectedOfferingPriceMin(expectedOfferingPriceMin)
                .expectedOfferingPriceMax(expectedOfferingPriceMax)
                .fixedOfferingPrice(fixedOfferingPrice)
                .totalAmount(totalAmount)
                .competitionRate(competitionRate)
                .lockupRate(lockupRate)
                .agents(agents)
                .subscribeStartDate(subscribeStartDate)
                .subscribeEndDate(subscribeEndDate)
                .listedDate(listedDate)
                .stockCode(stockCode)
                .build();

        return crawlerDto;
    }

    // 청약 시작일, 청약 종료일, 상장일 시초가, 종목코드 데이터를 담은 String 리스트를 반환
    private static List<String> getAdditionalIPOData(String url) throws Exception {
        List<String> result = new ArrayList<>();

        String subscribeDateSelector1 = "body > table:nth-child(5) > tbody > tr > td > table > tbody > tr > td:nth-child(1) >" +
                "table:nth-child(16) > tbody > tr:nth-child(2) > td:nth-child(2)";
        String subscribeDateSelector2 = "body > table:nth-child(5) > tbody > tr > td > table > tbody > tr > td:nth-child(1) >" +
                "table:nth-child(18) > tbody > tr:nth-child(2) > td:nth-child(2)";
        String listedDateSelector1 = "body > table:nth-child(5) > tbody > tr > td > table > tbody > tr > td:nth-child(1)" +
                "> table:nth-child(16) > tbody > tr:nth-child(9) > td:nth-child(2) > table > tbody > tr > td:nth-child(2)";
        String listedDateSelector2 = "body > table:nth-child(5) > tbody > tr > td > table > tbody > tr > td:nth-child(1) >" +
                " table:nth-child(18) > tbody > tr:nth-child(6) > td:nth-child(2)";
        String stockCodeSelector = "body > table:nth-child(5) > tbody > tr > td > table > tbody > tr > td:nth-child(1) > " +
                "table:nth-child(10) > tbody > tr:nth-child(2) > td:nth-child(4)";


        try {
            Document document = Jsoup.connect(url).get();

            /* 청약시작일, 청약종료일*/
            String subscribeStartDate = "error", subscribeEndDate = "error";
            String subscribeDate = document.select(subscribeDateSelector1).text();

            if(subscribeDate == null && subscribeDate.isEmpty()) {
                subscribeDate = document.select(subscribeDateSelector2).text();
            }
            if(subscribeDate != null && !subscribeDate.isEmpty()) {
                String[] subscribeDates = subscribeDate.replace(" ", "").split("~");
                if(subscribeDates.length > 0) {
                    subscribeStartDate = subscribeDates[0];
                }
                if(subscribeDates.length > 1) {
                    subscribeEndDate = subscribeDates[1];
                }
            }

            /* 상장일 */
            String listedDate = "error";
            String rawData = document.select(listedDateSelector1).text();
            if (rawData == null || rawData.isEmpty()) {
                rawData = document.select(listedDateSelector2).text();
            }
            if(rawData != null && !rawData.isEmpty()) {
                listedDate = rawData;
            }

            /* 종목 코드 */
            String stockCode = "error";
            rawData = document.select(stockCodeSelector).text();
            if(rawData != null && !rawData.isEmpty()) {
                stockCode = rawData;
            }

            result.add(subscribeStartDate);
            result.add(subscribeEndDate);
            result.add(listedDate);
            result.add(stockCode);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // '####.##:1' 형식에서 소수점 아래를 제거한 정수 부분을 int로 반환 / 정보가 없는 경우 0을 반환
    private int getCleanRate(String rawData) {
        String rate = "0";

        if (rawData.matches(".*[0-9].*")) {
            String[] parts = rawData.split("[.:]");
            if (parts.length > 0) {
                rate = parts[0].replace(",", "");
            }
        }

        return Integer.parseInt(rate);
    }

    private IPO createIPO(CrawlerDto crawlerDto, int initialMarketPrice) {
        IPO ipo = IPO.builder()
                .stockName(crawlerDto.getStockName())
                .expectedOfferingPriceMin(crawlerDto.getExpectedOfferingPriceMin())
                .expectedOfferingPriceMax(crawlerDto.getExpectedOfferingPriceMax())
                .fixedOfferingPrice(crawlerDto.getFixedOfferingPrice())
                .totalAmount(crawlerDto.getTotalAmount())
                .competitionRate(crawlerDto.getCompetitionRate())
                .lockupRate(crawlerDto.getLockupRate())
                .agents(crawlerDto.getAgents())
                .subscribeStartDate(crawlerDto.getSubscribeStartDate())
                .subscribeEndDate(crawlerDto.getSubscribeEndDate())
                .listedDate(crawlerDto.getListedDate())
                .stockCode(crawlerDto.getStockCode())
                .initialMarketPrice(initialMarketPrice)
                .build();

        return ipo;
    }
}