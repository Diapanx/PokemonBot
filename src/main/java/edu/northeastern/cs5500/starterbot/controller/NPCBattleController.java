package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.exception.PokemonNotExistException;
import edu.northeastern.cs5500.starterbot.model.NPCBattle;
import edu.northeastern.cs5500.starterbot.model.Pokemon;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import java.util.Random;
import javax.inject.Inject;

public class NPCBattleController {
    TrainerController trainerController;
    PokemonController pokemonController;

    @Inject
    public NPCBattleController(
            TrainerController trainerController, PokemonController pokemonController) {
        this.trainerController = trainerController;
        this.pokemonController = pokemonController;
    }

    public NPCBattle startBattle(Trainer trainer, String pokemonName)
            throws PokemonNotExistException {
        Pokemon trainerPokemon = trainerController.getInventoryPokemonByName(trainer, pokemonName);
        Pokemon npcPokemon = pokemonController.spawnRandomPokemon();
        return new NPCBattle(trainer, trainerPokemon, npcPokemon);
    }

    public boolean checkIfBattleEnds(NPCBattle npcBattle) {
        return (npcBattle.getTrainerPokemon().getCurrentHp() <= 0
                || npcBattle.getNpcPokemon().getCurrentHp() <= 0);
    }

    public boolean checkIfPokemonInventoryIsNull(Trainer trainer) {
        return trainer.getPokemonInventory().isEmpty();
    }

    public void endBattle(NPCBattle npcBattle) {
        npcBattle.getTrainerPokemon().setCurrentHp(npcBattle.getTrainerPokemon().getHp());
    }

    public void performNPCAttack(NPCBattle npcBattle) {
        Random random = new Random();
        // Assuming 0 for attack and 1 for specialAttack
        int attackType = random.nextInt(2);
        if (attackType == 0) {
            basicAttack(npcBattle.getNpcPokemon(), npcBattle.getTrainerPokemon());
        } else {
            specialAttack(npcBattle.getNpcPokemon(), npcBattle.getTrainerPokemon());
        }
    }

    public void basicAttack(Pokemon attackPokemon, Pokemon defensePokemon) {
        // deals minimum 1 damage if attack is less than defense.
        int damage = Math.max(1, attackPokemon.getAttack() - defensePokemon.getDefense());
        int newHP = Math.max(0, defensePokemon.getCurrentHp() - damage);
        defensePokemon.setCurrentHp(newHP);
    }

    public void specialAttack(Pokemon attackPokemon, Pokemon defensePokemon) {
        // deals minimum 1 damaged if attack is less than defense.
        int damage =
                Math.max(1, attackPokemon.getSpecialAttack() - defensePokemon.getSpecialDefense());
        int newHP = Math.max(0, defensePokemon.getCurrentHp() - damage);
        defensePokemon.setCurrentHp(newHP);
    }
}
