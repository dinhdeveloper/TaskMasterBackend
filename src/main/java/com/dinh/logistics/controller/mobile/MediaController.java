package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.LoginDto;
import com.dinh.logistics.respository.UserRepository;
import com.dinh.logistics.respository.MediaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class MediaController {

    @Autowired
    MediaRepository mediaRepository;

    @PostMapping("/login")
    public ResponseEntity<Object> uploadMedia(@Valid @RequestBody LoginDto loginDto){
        return null;
    }

}
