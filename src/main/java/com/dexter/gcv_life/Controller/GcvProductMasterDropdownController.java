package com.dexter.gcv_life.Controller;

import com.dexter.gcv_life.Entity.GcvProductMaster;
import com.dexter.gcv_life.Entity.GcvProductMasterRequest;
import com.dexter.gcv_life.Repository.GcvProductMasterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class GcvProductMasterDropdownController {
    @Autowired
    GcvProductMasterRepo repository;

    @GetMapping("/accountDropDown")
    public ResponseEntity<List> account(){
        List list = repository.getDistinctAccount();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/GProductNameDropDown")
    public ResponseEntity<List> distinctGProductName(){
        List list = repository.getDistinctGProductName();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/GDivisionDropDown")
    public ResponseEntity<List> distinctGDivision(){
        List list = repository.getDistinctGDivision();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/getGcvProductMasterData")
    public ResponseEntity<List<GcvProductMaster>> getGcvProductMasterData(@RequestBody GcvProductMasterRequest request){
        String account = request.getAccount();
        String gProductName = request.getG_product_name();
        String gDivision = request.getG_division();

        try{
            // When all three dropdown selected
            if(!account.isEmpty() && (!gProductName.isEmpty() || gProductName != null) && !gDivision.isEmpty()){
                List<GcvProductMaster> data = repository.getGcvProductMasterData(account,gProductName,gDivision);
                return new ResponseEntity<List<GcvProductMaster>>(data, HttpStatus.OK);
            }

            // When Account And G-Product Name is selected
            if(!account.isEmpty() && !gProductName.isEmpty() && gDivision.isEmpty()){
                List<GcvProductMaster> data = repository.getGcvProductMasterDataAccountAndGProductName(account,gProductName);
                return new ResponseEntity<List<GcvProductMaster>>(data, HttpStatus.OK);
            }

            // When G-Product Name And G-Division is selected
            if(account.isEmpty() && !gProductName.isEmpty() && !gDivision.isEmpty()){
                List<GcvProductMaster> data = repository.getGcvProductMasterDataGProductNameAndGDivision(gProductName,gDivision);
                return new ResponseEntity<List<GcvProductMaster>>(data, HttpStatus.OK);
            }

            // When Account And G-Division is selected
            if(!account.isEmpty() && gProductName.isEmpty() && !gDivision.isEmpty()){
                List<GcvProductMaster> data = repository.getGcvProductMasterDataAccountAndGDivision(account,gDivision);
                return new ResponseEntity<List<GcvProductMaster>>(data, HttpStatus.OK);
            }

            // When Account is selected
            if(!account.isEmpty() && gProductName.isEmpty() && gDivision.isEmpty()){
                List<GcvProductMaster> data = repository.getGcvProductMasterDataAccount(account);
                return new ResponseEntity<List<GcvProductMaster>>(data, HttpStatus.OK);
            }

            // When G-Product Name is selected
            if(account.isEmpty() && !gProductName.isEmpty() && gDivision.isEmpty()){
                List<GcvProductMaster> data = repository.getGcvProductMasterDataGProductName(gProductName);
                return new ResponseEntity<List<GcvProductMaster>>(data, HttpStatus.OK);
            }


            // When G-Division is selected
            if(account.isEmpty() && gProductName.isEmpty() && !gDivision.isEmpty()){
                List<GcvProductMaster> data = repository.getGcvProductMasterDataGDivision(gDivision);
                return new ResponseEntity<List<GcvProductMaster>>(data, HttpStatus.OK);
            }

        } catch (Exception e){
            return null;
        }
        return null;
    }


}
