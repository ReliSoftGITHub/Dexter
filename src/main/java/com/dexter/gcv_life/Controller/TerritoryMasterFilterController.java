package com.dexter.gcv_life.Controller;

import com.dexter.gcv_life.Entity.TerritoryMasterFinal;
import com.dexter.gcv_life.Entity.TerritoryMasterRequest;
import com.dexter.gcv_life.Repository.TerritoryMasterFinalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TerritoryMasterFilterController {
    @Autowired
    TerritoryMasterFinalRepository repository;

    @GetMapping("/divisionDropdown")
    public ResponseEntity<List> divisionOnly(){
        List list = repository.getDistinctDivision();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/territoryNameDropdown")
    public ResponseEntity<List> territoryName(){
        List list = repository.getDistinctTerritoryName();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/zoneNameDropdown")
    public ResponseEntity<List> zoneName(){
        List list = repository.getDistinctZoneName();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/getTerritoryMasterData")
    public ResponseEntity<List<TerritoryMasterFinal>> getTerritoryMasterData(@RequestBody TerritoryMasterRequest request){

        String division = request.getDivision();
        String territory_name = request.getTerritory_name();
        String zone_name = request.getZone_name();

        try{

            if(!division.isEmpty() && !territory_name.isEmpty() && !zone_name.isEmpty()){
                List<TerritoryMasterFinal> data = repository.getTerritoryMasterData(division,territory_name,zone_name);
                return new ResponseEntity<List<TerritoryMasterFinal>>(data, HttpStatus.OK);
            }

            if(!division.isEmpty() && !territory_name.isEmpty() && zone_name.isEmpty()){
                List<TerritoryMasterFinal> data = repository.getDivisionAndTerritoryName(division,territory_name);
                return new ResponseEntity<List<TerritoryMasterFinal>>(data, HttpStatus.OK);
            }

            if(division.isEmpty() && !territory_name.isEmpty() && !zone_name.isEmpty()){
                List<TerritoryMasterFinal> data = repository.getTerritoryNameAndZoneName(territory_name,zone_name);
                return new ResponseEntity<List<TerritoryMasterFinal>>(data, HttpStatus.OK);
            }

            if(!division.isEmpty() && territory_name.isEmpty() && !zone_name.isEmpty()){
                List<TerritoryMasterFinal> data = repository.getDivisionAndZoneName(division,zone_name);
                return new ResponseEntity<List<TerritoryMasterFinal>>(data, HttpStatus.OK);
            }

            if(!division.isEmpty() && territory_name.isEmpty() && zone_name.isEmpty()){
                List<TerritoryMasterFinal> data = repository.getDivisionOny(division);
                return new ResponseEntity<List<TerritoryMasterFinal>>(data, HttpStatus.OK);
            }
            if(division.isEmpty() && !territory_name.isEmpty() && zone_name.isEmpty()){
                List<TerritoryMasterFinal> data = repository.getTerritoryNameOnly(territory_name);
                return new ResponseEntity<List<TerritoryMasterFinal>>(data, HttpStatus.OK);
            } if(division.isEmpty() && territory_name.isEmpty() && !zone_name.isEmpty()){
                List<TerritoryMasterFinal> data = repository.getZoneNameOnly(zone_name);
                return new ResponseEntity<List<TerritoryMasterFinal>>(data, HttpStatus.OK);
            }

        } catch (Exception e){
            e.getMessage();
            return null;
        }




        return null;
    }

}
