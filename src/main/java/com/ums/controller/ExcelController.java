package com.ums.controller;

import com.ums.response.ResponseModel;
import com.ums.service.ExcelService;
import com.ums.service.ExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/uploadExcel")
    public ResponseModel uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            excelService.readAndSaveFromExcel(file);
            return new ResponseModel(HttpStatus.OK.value(), "Excel data uploaded and saved to database successfully!", null);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred while uploading Excel data.", null);
        }
    }
}
