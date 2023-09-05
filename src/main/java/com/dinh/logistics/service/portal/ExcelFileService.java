package com.dinh.logistics.service.portal;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.model.CollectPoint;
import com.dinh.logistics.model.Customers;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.JobEmployee;
import com.dinh.logistics.model.Jobs;
import com.dinh.logistics.model.Users;
import com.dinh.logistics.respository.CollectPointRepository;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.respository.JobEmployeeRepository;
import com.dinh.logistics.respository.JobRepository;
import com.dinh.logistics.respository.JobTypeRepository;
import com.dinh.logistics.respository.RolePjRepository;
import com.dinh.logistics.respository.UserRepository;
import com.dinh.logistics.respository.CustomerRepository;
import com.dinh.logistics.ultils.AppConstants;

@Service
public class ExcelFileService {
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	CollectPointRepository collectPointRepository;
	
	@Autowired
	JobTypeRepository jobTypeRepository;
	
	@Autowired
	RolePjRepository rolePjRepository;
	
	@Autowired
	JobEmployeeRepository jobEmployeeRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JobDao jobDao;

	public File uploadJobs (MultipartFile file) {
		try {
            // Đọc dữ liệu từ tệp Excel và lưu vào cơ sở dữ liệu
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            int count = 0;
            
            //Địa điểm id
            List<Integer> collectPointIds = collectPointRepository.findAllCollectPointId();
            
            //Loại việc Id
            List<Integer> jobTypeIds = jobTypeRepository.findAllJobTypeId();
            
            //Nhân viên Id
            List<Integer> nvIds = employeeRepository.findAllIdByRole("NV");
            
            //Tài xế
            List<Integer> txIds = employeeRepository.findAllIdByRole("TX");
            
            while (rowIterator.hasNext()) {
            	count++;
                Row row = rowIterator.next();

                if(count > 3) {
                	
//                	String column1Value = row.getCell(0).getStringCellValue();
//                    String column2Value = row.getCell(1).getStringCellValue();
//                    String column3Value = row.getCell(2).getStringCellValue();
                	Cell cell1 = row.getCell(0);
                	Cell cell2 = row.getCell(1);
                	
                    Cell cell4 = row.getCell(3);
                    Cell cell5 = row.getCell(4);
                    Cell cell6 = row.getCell(5);
                    Cell cell7 = row.getCell(6);
                    Cell cell8 = row.getCell(7);
                    Cell cell9 = row.getCell(8);
                    Cell cell10 = row.getCell(9);
                    
                    String validate = null;
                    
                    String column4Validate = validateUploadJobsCellString("Địa điểm", cell4, collectPointIds);
                    String column5Validate = validateUploadJobsCellString("Loại việc", cell5, jobTypeIds);
                    String column6Validate = validateUploadJobsCellString("NV TG 1", cell6, nvIds);
                    String column7Validate = validateUploadJobsCellString("NV TG 2", cell7, nvIds);
                    String column8Validate = validateUploadJobsCellString("Tài xế", cell8, txIds);
                    String column9Validate = validateUploadJobsCellString2("Ưu tiên", cell9);

                    if(column4Validate != null) {
                    	validate += column4Validate;
                    }
                    if(column5Validate != null) {
                    	validate += column5Validate;
                    }
                    if( column6Validate != null && column7Validate != null && column8Validate != null) {
                    	validate += "Phải có ít nhất 1 nhân viên";
                    }
//                    if(column6Validate != null) {
//                    	validate += column6Validate;
//                    }
//                    if(column7Validate != null) {
//                    	validate += column7Validate;
//                    }
//                    if(column8Validate != null) {
//                    	validate += column8Validate;
//                    }
                    if(column9Validate != null) {
                    	validate += column9Validate;
                    }
                    
                    if(validate == null) {
                    	//
                    	Jobs job = new Jobs();
                    	job.setCollePointId(getIntValueFromCell(cell4));
                    	job.setJobTypeId((getIntValueFromCell(cell5)));
                    	
                    	String priorityString = cell9.getStringCellValue();
                    	Integer priority = Integer.parseInt(priorityString);
                    	job.setPriority(priority);
                    	job.setNote(cell10.getStringCellValue());
                    	job.setPaymentStateId(1);
                    	
                    	LocalDateTime currentDateTime = LocalDateTime.now();
                        Timestamp currentTimestamp = Timestamp.valueOf(currentDateTime);
                        
                        job.setCreationTime(currentTimestamp);
                        job.setAssignTime(currentTimestamp);
                    	
                    	Jobs newJob = jobRepository.save(job);
                    	
                    	//luu vao excel
                    	cell2.setCellValue(newJob.getJob_id().toString());
                    	
                    	//
                    	JobEmployee jobEmp1 = new JobEmployee();
                    	jobEmp1.setJobId(newJob.getJob_id());
                    	jobEmp1.setEmpId(getIntValueFromCell(cell6));
                    	jobEmp1.setState(true);
                    	jobEmp1.setSerialNumber("1");;
                    	jobEmployeeRepository.save(jobEmp1);
                    	
                    	//
                    	JobEmployee jobEmp2 = new JobEmployee();
                    	jobEmp2.setJobId(newJob.getJob_id());
                    	jobEmp2.setEmpId(getIntValueFromCell(cell7));
                    	jobEmp2.setState(true);
                    	jobEmp2.setSerialNumber("2");
                    	jobEmployeeRepository.save(jobEmp2);
                    	
                    	//
                    	JobEmployee jobEmp3 = new JobEmployee();
                    	jobEmp3.setJobId(newJob.getJob_id());
                    	jobEmp3.setEmpId(getIntValueFromCell(cell8));
                    	jobEmp3.setState(true);
                    	jobEmp3.setSerialNumber("3");
                    	jobEmployeeRepository.save(jobEmp3);
                    	
                    } else {
                    	//
                    	cell1.setCellValue(validate);
                    	continue;
                    }
                    
                }
                
            }

         // Ghi dữ liệu đã sửa vào tệp mới
            File outputFile = new File("CongViec-Upload-Report.xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                workbook.write(outputStream);
            }
            workbook.close();
            return outputFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public File exportJobs(String startDate, String endDate, int page, int size) {
		try {
			//role_id tx
			List<Integer> roleTxId = rolePjRepository.getListIdByRoleCode(AppConstants.ROLE_CODE_TX);
			
			//get job list
			List<JobListDto> jobList = jobDao.getAllJobByFilter(startDate, endDate, page, size);
					
			//get employee
			for (JobListDto job : jobList) {
				List<JobEmployee> jobEmplList = jobEmployeeRepository.findAllByJobId(job.getId());
				for (JobEmployee JobEmployee : jobEmplList) {
					Employee employee = employeeRepository.findById(JobEmployee.getEmpId()).orElse(null);
					if (employee != null) {
						if (employee.getRoleId() == roleTxId.get(0)) {
							job.setEmployee_3(employee.getName());
						} else {
							if (StringUtils.isEmpty(job.getEmployee_1())) {
								job.setEmployee_1(employee.getName());
							} else {
								job.setEmployee_2(employee.getName());
							}
						}
					}
				}
			}
			
			Workbook workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet("Data");

	        int rowNum = 0;
	        Row headerRow = sheet.createRow(rowNum++);
	        headerRow.createCell(0).setCellValue("Mã công việc");
	        headerRow.createCell(1).setCellValue("Địa điểm");
	        headerRow.createCell(2).setCellValue("Ngày tạo");
	        headerRow.createCell(3).setCellValue("Loại việc");
	        headerRow.createCell(4).setCellValue("NV 1");
	        headerRow.createCell(5).setCellValue("NV 2");
	        headerRow.createCell(6).setCellValue("Tài xế");
	        headerRow.createCell(7).setCellValue("Ưu tiên");
	        headerRow.createCell(8).setCellValue("Ghi chú");

	        for (JobListDto job : jobList) {
	            Row row = sheet.createRow(rowNum++);
	            row.createCell(0).setCellValue(job.getId());
	            row.createCell(1).setCellValue(job.getCollectPoint());
	            row.createCell(2).setCellValue(job.getCreateDate());
	            row.createCell(3).setCellValue(job.getJobType());
	            row.createCell(4).setCellValue(job.getEmployee_1());
	            row.createCell(5).setCellValue(job.getEmployee_2());
	            row.createCell(6).setCellValue(job.getEmployee_3());
	            row.createCell(7).setCellValue(job.getPriority());
	            row.createCell(8).setCellValue(job.getNote());
	        }

	        File outputFile = new File("export.xlsx");
	        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
	            workbook.write(outputStream);
	        }
	        workbook.close();
	        return outputFile;
		} catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public String validateUploadJobsCellString(String columnName, Cell cell, List<Integer> list) {
		
		String result = null;
		if(cell == null) {
			return result = columnName + " không được để trống.";
		}
		if(StringUtils.isBlank(cell.getStringCellValue())) {
			return result = columnName + " không được để trống.";
		}
		String[] parts = cell.getStringCellValue().split(":");
		Integer intValue = Integer.parseInt(parts[1]);
		if(list.contains(intValue)) {
			return result;
		} else {
			return result = "Không tìm thấy " + columnName + ".";
		}
	}
	
public String validateUploadJobsCellString2(String columnName, Cell cell) {
		
		String result = null;
		if(cell == null) {
			return result = columnName + " không được để trống.";
		}
		if(StringUtils.isBlank(cell.getStringCellValue())) {
			return result = columnName + " không được để trống.";
		}
		return result;
	}
	
	@SuppressWarnings("unused")
	public String validateUploadJobsEmptyInt(String columnName, Cell cell) {
		
		String result = null;
		if(cell == null) {
			return result = columnName + " không được để trống.";
		}
		Double value = cell.getNumericCellValue();
		if(value == null) {
			return result = columnName + " không được để trống.";
		}
		return result;
	}
	
	public Integer getIntValueFromCell(Cell cell) {
		String[] parts = cell.getStringCellValue().split(":");
		Integer intValue = Integer.parseInt(parts[1]);
		return intValue;
	}
	
	public File exportToExcelWithResultSet(ResultSet resultSet, String excel_output_file, int row_start, int column_start){

		List<String> headerValues=new ArrayList<String>();
	    XSSFWorkbook workbook = new XSSFWorkbook();
		try {
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
				for (int j = 1; j <= cc + column_start; j++) {
					// System.out.println(resultSet.getString(j));
					XSSFRow row1 = spreadsheet.createRow((short) i);
					row1.createCell((short) i)
							.setCellValue(resultSet.getString(resultSet.getMetaData().getColumnName(j)));
					i++;

				}
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

	    }catch(Exception e){
	    	e.printStackTrace();
            return null;
	    }
	}
	
	public void uploadTableCustomers (MultipartFile file) {
		try {
            // Đọc dữ liệu từ tệp Excel và lưu vào cơ sở dữ liệu
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            int count = 0;
            
            List<Customers> entityList = new ArrayList<>();
            
            while (rowIterator.hasNext()) {
            	count++;
                Row row = rowIterator.next();

                if(count > 1) {
                	
                	Cell cell1 = row.getCell(0);
                	Cell cell2 = row.getCell(1);
                	Cell cell3 = row.getCell(2);
                    Cell cell4 = row.getCell(3);
                    Cell cell5 = row.getCell(4);
                    Cell cell6 = row.getCell(5);
                    Cell cell7 = row.getCell(6);
                    Cell cell8 = row.getCell(7);
                    Cell cell9 = row.getCell(8);
                    Cell cell10 = row.getCell(9);
                    Cell cell11 = row.getCell(10);
                    
                    //
//                    Double idDouble = cell1.getNumericCellValue();
//                	Integer id = idDouble.intValue();
                    Customers entity = new Customers();
                    if(cell1 != null) {
                    	if(!StringUtils.isEmpty(cell1.getStringCellValue())) {
                    		entity = customerRepository.findById(Integer.parseInt(cell1.getStringCellValue())).orElse(new Customers());
                    		if(entity == null) {
                        		continue;
                        	}
                    	}
                    }
                    if(cell2 != null) {
                    	if(!StringUtils.isEmpty(cell2.getStringCellValue())) {
                    		entity.setCustomName(cell2.getStringCellValue());
                    	}
                    }
                    if(cell3 != null) {
                    	if(!StringUtils.isEmpty(cell3.getStringCellValue())) {
                    		entity.setContactName1(cell3.getStringCellValue());
                    	}
                    }
                    if(cell4 != null) {
                    	if(!StringUtils.isEmpty(cell4.getStringCellValue())) {
                    		entity.setPhone1(cell4.getStringCellValue());
                    	}
                    }
                    if(cell5 != null) {
                    	if(!StringUtils.isEmpty(cell5.getStringCellValue())) {
                    		entity.setContactName2(cell5.getStringCellValue());
                    	}
                    }
                    if(cell6 != null) {
                    	if(!StringUtils.isEmpty(cell6.getStringCellValue())) {
                    		entity.setPhone2(cell6.getStringCellValue());
                    	}
                    }
                    if(cell7 != null) {
                    	if(!StringUtils.isEmpty(cell7.getStringCellValue())) {
                    		entity.setType(Integer.parseInt(cell7.getStringCellValue()));
                    	}
                    }
                    if(cell8 != null) {
                    	if(!StringUtils.isEmpty(cell8.getStringCellValue())) {
                    		entity.setBankAcctName(cell8.getStringCellValue());
                    	}
                    }
                    if(cell9 != null) {
                    	if(!StringUtils.isEmpty(cell9.getStringCellValue())) {
                    		entity.setBankAcct(cell9.getStringCellValue());
                    	}
                    }
                    if(cell10 != null) {
                    	if(!StringUtils.isEmpty(cell10.getStringCellValue())) {
                    		entity.setBankAcctNumber(cell10.getStringCellValue());
                    	}
                    }
                	
                	
                	String cell11Val = cell11.getStringCellValue();
                	if(StringUtils.equalsIgnoreCase(cell11Val, "true")) {
                		entity.setState(true);
                	}else {
                		entity.setState(false);
                	}
                	
                	entityList.add(entity);
                }
                
            }
            customerRepository.saveAll(entityList);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void uploadTableCollectPoint (MultipartFile file) {
		try {
            // Đọc dữ liệu từ tệp Excel và lưu vào cơ sở dữ liệu
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            int count = 0;
            
            List<CollectPoint> entityList = new ArrayList<>();
            
            while (rowIterator.hasNext()) {
            	count++;
                Row row = rowIterator.next();

                if(count > 1) {
                	
                	Cell cell1 = row.getCell(0);
                	Cell cell2 = row.getCell(1);
                	Cell cell3 = row.getCell(2);
                    Cell cell4 = row.getCell(3);
                    Cell cell5 = row.getCell(4);
                    Cell cell6 = row.getCell(5);
                    Cell cell7 = row.getCell(6);
                    Cell cell8 = row.getCell(7);
                    Cell cell9 = row.getCell(8);
                    Cell cell10 = row.getCell(9);
                    Cell cell11 = row.getCell(10);
                    Cell cell12 = row.getCell(11);
                	Cell cell13 = row.getCell(12);
                    Cell cell14 = row.getCell(13);
                    Cell cell15 = row.getCell(14);
                    Cell cell16 = row.getCell(15);
                    Cell cell17 = row.getCell(16);
                    Cell cell18 = row.getCell(17);
                    
                    CollectPoint entity = new CollectPoint();
                    if(cell1 != null) {
                    	if(!StringUtils.isEmpty(cell1.getStringCellValue())) {
                    		entity = collectPointRepository.findById(Integer.parseInt(cell1.getStringCellValue())).orElse(new CollectPoint());
                    		if(entity == null) {
                        		continue;
                        	}
                    	}
                    }
                    if(cell2 != null) {
                    	if(!StringUtils.isEmpty(cell2.getStringCellValue())) {
                    		entity.setName(cell2.getStringCellValue());
                    	}
                    }
                    if(cell3 != null) {
                    	if(!StringUtils.isEmpty(cell3.getStringCellValue())) {
                    		entity.setNumAddress(cell3.getStringCellValue());
                    	}
                    }
                    if(cell4 != null) {
                    	if(!StringUtils.isEmpty(cell4.getStringCellValue())) {
                    		entity.setStreetAddress(cell4.getStringCellValue());
                    	}
                    }
                    if(cell5 != null) {
                    	if(!StringUtils.isEmpty(cell5.getStringCellValue())) {
                    		entity.setWard(cell5.getStringCellValue());
                    	}
                    }
                    if(cell6 != null) {
                    	if(!StringUtils.isEmpty(cell6.getStringCellValue())) {
                    		entity.setDist(cell6.getStringCellValue());
                    	}
                    }
                    if(cell7 != null) {
                    	if(!StringUtils.isEmpty(cell7.getStringCellValue())) {
                    		entity.setProvince(cell7.getStringCellValue());
                    	}
                    }
                    if(cell8 != null) {
                    	if(!StringUtils.isEmpty(cell8.getStringCellValue())) {
                    		entity.setRefPlace(cell8.getStringCellValue());
                    	}
                    }
                    if(cell9 != null) {
                    	if(!StringUtils.isEmpty(cell9.getStringCellValue())) {
                    		entity.setContactName(cell9.getStringCellValue());
                    	}
                    }
                    if(cell10 != null) {
                    	if(!StringUtils.isEmpty(cell10.getStringCellValue())) {
                    		entity.setPhone(cell10.getStringCellValue());
                    	}
                    }
                    if(cell11 != null) {
                    	if(!StringUtils.isEmpty(cell11.getStringCellValue())) {
                    		entity.setCustomId(Integer.parseInt(cell11.getStringCellValue()));
                    	}
                    }
                    if(cell12 != null) {
                    	if(!StringUtils.isEmpty(cell12.getStringCellValue())) {
                    		entity.setPosLong(cell12.getStringCellValue());
                    	}
                    }
                    if(cell13 != null) {
                    	if(!StringUtils.isEmpty(cell13.getStringCellValue())) {
                    		entity.setPosLat(cell13.getStringCellValue());
                    	}
                    }
                    if(cell14 != null) {
                    	if(!StringUtils.isEmpty(cell14.getStringCellValue())) {
                    		entity.setBankAcctName(cell14.getStringCellValue());
                    	}
                    }
                    if(cell15 != null) {
                    	if(!StringUtils.isEmpty(cell15.getStringCellValue())) {
                    		entity.setBankAcct(cell15.getStringCellValue());
                    	}
                    }
                    if(cell16 != null) {
                    	if(!StringUtils.isEmpty(cell16.getStringCellValue())) {
                    		entity.setBankAcctNumber(cell16.getStringCellValue());
                    	}
                    }
                    if(cell17 != null) {
                    	if(!StringUtils.isEmpty(cell17.getStringCellValue())) {
                    		entity.setUseCusBank(Integer.parseInt(cell17.getStringCellValue()));
                    	}
                    }
                    
                	String cell18Val = cell18.getStringCellValue();
                	if(StringUtils.equalsIgnoreCase(cell18Val, "true")) {
                		entity.setState(true);
                	}else {
                		entity.setState(false);
                	}
                	
                	entityList.add(entity);
                }
                
            }
            collectPointRepository.saveAll(entityList);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void uploadTableUser (MultipartFile file) {
		try {
            // Đọc dữ liệu từ tệp Excel và lưu vào cơ sở dữ liệu
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            int count = 0;
            
            List<Users> entityList = new ArrayList<>();
            
            while (rowIterator.hasNext()) {
            	count++;
                Row row = rowIterator.next();

                if(count > 1) {
                	
                	Cell cell1 = row.getCell(0);
                	Cell cell2 = row.getCell(1);
                	Cell cell3 = row.getCell(2);
                    Cell cell4 = row.getCell(3);
                    Cell cell5 = row.getCell(4);
                    Cell cell6 = row.getCell(5);
                    
                    Users entity = new Users();
                    if(cell1 != null) {
                    	if(!StringUtils.isEmpty(cell1.getStringCellValue())) {
                    		entity = userRepository.findById(Integer.parseInt(cell1.getStringCellValue())).orElse(new Users());
                    		if(entity == null) {
                        		continue;
                        	}
                    	}
                    }
                    if(cell2 != null) {
                    	if(!StringUtils.isEmpty(cell2.getStringCellValue())) {
                    		entity.setEmployeeId(Integer.parseInt(cell2.getStringCellValue()));
                    	}
                    }
                    if(cell3 != null) {
                    	if(!StringUtils.isEmpty(cell3.getStringCellValue())) {
                    		entity.setCusId(Integer.parseInt(cell3.getStringCellValue()));
                    	}
                    }
                    if(cell4 != null) {
                    	if(!StringUtils.isEmpty(cell4.getStringCellValue())) {
                    		entity.setUserName(cell4.getStringCellValue());
                    	}
                    }
                    if(cell5 != null) {
                    	if(!StringUtils.isEmpty(cell5.getStringCellValue())) {
                    		entity.setPassword(cell5.getStringCellValue());
                    	}
                    }
                    
                	String cell6Val = cell6.getStringCellValue();
                	if(StringUtils.equalsIgnoreCase(cell6Val, "true")) {
                		entity.setState(true);
                	}else {
                		entity.setState(false);
                	}
                	
                	entityList.add(entity);
                }
                
            }
            userRepository.saveAll(entityList);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
}
