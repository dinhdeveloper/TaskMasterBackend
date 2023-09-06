package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.dto.FirebaseDataDto;
import com.dinh.logistics.dto.mobile.DataUpdateJobRequest;
import com.dinh.logistics.dto.mobile.JobDetailsDTO;
import com.dinh.logistics.dto.mobile.MaterialJob;
import com.dinh.logistics.dto.mobile.MediaDto;
import com.dinh.logistics.model.*;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class JobsRepositoryImp {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UtilsNotification utilsNotification;

    public void addJobs(int jobType,int jobStateId, int idNV1, int idNV2, int assignId, List<Integer> listIdPoint, String ghiChu) {
        for (int i = 0; i < listIdPoint.size(); i++) {
            String sql = "INSERT INTO jobs(colle_point_id, job_type_id,payment_state_id, note, emp_assign_id,job_state_id ) VALUES (?, ?, ?, ?, ?,?) RETURNING job_id";
            Integer generatedId = (Integer) entityManager.createNativeQuery(sql)
                    .setParameter(1, listIdPoint.get(i))
                    .setParameter(2, jobType)
                    .setParameter(3, 1)
                    .setParameter(4, ghiChu)
                    .setParameter(5, assignId)
                    .setParameter(6, jobStateId)
                    .getSingleResult();

            String sql2 = "INSERT INTO job_employee(job_id, emp_id, serial_number ) VALUES (?, ?, ?)";
            entityManager.createNativeQuery(sql2)
                    .setParameter(1, generatedId)
                    .setParameter(2, idNV1)
                    .setParameter(3, 1)
                    .executeUpdate();

            pushNotifyAddTask(idNV1, generatedId, assignId);

            if (idNV2 != -1){
                String sql3 = "INSERT INTO job_employee(job_id, emp_id, serial_number) VALUES (?, ?, ?)";
                entityManager.createNativeQuery(sql3)
                        .setParameter(1, generatedId)
                        .setParameter(2, idNV2)
                        .setParameter(3, 2)
                        .executeUpdate();
                pushNotifyAddTask(idNV2, generatedId, assignId);
            }
        }
    }

    private void pushNotifyAddTask(int idNV, int generatedJobId, int assignId) {
        List<NotifyTopic> notifyTopicList = utilsNotification.pushNotifyByEmpId(idNV, generatedJobId);
        for (NotifyTopic notifyTopic : notifyTopicList) {
            if (notifyTopic.getEmp_id() != assignId) {
                String content = "Nhân viên: " + notifyTopic.getName() +
                        ", Loại công việc: " + notifyTopic.getJtName() +
                        ", Địa điểm: " + notifyTopic.getCpName();

                FirebaseDataDto sendFirebaseData = new FirebaseDataDto();
                sendFirebaseData.setTitle("Công việc mới");
                sendFirebaseData.setType("WORK");
                sendFirebaseData.setBody(content);
                sendFirebaseData.setData(String.valueOf(generatedJobId));

                Gson gson = new Gson();
                String jsonData = gson.toJson(sendFirebaseData);

                // Gửi
                if (notifyTopic.getFirebase_token() != null && notifyTopic.getIs_active_access_token()){
                    Message message = Message.builder()
                            .setToken(notifyTopic.getFirebase_token())
                            .putData("data", jsonData)
                            .build();
                    try {
                        FirebaseMessaging.getInstance().send(message);
//                        String response = FirebaseMessaging.getInstance().send(message);
//                        if (response != null){
//                            utilsNotification.insertDataToNotification(data,jobsNew.getJob_id());
//                        }
                    } catch (FirebaseMessagingException e) {
                        // Xử lý ngoại lệ ở đây
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public JobDetailsDTO jobsDetails(Integer job_id, Integer emp_id) {
        String query = "SELECT j.job_id, jt.job_state_id, jt.job_state_desc, cp.num_address, cp.name, j.priority, j.note,jt.job_state_code,j.amount_paid_emp  " +
                "FROM jobs j " +
                "LEFT JOIN job_state jt ON j.job_state_id = jt.job_state_id " +
                "LEFT JOIN collect_point cp ON j.colle_point_id = cp.colle_point_id " +
                "LEFT JOIN job_employee jm ON j.job_id = jm.job_id AND jm.emp_id = :emp_id " +
                "WHERE j.job_id = :job_id";

        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("job_id", job_id);
        nativeQuery.setParameter("emp_id", emp_id);

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
            dto.setJobStateCode((String) singleResult[7]);
            dto.setAmountPaidEmp((BigDecimal) singleResult[8]);
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

    public void pushNotifyUpdateJobState(Jobs jobsNew) {
        if (jobsNew == null) {
            return;
        }

        String query = "SELECT je.empId FROM JobEmployee je WHERE je.jobId = :job_id";
        Query queryData = entityManager.createQuery(query);
        queryData.setParameter("job_id", jobsNew.getJob_id());
        List<Integer> dataNVID = queryData.getResultList();

        for (Integer data : dataNVID) {
            List<NotifyTopic> notifyTopicList = utilsNotification.pushNotifyByEmpId(data, jobsNew.getJob_id());
            for (NotifyTopic notifyTopic : notifyTopicList) {
                if (notifyTopic.getEmp_id() != jobsNew.getEmpAssignId()) {
                    String content = "Nhân viên: " + notifyTopic.getName() +
                            ", Loại công việc: " + notifyTopic.getJtName() +
                            ", Địa điểm: " + notifyTopic.getCpName();

                    FirebaseDataDto sendFirebaseData = new FirebaseDataDto();
                    sendFirebaseData.setTitle("Công việc mới");
                    sendFirebaseData.setType("WORK");
                    sendFirebaseData.setBody(content);
                    sendFirebaseData.setData(String.valueOf(jobsNew.getJob_id()));

                    Gson gson = new Gson();
                    String jsonData = gson.toJson(sendFirebaseData);

                    // Gửi
                    if (notifyTopic.getFirebase_token() != null && notifyTopic.getIs_active_access_token()){
                        Message message = Message.builder()
                                .setToken(notifyTopic.getFirebase_token())
                                .putData("data", jsonData)
                                .build();
                        try {
                            FirebaseMessaging.getInstance().send(message);
//                        String response = FirebaseMessaging.getInstance().send(message);
//                        if (response != null){
//                            utilsNotification.insertDataToNotification(data,jobsNew.getJob_id());
//                        }
                        } catch (FirebaseMessagingException e) {
                            // Xử lý ngoại lệ ở đây
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Jobs updateJobSave(DataUpdateJobRequest dataUpdateJobRequest, Jobs job) {
        if (job != null){
            if (dataUpdateJobRequest.getTotalMoney() != null){
                job.setAmount(Integer.parseInt(dataUpdateJobRequest.getTotalMoney()));
            }
            if (dataUpdateJobRequest.getPriority() != -1 || dataUpdateJobRequest.getPriority() != null){
                job.setPriority(dataUpdateJobRequest.getPriority());
            }
            if (dataUpdateJobRequest.getAmountPaidEmp() != null){
                job.setAmountPaidEmp(Integer.parseInt(dataUpdateJobRequest.getAmountPaidEmp()));
            }
            if (dataUpdateJobRequest.getNote() != null){
                job.setNote(dataUpdateJobRequest.getNote());
            }

            String sqlQueryJobEmployee = "UPDATE JobEmployee SET empId = :empIdNew WHERE empId = :empIdOld AND jobId = :jodId";
            Query queryJobEmployee = entityManager.createQuery(sqlQueryJobEmployee);
            queryJobEmployee.setParameter("empIdNew", dataUpdateJobRequest.getEmpNewId());
            queryJobEmployee.setParameter("empIdOld", dataUpdateJobRequest.getEmpOldId());
            queryJobEmployee.setParameter("jodId", dataUpdateJobRequest.getJodId());
            queryJobEmployee.executeUpdate();

           return entityManager.merge(job);
        }
        return job;
    }
}
