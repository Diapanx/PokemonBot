package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import lombok.NonNull;

import javax.annotation.Nonnull;

@Data
public class PokemonSpecies {
    @NonNull
    final Integer pokedexNumber;

    @Nonnull
    final String imageUrl;

    @Nonnull
    final String name;

    @Nonnull
    final PokemonType[] types;

    MoveEffectiveness getEffectiveness(PokemonType attackType) {
        return PokemonType.getEffectiveness(attackType, types);
    }
}
