package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;

import org.bson.types.ObjectId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Item implements Model {
    @Nonnull
    ObjectId id = new ObjectId();

    @Nonnull
    String name;
    @Nonnull
    String description;

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