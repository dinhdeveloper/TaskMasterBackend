package com.dinh.logistics.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.dao.TableDao;

@Repository
public class TableDaoImpl implements TableDao {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public File exportToExcelWithResultSet(String sql, String excel_output_file, int row_start, int column_start) {
		
		// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);

        // Lấy Connection từ Session
        Connection connection = session.doReturningWork(sessionConnection  -> {
            return sessionConnection ;
        });
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Thực hiện truy vấn SQL để lấy ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();
            
            List<String> headerValues=new ArrayList<String>();
    	    XSSFWorkbook workbook = new XSSFWorkbook();
            
    	    XSSFSheet spreadsheet = workbook.createSheet("data");
			XSSFRow row = spreadsheet.createRow(0);
			XSSFCell cell;
			int cc = resultSet.getMetaData().getColumnCount();
			for (int i = 1; i <= cc; i++) {
				String headerVal = resultSet.getMetaData().getColumnName(i);
				headerValues.add(headerVal);
				cell = row.createCell(i - 1);
				cell.setCellValue(resultSet.getMetaData().getColumnName(i));
			}
			// System.out.println(headerValues);
			int i = row_start;
			while (resultSet.next()) {
				XSSFRow row1 = spreadsheet.createRow((short) i);
				for (int j = 1; j < cc + column_start; j++) {
					String type = resultSet.getMetaData().getColumnTypeName(j);
					String value = resultSet.getString(resultSet.getMetaData().getColumnName(j));
					if(resultSet.getMetaData().getColumnTypeName(j) == "bool") {
						if(resultSet.getBoolean(resultSet.getMetaData().getColumnName(j)) == true) {
							row1.createCell((short) j-1)
							.setCellValue("true");
						}else {
							row1.createCell((short) j-1)
							.setCellValue("false");
						}
					}else {
						row1.createCell((short) j-1)
						.setCellValue(resultSet.getString(resultSet.getMetaData().getColumnName(j)));
					}
					
				}
				i++;
			}

//			FileOutputStream out = new FileOutputStream(new File(excel_output_file));
//			workbook.write(out);
//			out.close();
			// System.out.println("exceldatabase.xlsx written successfully");

			// Ghi dữ liệu đã sửa vào tệp mới
            File outputFile = new File(excel_output_file);
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                workbook.write(outputStream);
            }
            workbook.close();
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
}
