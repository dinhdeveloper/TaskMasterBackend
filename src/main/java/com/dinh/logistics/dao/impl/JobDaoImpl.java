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

import com.dinh.logistics.dao.JobDao;
import com.dinh.logistics.dto.UserDeviceDto;
import com.dinh.logistics.dto.mobile.JobSearchResponseDto;
import com.dinh.logistics.dto.portal.JobListDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class JobDaoImpl implements JobDao{

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
    public List<JobListDto> getAllJobNotiByFilter(List<Integer> ids) {
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT j.job_id, cp.num_address, j.creation_time, jt.job_type_name, j.priority, j.note,  ");
        builder.append(" FROM jobs j ");
        builder.append(" LEFT JOIN collect_point cp on cp.colle_point_id = j.colle_point_id ");
        builder.append(" LEFT JOIN job_type jt on jt.job_type_id = j.job_type_id ");
        builder.append(" WHERE 1=1 ");
        generateSearchFilter(builder, ids);
        Query query = entityManager.createNativeQuery(builder.toString());

        setSearchFilter(ids, query);

        List<Object[]> results = query.getResultList();

        return convertJob(results);
    }

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

    @Override
    public List<JobSearchResponseDto> searchJobByFilter(Integer empId, Integer status, Integer paymentStatus, String startDate, String endDate, Integer jobId, String collectPoint, Integer teamId) {
        StringBuilder builder = new StringBuilder();

        builder.append(" select j.job_id, cp.name as collect_point_name, e.name as employee_name, js.job_state_desc, j.priority, j.creation_time, je.emp_id ");
        builder.append(" from jobs j ");
        builder.append(" left join job_employee je on j.job_id = je.job_id ");
        builder.append(" left join job_state js on j.job_state_id = js.job_state_id ");
        builder.append(" left join payment_state ps on j.payment_state_id = ps.payment_state_id ");
        builder.append(" left join collect_point cp on j.colle_point_id = cp.colle_point_id ");
        builder.append(" left join employee e on e.emp_id = je.emp_id ");
        builder.append(" WHERE 1=1 ");
        generateSearchFilter(builder, empId, status, paymentStatus, startDate,  endDate, jobId, collectPoint, teamId);
        Query query = entityManager.createNativeQuery(builder.toString());

        setSearchFilter(query, empId, status, paymentStatus, startDate,  endDate, jobId, collectPoint, teamId);

        List<Object[]> results = query.getResultList();

        return convertJobSearchResponse(results);
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
						null, null, (BigDecimal) result[4], (String) result[5], null, null, null);
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
    
    public void generateSearchFilter(StringBuilder stringBuilder, List<Integer> ids){

    	if (ids != null) {
            stringBuilder.append(" and j.job_id in (:ids) ");
        }

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
    
    public void generateSearchFilter(StringBuilder stringBuilder, Integer empId, Integer status, Integer paymentStatus, String startDate, String endDate, Integer jobId, String collectPoint, Integer teamId){

    	if (!StringUtils.isEmpty(startDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.creation_time) >= TO_DATE(:startDate,'dd/MM/yyyy') ");
        }

        if (!StringUtils.isEmpty(endDate)) {
            stringBuilder.append(" AND DATE_TRUNC('day', j.creation_time) <= TO_DATE(:endDate,'dd/MM/yyyy') ");
        }
        
        if (empId != null) {
            stringBuilder.append(" and e.emp_id = :empId ");
        }
        if (status != null) {
        	if(status == 1) {
        		stringBuilder.append(" and (js.job_state_code = 'NEW' or js.job_state_code = 'ASSIGNED' or js.job_state_code = 'ACCEPTED' or js.job_state_code = 'COMPACTED' or js.job_state_code = 'WEIGHTED' or js.job_state_code = 'ON_TRUCK') ");
        	}else if(status == 2) {
        		stringBuilder.append(" and (js.job_state_code = 'DONE' or js.job_state_code = 'REJECTED' or js.job_state_code = 'CANCELED') ");
        	}else {
        		//
        	}
            
        }
        if (paymentStatus != null) {
            stringBuilder.append(" and ps.payment_state_status = :paymentStatus ");
        }
        if (jobId != null) {
            stringBuilder.append(" and j.job_id = :jobId ");
        }
        if (teamId != -1){
            stringBuilder.append(" and e.team_id = :teamId ");
        }
        if (!StringUtils.isEmpty(collectPoint)) {
            stringBuilder.append(" and lower(cp.name) like lower(:collectPoint) ");
        }

//        if(isCount == false) {
//        	stringBuilder.append(" ORDER BY j.creation_time desc");
//        }
        
    }
    
    public void setSearchFilter(Query query, Integer empId, Integer status, Integer paymentStatus, String startDate, String endDate, Integer jobId, String collectPoint, Integer teamId){

        if (!StringUtils.isEmpty(startDate)) {
            query.setParameter("startDate",startDate);
        }

        if (!StringUtils.isEmpty(endDate)) {
            query.setParameter("endDate",endDate);
        }
        
        if (empId != null) {
            query.setParameter("empId",empId);
        }
        if (teamId != -1) {
            query.setParameter("teamId",teamId);
        }


//        if (status != null) {
//            query.setParameter("status",status);
//        }
        
        if (paymentStatus != null) {
            query.setParameter("paymentStatus",paymentStatus);
        }

        if (jobId != null) {
            query.setParameter("jobId",jobId);
        }
        
        if (!StringUtils.isEmpty(collectPoint)) {
            query.setParameter("collectPoint", "%" + collectPoint + "%");
        }

    }
    
    public List<JobSearchResponseDto> convertJobSearchResponse (List<Object[]> storedProcedureResults) {
		try {
			return storedProcedureResults.stream().map(result -> {
				JobSearchResponseDto resultList = new JobSearchResponseDto(
				        (Integer) result[0],
                        (String) result[1],
						(String) result[2],
                        (String) result[3],
						(BigDecimal) result[4],
                        (Date) result[5],
                        (Integer)(result[6]));
				return resultList;
			}).collect(Collectors.toList());
		} catch (Exception e) {
        	log.error("convertJob: "+ e.getMessage());
			return null;
		}
    }
    
}
