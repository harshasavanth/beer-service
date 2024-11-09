package com.brewery.microservice.beerservice.web.mappers;

import com.brewery.microservice.beerservice.domain.Beer;
import com.brewery.microservice.beerservice.service.inventory.BeerInventoryService;
import com.brewery.microservice.beerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public abstract class BeerMapperDecorator implements BeerMapper {

    private BeerMapper beerMapper;
    private BeerInventoryService beerInventoryService;

    @Autowired
    public void setBeerMapper(BeerMapper beerMapper) {
        this.beerMapper = beerMapper;
    }

    @Autowired
    public void setBeerInventoryService(BeerInventoryService beerInventoryService) {
        this.beerInventoryService = beerInventoryService;
    }

    @Override
    public BeerDto beerToBeerDto(Beer beer) {
        return beerMapper.beerToBeerDto(beer);
    }

    @Override
    public BeerDto beerToBeerDtoWithInventory(Beer beer) {
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setQuantityOnHand(beerInventoryService.getOnHandInventory(beer.getId()));
        return beerDto;
    }

    @Override
    public Beer beerDtoToBeer(BeerDto beerDto) {
        return beerMapper.beerDtoToBeer(beerDto);
    }


}
