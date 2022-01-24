package org.acme.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FruitService {

    @Inject MongoClient mongoClient;

    public void add(Fruit fruit){
        System.out.println(fruit.getIdentifier());
        getCollection().insertOne(fruit);
    }


    private MongoCollection<Fruit> getCollection(){
        return mongoClient.getDatabase("fruits").getCollection("demo.fruit", Fruit.class);
    }
}