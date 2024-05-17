package com.ums.entity;

import com.ums.entity.Base;
import com.ums.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreationDTO extends Base {
    private User userDetails;
    private String company_id;
}
