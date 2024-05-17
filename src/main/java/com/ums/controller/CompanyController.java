package com.ums.controller;
import com.ums.dto.CompanyDTO;
import com.ums.entity.Company;
import com.ums.entity.User;
import com.ums.exception.CustomException;
import com.ums.response.CompanyResponse;
import com.ums.response.ResponseModel;
import com.ums.service.CompanyService;
import com.ums.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/companies")
public class CompanyController {

    private  final CompanyService companyService; // Inject CompanyService

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/create")
    public ResponseModel createCompany(@RequestBody @Valid CompanyDTO company) {
        Company createdCompany = companyService.createCompany(company);
        return ResponseModel.success(HttpStatus.OK, "Company created successfully", createdCompany);
    }

    @GetMapping("/getallcompanies")
    public ResponseModel getCompany() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseModel.success(HttpStatus.OK, "Success", companies);
    }

    @GetMapping("/getcompany")
    public ResponseModel getOnlycompany() {
        List<CompanyResponse> companyResponses = companyService.getCompanies();
        return ResponseModel.success(HttpStatus.OK, "Success", companyResponses);
    }
}



