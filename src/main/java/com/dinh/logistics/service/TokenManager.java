package com.dinh.logistics.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenManager {
    private static final Map<String, String> tokenMap = new ConcurrentHashMap<>();

    public void addToken(String username, String token) {
        tokenMap.put(username, token);
    }

    public static String getToken(String username) {
        return tokenMap.get(username);
    }

    public static boolean containsToken(String username, String token) {
        String storedToken = getToken(username);
        return storedToken != null && storedToken.equals(token);
    }

    public static void removeToken(String username) {
        tokenMap.remove(username);
    }
}
