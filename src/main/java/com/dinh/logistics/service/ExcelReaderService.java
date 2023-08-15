package com.dinh.logistics.service;

import com.dinh.logistics.model.YourEntity;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ExcelReaderService {

    public List<YourEntity> readExcelData(InputStream inputStream) throws IOException {
        List<YourEntity> entities = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Skip header row if needed
        Iterator<Row> rowIterator = sheet.iterator();
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (!isRowEmpty(row)) {
                YourEntity entity = createEntityFromRow(row);
                entities.add(entity);
            }
        }

        workbook.close();
        return entities;
    }

    private boolean isRowEmpty(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private YourEntity createEntityFromRow(Row row) {
        YourEntity entity = new YourEntity();

        List<String> cellValues = IntStream.range(0, 3) // Assuming you have 3 fields (0 to 2)
                .mapToObj(index -> getCellValueAsString(row.getCell(index)))
                .collect(Collectors.toList());

        entity.setField1(cellValues.get(0));
        entity.setField2(cellValues.get(1));
        entity.setField3(cellValues.get(2));
        // Set other fields if needed

        return entity;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}



