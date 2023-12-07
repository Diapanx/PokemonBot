package edu.northeastern.cs5500.starterbot.model;

import java.util.List;
import javax.annotation.Nonnull;
import lombok.NonNull;

public enum PokemonType {
    FIRE("Fire", "🔥"),
    WATER("Water", "💧"),
    GRASS("Grass", "🍀"),
    NORMAL("Normal", "😐"),
    ELECTRIC("Electric", "⚡"),
    ICE("Ice", "❄️"),
    FIGHTING("Fighting", "🥊"),
    POISON("Poison", "☠️"),
    GROUND("Ground", "🌍"),
    FLYING("Flying", "🕊️"),
    PSYCHIC("Psychic", "🔮"),
    BUG("Bug", "🐛"),
    ROCK("Rock", "🪨"),
    GHOST("Ghost", "👻"),
    DRAGON("Dragon", "🐉"),
    DARK("Dark", "🌑"),
    STEEL("Steel", "🛡️"),
    FAIRY("Fairy", "🧚");

    @NonNull String name;

    @NonNull String emoji;

    PokemonType(@Nonnull String name, @Nonnull String emoji) {
        this.name = name;
        this.emoji = emoji;
    }

    public String getName() {
        return name;
    }

    public String getEmoji() {
        return emoji;
    }

    public static PokemonType fromString(String typeStr) {
        for (PokemonType type : PokemonType.values()) {
            if (type.name.equalsIgnoreCase(typeStr)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No PokemonType with name " + typeStr + " found");
    }

    @Nonnull
    public static PokemonType[] getSingleTypeArray(PokemonType type) {
        PokemonType[] types = new PokemonType[1];
        types[0] = type;
        return types;
    }

    public static MoveEffectiveness getEffectiveness(
            PokemonType attackType, List<PokemonType> types) {
        PokemonType defenderType = types.get(0);
        switch (defenderType) {
            case NORMAL:
                return MoveEffectiveness.FULL_EFFECT;
            case FIRE:
                switch (attackType) {
                    case FIRE:
                        return MoveEffectiveness.HALF_EFFECT;
                    case WATER:
                        return MoveEffectiveness.DOUBLE_EFFECT;
                    case GRASS:
                        return MoveEffectiveness.HALF_EFFECT;
                    case NORMAL:
                        return MoveEffectiveness.FULL_EFFECT;
                }
                break;
            case WATER:
                switch (attackType) {
                    case FIRE:
                        return MoveEffectiveness.HALF_EFFECT;
                    case WATER:
                        return MoveEffectiveness.HALF_EFFECT;
                    case GRASS:
                        return MoveEffectiveness.DOUBLE_EFFECT;
                    case NORMAL:
                        return MoveEffectiveness.FULL_EFFECT;
                }
                break;
            case GRASS:
                switch (attackType) {
                    case FIRE:
                        return MoveEffectiveness.DOUBLE_EFFECT;
                    case WATER:
                        return MoveEffectiveness.HALF_EFFECT;
                    case GRASS:
                        return MoveEffectiveness.HALF_EFFECT;
                    case NORMAL:
                        return MoveEffectiveness.FULL_EFFECT;
                }
                break;
        }
        throw new IllegalStateException();
    }
}
