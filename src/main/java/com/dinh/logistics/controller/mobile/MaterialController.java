package com.dinh.logistics.controller.mobile;

import com.dinh.logistics.dto.mobile.CollectPointDto;
import com.dinh.logistics.dto.mobile.DeleteMaterialRequest;
import com.dinh.logistics.dto.mobile.DeleteMediaRequest;
import com.dinh.logistics.dto.mobile.MaterialDto;
import com.dinh.logistics.model.Material;
import com.dinh.logistics.respository.MaterialRepository;
import com.dinh.logistics.service.mobile.CollectPointService;
import com.dinh.logistics.service.mobile.MaterialService;
import com.dinh.logistics.ultils.ResponseHandler;
import com.dinh.logistics.ultils.StatusResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mobile")
@Slf4j
@Transactional
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialService materialService;

    @GetMapping("/material_list")
    public ResponseEntity<Object> getAllMaterial(){
        List<Material> collectPoints = materialRepository.findAllMaterialByState();
        if (collectPoints.isEmpty()){
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, null);
        } else {
            List<Material> dTOList = new ArrayList<>();
            for (Material jobType : collectPoints) {
                dTOList.add(jobType);
            }
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("listItem", dTOList);

            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, responseData);
        }
    }

    @PostMapping("/add_material")
    public ResponseEntity<Object> addCollectPoint(@RequestBody MaterialDto materialDto){
        if (materialService.addMaterial(materialDto)) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Thêm vật liệu thành công");
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Thêm vật liệu thất bại");
        }
    }

    @PostMapping("/delete_material")
    public ResponseEntity<Object> deleteMaterial(@RequestBody DeleteMaterialRequest deleteMaterialRequest) {
        if (materialService.deleteMaterial(deleteMaterialRequest.getJobMateId())) {
            return ResponseHandler.generateResponse(HttpStatus.OK, 0, StatusResult.SUCCESS, "Xóa thành công");
        } else {
            return ResponseHandler.generateResponse(HttpStatus.OK, -99, StatusResult.ERROR, "Xóa thất bại");
        }
    }
}
