package com.ums.response;

import lombok.Data;

@Data

public class CompanyResponse {
    private String id;
    private String name;

    public CompanyResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
}


