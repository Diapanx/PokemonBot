package edu.northeastern.cs5500.starterbot.model;

import lombok.Getter;

public enum PokemonMove {
    TACKLE("Tackle", 40, PokemonType.NORMAL),
    VINE_WHIP("Vine Whip", 45, PokemonType.GRASS),
    EMBER("Ember", 45, PokemonType.FIRE),
    WATER_GUN("Water Gun", 45, PokemonType.WATER);

    @Getter String name;

    @Getter int power;

    @Getter PokemonType type;

    PokemonMove(String name, int power, PokemonType type) {
        this.name = name;
        this.power = power;
        this.type = type;
    }
}
