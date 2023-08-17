package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;
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

    @Column(name = "job_media_id")
    private Integer jobMediaId;

    @Column(name = "payment_state_id")
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
    private String creationTime;

    @Column(name = "assign_time")
    private Date assignTime;

    @Column(name = "collect_finish_time")
    private Date collectFinishTime;

    @Column(name = "trans_time")
    private Date transTime;

    @Column(name = "finish_time")
    private Date finishTime;

    @Column(name = "note")
    private String note;


}
