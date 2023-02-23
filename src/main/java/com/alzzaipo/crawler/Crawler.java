package com.alzzaipo.crawler;

import com.alzzaipo.api.InitialMarketPriceApi;
import com.alzzaipo.service.IPOService;
import com.alzzaipo.web.domain.IPO.IPO;
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

@Component
@RequiredArgsConstructor
public class Crawler {

    private final IPOService ipoService;

    // 작년 공모주 정보를 데이터베이스에 저장
    @Transactional
    public void updateIPOListForm(int year) {
        int pageNumber = 1;
        boolean stopFlag = false;

        try {

            while (true) {
                List<IPO> ipoList = getIPOListFromPage(pageNumber);

                for (IPO ipo : ipoList) {
                    if (ipo.getListedDate().getYear() >= year) {
                        ipoService.save(ipo);
                    } else {
                        stopFlag = true;
                        break;
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
            int initialMarketPrice = InitialMarketPriceApi.getInitialMarketPrice(crawlerDto.getStockCode(), crawlerDto.getListedDate());
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
        String stockName = ipoRawData.child(0).text();

        // 희망공모가 하단
        int expectedOfferingPriceMin = Integer.valueOf(ipoRawData.child(2).text().split("~")[0].replace(",", ""));

        // 희망공모가 상단
        int expectedOfferingPriceMax = Integer.valueOf(ipoRawData.child(2).text().split("~")[1].replace(",", ""));

        // 최종공모가
        int fixedOfferingPrice = Integer.valueOf(ipoRawData.child(3).text().replace(",", ""));

        // 공모금액
        int totalAmount = Integer.valueOf(ipoRawData.child(4).text().replace(",", ""));

        // 기관경쟁률
        int competitionRate = getCleanRate(ipoRawData.child(5).text());

        // 의무보유확약비율
        int lockupRate = getCleanRate(ipoRawData.child(6).text());

        // 주관사
        String agents = ipoRawData.child(7).text();


        /* 상세 페이지에서 추가 청약정보를 가져옵니다 */
        String newUrl = "http://www.ipo38.co.kr/ipo/" + ipoRawData.child(0).child(0).attr("href").substring(2);
        List<String> additionalData = getAdditionalIPOData(newUrl);

        // 청약 시작일
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate subscribeStartDate = LocalDate.parse(additionalData.get(0), formatter);

        // 청약 종료일
        LocalDate subscribeEndDate = LocalDate.parse(additionalData.get(1), formatter);

        // 상장일
        LocalDate listedDate = LocalDate.parse(additionalData.get(2), formatter);

        // 종목코드
        int stockCode = Integer.valueOf(additionalData.get(3));

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

            String subscribeDate = document.select(subscribeDateSelector1).text();
            if (subscribeDate.isEmpty()) {
                subscribeDate = document.select(subscribeDateSelector2).text();
            }

            // 청약 시작일
            String subscribeStartDate = subscribeDate.replace(" ", "").split("~")[0];
            // 청약 종료일
            String subscribeEndDate = subscribeDate.replace(" ", "").split("~")[1];

            // 상장일
            String listedDate = document.select(listedDateSelector1).text(); /* 상장일 */
            if (listedDate.isEmpty()) {
                listedDate = document.select(listedDateSelector2).text();
            }

            // 종목 코드
            String stockCode = document.select(stockCodeSelector).text();

            result.add(subscribeStartDate);
            result.add(subscribeEndDate);
            result.add(listedDate);
            result.add(stockCode);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // '####.##:1' 형식에서 소수점 아래를 제외한 정보를 int로 반환 / 정보가 없는 경우 0을 반환
    private int getCleanRate(String rawData) {
        String rate;

        if(rawData.matches(".*[0-9].*")) {
            if (rawData.contains(".")) {
                rate = rawData.split("\\.")[0].replace(",", "");
            } else {
                rate = rawData.split(":")[0].replace(",", "");
            }
        } else {
            rate = "0";
        }

        return Integer.valueOf(rate);
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