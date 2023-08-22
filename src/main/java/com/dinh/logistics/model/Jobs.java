package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Integer job_id;

    @Column(name = "colle_point_id")
    private Integer collePointId;

    @Column(name = "job_type_id")
    private Integer jobTypeId;

    @Column(name = "payment_state_id", nullable = false, columnDefinition = "integer default 1")
    private Integer paymentStateId;

    @Column(name = "payment_method")
    private Integer paymentMethod;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "amount_paid_emp")
    private Integer amountPaidEmp;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "job_state_id")
    private Integer jobStateId;

    @Column(name = "creation_time")
    private Timestamp creationTime;

    @Column(name = "assign_time")
    private Timestamp assignTime;

    @Column(name = "collect_finish_time")
    private Timestamp collectFinishTime;

    @Column(name = "trans_time")
    private Timestamp transTime;

    @Column(name = "finish_time")
    private Timestamp finishTime;

    @Column(name = "note")
    private String note;

    @Column(name = "emp_assign_id")
    private String empAssignId;


}
