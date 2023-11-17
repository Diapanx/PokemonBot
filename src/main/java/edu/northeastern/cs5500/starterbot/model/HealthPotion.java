package edu.northeastern.cs5500.starterbot.model;

public class HealthPotion extends Item {

    double restoreHP = 0.25;

    public HealthPotion(String name, String description) {
        super(name, description);
        this.name = "Health Potion";
        this.description = "restores 25% of Pokemon's max HP.";
    }

}
