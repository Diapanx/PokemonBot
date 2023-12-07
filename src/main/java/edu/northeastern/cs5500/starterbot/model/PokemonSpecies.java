package edu.northeastern.cs5500.starterbot.model;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class PokemonSpecies {
    @NonNull final Integer pokedexNumber;

    @Nonnull final String imageUrl;

    @Nonnull final String name;

    @Nonnull final List<PokemonType> types;

    @Nonnull final Map<String, Integer> base;

    MoveEffectiveness getEffectiveness(PokemonType attackType) {
        return PokemonType.getEffectiveness(attackType, types);
    }
}
