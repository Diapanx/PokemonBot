package edu.northeastern.cs5500.starterbot.model;

public class NPCBattle {
    Trainer trainer;
    Pokemon trainerPokemon;
    Pokemon npcPokemon;
    int trainerPokemonAP;
    int npcPokemonAP;

    public NPCBattle(Trainer trainer, Pokemon trainerPokemon, Pokemon npcPokemon) {
        this.trainer = trainer;
        this.trainerPokemon = trainerPokemon;
        this.npcPokemon = npcPokemon;
        this.trainerPokemonAP = trainerPokemon.getSpeed();
        this.npcPokemonAP = npcPokemon.getSpeed();
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Pokemon getTrainerPokemon() {
        return trainerPokemon;
    }

    public void setTrainerPokemon(Pokemon myPokemon) {
        this.trainerPokemon = myPokemon;
    }

    public Pokemon getNpcPokemon() {
        return npcPokemon;
    }

    public void setNpcPokemon(Pokemon npcPokemon) {
        this.npcPokemon = npcPokemon;
    }

    public int getTrainerPokemonAP() {
        return trainerPokemonAP;
    }

    public void setTrainerPokemonAP(int trainerPokemonAP) {
        this.trainerPokemonAP = trainerPokemonAP;
    }

    public int getNpcPokemonAP() {
        return npcPokemonAP;
    }

    public void setNpcPokemonAP(int npcPokemonAP) {
        this.npcPokemonAP = npcPokemonAP;
    }
}
