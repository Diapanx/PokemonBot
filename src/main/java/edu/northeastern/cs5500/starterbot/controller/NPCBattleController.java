package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.NPCBattle;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bson.types.ObjectId;

public class NPCBattleController {
    TrainerController trainerController;
    PokemonController pokemonController;
    private static final int ACTION_THRESHOLD = 100;

    @Inject
    public NPCBattleController(
            TrainerController trainerController,
            PokemonController pokemonController) {
        this.trainerController = trainerController;
        this.pokemonController = pokemonController;
    }

    public NPCBattle startBattle(Trainer trainer, String pokemonName) throws PokemonNotExistException {
        Pokemon trainerPokemon = trainerController.getInventoryPokemonByName(trainer, pokemonName);
        Pokemon npcPokemon = pokemonController.spawnRandomPokemon();
        return new NPCBattle(trainer, trainerPokemon, npcPokemon);
    }

    private boolean checkBattleEndConditions(NPCBattle npcBattle) {
        if (npcBattle.getTrainerPokemon().getCurrentHp() == 0 || npcBattle.getNpcPokemon().getCurrentHp() == 0) {
            return true;
        }
        return false;
    }

}
