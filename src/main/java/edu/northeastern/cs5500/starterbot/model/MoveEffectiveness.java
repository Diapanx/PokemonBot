package edu.northeastern.cs5500.starterbot.model;

import com.mongodb.lang.Nullable;
import lombok.Getter;

public enum MoveEffectiveness {
    NO_EFFECT("It has no effect!", 0),
    HALF_EFFECT("It's not very effective...", 0.5),
    FULL_EFFECT(null, 1),
    DOUBLE_EFFECT("It's super effective!", 2),
    QUAD_EFFECT("It's super effective!", 4);

    @Nullable @Getter String text;

    double effectiveness;

    MoveEffectiveness(String text, double effectiveness) {
        this.text = text;
        this.effectiveness = effectiveness;
    }
}
