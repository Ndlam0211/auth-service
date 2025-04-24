package com.example.ecommerce.config;

public class AppConstants {
    public static final Integer PAGE_NUMBER = 1;
    public static final Integer PAGE_SIZE = 10;
    public static final String SORT_BY = "id";
    public static final String SORT_ORDERS_BY = "totalAmount";
    public static final String SORT_DIR = "asc";
    public static final String[] AUTH_URLS = {"/api/v1/auth/**"};
    public static final String[] USER_URLS = { "/api/v1/**" };
    public static final String[] ADMIN_URLS = { "/api/v1/admin/**" };
    public static final String[] PUBLIC_URLS = { "/v3/api-docs/**", "/swagger-ui/**", "/api/v1/public/products/image/**" };
}
