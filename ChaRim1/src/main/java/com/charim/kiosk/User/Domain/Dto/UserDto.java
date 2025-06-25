package com.charim.kiosk.User.Domain.Dto;

import com.charim.kiosk.User.Domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    public String name;

    public String password;
}
