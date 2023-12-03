package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class TradeOffer implements Model {
    ObjectId id;

    // If null, this trade is open to anyone (i.e. my Pikachu is up for trade)
    // If not null, must refer to an open trade offer (i.e. my Clefable for your
    // Pikachu)
    @Nullable ObjectId parent;
    @Nonnull ObjectId trainerId;
    @Nonnull ObjectId pokemonId;

    public TradeOffer(ObjectId trainerId, ObjectId pokemonId) {
        this.trainerId = trainerId;
        this.pokemonId = pokemonId;
    }

    public TradeOffer() {
        // public constructor for MongoDB
    }
}
