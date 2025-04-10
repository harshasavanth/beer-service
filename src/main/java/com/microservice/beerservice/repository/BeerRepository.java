package com.microservice.beerservice.repository;

import com.microservice.beerservice.domain.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    Page<Beer> findAllByBeerName(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(String beerStyle, Pageable pageable);

    Beer findByUpc(String upc);

    Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, String beerStyle,Pageable pageable);
}
