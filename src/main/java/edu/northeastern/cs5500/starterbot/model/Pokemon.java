package edu.northeastern.cs5500.starterbot.model;

import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.bson.types.ObjectId;

@Builder
@Data
@AllArgsConstructor
public class Pokemon implements Model {
    public Pokemon() {
        this.id = new ObjectId();
        this.level = 5;
    }

    @Nonnull @Builder.Default ObjectId id = new ObjectId();

    @Nonnull Integer pokedexNumber;

    @Nonnull @Builder.Default Integer level = 5;

    @Nonnull @Nonnegative Integer currentHp;
    PokemonStatus currentStatus;

    @Nonnull Integer hp;
    @Nonnull Integer attack;
    @Nonnull Integer defense;
    @Nonnull Integer specialAttack;
    @Nonnull Integer specialDefense;
    @Nonnull Integer speed;
    @Nonnull String name;
    @Nonnull List<PokemonType> types;
    @Nonnull String imageUrl;

    @Singular @Nonnull List<PokemonMove> moves;
}
