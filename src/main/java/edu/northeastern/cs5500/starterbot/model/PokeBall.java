package edu.northeastern.cs5500.starterbot.model;

public class PokeBall extends Item {

    double captureChance;

    public PokeBall(String name, String description) {
        super(name, description);
        this.captureChance = 0.25;
    }
}
