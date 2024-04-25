package com.ums.service;

import com.ums.dto.CompanyDTO;
import com.ums.entity.Company;
import com.ums.exception.CustomException;
import com.ums.repository.CompanyRepository;
import com.ums.response.CompanyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CompanyService {

    @Autowired
    private  CompanyRepository companyRepository;

    public Company createCompany(CompanyDTO company) {
        Company company1 = new Company();
        company1.setAddress(company.getAddress());
        company1.setName(company.getName());
        company1.setEmail(company.getEmail());
        return companyRepository.save(company1);
    }

    public Company findById(String companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Company not found"));
    }
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    public List<CompanyResponse> getCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> new CompanyResponse(company.getId(), company.getName()))
                .collect(Collectors.toList());
    }


}
