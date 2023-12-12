package edu.northeastern.cs5500.starterbot.model;

public class NPCBattle {
    Trainer trainer;
    Pokemon trainerPokemon;
    Pokemon npcPokemon;

    public NPCBattle(Trainer trainer, Pokemon trainerPokemon, Pokemon npcPokemon) {
        this.trainer = trainer;
        this.trainerPokemon = trainerPokemon;
        this.npcPokemon = npcPokemon;
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
}
