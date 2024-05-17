package com.ums.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginRequestDTO {
    private String username;
    private String password;

}
