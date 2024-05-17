package com.ums.entity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter

public class Login extends Base{

    private String userName;
    private String password;


}
