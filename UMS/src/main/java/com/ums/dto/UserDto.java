package com.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String name;
    private Number mobileNumber;
    private Boolean isDeleted;
    private Boolean isValid;
    private String role;
    private String company_id;
}
