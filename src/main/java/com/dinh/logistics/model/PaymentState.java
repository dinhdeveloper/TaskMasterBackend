package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PaymentState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_state_id")
    private Integer paymentStateId;

    @Column(name = "payment_state_name")
    private String paymentStateName;

    @Column(name = "payment_state_desc")
    private String paymentStateDesc;

    @Column(name = "state")
    private boolean state;
}
