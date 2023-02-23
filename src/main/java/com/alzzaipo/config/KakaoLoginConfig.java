package com.alzzaipo.config;

public class KakaoLoginConfig {

    private final static String restAPIKey = System.getenv("KAKAO_REST_API_KEY");
    private final static String adminKey = System.getenv("KAKAO_ADMIN_KEY");
    private final static String clientSecret = System.getenv("KAKAO_CLIENT_SECRET");
    private final static String redirectURI = "http://alzzaipo/kakao_callback";

    public static String getRestAPIKey() {
        return restAPIKey;
    }

    public static String getAdminKey() {
        return adminKey;
    }

    public static String getRedirectURI() {
        return redirectURI;
    }

    public static String getClientSecret() { return clientSecret; }

}
