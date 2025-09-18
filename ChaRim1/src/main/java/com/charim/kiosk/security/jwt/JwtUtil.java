package com.charim.kiosk.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    /**
     * application.yml (혹은 properties)에 설정해둘 비밀키
     * HS256은 최소 256bit(=32바이트) 이상의 키가 필요하다.
     * 예: "this_is_a_demo_secret_key_with_length_over_32_bytes!"
     */
    @Value("${jwt.secret}")
    private String secret;

    private Key signingKey;

    @PostConstruct
    public void init() {
        // 문자열 시크릿을 바이트로 변환해 HMAC-SHA 키 생성
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 액세스/리프레시 토큰 발급 공용 메서드
     *
     * @param username  사용자 식별자(보통 subject)
     * @param role      권한(ROLE_USER 등)
     * @param ttlMillis 만료까지 남은 시간(ms). 예: 10분 = 1000*60*10L
     * @return 서명된 JWT 문자열
     */
    public String generateJwt(String username, String role, long ttlMillis) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + ttlMillis);

        return Jwts.builder()
                .setSubject(username)                      // sub
                .claim("role", role)                       // 커스텀 클레임: role
                .setIssuedAt(issuedAt)                     // iat
                .setExpiration(expiration)                 // exp
                .signWith(signingKey, SignatureAlgorithm.HS256) // HS256 서명
                .compact();
    }

    /**
     * 토큰 만료/유효성 검사.
     * - 파싱 과정에서 서명 검증이 수행됨.
     * - 만료된 토큰은 ExpiredJwtException 발생.
     * - 변조/형식 오류는 JwtException 하위 예외 발생.
     *
     * 컨트롤러에서 try-catch로 예외 흐름을 그대로 사용하므로
     * 굳이 값을 리턴하지 않고 '검증만' 수행한다.
     */
    public void isExpired(String token) throws ExpiredJwtException, JwtException, IllegalArgumentException {
        // "Bearer " 접두사가 붙어있을 수 있으니 방어적으로 제거
        String pureToken = stripBearer(token);

        // parseClaimsJws 자체가:
        //  - 서명 검증
        //  - exp 검사(만료 시 ExpiredJwtException)
        //  - 형식 오류 시 JwtException
        Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(pureToken);
    }

    /**
     * 토큰에서 username(sub) 추출
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.getSubject();
    }

    /**
     * 토큰에서 role 추출 (커스텀 클레임)
     */
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("role", String.class);
    }

    /**
     * 내부 공용: 토큰 파싱하여 Claims 획득
     */
    private Claims getAllClaims(String token) throws JwtException, IllegalArgumentException {
        String pureToken = stripBearer(token);

        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(pureToken);

        return jws.getBody();
    }

    /**
     * "Bearer {token}" 형태가 들어오면 접두사 제거
     */
    private String stripBearer(String token) {
        if (token == null) return null;
        String t = token.trim();
        if (t.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return t.substring(7).trim();
        }
        return t;
    }
}
