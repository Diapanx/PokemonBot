package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import lombok.NonNull;

public enum PokemonType {
    FIRE("Fire", "🔥"),
    WATER("Water", "💧"),
    GRASS("Grass", "🍀");

    @NonNull String name;

    @NonNull String emoji;

    PokemonType(@Nonnull String name, @Nonnull String emoji) {
        this.name = name;
        this.emoji = emoji;
    }

    public static MoveEffectiveness getEffectiveness(PokemonType attackType, PokemonType[] types) {
        return null;
    }
}
