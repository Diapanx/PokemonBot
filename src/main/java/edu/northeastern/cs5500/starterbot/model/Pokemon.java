package edu.northeastern.cs5500.starterbot.model;

import org.bson.types.ObjectId;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

@Data
@RequiredArgsConstructor
public class Pokemon implements Model{

    @NonNull
    ObjectId id = new ObjectId();


    @Nonnull
    Integer pokedexNumber;

    @NonNull
    Integer level = 5;

    @NonNull
    @Nonnegative
    Integer currentHp;

    @NonNull
    Integer hp;
    @NonNull
    Integer attack;
    @NonNull
    Integer defense;
    @NonNull
    Integer specialAttack;
    @NonNull
    Integer specialDefense;
    @NonNull
    Integer speed;

}
