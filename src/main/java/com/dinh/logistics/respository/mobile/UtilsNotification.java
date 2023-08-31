package com.dinh.logistics.respository.mobile;

import com.dinh.logistics.dto.FirebaseDataDto;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UtilsNotification {

    @PersistenceContext
    private EntityManager entityManager;

    public static void pushNotifyByEmpId(int empID,String title,String type){

//        String query = "SELECT ";
//
//        FirebaseDataDto sendFirebaseData = new FirebaseDataDto();
//        sendFirebaseData.setTitle(title);
//        sendFirebaseData.setType(type);
//        sendFirebaseData.setData(message);
//
//        Gson gson = new Gson();
//        String jsonData = gson.toJson(sendFirebaseData);
//
//        //Gửi
//        Message mess = Message.builder()
//                .setToken(token)
//                .putData("data", jsonData)
//                .build();
//        String response = FirebaseMessaging.getInstance().send(mess);
    }
}

