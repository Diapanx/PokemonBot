package edu.northeastern.cs5500.starterbot.model;

import org.bson.types.ObjectId;

public class Item implements Model {

    ObjectId id = new ObjectId();

    String name;
    String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ObjectId getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setId(ObjectId id) {
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }
}
