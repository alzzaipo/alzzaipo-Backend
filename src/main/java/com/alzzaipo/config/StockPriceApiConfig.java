package com.alzzaipo.config;

public class StockPriceApiConfig {

    private static String serviceKey = System.getenv("STOCK_PRICE_API_SERVICE_KEY");

    public static String getServiceKey() {
        return serviceKey;
    }
}
