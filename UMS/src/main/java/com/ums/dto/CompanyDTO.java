package com.ums.dto;



import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class CompanyDTO {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Address is required")
    private String address;

    @NotEmpty(message = "Email is required")
    private String email;


}
