package com.microservice.beerservice.web.controller;


import com.microservice.beerservice.service.BeerService;
import com.microservice.beerservice.web.model.BeerDto;
import com.microservice.beerservice.web.model.BeerPagedList;
import com.microservice.beerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;


    private final BeerService beerService;

    @GetMapping(produces = {"application/json"}, path = "beer")
    public ResponseEntity<BeerPagedList> listBeers(@RequestParam(value = "pageNumber", required = false)Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false)Integer pageSize,
                                                   @RequestParam(value = "beerName", required = false)String beerName,
                                                   @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
                                                   @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if(pageNumber==null||pageNumber<0) {
            pageNumber=DEFAULT_PAGE_NUMBER;
        }

        if(pageSize==null||pageSize<1) {
            pageSize=DEFAULT_PAGE_SIZE;
        }

        if(showInventoryOnHand==null){
            showInventoryOnHand=false;
        }
        return new ResponseEntity<>(beerService.listBeers(beerName,beerStyle, PageRequest.of(pageNumber,pageSize),showInventoryOnHand),HttpStatus.OK);
    }

    @GetMapping("beer/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
                                               @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if(showInventoryOnHand==null){
            showInventoryOnHand=false;
        }
        return new ResponseEntity<BeerDto>(beerService.getById(beerId, showInventoryOnHand), HttpStatus.OK);
    }

    @GetMapping("beerUpc/{upc}")
    public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc) {
        return new ResponseEntity<BeerDto>(beerService.getByUpc(upc), HttpStatus.OK);
    }

    @PostMapping(path = "beer")
    public ResponseEntity<BeerDto> saveBeer(@RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity<BeerDto>(beerService.saveNewBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("beer/{beerId}")
    public ResponseEntity<BeerDto> updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity<BeerDto>(beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT);
    }
}
