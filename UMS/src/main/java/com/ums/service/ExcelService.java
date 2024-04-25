package com.ums.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ExcelService {
    void readAndSaveFromExcel(MultipartFile file) throws IOException;
}