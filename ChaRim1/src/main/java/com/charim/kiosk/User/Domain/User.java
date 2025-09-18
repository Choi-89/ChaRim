package com.charim.kiosk.User.Domain;


import com.charim.kiosk.User.Domain.Dto.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    private String name;

    private String password;

    private String team;

    public User(UserDto userDto) {
        this.name = userDto.getName();
        this.password = userDto.getPassword();
        this.team = userDto.getTeam();
    }
}
