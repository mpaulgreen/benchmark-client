package org.acme.mongodb;

import java.util.Objects;

public class Fruit {

    private String name;
    private String description;
    private String identifier;

    public Fruit() {
    }

    public Fruit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setIdentifier(String id) {
        this.identifier = id;
    }

    public String getIdentifier() {
        return identifier;
    }
}