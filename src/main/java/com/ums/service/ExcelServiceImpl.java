package com.ums.service;
import com.ums.entity.Company;
import com.ums.entity.User;
import com.ums.repository.CompanyRepository;
import com.ums.repository.IUser;
import io.micrometer.common.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

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

            // Create a new workbook for errors
            Workbook errorWorkbook = new XSSFWorkbook();
            Sheet errorSheet = errorWorkbook.createSheet("ErrorSheet");

            // Create headers for the error sheet
            Row headerRow = errorSheet.createRow(0);
            headerRow.createCell(0).setCellValue("Username");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Mobile Number");
            headerRow.createCell(3).setCellValue("Company ID");
            headerRow.createCell(4).setCellValue("Error");

            int errorRowIndex = 1; // Start from the second row for error data

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isEmptyRow(row)) {
                    // Skip header row or empty rows
                    continue;
                }

                Cell userNameCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell mobileNumberCell = row.getCell(2);
                Cell companyIdCell = row.getCell(3);

                if (userNameCell == null && nameCell == null && mobileNumberCell == null && companyIdCell == null) {
                    continue;
                }

                String userName = getStringValueFromCell(userNameCell);
                String name = getStringValueFromCell(nameCell);
                String mobileNumber = getStringValueFromCell(mobileNumberCell);
                String companyId = getStringValueFromCell(companyIdCell);

                // Perform additional validation
                List<String> errors = new ArrayList<>();
                if (StringUtils.isEmpty(userName)) {
                    errors.add("Username is required");
                } else if (userName.length() < 2 || userName.length() > 10) {
                    errors.add("Username must be between 2 to 10 characters");
                }

                if (StringUtils.isEmpty(name)) {
                    errors.add("Name is required");
                }

                if (StringUtils.isEmpty(mobileNumber)) {
                    errors.add("Mobile number is required");
                } else if (mobileNumber.length() != 10) {
                    errors.add("Mobile number must be exactly 10 digits");
                }

                if (StringUtils.isEmpty(companyId)) {
                    errors.add("Company ID is required");
                }

                if (!errors.isEmpty()) {
                    // Record errors in the error sheet
                    Row errorRow = errorSheet.createRow(errorRowIndex++);
                    errorRow.createCell(0).setCellValue(userName);
                    errorRow.createCell(1).setCellValue(name);
                    errorRow.createCell(2).setCellValue(mobileNumber);
                    errorRow.createCell(3).setCellValue(companyId);
                    errorRow.createCell(4).setCellValue(String.join(", ", errors));
                    continue; // Skip saving to database if there are errors
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

            // Specify the output file path for the error sheet
            String errorOutputPath = "/home/dharane@ADCNST.COM/Downloads/DHANAM/DHANAM_Backend/UMS/ErrorSheet.xlsx";

            // Save the error workbook to persist the error data
            try (FileOutputStream errorFileOut = new FileOutputStream(errorOutputPath)) {
                errorWorkbook.write(errorFileOut);
                errorWorkbook.close();
            }

            // Specify the output file path for the main workbook
            String mainOutputPath = "/home/dharane@ADCNST.COM/Downloads/DHANAM/DHANAM_Backend/UMS/Book.xlsx";

            // Save the main workbook to persist changes
            workbook.write(new FileOutputStream(mainOutputPath));
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
