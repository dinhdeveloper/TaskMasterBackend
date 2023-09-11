package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.model.Employee;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class EmployeeRepositoryImp {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Employee> findEmployeesByJobId(Integer jobId) {
        String hql = "SELECT e FROM Employee e " +
                "LEFT JOIN JobEmployee jm ON e.empId = jm.empId AND jm.jobId = :jobId " +
                "WHERE jm.JobEmpId IS NULL  AND e.state = true";
        TypedQuery<Employee> query = entityManager.createQuery(hql, Employee.class);
        query.setParameter("jobId", jobId);
        return query.getResultList();
    }
}
