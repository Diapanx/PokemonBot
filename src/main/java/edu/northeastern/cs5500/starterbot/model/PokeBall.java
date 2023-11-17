package edu.northeastern.cs5500.starterbot.model;

public class PokeBall extends Item {

    double captureChance = 0.25;

    public PokeBall(String name, String description) {
        super(name, description);
        this.name = "Poke Ball";
        this.description = "Poke Ball is used to capture Pokemon";
    }

}
