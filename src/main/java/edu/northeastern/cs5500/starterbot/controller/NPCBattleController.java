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
    private static final int ACTION_THRESHOLD = 100;

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
        return (npcBattle.getTrainerPokemon().getCurrentHp() == 0
                || npcBattle.getNpcPokemon().getCurrentHp() == 0);
    }

    public boolean checkIfPokemonInventoryIsNull(Trainer trainer) {
        return trainer.getPokemonInventory().isEmpty();
    }

    public void endBattle(NPCBattle npcBattle) {
        npcBattle.getTrainerPokemon().setCurrentHp(npcBattle.getTrainerPokemon().getHp());
    }

    public Pokemon checkWhosTurn(NPCBattle npcBattle) {
        int npcPokemonAP = npcBattle.getTrainerPokemonAP();
        int trainerPokemonAP = npcBattle.getTrainerPokemonAP();

        while (npcPokemonAP <= ACTION_THRESHOLD || trainerPokemonAP <= ACTION_THRESHOLD) {
            npcPokemonAP += npcBattle.getNpcPokemon().getSpeed();
            trainerPokemonAP += npcBattle.getTrainerPokemon().getSpeed();
            if (npcBattle.getNpcPokemon().getSpeed() == 0
                    || npcBattle.getTrainerPokemon().getSpeed() == 0) {
                break;
            }
        }
        npcBattle.setNpcPokemonAP(npcPokemonAP);
        npcBattle.setTrainerPokemonAP(trainerPokemonAP);

        if (npcPokemonAP >= trainerPokemonAP) {
            npcBattle.setNpcPokemonAP(npcPokemonAP - ACTION_THRESHOLD);
            return npcBattle.getNpcPokemon();
        } else {
            npcBattle.setTrainerPokemonAP(trainerPokemonAP - ACTION_THRESHOLD);
            return npcBattle.getTrainerPokemon();
        }
    }

    public void performNPCAttack(NPCBattle npcBattle, Pokemon pokemon) {
        if (npcBattle.getNpcPokemon().equals(pokemon)) {
            Random random = new Random();
            // Assuming 0 for attack and 1 for specialAttack
            int attackType = random.nextInt(2);
            if (attackType == 0) {
                basicAttack(pokemon, npcBattle.getTrainerPokemon());
            } else {
                specialAttack(pokemon, npcBattle.getTrainerPokemon());
            }
        } else {
            throw new IllegalStateException("Unknown error occured");
        }
    }

    public void basicAttack(Pokemon attackPokemon, Pokemon defensePokemon) {
        int attackValue = attackPokemon.getAttack();
        int defenseValue = defensePokemon.getDefense();
        int defensePokemonCurrentHP = defensePokemon.getCurrentHp();
        int damage = Math.max(1, attackValue - defenseValue);
        defensePokemon.setCurrentHp(defensePokemonCurrentHP - damage);
    }

    public void specialAttack(Pokemon attackPokemon, Pokemon defensePokemon) {
        int attackValue = attackPokemon.getSpecialAttack();
        int defenseValue = defensePokemon.getSpecialDefense();
        int defensePokemonCurrentHP = defensePokemon.getCurrentHp();
        int damage = Math.max(1, attackValue - defenseValue);
        defensePokemon.setCurrentHp(defensePokemonCurrentHP - damage);
    }
}
