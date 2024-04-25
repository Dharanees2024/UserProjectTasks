package com.ums.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "userss")
public class User extends Base {

    @NotEmpty(message = "Username is required")
    private String userName;


    @NotEmpty(message = "Name is required")
    private String name;

    @NotNull(message = "Mobile number is required")
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore

    private Company company;

    private Boolean isDelete = false;
}

