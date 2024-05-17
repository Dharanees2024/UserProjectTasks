package com.ums.service;


import com.ums.entity.Company;
import com.ums.entity.User;
import com.ums.exception.CustomException;
import com.ums.repository.CompanyRepository;
import com.ums.repository.IUser;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private IUser userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void readAndSaveFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet
            Set<String> existingUsernames = new HashSet<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0|| isEmptyRow(row)) {
                    // Skip header row
                    continue;
                }

                Cell userNameCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell mobileNumberCell = row.getCell(2);
                Cell companyIdCell = row.getCell(3);

                if (userNameCell == null || nameCell == null || mobileNumberCell == null || companyIdCell == null) {
                    continue;
                }

                String userName = getStringValueFromCell(userNameCell);
                String name = getStringValueFromCell(nameCell);
                String mobileNumber = getStringValueFromCell(mobileNumberCell);
                String companyId = getStringValueFromCell(companyIdCell);

                if (existingUsernames.contains(userName)) {

                    continue;
                }


                if (userName.length() < 2 || userName.length() > 10) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Username must be between 2 to 10 characters");
                }

                if (mobileNumber == null || (mobileNumber).length() != 10) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Mobile number must be exactly 10 digits");
                }

                // Find the company by ID or create a new one if it doesn't exist
                Company company = companyRepository.findById(companyId)
                        .orElseGet(() -> {
                            Company newCompany = new Company();
                            newCompany.setId(companyId);
                            // Set other properties of the company if needed
                            return companyRepository.save(newCompany);
                        });

                User user = new User();
                user.setUserName(userName);
                user.setName(name);
                user.setMobileNumber(mobileNumber);
                user.setCompany(company);

                if (userRepository.findByUserName(userName) == null) {
                    // If not, save the user and add the username to the set of existing usernames
                    userRepository.save(user);
                    existingUsernames.add(userName);
                }
            }
            workbook.close();
        }
    }

    private String getStringValueFromCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Handle numeric cell types
                return String.valueOf((int) cell.getNumericCellValue()); // Or handle as needed
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
    private boolean isEmptyRow(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }


}