package com.redhat.database.benchmark;

import org.acme.mongodb.Fruit;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@RegisterRestClient(configKey = "process-api")
public interface BenchmarkService {

    @POST
    @Path("/fruits")
    @Consumes("application/json")
    void add(Fruit fruit);
}
