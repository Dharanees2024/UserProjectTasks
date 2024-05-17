package com.ums.dto;

import com.ums.entity.UserCreationDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter

public class UserCreationBatchDTO {
    private List<UserCreationDTO> userCreationDTOList;

}
