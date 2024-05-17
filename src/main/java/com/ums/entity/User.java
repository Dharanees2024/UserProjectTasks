package com.ums.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "userss")
public class User extends Base {


    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }


    @NotEmpty(message = "Username is required")
    private String userName;


    private String password;


    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Mobile number is required")
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore

    private Company company;

    private Boolean isDelete = false;


    public  void setPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        this.password=passwordEncoder.encode(password);
    }


}

