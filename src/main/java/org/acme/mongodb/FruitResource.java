package org.acme.mongodb;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/fruits")
public class FruitResource {

    @Inject
    FruitService fruitService;

    @POST
    public void add(Fruit fruit) {
        fruitService.add(fruit);
    }
}