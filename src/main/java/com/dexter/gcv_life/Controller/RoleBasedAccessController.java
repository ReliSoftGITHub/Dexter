package com.dexter.gcv_life.Controller;

import com.dexter.gcv_life.Entity.*;
import com.dexter.gcv_life.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class RoleBasedAccessController {

    @Autowired
    PriceMasterRepo priceMasterRepository;

    @Autowired
    GeographyMasterRepository geographyMasterRepo;

    @Autowired
    GcvProductMasterRepo gcvProductMasterRepository;

    @Autowired
    TerritoryMasterFinalRepository territoryMasterFinalRepo;

    @Autowired
    MonthlyDataMasterRepo monthlyDataMasterRepository;

    @GetMapping("/getPriceMasterRoleBasedData/{g_division}")
    public ResponseEntity<List<PriceMaster>> getPriceMasterRoleBasedData(@PathVariable String g_division){
        List<PriceMaster> data = priceMasterRepository.getDataDivisionBased(g_division);
        return new ResponseEntity<List<PriceMaster>>(data, HttpStatus.OK);
    }

    @GetMapping("/getGeographyMasterRoleBasedData/{zone}")
    public ResponseEntity<List<GeographyMaster>> getGeographyMasterRoleBasedData(@PathVariable String zone){
        List<GeographyMaster> data = geographyMasterRepo.getDataDivisionBasedGeo(zone);
        return new ResponseEntity<List<GeographyMaster>>(data, HttpStatus.OK);
    }

    @GetMapping("/getGcvProdMasterRoleBasedData/{g_division}")
    public ResponseEntity<List<GcvProductMaster>> getGcvProdMasterRoleBasedData(@PathVariable String g_division){
        List<GcvProductMaster> data = gcvProductMasterRepository.getDataDivisionBasedGcvProd(g_division);
        return new ResponseEntity<List<GcvProductMaster>>(data, HttpStatus.OK);
    }

    @GetMapping("/getTerritoryMasterRoleBasedData/{division}")
    public ResponseEntity<List<TerritoryMasterFinal>> getTerritoryMasterRoleBasedData(@PathVariable String division){
        List<TerritoryMasterFinal> data = territoryMasterFinalRepo.getDivisionRoleBasedData(division);
        return new ResponseEntity<List<TerritoryMasterFinal>>(data, HttpStatus.OK);
    }

//    @GetMapping("/getMonthlyMasterRoleBasedData/{division}")
//    public ResponseEntity<List<MonthlyDataMaster>> getMonthlyMasterRoleBasedData(@PathVariable String division){
//        List<MonthlyDataMaster> data = monthlyDataMasterRepository.getDivisionRoleBasedMonthlyData(division);
//        return new ResponseEntity<List<MonthlyDataMaster>>(data, HttpStatus.OK);
//    }



}
