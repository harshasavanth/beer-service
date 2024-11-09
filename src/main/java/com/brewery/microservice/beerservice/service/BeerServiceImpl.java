package com.brewery.microservice.beerservice.service;

import com.brewery.microservice.beerservice.domain.Beer;
import com.brewery.microservice.beerservice.exception.NotFoundException;
import com.brewery.microservice.beerservice.repository.BeerRepository;
import com.brewery.microservice.beerservice.web.mappers.BeerMapper;
import com.brewery.microservice.beerservice.web.model.BeerDto;
import com.brewery.microservice.beerservice.web.model.BeerPagedList;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, String beerStyle, Boolean showInventoryOnHand, PageRequest pageRequest) {

        BeerPagedList beerPagedList;
        Page<Beer> beerPage;
        if (StringUtils.isNotBlank(beerName) && StringUtils.isNotBlank(beerStyle)) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if (StringUtils.isNotBlank(beerName) && StringUtils.isBlank(beerStyle)) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (StringUtils.isBlank(beerName) && StringUtils.isNotBlank(beerStyle)) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventoryOnHand) {
            beerPagedList = new BeerPagedList(beerPage
                    .stream()
                    .map(beerMapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList()), PageRequest.of(
                    beerPage.getPageable().getPageNumber(),
                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        } else {
            beerPagedList = new BeerPagedList(beerPage
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList()), PageRequest.of(
                    beerPage.getPageable().getPageNumber(),
                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        }
        return beerPagedList;
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand) {
        if (null != showInventoryOnHand && showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
        } else {
            return beerMapper.beerToBeerDto(beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
        }
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());
        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }
}
