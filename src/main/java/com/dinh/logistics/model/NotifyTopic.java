package com.dinh.logistics.model;

import lombok.Data;

@Data
public class NotifyTopic {
    Integer emp_id;
    String name;
    Integer team_id;
    Integer leader_id;
    String firebase_token;
    String access_token;
    String device_id;
    String device_name;
    Boolean is_active_access_token;
    String cpName;
    String jtName;
}
