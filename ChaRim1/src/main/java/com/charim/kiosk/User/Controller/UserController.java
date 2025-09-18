package com.charim.kiosk.User.Controller;

import com.charim.kiosk.User.Domain.Dto.UserDto;
import com.charim.kiosk.User.Service.JoinService;
import com.charim.kiosk.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final JoinService joinService;
    private final JwtUtil jwtUtil;

    // name 중복되는지 확인하고 exception 처리
    @PostMapping("/api/join")
    public ResponseEntity<Integer> join(UserDto userDto) {
        joinService.Join(userDto);
        return ResponseEntity.ok(200);
    }

    @GetMapping("/api/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰 없음");
        }
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("aksfyehls flvmfptl xhzms");

        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 리프레시 토큰");
        }

        // 새 엑세스 토큰 발급
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        String role = jwtUtil.getRoleFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateJwt(username, role, 1000*60*10L);

        // 응답에 새 엑세스 토큰 추가
        response.addHeader("Authorization", "Beare " + newAccessToken);
        return ResponseEntity.ok("토큰 갱신");
    }
}
