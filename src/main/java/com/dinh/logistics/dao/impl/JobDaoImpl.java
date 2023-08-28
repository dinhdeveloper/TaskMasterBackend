package com.dinh.logistics.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.UserDeviceDto;
import com.dinh.logistics.dto.portal.JobListDto;
import com.dinh.logistics.model.UserDevice;
import com.dinh.logistics.ultils.DateHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class JobDaoImpl implements JobDao{

	@PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<JobListDto> getAllJobByFilter(String startDate,String endDate, int page, int size) {
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT j.job_id, cp.num_address, j.creation_time, jt.job_type_name, j.priority, j.note ");
        builder.append(" FROM jobs j ");
        builder.append(" LEFT JOIN collect_point cp on cp.colle_point_id = j.colle_point_id ");
        builder.append(" LEFT JOIN job_type jt on jt.job_type_id = j.job_type_id ");
        builder.append(" WHERE 1=1 ");
        generateSearchFilter(startDate, endDate, builder, false);
        Query query = entityManager.createNativeQuery(builder.toString());

        setSearchFilter(startDate,endDate,query);

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        List<Object[]> results = query.getResultList();

        return convertJob(results);
    }
    
    @Override
    public int getCountAllJobByFilter(String startDate,String endDate, int page, int size) {
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT count(*) ");
        builder.append(" FROM jobs j ");
        builder.append(" LEFT JOIN collect_point cp on cp.colle_point_id = j.colle_point_id ");
        builder.append(" LEFT JOIN job_type jt on jt.job_type_id = j.job_type_id ");
        builder.append(" WHERE 1=1 ");
        generateSearchFilter(startDate, endDate, builder, true);
        Query query = entityManager.createNativeQuery(builder.toString());

        setSearchFilter(startDate,endDate,query);

        int total = query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;

        return total;
    }
    
    @Override
    public List<UserDeviceDto> getListUserDeviceToPushNotification(List<Integer> ids) {
    	StringBuilder builder = new StringBuilder();

        builder.append(" select distinct ud.* from user_devices ud ");
        builder.append(" join users u on ud.user_id = u.user_id ");
        builder.append(" join job_employee je on u.employee_id = je.emp_id ");
        builder.append(" WHERE 1=1 ");
        
        generateSearchFilter(ids, builder);
        Query query = entityManager.createNativeQuery(builder.toString());
        setSearchFilter(ids, query);

        List<Object[]> results = query.getResultList();

        return convertUserDevice(results);
    }
    
    public void generateSearchFilter(String startDate,String endDate, StringBuilder stringBuilder, boolean isCount){

    	if (!StringUtils.isEmpty(startDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.creation_time) >= TO_DATE(:startDate,'dd/MM/yyyy') ");
        }

        if (!StringUtils.isEmpty(endDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.creation_time) <= TO_DATE(:endDate,'dd/MM/yyyy') ");
        }

        if(isCount == false) {
        	stringBuilder.append(" ORDER BY j.creation_time desc");
        }
        
    }
    public void setSearchFilter(String startDate,String endDate, Query query){

        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate",startDate);
        }

        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate",endDate);
        }
        
    }
    
    public List<JobListDto> convertJob (List<Object[]> storedProcedureResults) {
		try {
			return storedProcedureResults.stream().map(result -> {
				JobListDto jobListDto = new JobListDto((Integer) result[0], (String) result[1],
						(Date) result[2], (String) result[3], null,
						null, null, (BigDecimal) result[4], (String) result[5]);
				return jobListDto;
			}).collect(Collectors.toList());
		} catch (Exception e) {
        	log.error("convertJob: "+ e.getMessage());
			return null;
		}
    }
    
    public void generateSearchFilter(List<Integer> ids, StringBuilder stringBuilder){

    	if (ids != null) {
            stringBuilder.append(" and je.job_id in (:ids) ");
        }

    	stringBuilder.append(" and ud.is_active_access_token = true ");
    }
    
    public void setSearchFilter(List<Integer> ids, Query query){

        if (!ids.isEmpty()) {
            query.setParameter("ids",ids);
        }

    }
    
    public List<UserDeviceDto> convertUserDevice (List<Object[]> storedProcedureResults) {
		try {
			return storedProcedureResults.stream().map(result -> {
				UserDeviceDto userDeviceDto = new UserDeviceDto((Integer) result[0], (String) result[1],
						(Integer) result[2], (String) result[4], (String) result[5]);
				
				return userDeviceDto;
			}).collect(Collectors.toList());
		} catch (Exception e) {
        	log.error("convertJob: "+ e.getMessage());
			return null;
		}
    }
}
