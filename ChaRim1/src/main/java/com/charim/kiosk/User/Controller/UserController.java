package com.charim.kiosk.User.Controller;

import com.charim.kiosk.User.Domain.Dto.UserDto;
import com.charim.kiosk.User.Service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final JoinService joinService;

    // name 중복되는지 확인하고 exception 처리
    @GetMapping("/api/join")
    public ResponseEntity<Integer> join(UserDto userDto) {
        joinService.Join(userDto);
        return ResponseEntity.ok(200);
    }
}
