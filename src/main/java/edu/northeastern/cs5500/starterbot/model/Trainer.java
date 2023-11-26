package edu.northeastern.cs5500.starterbot.model;

import com.mongodb.lang.Nullable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Trainer implements Model {
    ObjectId id;

    // This is the "snowflake id" of the user
    // e.g. event.getUser().getId()
    String discordUserId;
    @Nonnull List<ObjectId> pokemonInventory = new ArrayList<>();
    @Nonnull ObjectId[] team = new ObjectId[6];
    @Nullable NPCBattle currentBattle;

    @Nonnull @Nonnegative Integer pokeCoins = 0;
}
