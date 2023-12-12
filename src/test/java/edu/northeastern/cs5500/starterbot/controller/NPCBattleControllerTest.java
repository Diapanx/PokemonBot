package edu.northeastern.cs5500.starterbot.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.NPCBattle;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

class NPCBattleControllerTest {

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

    NPCBattleController getBattleController(
            TrainerController trainerController, PokemonController pokemonController) {
        return new NPCBattleController(trainerController, pokemonController);
    }

    @Test
    void testStartBattle() throws PokemonNotExistException {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);
        NPCBattleController npcBattleController =
                getBattleController(trainerController, pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon trainerPokemon =
                pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        NPCBattle npcBattle = npcBattleController.startBattle(trainer, trainerPokemon.getName());

        assertEquals(trainer, npcBattle.getTrainer());
        assertEquals(trainerPokemon, npcBattle.getTrainerPokemon());
    }

    @Test
    void testIfBattleEnds() throws PokemonNotExistException {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);
        NPCBattleController npcBattleController =
                getBattleController(trainerController, pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon trainerPokemon =
                pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        NPCBattle npcBattle = npcBattleController.startBattle(trainer, trainerPokemon.getName());

        trainerPokemon.setCurrentHp(0);
        assertTrue(npcBattleController.checkIfBattleEnds(npcBattle));

        trainerPokemon.setCurrentHp(30);
        assertFalse(npcBattleController.checkIfBattleEnds(npcBattle));
    }

    @Test
    void testCheckPokemonInventoryIsEmpty() throws PokemonNotExistException {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);
        NPCBattleController npcBattleController =
                getBattleController(trainerController, pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon trainerPokemon =
                pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));

        assertFalse(npcBattleController.checkIfPokemonInventoryIsNull(trainer));
        trainerController.removePokemonFromTrainer(trainer, trainerPokemon);
        assertTrue(npcBattleController.checkIfPokemonInventoryIsNull(trainer));
    }

    @Test
    void testBasicAttack() throws PokemonNotExistException {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);
        NPCBattleController npcBattleController =
                getBattleController(trainerController, pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon trainerPokemon =
                pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        NPCBattle npcBattle = npcBattleController.startBattle(trainer, trainerPokemon.getName());

        Pokemon npcPokemon = npcBattle.getNpcPokemon();
        Integer damage = Math.min(1, npcPokemon.getAttack() - trainerPokemon.getDefense());
        Integer newHP = Math.max(0, trainerPokemon.getCurrentHp() - damage);

        npcBattleController.basicAttack(npcPokemon, trainerPokemon);
        assertEquals(newHP, trainerPokemon.getCurrentHp());
    }

    @Test
    void testSpecialAttack() throws PokemonNotExistException {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);
        NPCBattleController npcBattleController =
                getBattleController(trainerController, pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon trainerPokemon =
                pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        NPCBattle npcBattle = npcBattleController.startBattle(trainer, trainerPokemon.getName());

        Pokemon npcPokemon = npcBattle.getNpcPokemon();
        Integer damage =
                Math.min(1, npcPokemon.getSpecialAttack() - trainerPokemon.getSpecialDefense());
        Integer newHP = Math.max(0, trainerPokemon.getCurrentHp() - damage);

        npcBattleController.specialAttack(npcPokemon, trainerPokemon);
        assertEquals(newHP, trainerPokemon.getCurrentHp());
    }

    @Test
    void testBattleEnd() throws PokemonNotExistException {
        PokedexController pokedexController = getPokedexController();
        PokemonController pokemonController = getPokemonController(pokedexController);
        TrainerController trainerController = getTrainerController(pokemonController);
        NPCBattleController npcBattleController =
                getBattleController(trainerController, pokemonController);

        Trainer trainer = trainerController.getTrainerForMemberId(DISCORD_USER_ID_1);
        Pokemon trainerPokemon =
                pokemonController.getPokemonById(trainer.getPokemonInventory().get(0));
        NPCBattle npcBattle = npcBattleController.startBattle(trainer, trainerPokemon.getName());

        npcBattleController.endBattle(npcBattle);
        assertEquals(trainerPokemon.getHp(), trainerPokemon.getCurrentHp());
    }
}
