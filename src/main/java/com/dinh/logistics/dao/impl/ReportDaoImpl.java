package com.dinh.logistics.dao.impl;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.dao.ReportDao;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.dto.portal.ReportListDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ReportDaoImpl implements ReportDao {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Value("${app.file.sql-select-material-report}")
    private String sqlSelectMaterialReportPath;

    @Override
    public List<Object[]> getReportByFilter(String startDate,String endDate, String cusName) {
    	try {
    		StringBuilder builder = new StringBuilder();
            
            Path path = Paths.get(sqlSelectMaterialReportPath);
            byte[] bytes = Files.readAllBytes(path);

            // Chuyển đổi các byte thành chuỗi sử dụng UTF-8 hoặc một bộ mã khác (tuỳ thuộc vào tệp)
            String content = new String(bytes, StandardCharsets.UTF_8);

            builder.append(content);
//            generateSearchFilter(builder, startDate, endDate, cusName);
            Query query = entityManager.createNativeQuery(builder.toString());

            setSearchFilter(query, startDate, endDate, cusName);

//            query.setFirstResult((page - 1) * size);
//            query.setMaxResults(size);
            List<Object[]> results = query.getResultList();

            return results;
//            return convertJob(results);
    	}catch (Exception e) {
    		return null;
    	}
        
    }
    
    @Override
    public int getCountAllJobByFilter(String startDate,String endDate, String cusName) {
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT count(*) ");
        builder.append(" FROM jobs j ");
        builder.append(" LEFT JOIN collect_point cp on cp.colle_point_id = j.colle_point_id ");
        builder.append(" LEFT JOIN job_type jt on jt.job_type_id = j.job_type_id ");
        builder.append(" WHERE 1=1 ");
        generateSearchFilter(builder, startDate, endDate, cusName);
        Query query = entityManager.createNativeQuery(builder.toString());

        setSearchFilter(query, startDate, endDate, cusName);

        int total = query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;

        return total;
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
