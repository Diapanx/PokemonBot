package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.PokemonController;
import edu.northeastern.cs5500.starterbot.controller.TrainerController;
import edu.northeastern.cs5500.starterbot.model.Trainer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bson.types.ObjectId;

@Slf4j
public class InventoryCommand implements SlashCommandHandler {

    static final String NAME = "inventory";

    @Inject PokemonController pokemonController;
    @Inject TrainerController trainerController;

    @Inject
    public InventoryCommand() {
        // Defined public and empty for Dagger injection
    }

    @Override
    @Nonnull
    public String getName() {
        return NAME;
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Display user's pokemon Inventory.");
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /inventory");
        String trainerDiscordId = event.getMember().getId();
        Trainer trainer = trainerController.getTrainerForMemberId(trainerDiscordId);

        List<String> pokemonNameList = new ArrayList<>();
        for (ObjectId pokemonId : trainer.getPokemonInventory()) {
            pokemonNameList.add(pokemonController.getNameById(pokemonId));
        }

        StringBuilder message = new StringBuilder();
        message.append(String.format("Player <@%s>'s Pokemon Inventory:%n", trainerDiscordId));

        for (String pokemonName : pokemonNameList) {
            message.append("     " + pokemonName + "\n");
        }
        event.reply(message.toString()).setEphemeral(true).queue();
    }
}
