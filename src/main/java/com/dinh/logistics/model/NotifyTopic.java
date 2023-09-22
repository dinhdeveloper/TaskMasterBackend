package com.dinh.logistics.model;

import lombok.Data;

@Data
public class NotifyTopic {
    Integer emp_id;
    String name;
    Integer team_id;
    Integer leader_id;
    String firebase_token = null;
    String access_token= null;
    String device_id= null;
    String device_name= null;
    Boolean is_active_access_token;
    String role_code= null;
    String cpName= null;
    String jtName= null;
}
