package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Pokemon.PokemonBuilder;

import edu.northeastern.cs5500.starterbot.model.PokemonMove;

import edu.northeastern.cs5500.starterbot.model.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

@Singleton
public class PokemonController {

    GenericRepository<Pokemon> pokemonRepository;
    PokedexController pokedexController;

    @Inject
    PokemonController(
            GenericRepository<Pokemon> pokemonRepository, PokedexController pokedexController) {
        this.pokemonRepository = pokemonRepository;
        this.pokedexController = pokedexController;
    }

    /**
     * Create a new Pokemon of the specified number and add it to the repository.
     *
     * @param pokedexNumber the number of the Pokemon to spawn
     * @return a new Pokemon with a unique ID
     */
    @Nonnull
    Pokemon spawnPokemon(int pokedexNumber) {
        PokemonSpecies species = pokedexController.getPokemonSpeciesByNumber(pokedexNumber);
        PokemonBuilder builder = Pokemon.builder();
        builder.pokedexNumber(pokedexNumber);
        builder.name(species.getName());

        Map<String, Integer> baseStats = species.getBase();
        builder.currentHp(baseStats.get("HP"));
        builder.hp(baseStats.get("HP"));
        builder.attack(baseStats.get("Attack"));
        builder.defense(baseStats.get("Defense"));
        builder.specialAttack(baseStats.get("Sp. Attack"));
        builder.specialDefense(baseStats.get("Sp. Defense"));
        builder.speed(baseStats.get("Speed"));
        builder.types(species.getTypes());
        builder.imageUrl(species.getImageUrl());

        System.out.println(builder.build());
        return pokemonRepository.add(Objects.requireNonNull(builder.build()));
    }

    public Pokemon spawnRandomPokemon() {
        // Chosen randomly
        Random random = new Random();
        int randomIndex = random.nextInt(898) + 1;
        System.out.println("random number is " + randomIndex);
        return spawnPokemon(randomIndex);
    }

    public Pokemon getPokemonById(String pokemonId) {
        return getPokemonById(new ObjectId(pokemonId));
    }

    public Pokemon getPokemonById(ObjectId pokemonId) {
        return pokemonRepository.get(Objects.requireNonNull(pokemonId));
    }

    public int getPokedexByName(String pokemonName) {
        return pokedexController.getSpeciesByName(pokemonName).getPokedexNumber();
    }

    public String getNameById(ObjectId pokemonId) {
        Pokemon pokemon = getPokemonById(pokemonId);
        PokemonSpecies pokemonSpecies =
                pokedexController.getPokemonSpeciesByNumber(pokemon.getPokedexNumber());
        return pokemonSpecies.getName();
    }
}
