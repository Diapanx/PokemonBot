package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.PokemonSpecies.PokemonSpeciesBuilder;
import edu.northeastern.cs5500.starterbot.model.PokemonType;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PokedexController {

    @Inject
    PokedexController() {
        // empty and defined for Dagger
    }

    @Nonnull
    public PokemonSpecies getPokemonSpeciesByNumber(int pokedexNumber) {
        PokemonSpeciesBuilder builder = PokemonSpecies.builder();
        builder.pokedexNumber(pokedexNumber);
        switch (pokedexNumber) {
            case 1: // Bulbasaur
                builder.name("Bulbasaur");
                builder.types(PokemonType.getSingleTypeArray(PokemonType.GRASS));
                builder.imageUrl("https://placehold.co/256x256/green/white.png?text=Bulbasaur");
                break;
            case 4: // Charmander
                builder.name("Charmander");
                builder.types(PokemonType.getSingleTypeArray(PokemonType.FIRE));
                builder.imageUrl("https://placehold.co/256x256/red/white.png?text=Charmander");
                break;
            case 7: // Squirtle
                builder.name("Squirtle");
                builder.types(PokemonType.getSingleTypeArray(PokemonType.WATER));
                builder.imageUrl("https://placehold.co/256x256/blue/white.png?text=Squirtle");
                break;
            case 19: // Rattata
                builder.name("Rattata");
                builder.types(PokemonType.getSingleTypeArray(PokemonType.NORMAL));
                builder.imageUrl("https://placehold.co/256x256.png?text=Rattata");
                break;
            default:
                throw new IllegalStateException();
        }

        return Objects.requireNonNull(builder.build());
    }

    public PokemonSpecies getSpeciesByName(@Nonnull String name) {
        PokemonSpeciesBuilder builder = PokemonSpecies.builder();
        builder.name(name);
        switch (name) {
            case "Bulbasaur": // Bulbasaur
                builder.pokedexNumber(1);
                builder.types(PokemonType.getSingleTypeArray(PokemonType.GRASS));
                builder.imageUrl("https://placehold.co/256x256/green/white.png?text=Bulbasaur");
                break;
            case "Charmander": // Charmander
                builder.pokedexNumber(4);
                builder.types(PokemonType.getSingleTypeArray(PokemonType.FIRE));
                builder.imageUrl("https://placehold.co/256x256/red/white.png?text=Charmander");
                break;
            case "Squirtle": // Squirtle
                builder.pokedexNumber(7);
                builder.types(PokemonType.getSingleTypeArray(PokemonType.WATER));
                builder.imageUrl("https://placehold.co/256x256/blue/white.png?text=Squirtle");
                break;
            case "Rattata": // Rattata
                builder.pokedexNumber(19);
                builder.types(PokemonType.getSingleTypeArray(PokemonType.NORMAL));
                builder.imageUrl("https://placehold.co/256x256.png?text=Rattata");
                break;
            default:
                throw new IllegalStateException();
        }

        return Objects.requireNonNull(builder.build());
    }
}
