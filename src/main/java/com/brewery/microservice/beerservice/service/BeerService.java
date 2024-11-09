package com.brewery.microservice.beerservice.service;

import com.brewery.microservice.beerservice.web.model.BeerDto;
import com.brewery.microservice.beerservice.web.model.BeerPagedList;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {

    BeerPagedList listBeers(String beerName, String beerStyle, Boolean showInventoryOnHand, PageRequest of);

    BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto getBeerByUpc(String upc);
}
