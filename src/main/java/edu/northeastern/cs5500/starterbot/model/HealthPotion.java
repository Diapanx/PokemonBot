package edu.northeastern.cs5500.starterbot.model;

public class HealthPotion extends Item {

    double restoreHP;

    public HealthPotion(String name, String description) {
        super(name, description);
        this.restoreHP = 0.25;
    }

}
