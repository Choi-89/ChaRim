package com.charim.kiosk.User.Service;

import com.charim.kiosk.User.Domain.Dto.UserDto;
import com.charim.kiosk.User.Domain.User;
import com.charim.kiosk.User.Repository.UserRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

@Service
@AutoConfiguration
public class JoinService {
    //exception으로 오류처리
    private UserRepository userRepository;

     public boolean Join(UserDto userDto) {
         User user = new User(userDto);
         userRepository.save(user);
         return true;
     }
}
