package edu.northeastern.cs5500.starterbot.model;

import lombok.Getter;

public enum PokemonMove {
    TACKLE("Tackle", 40, PokemonType.NORMAL),
    EMBER("Ember", 40, PokemonType.FIRE),
    WATER_GUN("Water Gun", 40, PokemonType.WATER),
    THUNDER_SHOCK("Thunder Shock", 40, PokemonType.ELECTRIC),
    VINE_WHIP("Vine Whip", 45, PokemonType.GRASS),
    ICE_SHARD("Ice Shard", 40, PokemonType.ICE),
    KARATE_CHOP("Karate Chop", 50, PokemonType.FIGHTING),
    POSION_STING("Poison Sting", 15, PokemonType.POISON),
    MUD_SLAP("Mud-Slap", 20, PokemonType.GROUND),
    PECK("Peck", 35, PokemonType.FLYING),
    CONFUSION("Confusion", 50, PokemonType.PSYCHIC),
    STRING_SHOT("String Shot", 0, PokemonType.BUG),
    ROCK_THROW("Rock Throw", 50, PokemonType.ROCK),
    LICK("Lick", 30, PokemonType.GHOST),
    DRAGON_BREATH("Dragon Breath", 60, PokemonType.DRAGON),
    BITE("Bite", 60, PokemonType.DARK),
    METAL_CLAW("Bite", 50, PokemonType.STEEL),
    FAIRY_WIND("Fairy Wind", 40, PokemonType.FAIRY);

    @Getter String name;

    @Getter int power;

    @Getter PokemonType type;

    PokemonMove(String name, int power, PokemonType type) {
        this.name = name;
        this.power = power;
        this.type = type;
    }
}
