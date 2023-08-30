package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.dto.mobile.JobDetailsDTO;
import com.dinh.logistics.dto.mobile.MaterialJob;
import com.dinh.logistics.dto.mobile.MediaDto;
import com.dinh.logistics.model.EmployeeJob;
import com.dinh.logistics.model.JobState;
import com.dinh.logistics.model.Jobs;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JobsRepositoryImp {

    @PersistenceContext
    private EntityManager entityManager;

    public void insertCollectPoint(int jobType, int idNV1, int idNV2,int assignId, List<Integer> listIdPoint, String ghiChu) {
        for (int i = 0; i < listIdPoint.size(); i++) {
            String sql = "INSERT INTO jobs(colle_point_id, job_type_id,payment_state_id, note, emp_assign_id) VALUES (?, ?, ?, ?, ?) RETURNING job_id";
            Integer generatedId = (Integer) entityManager.createNativeQuery(sql)
                    .setParameter(1, listIdPoint.get(i))
                    .setParameter(2, jobType)
                    .setParameter(3, 1)
                    .setParameter(4, ghiChu)
                    .setParameter(5, assignId)
                    .getSingleResult();

            String sql2 = "INSERT INTO job_employee(job_id, emp_id, serial_number ) VALUES (?, ?, ?)";
            entityManager.createNativeQuery(sql2)
                    .setParameter(1, generatedId)
                    .setParameter(2, idNV1)
                    .setParameter(3, 1)
                    .executeUpdate();

            String sql3 = "INSERT INTO job_employee(job_id, emp_id, serial_number) VALUES (?, ?, ?)";
            entityManager.createNativeQuery(sql3)
                    .setParameter(1, generatedId)
                    .setParameter(2, idNV2)
                    .setParameter(3, 2)
                    .executeUpdate();

        }
    }

    public JobDetailsDTO jobsDetails(Integer job_id) {
        String query = "SELECT j.job_id,jt.job_state_id, jt.job_state_desc, cp.num_address, cp.name, j.priority, j.note " +
                "FROM jobs j " +
                "LEFT JOIN job_state jt ON j.job_state_id = jt.job_state_id " +
                "LEFT JOIN collect_point cp ON j.colle_point_id = cp.colle_point_id " +
                "WHERE j.job_id = :job_id";

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("job_id", job_id);

        String sqlQueryMedia = "SELECT jm.jobMediaId, jm.url, jm.mediaType, jm.jobId FROM JobMedia jm WHERE jm.jobId = :jobId";
        Query queryMedia = entityManager.createQuery(sqlQueryMedia);
        queryMedia.setParameter("jobId", job_id);
        List<Object[]> dataMedia = queryMedia.getResultList();

        String sqlQueryMaterial = "SELECT jm.jobMateId, jm.weight, jm.WeightToCus, jm.unitPrice, jm.jobId, m.name " +
                "FROM JobMaterial jm " +
                "LEFT JOIN Material m ON m.mate_id = jm.mateId WHERE jm.jobId = :jobId";
        Query queryMaterial = entityManager.createQuery(sqlQueryMaterial);
        queryMaterial.setParameter("jobId", job_id);
        List<Object[]> dataMaterial = queryMaterial.getResultList();

        String sqlQueryJobEmployee = "SELECT jm.empId, jm.serialNumber, m.name " +
                "FROM JobEmployee jm " +
                "LEFT JOIN Employee m ON m.empId = jm.empId WHERE jm.jobId = :jobId " +
                "ORDER BY jm.serialNumber ASC";  // Sắp xếp theo serialNumber tăng dần
        Query queryJobEmployee = entityManager.createQuery(sqlQueryJobEmployee);
        queryJobEmployee.setParameter("jobId", job_id);
        List<Object[]> dataJobEmployee = queryJobEmployee.getResultList();

        try {
            //media
            List<MediaDto> mediaDtoList = new ArrayList<>();
            for (Object[] mediaData : dataMedia) {
                MediaDto mediaDto = new MediaDto();
                mediaDto.setMediaId((Integer) mediaData[0]);
                mediaDto.setUrl((String) mediaData[1]);
                mediaDto.setMediaType((Integer) mediaData[2]);
                mediaDto.setJobId((Integer) mediaData[3]);
                mediaDtoList.add(mediaDto);
            }
            //Material
            List<MaterialJob> materialDtoList = new ArrayList<>();
            for (Object[] mediaData : dataMaterial) {
                MaterialJob data = new MaterialJob();
                data.setMateId((Integer) mediaData[0]);
                data.setWeight((Integer) mediaData[1]);
                data.setWeightToCus((Integer) mediaData[2]);
                data.setPrice((Integer) mediaData[3]);
                data.setJobId((Integer) mediaData[4]);
                data.setName((String) mediaData[5]);
                materialDtoList.add(data);
            }
            //List JobEmployee
            List<EmployeeJob> employeeJobsList = new ArrayList<>();
            for (Object[] dataJobEmObjects : dataJobEmployee) {
                EmployeeJob data = new EmployeeJob();
                data.setEmpId((Integer) dataJobEmObjects[0]);
                data.setSerialNumber((String) dataJobEmObjects[1]);
                data.setName((String) dataJobEmObjects[2]);
                employeeJobsList.add(data);
            }
            //JobDetails
            Object[] singleResult = (Object[]) nativeQuery.getSingleResult();
            JobDetailsDTO dto = new JobDetailsDTO();
            dto.setJobId((Integer) singleResult[0]);
            dto.setJobStateId((Integer) singleResult[1]);
            dto.setStateDecs((String) singleResult[2]);
            dto.setNumAddress((String) singleResult[3]);
            dto.setNamePoint((String) singleResult[4]);
            dto.setPriority((BigDecimal) singleResult[5]);
            dto.setNoteJob((String) singleResult[6]);
            dto.setJobMedia(mediaDtoList);
            dto.setJobMaterial(materialDtoList);
            dto.setEmployeeJobs(employeeJobsList);

            return dto;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Jobs findJobById(Integer jobId) {
        return entityManager.find(Jobs.class, jobId);
    }
    public JobState findJobStateById(Integer jobId) {
        return entityManager.find(JobState.class, jobId);
    }


    public Jobs saveJob(Jobs job) {
        return entityManager.merge(job);
    }
}
