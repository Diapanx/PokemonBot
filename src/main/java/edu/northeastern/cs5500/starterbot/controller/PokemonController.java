package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Pokemon.PokemonBuilder;
import edu.northeastern.cs5500.starterbot.model.PokemonMove;
import edu.northeastern.cs5500.starterbot.model.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
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
        PokemonBuilder builder = Pokemon.builder();
        builder.pokedexNumber(pokedexNumber);
        switch (pokedexNumber) {
            case 1: // Bulbasaur
                builder.currentHp(19);
                builder.hp(19);
                builder.attack(9);
                builder.defense(9);
                builder.specialAttack(11);
                builder.specialDefense(11);
                builder.speed(9);
                builder.move(PokemonMove.TACKLE);
                builder.move(PokemonMove.VINE_WHIP);
                break;
            case 4: // Charmander
                builder.currentHp(18);
                builder.hp(18);
                builder.attack(10);
                builder.defense(9);
                builder.specialAttack(11);
                builder.specialDefense(10);
                builder.speed(11);
                builder.move(PokemonMove.TACKLE);
                builder.move(PokemonMove.EMBER);
                break;
            case 7: // Squirtle
                builder.currentHp(19);
                builder.hp(19);
                builder.attack(9);
                builder.defense(11);
                builder.specialAttack(10);
                builder.specialDefense(11);
                builder.speed(9);
                builder.move(PokemonMove.TACKLE);
                builder.move(PokemonMove.WATER_GUN);
                break;
            case 19: // Rattata
                builder.currentHp(18);
                builder.hp(18);
                builder.attack(10);
                builder.defense(8);
                builder.specialAttack(7);
                builder.specialDefense(8);
                builder.speed(12);
                builder.move(PokemonMove.TACKLE);
                break;
            default:
                throw new IllegalStateException();
        }

        return pokemonRepository.add(Objects.requireNonNull(builder.build()));
    }

    public Pokemon spawnRandomPokemon() {
        // Chosen randomly
        int[] arr = {1, 4, 7, 19};
        Random random = new Random();
        int randomIndex = random.nextInt(arr.length);
        int pokedex = arr[randomIndex];
        return spawnPokemon(pokedex);
    }

    public Pokemon getPokemonById(String pokemonId) {
        return getPokemonById(new ObjectId(pokemonId));
    }

    public Pokemon getPokemonById(ObjectId pokemonId) {
        return pokemonRepository.get(Objects.requireNonNull(pokemonId));
    }

    public int getPokedexByName(String pokemonName) {
        return pokedexController.getSpeciesByName(pokemonName).getPokedexNumber();

    public String getNameById(ObjectId pokemonId) {
        Pokemon pokemon = getPokemonById(pokemonId);
        PokemonSpecies pokemonSpecies =
                pokedexController.getPokemonSpeciesByNumber(pokemon.getPokedexNumber());
        return pokemonSpecies.getName();

    }
}
