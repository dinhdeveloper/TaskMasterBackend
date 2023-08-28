package com.dinh.logistics.service.portal;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.model.CollectPoint;
import com.dinh.logistics.model.Employee;
import com.dinh.logistics.model.JobEmployee;
import com.dinh.logistics.model.Jobs;
import com.dinh.logistics.respository.CollectPointRepository;
import com.dinh.logistics.respository.EmployeeRepository;
import com.dinh.logistics.respository.JobEmployeeRepository;
import com.dinh.logistics.respository.JobRepository;
import com.dinh.logistics.respository.JobTypeRepository;
import com.dinh.logistics.respository.RolePjRepository;
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
                    String column9Validate = validateUploadJobsEmptyInt("Ưu tiên", cell9);

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
                    	
                    	Double priorityDouble = cell9.getNumericCellValue();
                    	Integer priority = priorityDouble.intValue();
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
}
