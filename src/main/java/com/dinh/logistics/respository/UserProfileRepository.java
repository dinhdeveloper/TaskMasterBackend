package com.dinh.logistics.respository;

import com.dinh.logistics.model.UserProfileInfo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

@Repository
public class UserProfileRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public UserProfileInfo getUserInfoByUserName(String userName) {
        String query = "SELECT " +
                "e.emp_id, e.name, e.age, e.gender, e.phone, e.num_address, e.street_address, e.ward, e.dist, e.province, " +
                "t.team_id, t.name AS team_name, t.leader_id, t.territory, " +
                "r.role_id, r.role_name, r.role_code " +
                "FROM Users u " +
                "JOIN Employee e ON u.employee_id = e.emp_id " +
                "JOIN Team t ON e.team_id = t.team_id " +
                "JOIN Role_Pj r ON e.role_id = r.role_id " +
                "WHERE u.user_name = :userName";

        Object[] result = (Object[]) entityManager.createNativeQuery(query)
                .setParameter("userName", userName)
                .getSingleResult();

        Integer empId = (Integer) result[0];
        String name = (String) result[1];
        BigDecimal age = (BigDecimal) result[2];
        String gender = (String) result[3];
        String phone = (String) result[4];
        String numAddress = (String) result[5];
        String streetAddress = (String) result[6];
        String ward = (String) result[7];
        String dist = (String) result[8];
        String province = (String) result[9];
        Integer teamId = (Integer) result[10];
        String teamName = (String) result[11];
        Integer leaderId = (Integer) result[12];
        String territory = (String) result[13];
        Integer roleId = (Integer) result[14];
        String roleName = (String) result[15];
        String roleCode = (String) result[16];


        return new UserProfileInfo(
                empId,
                name,
                age,
                gender,
                phone,
                numAddress,
                streetAddress,
                ward,
                dist,
                province,
                teamId,
                teamName,
                leaderId,
                territory,
                roleId,
                roleName,
                roleCode);
    }
}
