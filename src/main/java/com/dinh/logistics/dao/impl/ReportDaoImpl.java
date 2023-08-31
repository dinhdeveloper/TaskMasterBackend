package com.dinh.logistics.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
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

    @Override
    public List<ReportListDto> getReportByFilter(String startDate,String endDate, String cusName) {
        StringBuilder builder = new StringBuilder();

        builder.append(" select c.custom_name, j.creation_time, jm1.weight as vl1, jm2.weight_to_cus as vl2, jm3.weight_to_cus as vl3, jm4.weight_to_cus as vl4, ");
        builder.append(" (jm1.weight + jm2.weight + jm3.weight + jm4.weight) as total ");
        builder.append(" from jobs j ");
        builder.append(" join job_material jm1 on jm1.job_id = j.job_id ");
        builder.append(" join job_material jm2 on jm2.job_id = j.job_id ");
        builder.append(" join job_material jm3 on jm3.job_id = j.job_id ");
        builder.append(" join job_material jm4 on jm4.job_id = j.job_id ");
        builder.append(" join material m1 on jm1.mate_id = m1.mate_id and m1.code = 'BRICH' ");
        builder.append(" join material m2 on jm2.mate_id = m2.mate_id and m2.code = 'ROCK' ");
        builder.append(" join material m3 on jm3.mate_id = m3.mate_id and m3.code = 'CEMENT' ");
        builder.append(" join material m4 on jm4.mate_id = m4.mate_id and m4.code = 'SAND' ");
        builder.append(" join collect_point cp on j.colle_point_id = cp.colle_point_id ");
        builder.append(" join customers c on cp.custom_id = c.custom_id ");
        builder.append(" WHERE 1=1 ");
        generateSearchFilter(builder, startDate, endDate, cusName);
        Query query = entityManager.createNativeQuery(builder.toString());

        setSearchFilter(query, startDate, endDate, cusName);

//        query.setFirstResult((page - 1) * size);
//        query.setMaxResults(size);
        List<Object[]> results = query.getResultList();

        return convertJob(results);
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
            stringBuilder.append(" AND DATE_TRUNC('day', j.creation_time) >= TO_DATE(:startDate,'dd/MM/yyyy') ");
        }

        if (!StringUtils.isEmpty(endDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.creation_time) <= TO_DATE(:endDate,'dd/MM/yyyy') ");
        }
        
        if (!StringUtils.isEmpty(cusName)) {
            stringBuilder.append(" and lower(c.custom_name) like lower(:cusName) ");
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
