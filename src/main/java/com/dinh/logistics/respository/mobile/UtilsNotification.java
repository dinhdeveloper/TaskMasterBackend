package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.model.NotifyTopic;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UtilsNotification {

    @PersistenceContext
    private EntityManager entityManager;

    public List<NotifyTopic> pushNotifyByEmpId(int empID,int jodID) {
        // Lấy team_id từ bảng Employee
       return getListNotify(empID,jodID);
    }

    private List<NotifyTopic> getListNotify(int empID,int jodID) {
        String queryTeamID = "SELECT team_id FROM employee WHERE emp_id = :empID";
        Query qTeam = entityManager.createNativeQuery(queryTeamID);
        qTeam.setParameter("empID", empID);
        Integer teamID = (Integer) qTeam.getSingleResult();

        String queryLeadID = "SELECT t.leader_id " +
                "FROM team t " +
                "JOIN employee e on t.team_id = e.team_id " +
                "WHERE t.team_id = :teamID and e.emp_id = :empID";
        Query qLeadID = entityManager.createNativeQuery(queryLeadID);
        qLeadID.setParameter("empID", empID);
        qLeadID.setParameter("teamID", teamID);
        Integer leaderId = (Integer) qLeadID.getSingleResult();

        // Lấy thông tin nhân viên:
        String queryEmployee =
                "SELECT e.emp_id, e.name, e.team_id, ud.firebase_token, ud.access_token," +
                        " ud.device_id, ud.device_name, ud.is_active_access_token " +
                        "FROM employee e " +
                        "LEFT JOIN users uv ON e.emp_id = uv.employee_id " +
                        "LEFT JOIN user_devices ud ON ud.user_id = uv.user_id " +
                        "WHERE e.emp_id = :empID and e.state = true " +
                "AND ud.date_create_login = (SELECT MAX(ud2.date_create_login) FROM user_devices ud2 WHERE ud2.user_id = ud.user_id )LIMIT 1";
        Query queryEmp = entityManager.createNativeQuery(queryEmployee);
        queryEmp.setParameter("empID", empID);
        List<Object[]> empResultList = queryEmp.getResultList();


        // Lấy thông tin leader:
        String queryLeader = "SELECT e.emp_id, e.name, e.team_id, ud.firebase_token, ud.access_token," +
                " ud.device_id, ud.device_name, ud.is_active_access_token " +
                "FROM employee e " +
                "LEFT JOIN users uv ON e.emp_id = uv.employee_id " +
                "LEFT JOIN user_devices ud ON ud.user_id = uv.user_id " +
                "WHERE e.emp_id = :leaderId and e.state = true " +
                "AND ud.date_create_login = (SELECT MAX(ud2.date_create_login) FROM user_devices ud2 WHERE ud2.user_id = ud.user_id )LIMIT 1";

        Query queryLead = entityManager.createNativeQuery(queryLeader);
        queryLead.setParameter("leaderId", leaderId);
        List<Object[]> leaderData = queryLead.getResultList();

        //Lấy thông tin jobs
        String queryJob =
                "SELECT cp.name as cpName, " +
                        "jt.job_type_name as jtName " +
                        "FROM jobs j " +
                        "LEFT JOIN job_type jt ON j.job_type_id = jt.job_type_id " +
                        "LEFT JOIN collect_point cp ON cp.colle_point_id = j.colle_point_id " +
                        "WHERE j.job_id = :jodID";

        Query qJobs = entityManager.createNativeQuery(queryJob);
        qJobs.setParameter("jodID", jodID);
        Object[] jobsData = (Object[]) qJobs.getSingleResult();

        //get master
        String queryMaster = "SELECT e.emp_id, e.name, e.team_id, ud.firebase_token, ud.access_token, " +
                "ud.device_id, ud.device_name, ud.is_active_access_token, t.leader_id " +
                "FROM employee e " +
                "LEFT JOIN users uv ON e.emp_id = uv.employee_id " +
                "JOIN user_devices ud ON ud.user_id = uv.user_id " +
                "JOIN role_pj rp ON rp.role_id = e.role_id " +
                "JOIN team t ON t.team_code = 'MASTER' "+
                "WHERE rp.role_code = 'MASTER' and e.state = true " +
                "AND ud.date_create_login = (SELECT MAX(ud2.date_create_login) FROM user_devices ud2 WHERE ud2.user_id = ud.user_id )LIMIT 1";

        Query queryM = entityManager.createNativeQuery(queryMaster);
        List<Object[]> masterData = queryM.getResultList();

        List<NotifyTopic> notifyTopicList = new ArrayList<>();
        String nameEmp = null;

        for (Object[] empData : empResultList) {
            NotifyTopic mediaDto = new NotifyTopic();
            mediaDto.setEmp_id((Integer) empData[0]);
            mediaDto.setName((String) empData[1]);
            nameEmp = (String) empData[1];
            mediaDto.setTeam_id((Integer) empData[2]);
            mediaDto.setFirebase_token((String) empData[3]);
            mediaDto.setAccess_token((String) empData[4]);
            mediaDto.setDevice_id((String) empData[5]);
            mediaDto.setDevice_name((String) empData[6]);
            mediaDto.setIs_active_access_token((Boolean) empData[7]);
            mediaDto.setLeader_id(leaderId);
            mediaDto.setCpName((String) jobsData[0]);
            mediaDto.setJtName((String) jobsData[1]);
            notifyTopicList.add(mediaDto);
        }

        for (Object[] leaData : leaderData) {
            NotifyTopic mediaDto = new NotifyTopic();
            mediaDto.setEmp_id((Integer) leaData[0]);
            mediaDto.setName(nameEmp);
            mediaDto.setTeam_id((Integer) leaData[2]);
            mediaDto.setFirebase_token((String) leaData[3]);
            mediaDto.setAccess_token((String) leaData[4]);
            mediaDto.setDevice_id((String) leaData[5]);
            mediaDto.setDevice_name((String) leaData[6]);
            mediaDto.setIs_active_access_token((Boolean) leaData[7]);
            mediaDto.setLeader_id(leaderId);
            mediaDto.setCpName((String) jobsData[0]);
            mediaDto.setJtName((String) jobsData[1]);
            notifyTopicList.add(mediaDto);
        }

        for (Object[] masData : masterData) {
            NotifyTopic mediaDto = new NotifyTopic();
            mediaDto.setEmp_id((Integer) masData[0]);
            mediaDto.setName(nameEmp);
            mediaDto.setTeam_id((Integer) masData[2]);
            mediaDto.setFirebase_token((String) masData[3]);
            mediaDto.setAccess_token((String) masData[4]);
            mediaDto.setDevice_id((String) masData[5]);
            mediaDto.setDevice_name((String) masData[6]);
            mediaDto.setIs_active_access_token((Boolean) masData[7]);
            mediaDto.setLeader_id((Integer) masData[8]);
            mediaDto.setCpName((String) jobsData[0]);
            mediaDto.setJtName((String) jobsData[1]);
            notifyTopicList.add(mediaDto);
        }

        return notifyTopicList;
    }

    public void insertDataToNotification(Integer data, Integer jobId) {
        //làm phase 2
    }
}

