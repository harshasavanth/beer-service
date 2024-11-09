package com.brewery.microservice.beerservice.web.controller;

import com.brewery.microservice.beerservice.service.BeerService;
import com.brewery.microservice.beerservice.web.model.BeerDto;
import com.brewery.microservice.beerservice.web.model.BeerPagedList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<BeerPagedList> listOrders(@RequestParam(value = "beerName", required = false) String beerName,
                                                    @RequestParam(value = "beerStyle", required = false) String beerStyle,
                                                    @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand,
                                                    @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        System.out.println("asd " + beerName + beerStyle + showInventoryOnHand);
        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return new ResponseEntity<>(beerService.listBeers(beerName, beerStyle, showInventoryOnHand, PageRequest.of(pageNumber, pageSize)), HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
                                               @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        return new ResponseEntity<BeerDto>(beerService.getBeerById(beerId, showInventoryOnHand), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<BeerDto> saveNewBeer(@RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity<BeerDto>(beerService.saveNewBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity<BeerDto>(beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT);
    }

}
