package com.microservice.beerservice.web.service.inventory;

import java.util.UUID;

public interface BeerInventoryService {

    Integer getOnhandInventory(UUID beerId);
}