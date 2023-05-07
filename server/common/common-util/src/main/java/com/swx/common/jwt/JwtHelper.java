package com.swx.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtHelper {

    private static long tokenExpiration = 60 * 60 * 1000;
    private static String tokenSignKey = "sw-code";

    public static String createToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject("AUTH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();

    }

    public static Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        String userId = body.get("userId").toString();
        return Long.parseLong(userId);
    }

    public static String getUsername(String token) {
        if (StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (String) body.get("username");
    }

    public static void main(String[] args) {
        String token = createToken(1L, "admin");
        // String token = createToken(2L, "wjl");
//        String token = createToken(3L, "lrsjl");
        System.out.println(token);
    }

}
