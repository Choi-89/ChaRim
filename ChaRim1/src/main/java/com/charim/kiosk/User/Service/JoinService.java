package com.charim.kiosk.User.Service;

import com.charim.kiosk.User.Domain.Dto.UserDto;
import com.charim.kiosk.User.Domain.User;
import com.charim.kiosk.User.Exception.PasswordEqualException;
import com.charim.kiosk.User.Exception.UsernameException;
import com.charim.kiosk.User.Repository.UserRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

@Service
@AutoConfiguration
public class JoinService {
    //exception으로 오류처리
    private UserRepository userRepository;

     public void Join(UserDto userDto) {
         usernameVerify(userDto.getName());
         passwordVerify(userDto.getPassword(),  userDto.getPasswordVerify());

         userRepository.save(new User(userDto));
     }

     private void usernameVerify(String username)
     {
         if(userRepository.existsByName(username)){
             throw new UsernameException("이미 존재하는 아이디입니다.");
         }
     }

     private void passwordVerify(String password, String passwordVerify)
     {
         if(!password.equals(passwordVerify)){
             throw new PasswordEqualException("비밀번호가 일치하지 않습니다");
         }
     }
}
