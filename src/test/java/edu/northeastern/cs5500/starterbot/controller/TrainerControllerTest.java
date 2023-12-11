package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.northeastern.cs5500.starterbot.exception.InvalidTeamPositionException;
import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

class TrainerControllerTest {
    final String DISCORD_USER_ID_1 = "discordUserId1";
    final String DISCORD_USER_ID_2 = "discordUserId2";

    PokedexController getPokedexController() {
        return new PokedexController();
    }

    PokemonController getPokemonController(PokedexController pokedexController) {
        return new PokemonController(new InMemoryRepository<>(), pokedexController);
    }

    TrainerController getTrainerController(PokemonController pokemonController) {
        TrainerController trainerController =
                new TrainerController(new InMemoryRepository<>(), pokemonController);

        trainerController.addPokemonToTrainer(
                DISCORD_USER_ID_1, pokemonController.spawnPokemon(1).getId().toString());
        trainerController.addPokemonToTrainer(
                DISCORD_USER_ID_2, pokemonController.spawnPokemon(4).getId().toString());

        return trainerController;
    }

    /*
     * Is this a positive or negative test?
     * Positive
     * If this test passes, what does it prove?
     * That inserting a Pokemon to a team via the controller has an effect.
     * If no exception is thrown, the Pokemon will move from the inventory to the team.
     * If this test fails, what should be done?
     * There must be a bug in either the controller or the repository; further debugging will be required
     * Can this test provide false positives or false negatives?
     * No
     * Does this test cover implemented behavior or specified behavior?
     * Specified behavior
     */
    @Test
    void testFormTeam() throws InvalidTeamPositionException, PokemonNotExistException {
        // setup
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);
        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon bulbasaur = pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));

        assertThrows(
                InvalidTeamPositionException.class,
                () -> {
                    trainerController.formTeam(DISCORD_USER_ID_1, "Bulbasaur", 2);
                });
        trainerController.formTeam(DISCORD_USER_ID_1, "Bulbasaur", 1);
        assertTrue(trainer.getTeam().size() == 1);
        assertTrue(trainer.getPokemonInventory().isEmpty());
        trainerController.addPokemonToTrainer(
                DISCORD_USER_ID_1, pokemonController.spawnPokemon(4).getId().toString());
        Pokemon charmander = pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        trainerController.formTeam(DISCORD_USER_ID_1, "Charmander", 1);
        assertEquals(charmander.getId(), trainer.getTeam().get(0));
        assertEquals(bulbasaur.getId(), trainer.getPokemonInventory().get(0));
        assertTrue(trainer.getTeam().size() == 1);
        assertTrue(trainer.getPokemonInventory().size() == 1);
    }

    /*
     * Is this a positive or negative test?
     * Positive
     * If this test passes, what does it prove?
     * That we can get the exact Pokemon in user's inventory by its name.
     * If this test fails, what should be done?
     * There must be a bug in either the controller or the repository; further debugging will be required
     * Can this test provide false positives or false negatives?
     * No
     * Does this test cover implemented behavior or specified behavior?
     * Implemented behavior
     */
    @Test
    void testGetInventoryPokemonByName() throws PokemonNotExistException {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon pokemon = pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        assertEquals(pokemon, trainerController.getInventoryPokemonByName(trainer, "Bulbasaur"));
    }

    /*
     * Is this a positive or negative test?
     * Positive
     * If this test passes, what does it prove?
     * That we can check whether a Pokemon exits in user's inventory.
     * If this test fails, what should be done?
     * There must be a bug in either the controller or the repository; further debugging will be required
     * Can this test provide false positives or false negatives?
     * No
     * Does this test cover implemented behavior or specified behavior?
     * Implemented behavior
     */
    @Test
    void testPokemonIsInInventory() {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon pokemon = pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        assertTrue(trainerController.pokemonIsInInventory(DISCORD_USER_ID_1, pokemon.getId()));
    }
}
