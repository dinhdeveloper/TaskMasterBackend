package com.dinh.logistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "name")
    private String name;

    @Column(name = "leader_id")
    private Integer leaderId;

    @Column(name = "territory")
    private String territory;

    @Column(name = "state")
    private boolean state;

}
