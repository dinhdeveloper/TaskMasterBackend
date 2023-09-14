package com.dinh.logistics.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.dao.ReportDao;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.dto.portal.ReportListDto;
import com.dinh.logistics.model.Customers;
import com.dinh.logistics.model.Users;
import com.dinh.logistics.respository.CustomerRepository;
import com.dinh.logistics.respository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ReportDaoImpl implements ReportDao {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Value("${app.file.sql-select-material-report}")
    private String sqlSelectMaterialReportPath;
	
	@Value("${app.file.sql-select-material-report-customer}")
    private String sqlSelectMaterialReportCustomerPath;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CustomerRepository customerRepository;

    @Override
    public List<Object[]> getReportByFilter(String startDate,String endDate, String cusName, String role, String userName) {
    	try {
    		List<Object[]> results = null;
    		StringBuilder builder = new StringBuilder();
            
    		if(StringUtils.equalsIgnoreCase(role, "CUSTOMER")) {
    			// Lấy file chứa câu sql
                Path path = Paths.get(sqlSelectMaterialReportCustomerPath);
                byte[] bytes = Files.readAllBytes(path);

                // Chuyển đổi các byte thành chuỗi sử dụng UTF-8 hoặc một bộ mã khác (tuỳ thuộc vào tệp)
                String content = new String(bytes, StandardCharsets.UTF_8);
                
                // Lấy user
                Users user = userRepository.findUserByUserName(userName);

                builder.append(content);
                Query query = entityManager.createNativeQuery(builder.toString());

                query.setParameter(1, startDate);
                query.setParameter(2, endDate);
                query.setParameter(3, user.getCusId());
                query.setParameter(4, startDate);
                query.setParameter(5, endDate);
                query.setParameter(6, user.getCusId());
                
                results = query.getResultList();
    		}else {
    			// Lấy file chứa câu sql
                Path path = Paths.get(sqlSelectMaterialReportPath);
                byte[] bytes = Files.readAllBytes(path);

                // Chuyển đổi các byte thành chuỗi sử dụng UTF-8 hoặc một bộ mã khác (tuỳ thuộc vào tệp)
                String content = new String(bytes, StandardCharsets.UTF_8);

                builder.append(content);
                Query query = entityManager.createNativeQuery(builder.toString());

                query.setParameter(1, startDate);
                query.setParameter(2, endDate);
                query.setParameter(3, "%" + cusName + "%");
                query.setParameter(4, startDate);
                query.setParameter(5, endDate);
                query.setParameter(6, "%" + cusName + "%");
                
                results = query.getResultList();
    		}
    		

            return results;
//            return convertJob(results);
    	}catch (Exception e) {
    		return null;
    	}
        
    }
    
    @Override
	public File exportToExcelWithResultSet(String excel_output_file, int row_start, int column_start, String startDate,String endDate, String cusName, String role, String userName) {
		
		// Lấy Session từ EntityManager
        Session session = entityManager.unwrap(Session.class);

        // Lấy Connection từ Session
        Connection connection = session.doReturningWork(sessionConnection  -> {
            return sessionConnection ;
        });
        try {
        	// Lấy user
        	Users user = userRepository.findUserByUserName(userName);
        	
        	ResultSet resultSet = null;
        	if(StringUtils.equalsIgnoreCase(role, "CUSTOMER")) {
        		// Lấy file chứa câu sql
                Path path = Paths.get(sqlSelectMaterialReportCustomerPath);
                byte[] bytes = Files.readAllBytes(path);

                // Chuyển đổi các byte thành chuỗi sử dụng UTF-8 hoặc một bộ mã khác (tuỳ thuộc vào tệp)
                String sql = new String(bytes, StandardCharsets.UTF_8);
            	PreparedStatement preparedStatement = connection.prepareStatement(sql);
            	
            	// Truyền tham số
            	preparedStatement.setString(1, startDate);
            	preparedStatement.setString(2, endDate);
            	preparedStatement.setInt(3, user.getCusId());
            	preparedStatement.setString(4, startDate);
            	preparedStatement.setString(5, endDate);
            	preparedStatement.setInt(6, user.getCusId());
            	
                // Thực hiện truy vấn SQL để lấy ResultSet
                resultSet = preparedStatement.executeQuery();
        	}else {
        		// Lấy file chứa câu sql
                Path path = Paths.get(sqlSelectMaterialReportPath);
                byte[] bytes = Files.readAllBytes(path);

                // Chuyển đổi các byte thành chuỗi sử dụng UTF-8 hoặc một bộ mã khác (tuỳ thuộc vào tệp)
                String sql = new String(bytes, StandardCharsets.UTF_8);
            	PreparedStatement preparedStatement = connection.prepareStatement(sql);
            	
            	// Truyền tham số
            	preparedStatement.setString(1, startDate);
            	preparedStatement.setString(2, endDate);
            	preparedStatement.setString(3, "%" + cusName + "%");
            	preparedStatement.setString(4, startDate);
            	preparedStatement.setString(5, endDate);
            	preparedStatement.setString(6, "%" + cusName + "%");
            	
                // Thực hiện truy vấn SQL để lấy ResultSet
                resultSet = preparedStatement.executeQuery();
        	}
        	
            
            List<String> headerValues=new ArrayList<String>();
    	    XSSFWorkbook workbook = new XSSFWorkbook();
            
    	    XSSFSheet spreadsheet = workbook.createSheet("data");
    	    
    	    // fromDate 
    	    XSSFRow row0 = spreadsheet.createRow(0);
    	    XSSFCell cellFromDate = row0.createCell(0);
    	    cellFromDate.setCellValue("from date: " + startDate);
    	    
    	    // toDate
    	    XSSFCell cellToDate = row0.createCell(1);
    	    cellToDate.setCellValue("to date: " + endDate);
    	    
    	    // cusName
    	    XSSFCell cellCusName = row0.createCell(2);
    	    if(StringUtils.equalsIgnoreCase(role, "CUSTOMER")) {
    	    	Customers cus = customerRepository.findById(user.getCusId()).orElse(null);
    	    	cellCusName.setCellValue("customer name: " + cus.getCustomName());
    	    }else {
    	    	if(StringUtils.isEmpty(userName)) {
    	    		cellCusName.setCellValue("customer name: All");
    	    	}else {
    	    		cellCusName.setCellValue("customer name: " + cusName);
    	    	}
    	    }
    	    
    	    //data
			XSSFRow row = spreadsheet.createRow(row_start - 1);
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
    
    public void generateSearchFilter(StringBuilder stringBuilder, String startDate,String endDate, String cusName){

    	if (!StringUtils.isEmpty(startDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.weight_time) >= TO_DATE(:startDate,'dd/MM/yyyy') ");
        }

        if (!StringUtils.isEmpty(endDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.weight_time) <= TO_DATE(:endDate,'dd/MM/yyyy') ");
        }
        
        if (!StringUtils.isEmpty(cusName)) {
            stringBuilder.append(" and unaccent(c.custom_name) ilike unaccent(:cusName) ");
        }

//        if(isCount == false) {
//        	stringBuilder.append(" ORDER BY j.creation_time desc");
//        }
        
    }
    public void setSearchFilter(Query query, String startDate,String endDate, String cusName){

        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate",startDate);
        }

        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate",endDate);
        }
        
        if (!StringUtils.isEmpty(cusName)) {
            query.setParameter("cusName", "%" + cusName + "%");
        }
        
    }
    
    public List<ReportListDto> convertJob (List<Object[]> storedProcedureResults) {
		try {
			return storedProcedureResults.stream().map(result -> {
				ReportListDto reportListDto = new ReportListDto((String) result[0], (Date) result[1],
						(BigDecimal) result[2], (BigDecimal) result[3],
						(BigDecimal) result[4], (BigDecimal) result[5]);
				return reportListDto;
			}).collect(Collectors.toList());
		} catch (Exception e) {
        	log.error("convertJob: "+ e.getMessage());
			return null;
		}
    }
    
    

}
