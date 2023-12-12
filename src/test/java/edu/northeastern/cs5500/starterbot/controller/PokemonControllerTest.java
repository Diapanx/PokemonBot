package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PokemonControllerTest {

    private PokemonController pokemonController;
    private PokedexController pokedexController;

    @BeforeEach
    void setUp() {
        pokedexController =
                new PokedexController(); // Replace with actual initialization or stubbing
        pokemonController = new PokemonController(new InMemoryRepository<>(), pokedexController);
    }

    @Test
    void testGetPokedexByName() {

        String pokemonName = "Bulbasaur";
        int result = pokemonController.getPokedexByName(pokemonName);
        assertEquals(1, result, "getPokedexByName for Bulbasaur should return 1.");
    }
}
