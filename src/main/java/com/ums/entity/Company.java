package com.ums.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "company")
public class Company extends Base {


    private String name;
    private String address;
    private String email;


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users;
}

