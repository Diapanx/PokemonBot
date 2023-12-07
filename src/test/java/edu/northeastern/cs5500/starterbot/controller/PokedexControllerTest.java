package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.model.PokemonSpecies;
import edu.northeastern.cs5500.starterbot.model.PokemonType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PokedexControllerTest {

    private PokedexController pokedexController;

    @BeforeEach
    void setUp() {
        pokedexController = new PokedexController();
    }

    @Test
    void testGetPokemonSpeciesByNumber() {
        int bulbasaurId = 1;
        PokemonSpecies bulbasaur = pokedexController.getPokemonSpeciesByNumber(bulbasaurId);

        assertNotNull(bulbasaur, "Bulbasaur should not be null");
        assertEquals(bulbasaurId, bulbasaur.getPokedexNumber(), "Pokedex number should match");
        assertEquals("Bulbasaur", bulbasaur.getName(), "Name should be Bulbasaur");
        assertEquals(
                List.of(PokemonType.GRASS, PokemonType.POISON),
                bulbasaur.getTypes(),
                "Types should match Grass and Poison");

        Map<String, Integer> baseStats = bulbasaur.getBase();
        assertNotNull(baseStats, "Base stats should not be null");
        assertEquals((Integer) 45, baseStats.get("HP"), "HP should be 45");
        assertEquals((Integer) 49, baseStats.get("Attack"), "Attack should be 49");
        assertEquals((Integer) 49, baseStats.get("Defense"), "Defense should be 49");
        assertEquals((Integer) 65, baseStats.get("Sp. Attack"), "Sp. Attack should be 65");
        assertEquals((Integer) 65, baseStats.get("Sp. Defense"), "Sp. Defense should be 65");
        assertEquals((Integer) 45, baseStats.get("Speed"), "Speed should be 45");
    }

    @Test
    void testGetSpeciesByName() {
        String pokemonName = "Bulbasaur";
        PokemonSpecies species = pokedexController.getSpeciesByName(pokemonName);

        assertNotNull(species, "Bulbasaur species should not be null");
        assertEquals(pokemonName, species.getName(), "Name should match Bulbasaur");
        assertEquals(1, species.getPokedexNumber(), "Pokedex number should be 1 for Bulbasaur");

        // Test types
        List<PokemonType> expectedTypes = List.of(PokemonType.GRASS, PokemonType.POISON);
        assertEquals(expectedTypes, species.getTypes(), "Types should match Grass and Poison");

        // Test base stats, assuming you know the expected values for Bulbasaur
        Map<String, Integer> baseStats = species.getBase();
        assertEquals((Integer) 45, baseStats.get("HP"), "HP should be 45");
        assertEquals((Integer) 49, baseStats.get("Attack"), "Attack should be 49");
    }
}
