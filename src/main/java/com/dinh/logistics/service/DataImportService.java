package com.dinh.logistics.service;

import com.dinh.logistics.model.YourEntity;
import com.dinh.logistics.respository.YourEntityRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class DataImportService {

    private final ExcelReaderService excelReaderService;
    private final YourEntityRepository yourEntityRepository;

    private static String UPLOADED_FOLDER = "D:\\MY_PROJECT\\LogisticsService\\src\\main\\resources\\static\\files\\";

    @Autowired
    public DataImportService(ExcelReaderService excelReaderService, YourEntityRepository yourEntityRepository) {
        this.excelReaderService = excelReaderService;
        this.yourEntityRepository = yourEntityRepository;
    }

    @Transactional
    public void importDataFromExcel(MultipartFile file) throws IOException {
        List<YourEntity> entities = excelReaderService.readExcelData(file.getInputStream());
        yourEntityRepository.saveAll(entities);
    }

    public void exportDataToExcelAndSave(HttpServletResponse response) throws IOException {
        List<YourEntity> entities = yourEntityRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Field 1");
        headerRow.createCell(1).setCellValue("Field 2");
        headerRow.createCell(2).setCellValue("Field 3");
        // Add other headers if needed

        // Populate data rows
        int rowIndex = 1;
        for (YourEntity entity : entities) {
            Row dataRow = sheet.createRow(rowIndex++);
            dataRow.createCell(0).setCellValue(entity.getField1());
            dataRow.createCell(1).setCellValue(entity.getField2());
            dataRow.createCell(2).setCellValue(entity.getField3());
            // Add other fields if needed
        }

        // Save the workbook to a file on the server
        String filePath = "/data_export.xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(UPLOADED_FOLDER + filePath)) {
            workbook.write(fileOut);

            // Provide the saved file as a downloadable response
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=data_export.xlsx");

            try (OutputStream out = response.getOutputStream();
                 FileInputStream fileIn = new FileInputStream(UPLOADED_FOLDER + filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileIn.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            // Handle the exception appropriately
            e.printStackTrace();
        } finally {
            workbook.close();
        }
    }
}



