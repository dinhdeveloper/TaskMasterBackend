package com.dinh.logistics.controller;

import com.dinh.logistics.service.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class DataImportController {

    @Autowired
    private DataImportService dataImportService;

    @PostMapping("/import")
    public String importData(@RequestParam("file") MultipartFile file) {
        try {
            dataImportService.importDataFromExcel(file);
            return "Data imported successfully!";
        } catch (IOException e) {
            // Handle the exception appropriately
            return "Error importing data: " + e.getMessage();
        }
    }

    @GetMapping("/export")
    public void exportData(HttpServletResponse response) {
        try {
            dataImportService.exportDataToExcelAndSave(response);
        } catch (IOException e) {
            // Handle the exception appropriately
        }
    }
}


